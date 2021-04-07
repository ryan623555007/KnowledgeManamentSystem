package org.jeecg.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.entity.ClassType;

/**
 * @Description: 类别字典
 * @Author: jeecg-boot
 * @Date:   2021-01-28
 * @Version: V1.0
 */
public interface ClassTypeMapper extends BaseMapper<ClassType> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
