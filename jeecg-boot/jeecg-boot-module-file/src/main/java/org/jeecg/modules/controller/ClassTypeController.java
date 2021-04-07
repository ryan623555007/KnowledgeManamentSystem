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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.entity.ClassType;
import org.jeecg.modules.service.IClassTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 类别字典
 * @Author: jeecg-boot
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Api(tags="类别字典")
@RestController
@RequestMapping("/ClassType/classType")
@Slf4j
public class ClassTypeController extends JeecgController<ClassType, IClassTypeService> {
	@Autowired
	private IClassTypeService classTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param classType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "类别字典-分页列表查询")
	@ApiOperation(value="类别字典-分页列表查询", notes="类别字典-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(ClassType classType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<ClassType> queryWrapper =  QueryGenerator.initQueryWrapper(classType, req.getParameterMap());
            List<ClassType> list = classTypeService.queryTreeListNoPage(queryWrapper);
            IPage<ClassType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = classType.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            classType.setPid(null);
            QueryWrapper<ClassType> queryWrapper = QueryGenerator.initQueryWrapper(classType, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<ClassType> page = new Page<ClassType>(pageNo, pageSize);
            IPage<ClassType> pageList = classTypeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param classType
      * @param req
      * @return
      */
	@AutoLog(value = "类别字典-获取子数据")
	@ApiOperation(value="类别字典-获取子数据", notes="类别字典-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(ClassType classType,HttpServletRequest req) {
		QueryWrapper<ClassType> queryWrapper = QueryGenerator.initQueryWrapper(classType, req.getParameterMap());
		List<ClassType> list = classTypeService.list(queryWrapper);
		IPage<ClassType> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "类别字典-批量获取子数据")
    @ApiOperation(value="类别字典-批量获取子数据", notes="类别字典-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<ClassType> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<ClassType> list = classTypeService.list(queryWrapper);
            IPage<ClassType> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param classType
	 * @return
	 */
	@AutoLog(value = "类别字典-添加")
	@ApiOperation(value="类别字典-添加", notes="类别字典-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ClassType classType) {
		classTypeService.addClassType(classType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param classType
	 * @return
	 */
	@AutoLog(value = "类别字典-编辑")
	@ApiOperation(value="类别字典-编辑", notes="类别字典-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ClassType classType) {
		classTypeService.updateClassType(classType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "类别字典-通过id删除")
	@ApiOperation(value="类别字典-通过id删除", notes="类别字典-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		classTypeService.deleteClassType(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "类别字典-批量删除")
	@ApiOperation(value="类别字典-批量删除", notes="类别字典-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.classTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "类别字典-通过id查询")
	@ApiOperation(value="类别字典-通过id查询", notes="类别字典-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ClassType classType = classTypeService.getById(id);
		if(classType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(classType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param classType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ClassType classType) {
		return super.exportXls(request, classType, ClassType.class, "类别字典");
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
		return super.importExcel(request, response, ClassType.class);
    }

}
