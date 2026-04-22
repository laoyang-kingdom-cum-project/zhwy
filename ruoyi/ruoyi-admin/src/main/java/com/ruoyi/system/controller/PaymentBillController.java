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
import com.ruoyi.system.domain.PaymentBill;
import com.ruoyi.system.service.IPaymentBillService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 缴费账单Controller
 * 
 * @author ruoyi
 * @date 2026-04-17 15:15:19
 */
@RestController
@RequestMapping("/system/bill")
public class PaymentBillController extends BaseController
{
    @Autowired
    private IPaymentBillService paymentBillService;

    /**
     * 查询缴费账单列表
     */
    @PreAuthorize("@ss.hasPermi('system:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentBill paymentBill)
    {
        startPage();
        List<PaymentBill> list = paymentBillService.selectPaymentBillList(paymentBill);
        return getDataTable(list);
    }

    /**
     * 导出缴费账单列表
     */
    @PreAuthorize("@ss.hasPermi('system:bill:export')")
    @Log(title = "缴费账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentBill paymentBill)
    {
        List<PaymentBill> list = paymentBillService.selectPaymentBillList(paymentBill);
        ExcelUtil<PaymentBill> util = new ExcelUtil<PaymentBill>(PaymentBill.class);
        util.exportExcel(response, list, "缴费账单数据");
    }

    /**
     * 获取缴费账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(paymentBillService.selectPaymentBillById(id));
    }

    /**
     * 新增缴费账单
     */
    @PreAuthorize("@ss.hasPermi('system:bill:add')")
    @Log(title = "缴费账单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentBill paymentBill)
    {
        return toAjax(paymentBillService.insertPaymentBill(paymentBill));
    }

    /**
     * 修改缴费账单
     */
    @PreAuthorize("@ss.hasPermi('system:bill:edit')")
    @Log(title = "缴费账单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentBill paymentBill)
    {
        return toAjax(paymentBillService.updatePaymentBill(paymentBill));
    }

    /**
     * 删除缴费账单
     */
    @PreAuthorize("@ss.hasPermi('system:bill:remove')")
    @Log(title = "缴费账单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(paymentBillService.deletePaymentBillByIds(ids));
    }
}
