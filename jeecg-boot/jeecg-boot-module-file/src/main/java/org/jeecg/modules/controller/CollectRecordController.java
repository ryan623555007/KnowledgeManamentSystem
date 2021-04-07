package org.jeecg.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.entity.CollectRecord;
import org.jeecg.modules.service.ICollectRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 收藏记录表
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
@Api(tags="收藏记录表")
@RestController
@RequestMapping("/conllect/collectRecord")
@Slf4j
public class CollectRecordController extends JeecgController<CollectRecord, ICollectRecordService> {
	@Autowired
	private ICollectRecordService collectRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "收藏记录表-分页列表查询")
	@ApiOperation(value="收藏记录表-分页列表查询", notes="收藏记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectRecord collectRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CollectRecord> queryWrapper = QueryGenerator.initQueryWrapper(collectRecord, req.getParameterMap());
		Page<CollectRecord> page = new Page<CollectRecord>(pageNo, pageSize);
		IPage<CollectRecord> pageList = collectRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param collectRecord
	 * @return
	 */
	@AutoLog(value = "收藏记录表-添加")
	@ApiOperation(value="收藏记录表-添加", notes="收藏记录表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectRecord collectRecord) {
		collectRecordService.save(collectRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param collectRecord
	 * @return
	 */
	@AutoLog(value = "收藏记录表-编辑")
	@ApiOperation(value="收藏记录表-编辑", notes="收藏记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectRecord collectRecord) {
		collectRecordService.updateById(collectRecord);
		return Result.OK("编辑成功!");
	}
	
 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "收藏记录表-通过id删除")
	 @ApiOperation(value="收藏记录表-通过id删除", notes="收藏记录表-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		 collectRecordService.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *   通过条件删除
	  *
	  * @param collectRecord
	  * @return
	  */
	 @AutoLog(value = "收藏记录表-通过条件删除")
	 @ApiOperation(value="收藏记录表-通过条件删除", notes="收藏记录表-通过id删除")
	 @PostMapping(value = "/deleteByField")
	 public Result<?> deleteByField(@RequestBody CollectRecord collectRecord) {

		 QueryWrapper queryWrapper = new QueryWrapper();
		 queryWrapper.eq("id",collectRecord.getId());
		 queryWrapper.eq("createBy",collectRecord.getCreateBy());

		 collectRecordService.remove(queryWrapper);
		 return Result.OK("删除成功!");
	 }

	 /**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收藏记录表-批量删除")
	@ApiOperation(value="收藏记录表-批量删除", notes="收藏记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.collectRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收藏记录表-通过id查询")
	@ApiOperation(value="收藏记录表-通过id查询", notes="收藏记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CollectRecord collectRecord = collectRecordService.getById(id);
		if(collectRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(collectRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectRecord collectRecord) {
        return super.exportXls(request, collectRecord, CollectRecord.class, "收藏记录表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CollectRecord.class);
    }

}
