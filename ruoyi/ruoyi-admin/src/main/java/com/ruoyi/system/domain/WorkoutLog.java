package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运动记录对象 workout_log
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public class WorkoutLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 运动开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "运动开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date workoutTime;

    /** 运动分类 */
    @Excel(name = "运动分类")
    private String category;

    /** 消耗卡路里 */
    @Excel(name = "消耗卡路里")
    private BigDecimal calories;

    /** 运动时长 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "运动时长", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date duration;

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

    public void setWorkoutTime(Date workoutTime) 
    {
        this.workoutTime = workoutTime;
    }

    public Date getWorkoutTime() 
    {
        return workoutTime;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setCalories(BigDecimal calories) 
    {
        this.calories = calories;
    }

    public BigDecimal getCalories() 
    {
        return calories;
    }

    public void setDuration(Date duration) 
    {
        this.duration = duration;
    }

    public Date getDuration() 
    {
        return duration;
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
            .append("workoutTime", getWorkoutTime())
            .append("category", getCategory())
            .append("calories", getCalories())
            .append("duration", getDuration())
            .append("userid", getUserid())
            .toString();
    }
}
