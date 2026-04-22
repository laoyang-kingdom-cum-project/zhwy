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
import com.ruoyi.system.domain.BloodSugar;
import com.ruoyi.system.service.IBloodSugarService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 血糖Controller
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@RestController
@RequestMapping("/system/sugar")
public class BloodSugarController extends BaseController
{
    @Autowired
    private IBloodSugarService bloodSugarService;

    /**
     * 查询血糖列表
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:list')")
    @GetMapping("/list")
    public TableDataInfo list(BloodSugar bloodSugar)
    {
        startPage();
        List<BloodSugar> list = bloodSugarService.selectBloodSugarList(bloodSugar);
        return getDataTable(list);
    }

    /**
     * 导出血糖列表
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:export')")
    @Log(title = "血糖", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BloodSugar bloodSugar)
    {
        List<BloodSugar> list = bloodSugarService.selectBloodSugarList(bloodSugar);
        ExcelUtil<BloodSugar> util = new ExcelUtil<BloodSugar>(BloodSugar.class);
        util.exportExcel(response, list, "血糖数据");
    }

    /**
     * 获取血糖详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bloodSugarService.selectBloodSugarById(id));
    }

    /**
     * 新增血糖
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:add')")
    @Log(title = "血糖", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BloodSugar bloodSugar)
    {
        return toAjax(bloodSugarService.insertBloodSugar(bloodSugar));
    }

    /**
     * 修改血糖
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:edit')")
    @Log(title = "血糖", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BloodSugar bloodSugar)
    {
        return toAjax(bloodSugarService.updateBloodSugar(bloodSugar));
    }

    /**
     * 删除血糖
     */
    @PreAuthorize("@ss.hasPermi('system:sugar:remove')")
    @Log(title = "血糖", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bloodSugarService.deleteBloodSugarByIds(ids));
    }
}
