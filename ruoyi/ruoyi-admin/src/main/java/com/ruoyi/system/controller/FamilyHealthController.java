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
import com.ruoyi.system.domain.FamilyHealth;
import com.ruoyi.system.service.IFamilyHealthService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 家人健康Controller
 * 
 * @author ruoyi
 * @date 2026-04-23 10:25:07
 */
@RestController
@RequestMapping("/system/health")
public class FamilyHealthController extends BaseController
{
    @Autowired
    private IFamilyHealthService familyHealthService;

    /**
     * 查询家人健康列表
     */
    @PreAuthorize("@ss.hasPermi('system:health:list')")
    @GetMapping("/list")
    public TableDataInfo list(FamilyHealth familyHealth)
    {
        startPage();
        List<FamilyHealth> list = familyHealthService.selectFamilyHealthList(familyHealth);
        return getDataTable(list);
    }

    /**
     * 导出家人健康列表
     */
    @PreAuthorize("@ss.hasPermi('system:health:export')")
    @Log(title = "家人健康", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FamilyHealth familyHealth)
    {
        List<FamilyHealth> list = familyHealthService.selectFamilyHealthList(familyHealth);
        ExcelUtil<FamilyHealth> util = new ExcelUtil<FamilyHealth>(FamilyHealth.class);
        util.exportExcel(response, list, "家人健康数据");
    }

    /**
     * 获取家人健康详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:health:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(familyHealthService.selectFamilyHealthById(id));
    }

    /**
     * 新增家人健康
     */
    @PreAuthorize("@ss.hasPermi('system:health:add')")
    @Log(title = "家人健康", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FamilyHealth familyHealth)
    {
        return toAjax(familyHealthService.insertFamilyHealth(familyHealth));
    }

    /**
     * 修改家人健康
     */
    @PreAuthorize("@ss.hasPermi('system:health:edit')")
    @Log(title = "家人健康", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FamilyHealth familyHealth)
    {
        return toAjax(familyHealthService.updateFamilyHealth(familyHealth));
    }

    /**
     * 删除家人健康
     */
    @PreAuthorize("@ss.hasPermi('system:health:remove')")
    @Log(title = "家人健康", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(familyHealthService.deleteFamilyHealthByIds(ids));
    }
}
