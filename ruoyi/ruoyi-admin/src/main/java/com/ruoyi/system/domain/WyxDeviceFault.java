package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备故障记录对象 wyx_device_fault
 * 
 * @author ruoyi
 * @date 2026-04-24 16:00:03
 */
public class WyxDeviceFault extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 故障ID */
    @Excel(name = "故障ID")
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 故障类型 */
    @Excel(name = "故障类型")
    private String faultType;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDesc;

    /** 上报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上报时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;

    /** 上报人ID */
    @Excel(name = "上报人ID")
    private Long reportBy;

    /** 处理状态：0-待处理、1-处理中、2-已处理 */
    @Excel(name = "处理状态：0-待处理、1-处理中、2-已处理")
    private String handleStatus;

    /** 处理人ID */
    @Excel(name = "处理人ID")
    private Long handleBy;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setFaultType(String faultType) 
    {
        this.faultType = faultType;
    }

    public String getFaultType() 
    {
        return faultType;
    }

    public void setFaultDesc(String faultDesc) 
    {
        this.faultDesc = faultDesc;
    }

    public String getFaultDesc() 
    {
        return faultDesc;
    }

    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }

    public void setReportBy(Long reportBy) 
    {
        this.reportBy = reportBy;
    }

    public Long getReportBy() 
    {
        return reportBy;
    }

    public void setHandleStatus(String handleStatus) 
    {
        this.handleStatus = handleStatus;
    }

    public String getHandleStatus() 
    {
        return handleStatus;
    }

    public void setHandleBy(Long handleBy) 
    {
        this.handleBy = handleBy;
    }

    public Long getHandleBy() 
    {
        return handleBy;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("faultType", getFaultType())
            .append("faultDesc", getFaultDesc())
            .append("reportTime", getReportTime())
            .append("reportBy", getReportBy())
            .append("handleStatus", getHandleStatus())
            .append("handleBy", getHandleBy())
            .append("handleTime", getHandleTime())
            .append("handleResult", getHandleResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
