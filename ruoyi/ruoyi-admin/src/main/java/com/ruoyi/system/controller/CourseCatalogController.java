package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CourseCatalog;
import com.ruoyi.system.service.ICourseCatalogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程目录Controller
 * 
 * @author ruoyi
 * @date 2026-01-21 15:23:49
 */
@RestController
@RequestMapping("/system/catalog")
public class CourseCatalogController extends BaseController
{
    @Autowired
    private ICourseCatalogService courseCatalogService;

    /**
     * 查询课程目录列表
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseCatalog courseCatalog)
    {
        startPage();
        List<CourseCatalog> list = courseCatalogService.selectCourseCatalogList(courseCatalog);
        return getDataTable(list);
    }

    /**
     * 导出课程目录列表
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:export')")
    @Log(title = "课程目录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseCatalog courseCatalog)
    {
        List<CourseCatalog> list = courseCatalogService.selectCourseCatalogList(courseCatalog);
        ExcelUtil<CourseCatalog> util = new ExcelUtil<CourseCatalog>(CourseCatalog.class);
        util.exportExcel(response, list, "课程目录数据");
    }

    /**
     * 获取课程目录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(courseCatalogService.selectCourseCatalogById(id));
    }

    /**
     * 新增课程目录
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:add')")
    @Log(title = "课程目录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseCatalog courseCatalog)
    {
        return toAjax(courseCatalogService.insertCourseCatalog(courseCatalog));
    }

    /**
     * 修改课程目录
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:edit')")
    @Log(title = "课程目录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseCatalog courseCatalog)
    {
        return toAjax(courseCatalogService.updateCourseCatalog(courseCatalog));
    }

    /**
     * 删除课程目录
     */
    @PreAuthorize("@ss.hasPermi('system:catalog:remove')")
    @Log(title = "课程目录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseCatalogService.deleteCourseCatalogByIds(ids));
    }
}
