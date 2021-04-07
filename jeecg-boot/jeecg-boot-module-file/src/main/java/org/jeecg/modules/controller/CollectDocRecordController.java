package org.jeecg.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.entity.CollectDocRecord;
import org.jeecg.modules.entity.CollectFileRecord;
import org.jeecg.modules.service.ICollectDocRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 文档记录收藏表
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
@Api(tags="文档记录收藏表")
@RestController
@RequestMapping("/collectDocRecord")
@Slf4j
public class CollectDocRecordController extends JeecgController<CollectDocRecord, ICollectDocRecordService> {
	@Autowired
	private ICollectDocRecordService collectDocRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectDocRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-分页列表查询")
	@ApiOperation(value="文档记录收藏表-分页列表查询", notes="文档记录收藏表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectDocRecord collectDocRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CollectDocRecord> queryWrapper = QueryGenerator.initQueryWrapper(collectDocRecord, req.getParameterMap());
		Page<CollectDocRecord> page = new Page<CollectDocRecord>(pageNo, pageSize);
		IPage<CollectDocRecord> pageList = collectDocRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param collectDocRecord
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-添加")
	@ApiOperation(value="文档记录收藏表-添加", notes="文档记录收藏表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectDocRecord collectDocRecord) {
		collectDocRecordService.save(collectDocRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param collectDocRecord
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-编辑")
	@ApiOperation(value="文档记录收藏表-编辑", notes="文档记录收藏表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectDocRecord collectDocRecord) {
		collectDocRecordService.updateById(collectDocRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-通过id删除")
	@ApiOperation(value="文档记录收藏表-通过id删除", notes="文档记录收藏表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		collectDocRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	 /**
	  *   通过条件删除
	  *
	  * @param collectDocRecord
	  * @return
	  */
	 @AutoLog(value = "收藏记录表-通过条件删除")
	 @ApiOperation(value="收藏记录表-通过条件删除", notes="收藏记录表-通过id删除")
	 @PostMapping(value = "/deleteByField")
	 public Result<?> deleteByField(@RequestBody CollectDocRecord collectDocRecord) {

		 QueryWrapper queryWrapper = new QueryWrapper();
		 queryWrapper.eq("id",collectDocRecord.getId());
		 queryWrapper.eq("create_By",collectDocRecord.getCreateBy());

		 collectDocRecordService.remove(queryWrapper);
		 return Result.OK("删除成功!");
	 }
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-批量删除")
	@ApiOperation(value="文档记录收藏表-批量删除", notes="文档记录收藏表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.collectDocRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文档记录收藏表-通过id查询")
	@ApiOperation(value="文档记录收藏表-通过id查询", notes="文档记录收藏表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CollectDocRecord collectDocRecord = collectDocRecordService.getById(id);
		if(collectDocRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(collectDocRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectDocRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectDocRecord collectDocRecord) {
        return super.exportXls(request, collectDocRecord, CollectDocRecord.class, "文档记录收藏表");
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
        return super.importExcel(request, response, CollectDocRecord.class);
    }

}
