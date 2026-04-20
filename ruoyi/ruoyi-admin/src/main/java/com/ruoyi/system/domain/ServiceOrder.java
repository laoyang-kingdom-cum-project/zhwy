package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务订单对象 service_order
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:33
 */
public class ServiceOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 服务类型（水电维修/家电维修/保洁服务/送水服务等） */
    @Excel(name = "服务类型", readConverterExp = "水=电维修/家电维修/保洁服务/送水服务等")
    private String type;

    /** 服务内容描述 */
    @Excel(name = "服务内容描述")
    private String content;

    /** 状态：0待处理，1处理中，2已完成，3已取消 */
    @Excel(name = "状态：0待处理，1处理中，2已完成，3已取消")
    private Integer status;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactPhone;

    /** 服务地址 */
    @Excel(name = "服务地址")
    private String address;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    /** 处理人ID */
    @Excel(name = "处理人ID")
    private Long handlerId;

    /** 处理人姓名 */
    @Excel(name = "处理人姓名")
    private String handlerName;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    /** 删除标志（0未删除，1已删除） */
    private Integer delFlag;

    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setContactName(String contactName) 
    {
        this.contactName = contactName;
    }

    public String getContactName() 
    {
        return contactName;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setAppointmentTime(Date appointmentTime) 
    {
        this.appointmentTime = appointmentTime;
    }

    public Date getAppointmentTime() 
    {
        return appointmentTime;
    }

    public void setHandlerId(Long handlerId) 
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId() 
    {
        return handlerId;
    }

    public void setHandlerName(String handlerName) 
    {
        this.handlerName = handlerName;
    }

    public String getHandlerName() 
    {
        return handlerName;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
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
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("type", getType())
            .append("content", getContent())
            .append("status", getStatus())
            .append("contactName", getContactName())
            .append("contactPhone", getContactPhone())
            .append("address", getAddress())
            .append("appointmentTime", getAppointmentTime())
            .append("remark", getRemark())
            .append("handlerId", getHandlerId())
            .append("handlerName", getHandlerName())
            .append("handleResult", getHandleResult())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
