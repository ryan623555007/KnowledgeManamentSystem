package org.jeecg.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.entity.UploadRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.entity.UploadRecordAndCollect;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 上传文件记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
public interface IUploadRecordService extends IService<UploadRecord> {

     IPage<UploadRecordAndCollect> queryPageList(UploadRecord uploadRecord, Integer pageNo, Integer pageSize, HttpServletRequest req);

}
