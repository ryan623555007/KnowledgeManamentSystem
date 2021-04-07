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
import org.jeecg.modules.entity.FileLog;
import org.jeecg.modules.service.IFileLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 文件日志表
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
@Api(tags="文件日志表")
@RestController
@RequestMapping("/fileLog/fileLog")
@Slf4j
public class FileLogController extends JeecgController<FileLog, IFileLogService> {
	@Autowired
	private IFileLogService fileLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param fileLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "文件日志表-分页列表查询")
	@ApiOperation(value="文件日志表-分页列表查询", notes="文件日志表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FileLog fileLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FileLog> queryWrapper = QueryGenerator.initQueryWrapper(fileLog, req.getParameterMap());
		Page<FileLog> page = new Page<FileLog>(pageNo, pageSize);
		IPage<FileLog> pageList = fileLogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param fileLog
	 * @returnrootList
	 */
	@AutoLog(value = "文件日志表-添加")
	@ApiOperation(value="文件日志表-添加", notes="文件日志表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FileLog fileLog) {
		fileLogService.save(fileLog);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param fileLog
	 * @return
	 */
	@AutoLog(value = "文件日志表-编辑")
	@ApiOperation(value="文件日志表-编辑", notes="文件日志表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FileLog fileLog) {
		fileLogService.updateById(fileLog);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件日志表-通过id删除")
	@ApiOperation(value="文件日志表-通过id删除", notes="文件日志表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		fileLogService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "文件日志表-批量删除")
	@ApiOperation(value="文件日志表-批量删除", notes="文件日志表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fileLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件日志表-通过id查询")
	@ApiOperation(value="文件日志表-通过id查询", notes="文件日志表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FileLog fileLog = fileLogService.getById(id);
		if(fileLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(fileLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param fileLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FileLog fileLog) {
        return super.exportXls(request, fileLog, FileLog.class, "文件日志表");
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
        return super.importExcel(request, response, FileLog.class);
    }

}
