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
import com.ruoyi.system.domain.Warning;
import com.ruoyi.system.service.IWarningService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 安全预警Controller
 * 
 * @author ruoyi
 * @date 2026-04-16 20:50:36
 */
@RestController
@RequestMapping("/system/warning")
public class WarningController extends BaseController
{
    @Autowired
    private IWarningService warningService;

    /**
     * 查询安全预警列表
     */
    @PreAuthorize("@ss.hasPermi('system:warning:list')")
    @GetMapping("/list")
    public TableDataInfo list(Warning warning)
    {
        startPage();
        List<Warning> list = warningService.selectWarningList(warning);
        return getDataTable(list);
    }

    /**
     * 导出安全预警列表
     */
    @PreAuthorize("@ss.hasPermi('system:warning:export')")
    @Log(title = "安全预警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Warning warning)
    {
        List<Warning> list = warningService.selectWarningList(warning);
        ExcelUtil<Warning> util = new ExcelUtil<Warning>(Warning.class);
        util.exportExcel(response, list, "安全预警数据");
    }

    /**
     * 获取安全预警详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:warning:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(warningService.selectWarningById(id));
    }

    /**
     * 新增安全预警
     */
    @PreAuthorize("@ss.hasPermi('system:warning:add')")
    @Log(title = "安全预警", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Warning warning)
    {
        return toAjax(warningService.insertWarning(warning));
    }

    /**
     * 修改安全预警
     */
    @PreAuthorize("@ss.hasPermi('system:warning:edit')")
    @Log(title = "安全预警", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Warning warning)
    {
        return toAjax(warningService.updateWarning(warning));
    }

    /**
     * 删除安全预警
     */
    @PreAuthorize("@ss.hasPermi('system:warning:remove')")
    @Log(title = "安全预警", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(warningService.deleteWarningByIds(ids));
    }
}
