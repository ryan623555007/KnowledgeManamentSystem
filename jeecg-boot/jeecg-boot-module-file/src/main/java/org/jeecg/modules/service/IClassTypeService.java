package org.jeecg.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.entity.ClassType;

import java.util.List;

/**
 * @Description: 类别字典
 * @Author: jeecg-boot
 * @Date:   2021-01-28
 * @Version: V1.0
 */
public interface IClassTypeService extends IService<ClassType> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addClassType(ClassType classType);
	
	/**修改节点*/
	void updateClassType(ClassType classType) throws JeecgBootException;
	
	/**删除节点*/
	void deleteClassType(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<ClassType> queryTreeListNoPage(QueryWrapper<ClassType> queryWrapper);

}
