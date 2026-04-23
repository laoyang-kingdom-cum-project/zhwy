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
import com.ruoyi.system.domain.VisitorAppointment;
import com.ruoyi.system.service.IVisitorAppointmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客预约Controller
 * 
 * @author ruoyi
 * @date 2026-04-23 11:04:09
 */
@RestController
@RequestMapping("/system/appointment")
public class VisitorAppointmentController extends BaseController
{
    @Autowired
    private IVisitorAppointmentService visitorAppointmentService;

    /**
     * 查询访客预约列表
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(VisitorAppointment visitorAppointment)
    {
        startPage();
        List<VisitorAppointment> list = visitorAppointmentService.selectVisitorAppointmentList(visitorAppointment);
        return getDataTable(list);
    }

    /**
     * 导出访客预约列表
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:export')")
    @Log(title = "访客预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VisitorAppointment visitorAppointment)
    {
        List<VisitorAppointment> list = visitorAppointmentService.selectVisitorAppointmentList(visitorAppointment);
        ExcelUtil<VisitorAppointment> util = new ExcelUtil<VisitorAppointment>(VisitorAppointment.class);
        util.exportExcel(response, list, "访客预约数据");
    }

    /**
     * 获取访客预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(visitorAppointmentService.selectVisitorAppointmentById(id));
    }

    /**
     * 新增访客预约
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:add')")
    @Log(title = "访客预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VisitorAppointment visitorAppointment)
    {
        return toAjax(visitorAppointmentService.insertVisitorAppointment(visitorAppointment));
    }

    /**
     * 修改访客预约
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:edit')")
    @Log(title = "访客预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VisitorAppointment visitorAppointment)
    {
        return toAjax(visitorAppointmentService.updateVisitorAppointment(visitorAppointment));
    }

    /**
     * 删除访客预约
     */
    @PreAuthorize("@ss.hasPermi('system:appointment:remove')")
    @Log(title = "访客预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(visitorAppointmentService.deleteVisitorAppointmentByIds(ids));
    }
}
