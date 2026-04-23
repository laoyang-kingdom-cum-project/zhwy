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
import com.ruoyi.system.domain.WyxDeviceFault;
import com.ruoyi.system.service.IWyxDeviceFaultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备故障记录Controller
 * 
 * @author ruoyi
 * @date 2026-04-23 17:10:30
 */
@RestController
@RequestMapping("/system/fault")
public class WyxDeviceFaultController extends BaseController
{
    @Autowired
    private IWyxDeviceFaultService wyxDeviceFaultService;

    /**
     * 查询设备故障记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:fault:list')")
    @GetMapping("/list")
    public TableDataInfo list(WyxDeviceFault wyxDeviceFault)
    {
        startPage();
        List<WyxDeviceFault> list = wyxDeviceFaultService.selectWyxDeviceFaultList(wyxDeviceFault);
        return getDataTable(list);
    }

    /**
     * 导出设备故障记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:fault:export')")
    @Log(title = "设备故障记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WyxDeviceFault wyxDeviceFault)
    {
        List<WyxDeviceFault> list = wyxDeviceFaultService.selectWyxDeviceFaultList(wyxDeviceFault);
        ExcelUtil<WyxDeviceFault> util = new ExcelUtil<WyxDeviceFault>(WyxDeviceFault.class);
        util.exportExcel(response, list, "设备故障记录数据");
    }

    /**
     * 获取设备故障记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fault:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wyxDeviceFaultService.selectWyxDeviceFaultById(id));
    }

    /**
     * 新增设备故障记录
     */
    @PreAuthorize("@ss.hasPermi('system:fault:add')")
    @Log(title = "设备故障记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WyxDeviceFault wyxDeviceFault)
    {
        return toAjax(wyxDeviceFaultService.insertWyxDeviceFault(wyxDeviceFault));
    }

    /**
     * 修改设备故障记录
     */
    @PreAuthorize("@ss.hasPermi('system:fault:edit')")
    @Log(title = "设备故障记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WyxDeviceFault wyxDeviceFault)
    {
        return toAjax(wyxDeviceFaultService.updateWyxDeviceFault(wyxDeviceFault));
    }

    /**
     * 删除设备故障记录
     */
    @PreAuthorize("@ss.hasPermi('system:fault:remove')")
    @Log(title = "设备故障记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wyxDeviceFaultService.deleteWyxDeviceFaultByIds(ids));
    }
}
