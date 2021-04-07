package org.jeecg.modules.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class DocRecordAndCollect implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
	/**类别*/
    private String docType;
	/**文件名称*/
    private String fileName;
	/**描述*/
    private String description;
	/**文件状态*/
    private String fileStatus;
	/**sql语句*/
    private String sqlText;
	/**创建人*/
    private String createBy;
	/**创建日期*/
    private java.util.Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
    private java.util.Date updateTime;
    /**收藏状态**/
    private String collectStatus;
    /**收藏人**/
    private String collectCreateBy;
}
