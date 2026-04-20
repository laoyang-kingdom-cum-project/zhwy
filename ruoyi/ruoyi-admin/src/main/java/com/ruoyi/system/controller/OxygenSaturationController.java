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
import com.ruoyi.system.domain.OxygenSaturation;
import com.ruoyi.system.service.IOxygenSaturationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 血氧饱和度Controller
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@RestController
@RequestMapping("/system/saturation")
public class OxygenSaturationController extends BaseController
{
    @Autowired
    private IOxygenSaturationService oxygenSaturationService;

    /**
     * 查询血氧饱和度列表
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:list')")
    @GetMapping("/list")
    public TableDataInfo list(OxygenSaturation oxygenSaturation)
    {
        startPage();
        List<OxygenSaturation> list = oxygenSaturationService.selectOxygenSaturationList(oxygenSaturation);
        return getDataTable(list);
    }

    /**
     * 导出血氧饱和度列表
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:export')")
    @Log(title = "血氧饱和度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OxygenSaturation oxygenSaturation)
    {
        List<OxygenSaturation> list = oxygenSaturationService.selectOxygenSaturationList(oxygenSaturation);
        ExcelUtil<OxygenSaturation> util = new ExcelUtil<OxygenSaturation>(OxygenSaturation.class);
        util.exportExcel(response, list, "血氧饱和度数据");
    }

    /**
     * 获取血氧饱和度详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oxygenSaturationService.selectOxygenSaturationById(id));
    }

    /**
     * 新增血氧饱和度
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:add')")
    @Log(title = "血氧饱和度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OxygenSaturation oxygenSaturation)
    {
        return toAjax(oxygenSaturationService.insertOxygenSaturation(oxygenSaturation));
    }

    /**
     * 修改血氧饱和度
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:edit')")
    @Log(title = "血氧饱和度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OxygenSaturation oxygenSaturation)
    {
        return toAjax(oxygenSaturationService.updateOxygenSaturation(oxygenSaturation));
    }

    /**
     * 删除血氧饱和度
     */
    @PreAuthorize("@ss.hasPermi('system:saturation:remove')")
    @Log(title = "血氧饱和度", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oxygenSaturationService.deleteOxygenSaturationByIds(ids));
    }
}
