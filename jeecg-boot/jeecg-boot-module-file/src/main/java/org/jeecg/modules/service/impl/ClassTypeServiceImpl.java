package org.jeecg.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.entity.ClassType;
import org.jeecg.modules.mapper.ClassTypeMapper;
import org.jeecg.modules.service.IClassTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 类别字典
 * @Author: jeecg-boot
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Service
public class ClassTypeServiceImpl extends ServiceImpl<ClassTypeMapper, ClassType> implements IClassTypeService {

	@Override
	public void addClassType(ClassType classType) {
		if(oConvertUtils.isEmpty(classType.getPid())){
			classType.setPid(IClassTypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			ClassType parent = baseMapper.selectById(classType.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(classType);
	}
	
	@Override
	public void updateClassType(ClassType classType) {
		ClassType entity = this.getById(classType.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = classType.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				classType.setPid(IClassTypeService.ROOT_PID_VALUE);
			}
			if(!IClassTypeService.ROOT_PID_VALUE.equals(classType.getPid())) {
				baseMapper.updateTreeNodeStatus(classType.getPid(), IClassTypeService.HASCHILD);
			}
		}
		baseMapper.updateById(classType);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteClassType(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    ClassType classType = this.getById(idVal);
                    String pidVal = classType.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<ClassType> dataList = baseMapper.selectList(new QueryWrapper<ClassType>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    if((dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            ClassType classType = this.getById(id);
            if(classType==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(classType.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<ClassType> queryTreeListNoPage(QueryWrapper<ClassType> queryWrapper) {
        List<ClassType> dataList = baseMapper.selectList(queryWrapper);
        List<ClassType> mapList = new ArrayList<>();
        for(ClassType data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !"0".equals(pidVal)){
                ClassType rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IClassTypeService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<ClassType>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IClassTypeService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private ClassType getTreeRoot(String pidVal){
        ClassType data =  baseMapper.selectById(pidVal);
        if(data != null && !"0".equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<ClassType> dataList = baseMapper.selectList(new QueryWrapper<ClassType>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(ClassType tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
