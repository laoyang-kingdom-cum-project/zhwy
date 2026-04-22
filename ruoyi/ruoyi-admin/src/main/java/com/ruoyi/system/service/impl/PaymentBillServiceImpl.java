package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PaymentBillMapper;
import com.ruoyi.system.domain.PaymentBill;
import com.ruoyi.system.service.IPaymentBillService;

/**
 * 缴费账单Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-17 15:15:19
 */
@Service
public class PaymentBillServiceImpl implements IPaymentBillService 
{
    @Autowired
    private PaymentBillMapper paymentBillMapper;

    /**
     * 查询缴费账单
     * 
     * @param id 缴费账单主键
     * @return 缴费账单
     */
    @Override
    public PaymentBill selectPaymentBillById(Long id)
    {
        return paymentBillMapper.selectPaymentBillById(id);
    }

    /**
     * 查询缴费账单列表
     * 
     * @param paymentBill 缴费账单
     * @return 缴费账单
     */
    @Override
    public List<PaymentBill> selectPaymentBillList(PaymentBill paymentBill)
    {
        return paymentBillMapper.selectPaymentBillList(paymentBill);
    }

    /**
     * 新增缴费账单
     * 
     * @param paymentBill 缴费账单
     * @return 结果
     */
    @Override
    public int insertPaymentBill(PaymentBill paymentBill)
    {
        paymentBill.setCreateTime(DateUtils.getNowDate());
        return paymentBillMapper.insertPaymentBill(paymentBill);
    }

    /**
     * 修改缴费账单
     * 
     * @param paymentBill 缴费账单
     * @return 结果
     */
    @Override
    public int updatePaymentBill(PaymentBill paymentBill)
    {
        paymentBill.setUpdateTime(DateUtils.getNowDate());
        return paymentBillMapper.updatePaymentBill(paymentBill);
    }

    /**
     * 批量删除缴费账单
     * 
     * @param ids 需要删除的缴费账单主键
     * @return 结果
     */
    @Override
    public int deletePaymentBillByIds(Long[] ids)
    {
        return paymentBillMapper.deletePaymentBillByIds(ids);
    }

    /**
     * 删除缴费账单信息
     * 
     * @param id 缴费账单主键
     * @return 结果
     */
    @Override
    public int deletePaymentBillById(Long id)
    {
        return paymentBillMapper.deletePaymentBillById(id);
    }
}
