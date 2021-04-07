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
import org.jeecg.modules.entity.CollectFileRecord;
import org.jeecg.modules.entity.CollectRecord;
import org.jeecg.modules.service.ICollectFileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 文件收藏记录
 * @Author: jeecg-boot
 * @Date:   2021-01-31
 * @Version: V1.0
 */
@Api(tags="文件收藏记录")
@RestController
@RequestMapping("/collectFileRecord")
@Slf4j
public class CollectFileRecordController extends JeecgController<CollectFileRecord, ICollectFileRecordService> {
	@Autowired
	private ICollectFileRecordService collectFileRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectFileRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-分页列表查询")
	@ApiOperation(value="文件收藏记录-分页列表查询", notes="文件收藏记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectFileRecord collectFileRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CollectFileRecord> queryWrapper = QueryGenerator.initQueryWrapper(collectFileRecord, req.getParameterMap());
		Page<CollectFileRecord> page = new Page<CollectFileRecord>(pageNo, pageSize);
		IPage<CollectFileRecord> pageList = collectFileRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param collectFileRecord
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-添加")
	@ApiOperation(value="文件收藏记录-添加", notes="文件收藏记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectFileRecord collectFileRecord) {
		collectFileRecordService.save(collectFileRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param collectFileRecord
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-编辑")
	@ApiOperation(value="文件收藏记录-编辑", notes="文件收藏记录-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectFileRecord collectFileRecord) {
		collectFileRecordService.updateById(collectFileRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-通过id删除")
	@ApiOperation(value="文件收藏记录-通过id删除", notes="文件收藏记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		collectFileRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	 /**
	  *   通过条件删除
	  *
	  * @param collectFileRecord
	  * @return
	  */
	 @AutoLog(value = "收藏记录表-通过条件删除")
	 @ApiOperation(value="收藏记录表-通过条件删除", notes="收藏记录表-通过id删除")
	 @PostMapping(value = "/deleteByField")
	 public Result<?> deleteByField(@RequestBody CollectFileRecord collectFileRecord) {

		 QueryWrapper queryWrapper = new QueryWrapper();
		 queryWrapper.eq("id",collectFileRecord.getId());
		 queryWrapper.eq("create_by",collectFileRecord.getCreateBy());

		 collectFileRecordService.remove(queryWrapper);
		 return Result.OK("删除成功!");
	 }
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-批量删除")
	@ApiOperation(value="文件收藏记录-批量删除", notes="文件收藏记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.collectFileRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件收藏记录-通过id查询")
	@ApiOperation(value="文件收藏记录-通过id查询", notes="文件收藏记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CollectFileRecord collectFileRecord = collectFileRecordService.getById(id);
		if(collectFileRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(collectFileRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectFileRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectFileRecord collectFileRecord) {
        return super.exportXls(request, collectFileRecord, CollectFileRecord.class, "文件收藏记录");
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
        return super.importExcel(request, response, CollectFileRecord.class);
    }

}
