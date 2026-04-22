package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 跑步路线对象 running_routes
 * 
 * @author ruoyi
 * @date 2026-01-10 15:38:30
 */
public class RunningRoutes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 路线名称 */
    @Excel(name = "路线名称")
    private String routeName;

    /** 路线图片 */
    @Excel(name = "路线图片")
    private String routeImg;

    /** 公里数 */
    @Excel(name = "公里数")
    private BigDecimal distanceKm;

    /** 跑过人数 */
    @Excel(name = "跑过人数")
    private Integer runners;

    /** 起点距您公里数 */
    @Excel(name = "起点距您公里数")
    private BigDecimal startDistKm;

    /** 路线分类 */
    @Excel(name = "路线分类")
    private String category;

    /** 具体起点地址 */
    @Excel(name = "具体起点地址")
    private String exactStartingAddress;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRouteName(String routeName) 
    {
        this.routeName = routeName;
    }

    public String getRouteName() 
    {
        return routeName;
    }

    public void setRouteImg(String routeImg) 
    {
        this.routeImg = routeImg;
    }

    public String getRouteImg() 
    {
        return routeImg;
    }

    public void setDistanceKm(BigDecimal distanceKm) 
    {
        this.distanceKm = distanceKm;
    }

    public BigDecimal getDistanceKm() 
    {
        return distanceKm;
    }

    public void setRunners(Integer runners) 
    {
        this.runners = runners;
    }

    public Integer getRunners() 
    {
        return runners;
    }

    public void setStartDistKm(BigDecimal startDistKm) 
    {
        this.startDistKm = startDistKm;
    }

    public BigDecimal getStartDistKm() 
    {
        return startDistKm;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setExactStartingAddress(String exactStartingAddress) 
    {
        this.exactStartingAddress = exactStartingAddress;
    }

    public String getExactStartingAddress() 
    {
        return exactStartingAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("routeName", getRouteName())
            .append("routeImg", getRouteImg())
            .append("distanceKm", getDistanceKm())
            .append("runners", getRunners())
            .append("startDistKm", getStartDistKm())
            .append("category", getCategory())
            .append("exactStartingAddress", getExactStartingAddress())
            .toString();
    }
}
