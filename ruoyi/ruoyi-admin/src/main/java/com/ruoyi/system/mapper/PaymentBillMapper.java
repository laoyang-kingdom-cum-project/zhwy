package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PaymentBill;

/**
 * 缴费账单Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-17 15:15:19
 */
public interface PaymentBillMapper 
{
    /**
     * 查询缴费账单
     * 
     * @param id 缴费账单主键
     * @return 缴费账单
     */
    public PaymentBill selectPaymentBillById(Long id);

    /**
     * 查询缴费账单列表
     * 
     * @param paymentBill 缴费账单
     * @return 缴费账单集合
     */
    public List<PaymentBill> selectPaymentBillList(PaymentBill paymentBill);

    /**
     * 新增缴费账单
     * 
     * @param paymentBill 缴费账单
     * @return 结果
     */
    public int insertPaymentBill(PaymentBill paymentBill);

    /**
     * 修改缴费账单
     * 
     * @param paymentBill 缴费账单
     * @return 结果
     */
    public int updatePaymentBill(PaymentBill paymentBill);

    /**
     * 删除缴费账单
     * 
     * @param id 缴费账单主键
     * @return 结果
     */
    public int deletePaymentBillById(Long id);

    /**
     * 批量删除缴费账单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaymentBillByIds(Long[] ids);
}
