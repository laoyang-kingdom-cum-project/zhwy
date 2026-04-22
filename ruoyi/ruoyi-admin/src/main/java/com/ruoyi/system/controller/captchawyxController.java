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
import com.ruoyi.system.domain.captchawyx;
import com.ruoyi.system.service.IcaptchawyxService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 验证码池Controller
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/captchawyx")
public class captchawyxController extends BaseController
{
    @Autowired
    private IcaptchawyxService captchawyxService;

    /**
     * 查询验证码池列表
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:list')")
    @GetMapping("/list")
    public TableDataInfo list(captchawyx captchawyx)
    {
        startPage();
        List<captchawyx> list = captchawyxService.selectcaptchawyxList(captchawyx);
        return getDataTable(list);
    }

    /**
     * 导出验证码池列表
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:export')")
    @Log(title = "验证码池", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, captchawyx captchawyx)
    {
        List<captchawyx> list = captchawyxService.selectcaptchawyxList(captchawyx);
        ExcelUtil<captchawyx> util = new ExcelUtil<captchawyx>(captchawyx.class);
        util.exportExcel(response, list, "验证码池数据");
    }

    /**
     * 获取验证码池详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(captchawyxService.selectcaptchawyxById(id));
    }

    /**
     * 新增验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:add')")
    @Log(title = "验证码池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody captchawyx captchawyx)
    {
        return toAjax(captchawyxService.insertcaptchawyx(captchawyx));
    }

    /**
     * 修改验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:edit')")
    @Log(title = "验证码池", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody captchawyx captchawyx)
    {
        return toAjax(captchawyxService.updatecaptchawyx(captchawyx));
    }

    /**
     * 删除验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captchawyx:remove')")
    @Log(title = "验证码池", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(captchawyxService.deletecaptchawyxByIds(ids));
    }
}
