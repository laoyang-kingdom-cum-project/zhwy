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
import com.ruoyi.system.domain.CommonPhone;
import com.ruoyi.system.service.ICommonPhoneService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 常用电话Controller
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:27
 */
@RestController
@RequestMapping("/system/phone")
public class CommonPhoneController extends BaseController
{
    @Autowired
    private ICommonPhoneService commonPhoneService;

    /**
     * 查询常用电话列表
     */
    @PreAuthorize("@ss.hasPermi('system:phone:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommonPhone commonPhone)
    {
        startPage();
        List<CommonPhone> list = commonPhoneService.selectCommonPhoneList(commonPhone);
        return getDataTable(list);
    }

    /**
     * 导出常用电话列表
     */
    @PreAuthorize("@ss.hasPermi('system:phone:export')")
    @Log(title = "常用电话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommonPhone commonPhone)
    {
        List<CommonPhone> list = commonPhoneService.selectCommonPhoneList(commonPhone);
        ExcelUtil<CommonPhone> util = new ExcelUtil<CommonPhone>(CommonPhone.class);
        util.exportExcel(response, list, "常用电话数据");
    }

    /**
     * 获取常用电话详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:phone:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(commonPhoneService.selectCommonPhoneById(id));
    }

    /**
     * 新增常用电话
     */
    @PreAuthorize("@ss.hasPermi('system:phone:add')")
    @Log(title = "常用电话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommonPhone commonPhone)
    {
        return toAjax(commonPhoneService.insertCommonPhone(commonPhone));
    }

    /**
     * 修改常用电话
     */
    @PreAuthorize("@ss.hasPermi('system:phone:edit')")
    @Log(title = "常用电话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommonPhone commonPhone)
    {
        return toAjax(commonPhoneService.updateCommonPhone(commonPhone));
    }

    /**
     * 删除常用电话
     */
    @PreAuthorize("@ss.hasPermi('system:phone:remove')")
    @Log(title = "常用电话", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(commonPhoneService.deleteCommonPhoneByIds(ids));
    }
}
