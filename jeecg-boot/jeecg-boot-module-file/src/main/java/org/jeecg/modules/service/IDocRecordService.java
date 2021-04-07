package org.jeecg.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.entity.DocRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.entity.DocRecordAndCollect;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 文档记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
public interface IDocRecordService extends IService<DocRecord> {

    IPage<DocRecordAndCollect>  queryPageList(DocRecord docRecord,Integer pageNo, Integer pageSize, HttpServletRequest req);

}
