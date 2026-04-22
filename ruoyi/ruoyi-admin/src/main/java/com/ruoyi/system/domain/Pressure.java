package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 血压对象 pressure
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public class Pressure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "日期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date datatime;

    /** 高压 */
    @Excel(name = "高压")
    private String systolicPressure;

    /** 低压 */
    @Excel(name = "低压")
    private String diastolicPressure;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userid;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDatatime(Date datatime) 
    {
        this.datatime = datatime;
    }

    public Date getDatatime() 
    {
        return datatime;
    }

    public void setSystolicPressure(String systolicPressure) 
    {
        this.systolicPressure = systolicPressure;
    }

    public String getSystolicPressure() 
    {
        return systolicPressure;
    }

    public void setDiastolicPressure(String diastolicPressure) 
    {
        this.diastolicPressure = diastolicPressure;
    }

    public String getDiastolicPressure() 
    {
        return diastolicPressure;
    }

    public void setUserid(Long userid) 
    {
        this.userid = userid;
    }

    public Long getUserid() 
    {
        return userid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("datatime", getDatatime())
            .append("systolicPressure", getSystolicPressure())
            .append("diastolicPressure", getDiastolicPressure())
            .append("userid", getUserid())
            .toString();
    }
}
