package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 活动对象 activity
 * 
 * @author ruoyi
 * @date 2026-04-17 15:48:31
 */
public class Activity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID")
    private Long id;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String title;

    /** 活动详情内容 */
    @Excel(name = "活动详情内容")
    private String content;

    /** 活动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "活动时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String location;

    /** 状态：0报名中，1进行中，2已结束 */
    @Excel(name = "状态：0报名中，1进行中，2已结束")
    private Integer status;

    /** 已报名人数 */
    @Excel(name = "已报名人数")
    private Long joinCount;

    /** 最大报名人数 */
    @Excel(name = "最大报名人数")
    private Long maxCount;

    /** 活动图片URL */
    @Excel(name = "活动图片URL")
    private String image;

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

    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setJoinCount(Long joinCount) 
    {
        this.joinCount = joinCount;
    }

    public Long getJoinCount() 
    {
        return joinCount;
    }

    public void setMaxCount(Long maxCount) 
    {
        this.maxCount = maxCount;
    }

    public Long getMaxCount() 
    {
        return maxCount;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
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
            .append("title", getTitle())
            .append("content", getContent())
            .append("time", getTime())
            .append("location", getLocation())
            .append("status", getStatus())
            .append("joinCount", getJoinCount())
            .append("maxCount", getMaxCount())
            .append("image", getImage())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
