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
import com.ruoyi.system.domain.Captcha;
import com.ruoyi.system.service.ICaptchaService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 验证码池Controller
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/captcha")
public class CaptchaController extends BaseController
{
    @Autowired
    private ICaptchaService captchaService;

    /**
     * 查询验证码池列表
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:list')")
    @GetMapping("/list")
    public TableDataInfo list(Captcha captcha)
    {
        startPage();
        List<Captcha> list = captchaService.selectCaptchaList(captcha);
        return getDataTable(list);
    }

    /**
     * 导出验证码池列表
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:export')")
    @Log(title = "验证码池", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Captcha captcha)
    {
        List<Captcha> list = captchaService.selectCaptchaList(captcha);
        ExcelUtil<Captcha> util = new ExcelUtil<Captcha>(Captcha.class);
        util.exportExcel(response, list, "验证码池数据");
    }

    /**
     * 获取验证码池详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(captchaService.selectCaptchaById(id));
    }

    /**
     * 新增验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:add')")
    @Log(title = "验证码池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Captcha captcha)
    {
        return toAjax(captchaService.insertCaptcha(captcha));
    }

    /**
     * 修改验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:edit')")
    @Log(title = "验证码池", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Captcha captcha)
    {
        return toAjax(captchaService.updateCaptcha(captcha));
    }

    /**
     * 删除验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:captcha:remove')")
    @Log(title = "验证码池", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(captchaService.deleteCaptchaByIds(ids));
    }
}
