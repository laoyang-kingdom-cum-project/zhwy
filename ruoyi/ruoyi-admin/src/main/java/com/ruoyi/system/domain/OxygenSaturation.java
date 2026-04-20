package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 血氧饱和度对象 oxygen_saturation
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public class OxygenSaturation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    /** 值 */
    @Excel(name = "值")
    private String value;

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

    public void setDatetime(Date datetime) 
    {
        this.datetime = datetime;
    }

    public Date getDatetime() 
    {
        return datetime;
    }

    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
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
            .append("datetime", getDatetime())
            .append("value", getValue())
            .append("userid", getUserid())
            .toString();
    }
}
