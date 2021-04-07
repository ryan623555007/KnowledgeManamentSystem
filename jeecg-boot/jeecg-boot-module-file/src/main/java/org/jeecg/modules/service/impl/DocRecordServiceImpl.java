package org.jeecg.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.entity.*;
import org.jeecg.modules.mapper.CollectDocRecordMapper;
import org.jeecg.modules.mapper.DocRecordMapper;
import org.jeecg.modules.service.IDocRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文档记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
@Service
public class DocRecordServiceImpl extends ServiceImpl<DocRecordMapper, DocRecord> implements IDocRecordService {

    @Resource
    private DocRecordMapper docRecordMapper;
    @Resource
    private CollectDocRecordMapper collectDocRecordMapper;

    @Override
    public IPage<DocRecordAndCollect> queryPageList(DocRecord docRecord, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        QueryWrapper<DocRecord> queryWrapper = QueryGenerator.initQueryWrapper(docRecord, req.getParameterMap());
        Page<DocRecord> page = new Page<DocRecord>(pageNo, pageSize);
        List<DocRecordAndCollect> list = new ArrayList<DocRecordAndCollect>();
        IPage<DocRecord> pageList = docRecordMapper.selectPage(page, queryWrapper);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<DocRecord> records = pageList.getRecords();
        if(!records.isEmpty()) {
            for (DocRecord docRecord1 : records) {
                DocRecordAndCollect docRecordAndCollect = new DocRecordAndCollect();
                docRecordAndCollect.setId(docRecord1.getId());
                docRecordAndCollect.setDocType(docRecord1.getDocType());
                docRecordAndCollect.setFileName(docRecord1.getFileName());
                docRecordAndCollect.setDescription(docRecord1.getDescription());
                docRecordAndCollect.setFileStatus(docRecord1.getFileStatus());
                docRecordAndCollect.setSqlText(docRecord1.getSqlText());
                docRecordAndCollect.setCreateBy(docRecord1.getCreateBy());
                docRecordAndCollect.setCreateTime(docRecord1.getCreateTime());
                docRecordAndCollect.setUpdateBy(docRecord1.getUpdateBy());
                docRecordAndCollect.setUpdateTime(docRecord1.getUpdateTime());

                LambdaQueryWrapper<CollectDocRecord> thirdQuery = new LambdaQueryWrapper<>();
                thirdQuery.eq(CollectDocRecord::getId, docRecord1.getId());
                thirdQuery.eq(CollectDocRecord::getCreateBy, sysUser.getUsername());
                CollectDocRecord collectDocRecord = collectDocRecordMapper.selectOne(thirdQuery);
                docRecordAndCollect.setCollectStatus("1");//1：未收藏
                if (collectDocRecord != null) {

                    docRecordAndCollect.setCollectStatus(collectDocRecord.getCollectStatus());//0：已收藏
                    docRecordAndCollect.setCollectCreateBy(collectDocRecord.getCreateBy());
                }

                list.add(docRecordAndCollect);
            }
        }
        IPage<DocRecordAndCollect> result = new Page<DocRecordAndCollect>(page.getCurrent(), page.getSize(), list.size());
        result.setRecords(list);
        return result;
    }
}
