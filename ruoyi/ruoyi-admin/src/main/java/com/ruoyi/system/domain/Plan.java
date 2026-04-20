package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计划对象 plan
 * 
 * @author ruoyi
 * @date 2026-01-20 21:21:50
 */
public class Plan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 图片 */
    @Excel(name = "图片")
    private String imgurl;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 级别 */
    @Excel(name = "级别")
    private String level;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    /** 计划消耗千卡 */
    @Excel(name = "计划消耗千卡")
    private String plannedConsumption;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setImgurl(String imgurl) 
    {
        this.imgurl = imgurl;
    }

    public String getImgurl() 
    {
        return imgurl;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }

    public void setDatetime(Date datetime) 
    {
        this.datetime = datetime;
    }

    public Date getDatetime() 
    {
        return datetime;
    }

    public void setPlannedConsumption(String plannedConsumption) 
    {
        this.plannedConsumption = plannedConsumption;
    }

    public String getPlannedConsumption() 
    {
        return plannedConsumption;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("imgurl", getImgurl())
            .append("title", getTitle())
            .append("content", getContent())
            .append("level", getLevel())
            .append("datetime", getDatetime())
            .append("plannedConsumption", getPlannedConsumption())
            .toString();
    }
}
