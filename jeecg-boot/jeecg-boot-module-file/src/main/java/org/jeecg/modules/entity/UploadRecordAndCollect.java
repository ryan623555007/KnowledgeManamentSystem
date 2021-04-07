package org.jeecg.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


@Data
public class UploadRecordAndCollect implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**文件类别*/
    @Excel(name = "文件类别", width = 15, dicCode = "file_class")
    @Dict(dicCode = "file_class")
    @ApiModelProperty(value = "文件类别")
    private java.lang.String fileClass;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
    /**审批状态*/
    @Excel(name = "审批状态", width = 15, dicCode = "file_status")
    @Dict(dicCode = "file_status")
    @ApiModelProperty(value = "审批状态")
    private java.lang.String fileStatus;
    /**文件*/
    @Excel(name = "文件", width = 15)
    @ApiModelProperty(value = "文件")
    private java.lang.String files;
    /**上传者*/
    @ApiModelProperty(value = "上传者")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;

    /**收藏状态**/
    private String collectStatus;

    /**收藏人**/
    private String collectCreateBy;

}
