package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程信息主对象 course
 * 
 * @author ruoyi
 * @date 2026-01-19 15:00:52
 */
public class Course extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课程封面图片 */
    @Excel(name = "课程封面图片")
    private String courseCoverUrl;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程时长 */
    @Excel(name = "课程时长")
    private Integer courseDuration;

    /** 预计消耗卡路里 */
    @Excel(name = "预计消耗卡路里")
    private Integer courseCalories;

    /** 课程难度 */
    @Excel(name = "课程难度")
    private String courseLevel;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 视频 */
    @Excel(name = "视频")
    private String video;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCourseCoverUrl(String courseCoverUrl) 
    {
        this.courseCoverUrl = courseCoverUrl;
    }

    public String getCourseCoverUrl() 
    {
        return courseCoverUrl;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setCourseDuration(Integer courseDuration) 
    {
        this.courseDuration = courseDuration;
    }

    public Integer getCourseDuration() 
    {
        return courseDuration;
    }

    public void setCourseCalories(Integer courseCalories) 
    {
        this.courseCalories = courseCalories;
    }

    public Integer getCourseCalories() 
    {
        return courseCalories;
    }

    public void setCourseLevel(String courseLevel) 
    {
        this.courseLevel = courseLevel;
    }

    public String getCourseLevel() 
    {
        return courseLevel;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    public void setVideo(String video) 
    {
        this.video = video;
    }

    public String getVideo() 
    {
        return video;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("courseCoverUrl", getCourseCoverUrl())
            .append("courseName", getCourseName())
            .append("courseDuration", getCourseDuration())
            .append("courseCalories", getCourseCalories())
            .append("courseLevel", getCourseLevel())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("video", getVideo())
            .toString();
    }
}
