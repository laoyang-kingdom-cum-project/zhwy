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
import com.ruoyi.system.domain.ServiceOrder;
import com.ruoyi.system.service.IServiceOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务订单Controller
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:33
 */
@RestController
@RequestMapping("/system/order")
public class ServiceOrderController extends BaseController
{
    @Autowired
    private IServiceOrderService serviceOrderService;

    /**
     * 查询服务订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceOrder serviceOrder)
    {
        startPage();
        List<ServiceOrder> list = serviceOrderService.selectServiceOrderList(serviceOrder);
        return getDataTable(list);
    }

    /**
     * 导出服务订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:order:export')")
    @Log(title = "服务订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceOrder serviceOrder)
    {
        List<ServiceOrder> list = serviceOrderService.selectServiceOrderList(serviceOrder);
        ExcelUtil<ServiceOrder> util = new ExcelUtil<ServiceOrder>(ServiceOrder.class);
        util.exportExcel(response, list, "服务订单数据");
    }

    /**
     * 获取服务订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId)
    {
        return success(serviceOrderService.selectServiceOrderByOrderId(orderId));
    }

    /**
     * 新增服务订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "服务订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceOrder serviceOrder)
    {
        return toAjax(serviceOrderService.insertServiceOrder(serviceOrder));
    }

    /**
     * 修改服务订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:edit')")
    @Log(title = "服务订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceOrder serviceOrder)
    {
        return toAjax(serviceOrderService.updateServiceOrder(serviceOrder));
    }

    /**
     * 删除服务订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:remove')")
    @Log(title = "服务订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds)
    {
        return toAjax(serviceOrderService.deleteServiceOrderByOrderIds(orderIds));
    }
}
