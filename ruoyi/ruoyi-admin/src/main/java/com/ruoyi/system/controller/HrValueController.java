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
import com.ruoyi.system.domain.HrValue;
import com.ruoyi.system.service.IHrValueService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 心率数据Controller
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@RestController
@RequestMapping("/system/value")
public class HrValueController extends BaseController
{
    @Autowired
    private IHrValueService hrValueService;

    /**
     * 查询心率数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrValue hrValue)
    {
        startPage();
        List<HrValue> list = hrValueService.selectHrValueList(hrValue);
        return getDataTable(list);
    }

    /**
     * 导出心率数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:value:export')")
    @Log(title = "心率数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrValue hrValue)
    {
        List<HrValue> list = hrValueService.selectHrValueList(hrValue);
        ExcelUtil<HrValue> util = new ExcelUtil<HrValue>(HrValue.class);
        util.exportExcel(response, list, "心率数据数据");
    }

    /**
     * 获取心率数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:value:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hrValueService.selectHrValueById(id));
    }

    /**
     * 新增心率数据
     */
    @PreAuthorize("@ss.hasPermi('system:value:add')")
    @Log(title = "心率数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrValue hrValue)
    {
        return toAjax(hrValueService.insertHrValue(hrValue));
    }

    /**
     * 修改心率数据
     */
    @PreAuthorize("@ss.hasPermi('system:value:edit')")
    @Log(title = "心率数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrValue hrValue)
    {
        return toAjax(hrValueService.updateHrValue(hrValue));
    }

    /**
     * 删除心率数据
     */
    @PreAuthorize("@ss.hasPermi('system:value:remove')")
    @Log(title = "心率数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hrValueService.deleteHrValueByIds(ids));
    }
}
