package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 缴费账单对象 payment_bill
 * 
 * @author ruoyi
 * @date 2026-04-17 15:15:19
 */
public class PaymentBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID")
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 账单编号 */
    @Excel(name = "账单编号")
    private String billNo;

    /** 账单名称（物业费/水费/电费等） */
    @Excel(name = "账单名称", readConverterExp = "物=业费/水费/电费等")
    private String billName;

    /** 账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费 */
    @Excel(name = "账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费")
    private String billType;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 状态：0未缴费，1已缴费 */
    @Excel(name = "状态：0未缴费，1已缴费")
    private Integer status;

    /** 缴费时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "缴费时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 支付方式：wechat微信支付，alipay支付宝 */
    @Excel(name = "支付方式：wechat微信支付，alipay支付宝")
    private String payMethod;

    /** 账单周期（如：2024年4月） */
    @Excel(name = "账单周期", readConverterExp = "如=：2024年4月")
    private String billPeriod;

    /** 截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    /** 删除标志（0未删除，1已删除） */
    private Integer delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setBillNo(String billNo) 
    {
        this.billNo = billNo;
    }

    public String getBillNo() 
    {
        return billNo;
    }

    public void setBillName(String billName) 
    {
        this.billName = billName;
    }

    public String getBillName() 
    {
        return billName;
    }

    public void setBillType(String billType) 
    {
        this.billType = billType;
    }

    public String getBillType() 
    {
        return billType;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setPayMethod(String payMethod) 
    {
        this.payMethod = payMethod;
    }

    public String getPayMethod() 
    {
        return payMethod;
    }

    public void setBillPeriod(String billPeriod) 
    {
        this.billPeriod = billPeriod;
    }

    public String getBillPeriod() 
    {
        return billPeriod;
    }

    public void setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate() 
    {
        return dueDate;
    }

    public void setDelFlag(Integer delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("billNo", getBillNo())
            .append("billName", getBillName())
            .append("billType", getBillType())
            .append("amount", getAmount())
            .append("status", getStatus())
            .append("payTime", getPayTime())
            .append("payMethod", getPayMethod())
            .append("billPeriod", getBillPeriod())
            .append("dueDate", getDueDate())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
