package org.jeecg.modules.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.entity.FileLog;
import org.jeecg.modules.mapper.FileLogMapper;
import org.jeecg.modules.service.IFileLogService;
import org.springframework.stereotype.Service;

/**
 * @Description: 文件日志表
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
@Service
public class FileLogServiceImpl extends ServiceImpl<FileLogMapper, FileLog> implements IFileLogService {

}
