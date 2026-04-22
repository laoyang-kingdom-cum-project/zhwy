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
import com.ruoyi.system.domain.SleepData;
import com.ruoyi.system.service.ISleepDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 睡眠数据Controller
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@RestController
@RequestMapping("/system/data")
public class SleepDataController extends BaseController
{
    @Autowired
    private ISleepDataService sleepDataService;

    /**
     * 查询睡眠数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SleepData sleepData)
    {
        startPage();
        List<SleepData> list = sleepDataService.selectSleepDataList(sleepData);
        return getDataTable(list);
    }

    /**
     * 导出睡眠数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "睡眠数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SleepData sleepData)
    {
        List<SleepData> list = sleepDataService.selectSleepDataList(sleepData);
        ExcelUtil<SleepData> util = new ExcelUtil<SleepData>(SleepData.class);
        util.exportExcel(response, list, "睡眠数据数据");
    }

    /**
     * 获取睡眠数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sleepDataService.selectSleepDataById(id));
    }

    /**
     * 新增睡眠数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "睡眠数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SleepData sleepData)
    {
        return toAjax(sleepDataService.insertSleepData(sleepData));
    }

    /**
     * 修改睡眠数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "睡眠数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SleepData sleepData)
    {
        return toAjax(sleepDataService.updateSleepData(sleepData));
    }

    /**
     * 删除睡眠数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "睡眠数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sleepDataService.deleteSleepDataByIds(ids));
    }
}
