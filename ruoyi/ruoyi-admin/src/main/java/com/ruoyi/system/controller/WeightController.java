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
import com.ruoyi.system.domain.Weight;
import com.ruoyi.system.service.IWeightService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 体重Controller
 * 
 * @author ruoyi
 * @date 2026-01-15 15:18:23
 */
@RestController
@RequestMapping("/system/weight")
public class WeightController extends BaseController
{
    @Autowired
    private IWeightService weightService;

    /**
     * 查询体重列表
     */
    @PreAuthorize("@ss.hasPermi('system:weight:list')")
    @GetMapping("/list")
    public TableDataInfo list(Weight weight)
    {
        startPage();
        List<Weight> list = weightService.selectWeightList(weight);
        return getDataTable(list);
    }

    /**
     * 导出体重列表
     */
    @PreAuthorize("@ss.hasPermi('system:weight:export')")
    @Log(title = "体重", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Weight weight)
    {
        List<Weight> list = weightService.selectWeightList(weight);
        ExcelUtil<Weight> util = new ExcelUtil<Weight>(Weight.class);
        util.exportExcel(response, list, "体重数据");
    }

    /**
     * 获取体重详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:weight:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(weightService.selectWeightById(id));
    }

    /**
     * 新增体重
     */
    @PreAuthorize("@ss.hasPermi('system:weight:add')")
    @Log(title = "体重", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Weight weight)
    {
        return toAjax(weightService.insertWeight(weight));
    }

    /**
     * 修改体重
     */
    @PreAuthorize("@ss.hasPermi('system:weight:edit')")
    @Log(title = "体重", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Weight weight)
    {
        return toAjax(weightService.updateWeight(weight));
    }

    /**
     * 删除体重
     */
    @PreAuthorize("@ss.hasPermi('system:weight:remove')")
    @Log(title = "体重", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weightService.deleteWeightByIds(ids));
    }
}
