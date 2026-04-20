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
import com.ruoyi.system.domain.Mycaptcha;
import com.ruoyi.system.service.IMycaptchaService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 验证码池Controller
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/mycaptcha")
public class MycaptchaController extends BaseController
{
    @Autowired
    private IMycaptchaService mycaptchaService;

    /**
     * 查询验证码池列表
     */

    @GetMapping("/list")
    public TableDataInfo list(Mycaptcha mycaptcha)
    {
        startPage();
        List<Mycaptcha> list = mycaptchaService.selectMycaptchaList(mycaptcha);
        return getDataTable(list);
    }

    /**
     * 导出验证码池列表
     */
    @PreAuthorize("@ss.hasPermi('system:mycaptcha:export')")
    @Log(title = "验证码池", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Mycaptcha mycaptcha)
    {
        List<Mycaptcha> list = mycaptchaService.selectMycaptchaList(mycaptcha);
        ExcelUtil<Mycaptcha> util = new ExcelUtil<Mycaptcha>(Mycaptcha.class);
        util.exportExcel(response, list, "验证码池数据");
    }

    /**
     * 获取验证码池详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mycaptchaService.selectMycaptchaById(id));
    }

    /**
     * 新增验证码池
     */

    @Log(title = "验证码池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Mycaptcha mycaptcha)
    {
        return toAjax(mycaptchaService.insertMycaptcha(mycaptcha));
    }

    /**
     * 修改验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:mycaptcha:edit')")
    @Log(title = "验证码池", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Mycaptcha mycaptcha)
    {
        return toAjax(mycaptchaService.updateMycaptcha(mycaptcha));
    }

    /**
     * 删除验证码池
     */
    @PreAuthorize("@ss.hasPermi('system:mycaptcha:remove')")
    @Log(title = "验证码池", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mycaptchaService.deleteMycaptchaByIds(ids));
    }
}
