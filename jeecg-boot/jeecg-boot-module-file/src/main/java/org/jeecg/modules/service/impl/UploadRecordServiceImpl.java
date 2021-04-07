package org.jeecg.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.entity.CollectFileRecord;
import org.jeecg.modules.entity.UploadRecord;
import org.jeecg.modules.entity.UploadRecordAndCollect;
import org.jeecg.modules.mapper.CollectFileRecordMapper;
import org.jeecg.modules.mapper.UploadRecordMapper;
import org.jeecg.modules.service.IUploadRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 上传文件记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
@Service
public class UploadRecordServiceImpl extends ServiceImpl<UploadRecordMapper, UploadRecord> implements IUploadRecordService {
    @Resource
    private UploadRecordMapper uploadRecordMapper;
    @Resource
    private CollectFileRecordMapper collectFileRecordMapper;

    @Override
    public IPage<UploadRecordAndCollect> queryPageList(UploadRecord uploadRecord, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        
        QueryWrapper<UploadRecord> queryWrapper = QueryGenerator.initQueryWrapper(uploadRecord, req.getParameterMap());
        Page<UploadRecord> page = new Page<UploadRecord>(pageNo, pageSize);
        List<UploadRecordAndCollect> list = new ArrayList<UploadRecordAndCollect>();
        IPage<UploadRecord> pageList = uploadRecordMapper.selectPage(page, queryWrapper);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<UploadRecord> records = pageList.getRecords();
        if(!records.isEmpty()){
            for(UploadRecord uploadRecord1 : records){
                UploadRecordAndCollect uploadRecordAndCollect = new UploadRecordAndCollect();
                uploadRecordAndCollect.setId(uploadRecord1.getId());
                uploadRecordAndCollect.setTitle(uploadRecord1.getTitle());
                uploadRecordAndCollect.setFileClass(uploadRecord1.getFileClass());
                uploadRecordAndCollect.setDescription(uploadRecord1.getDescription());
                uploadRecordAndCollect.setFileStatus(uploadRecord1.getFileStatus());
                uploadRecordAndCollect.setFiles(uploadRecord1.getFiles());
                uploadRecordAndCollect.setCreateBy(uploadRecord1.getCreateBy());
                uploadRecordAndCollect.setCreateTime(uploadRecord1.getCreateTime());
                uploadRecordAndCollect.setUpdateBy(uploadRecord1.getUpdateBy());
                uploadRecordAndCollect.setUpdateTime(uploadRecord1.getUpdateTime());

                LambdaQueryWrapper<CollectFileRecord> thirdQuery = new LambdaQueryWrapper<>();
                thirdQuery.eq(CollectFileRecord::getId,uploadRecordAndCollect.getId());
                thirdQuery.eq(CollectFileRecord::getCreateBy,sysUser.getUsername());
                CollectFileRecord collectFileRecord = collectFileRecordMapper.selectOne(thirdQuery);
                uploadRecordAndCollect.setCollectStatus("1");//1：未收藏
                if (collectFileRecord != null){
                    uploadRecordAndCollect.setCollectStatus(collectFileRecord.getCollectStatus());//0：已收藏
                    uploadRecordAndCollect.setCollectCreateBy(collectFileRecord.getCreateBy());
                }

                list.add(uploadRecordAndCollect);
            }
        }
        IPage<UploadRecordAndCollect> result = new Page<UploadRecordAndCollect>(page.getCurrent(), page.getSize(), list.size());
        result.setRecords(list);
        return result;
    }
}
