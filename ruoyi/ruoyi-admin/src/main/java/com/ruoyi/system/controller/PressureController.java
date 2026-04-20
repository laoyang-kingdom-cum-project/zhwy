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
import com.ruoyi.system.domain.Pressure;
import com.ruoyi.system.service.IPressureService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 血压Controller
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@RestController
@RequestMapping("/system/pressure")
public class PressureController extends BaseController
{
    @Autowired
    private IPressureService pressureService;

    /**
     * 查询血压列表
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:list')")
    @GetMapping("/list")
    public TableDataInfo list(Pressure pressure)
    {
        startPage();
        List<Pressure> list = pressureService.selectPressureList(pressure);
        return getDataTable(list);
    }

    /**
     * 导出血压列表
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:export')")
    @Log(title = "血压", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pressure pressure)
    {
        List<Pressure> list = pressureService.selectPressureList(pressure);
        ExcelUtil<Pressure> util = new ExcelUtil<Pressure>(Pressure.class);
        util.exportExcel(response, list, "血压数据");
    }

    /**
     * 获取血压详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pressureService.selectPressureById(id));
    }

    /**
     * 新增血压
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:add')")
    @Log(title = "血压", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Pressure pressure)
    {
        return toAjax(pressureService.insertPressure(pressure));
    }

    /**
     * 修改血压
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:edit')")
    @Log(title = "血压", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Pressure pressure)
    {
        return toAjax(pressureService.updatePressure(pressure));
    }

    /**
     * 删除血压
     */
    @PreAuthorize("@ss.hasPermi('system:pressure:remove')")
    @Log(title = "血压", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pressureService.deletePressureByIds(ids));
    }
}
