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
import com.ruoyi.system.domain.Stress;
import com.ruoyi.system.service.IStressService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 压力Controller
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@RestController
@RequestMapping("/system/stress")
public class StressController extends BaseController
{
    @Autowired
    private IStressService stressService;

    /**
     * 查询压力列表
     */
    @PreAuthorize("@ss.hasPermi('system:stress:list')")
    @GetMapping("/list")
    public TableDataInfo list(Stress stress)
    {
        startPage();
        List<Stress> list = stressService.selectStressList(stress);
        return getDataTable(list);
    }

    /**
     * 导出压力列表
     */
    @PreAuthorize("@ss.hasPermi('system:stress:export')")
    @Log(title = "压力", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Stress stress)
    {
        List<Stress> list = stressService.selectStressList(stress);
        ExcelUtil<Stress> util = new ExcelUtil<Stress>(Stress.class);
        util.exportExcel(response, list, "压力数据");
    }

    /**
     * 获取压力详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stressService.selectStressById(id));
    }

    /**
     * 新增压力
     */
    @PreAuthorize("@ss.hasPermi('system:stress:add')")
    @Log(title = "压力", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Stress stress)
    {
        return toAjax(stressService.insertStress(stress));
    }

    /**
     * 修改压力
     */
    @PreAuthorize("@ss.hasPermi('system:stress:edit')")
    @Log(title = "压力", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Stress stress)
    {
        return toAjax(stressService.updateStress(stress));
    }

    /**
     * 删除压力
     */
    @PreAuthorize("@ss.hasPermi('system:stress:remove')")
    @Log(title = "压力", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stressService.deleteStressByIds(ids));
    }
}
