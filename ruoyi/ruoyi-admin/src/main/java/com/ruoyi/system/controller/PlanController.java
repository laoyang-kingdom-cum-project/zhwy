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
import com.ruoyi.system.domain.Plan;
import com.ruoyi.system.service.IPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 计划Controller
 * 
 * @author ruoyi
 * @date 2026-01-20 21:21:50
 */
@RestController
@RequestMapping("/system/plan")
public class PlanController extends BaseController
{
    @Autowired
    private IPlanService planService;

    /**
     * 查询计划列表
     */
    @PreAuthorize("@ss.hasPermi('system:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(Plan plan)
    {
        startPage();
        List<Plan> list = planService.selectPlanList(plan);
        return getDataTable(list);
    }

    /**
     * 导出计划列表
     */
    @PreAuthorize("@ss.hasPermi('system:plan:export')")
    @Log(title = "计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Plan plan)
    {
        List<Plan> list = planService.selectPlanList(plan);
        ExcelUtil<Plan> util = new ExcelUtil<Plan>(Plan.class);
        util.exportExcel(response, list, "计划数据");
    }

    /**
     * 获取计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(planService.selectPlanById(id));
    }

    /**
     * 新增计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:add')")
    @Log(title = "计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Plan plan)
    {
        return toAjax(planService.insertPlan(plan));
    }

    /**
     * 修改计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:edit')")
    @Log(title = "计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Plan plan)
    {
        return toAjax(planService.updatePlan(plan));
    }

    /**
     * 删除计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:remove')")
    @Log(title = "计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(planService.deletePlanByIds(ids));
    }
}
