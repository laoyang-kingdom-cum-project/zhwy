package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访客预约对象 visitor_appointment
 * 
 * @author ruoyi
 * @date 2026-04-23 11:04:09
 */
public class VisitorAppointment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID")
    private Long id;

    /** 预约人用户ID（居民） */
    @Excel(name = "预约人用户ID", readConverterExp = "居=民")
    private Long userId;

    /** 访客姓名 */
    @Excel(name = "访客姓名")
    private String visitorName;

    /** 访客手机号 */
    @Excel(name = "访客手机号")
    private String visitorPhone;

    /** 访客人数 */
    @Excel(name = "访客人数")
    private Long visitorCount;

    /** 访问日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitDate;

    /** 访问开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitTimeStart;

    /** 访问结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitTimeEnd;

    /** 访问事由 */
    @Excel(name = "访问事由")
    private String visitReason;

    /** 状态（0待审核 1已通过 2已拒绝 3已过期） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=已通过,2=已拒绝,3=已过期")
    private String status;

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

    public void setVisitorName(String visitorName) 
    {
        this.visitorName = visitorName;
    }

    public String getVisitorName() 
    {
        return visitorName;
    }

    public void setVisitorPhone(String visitorPhone) 
    {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorPhone() 
    {
        return visitorPhone;
    }

    public void setVisitorCount(Long visitorCount) 
    {
        this.visitorCount = visitorCount;
    }

    public Long getVisitorCount() 
    {
        return visitorCount;
    }

    public void setVisitDate(Date visitDate) 
    {
        this.visitDate = visitDate;
    }

    public Date getVisitDate() 
    {
        return visitDate;
    }

    public void setVisitTimeStart(Date visitTimeStart) 
    {
        this.visitTimeStart = visitTimeStart;
    }

    public Date getVisitTimeStart() 
    {
        return visitTimeStart;
    }

    public void setVisitTimeEnd(Date visitTimeEnd) 
    {
        this.visitTimeEnd = visitTimeEnd;
    }

    public Date getVisitTimeEnd() 
    {
        return visitTimeEnd;
    }

    public void setVisitReason(String visitReason) 
    {
        this.visitReason = visitReason;
    }

    public String getVisitReason() 
    {
        return visitReason;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("visitorName", getVisitorName())
            .append("visitorPhone", getVisitorPhone())
            .append("visitorCount", getVisitorCount())
            .append("visitDate", getVisitDate())
            .append("visitTimeStart", getVisitTimeStart())
            .append("visitTimeEnd", getVisitTimeEnd())
            .append("visitReason", getVisitReason())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
