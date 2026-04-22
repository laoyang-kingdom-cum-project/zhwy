package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 睡眠数据对象 sleep_data
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public class SleepData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private Long id;

    /** 记录日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "记录日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordDate;

    /** 总睡眠时间 */
    @Excel(name = "总睡眠时间")
    private String totalSleepTime;

    /** 深睡眠时间 */
    @Excel(name = "深睡眠时间")
    private String deepSleepTime;

    /** 浅睡眠时间 */
    @Excel(name = "浅睡眠时间")
    private String lightSleepTime;

    /** 快速眼动睡眠时间 */
    @Excel(name = "快速眼动睡眠时间")
    private String remSleepTime;

    /** 平均心率 */
    @Excel(name = "平均心率")
    private String averageHeartRate;

    /** 平均血氧饱和度 */
    @Excel(name = "平均血氧饱和度")
    private String averageOxygenSaturation;

    /** 平均呼吸率 */
    @Excel(name = "平均呼吸率")
    private String averageBreathingRate;

    /** 睡眠质量评分 */
    @Excel(name = "睡眠质量评分")
    private String sleepQualityScore;

    /** 生成用户id */
    @Excel(name = "生成用户id")
    private Long userid;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRecordDate(Date recordDate) 
    {
        this.recordDate = recordDate;
    }

    public Date getRecordDate() 
    {
        return recordDate;
    }

    public void setTotalSleepTime(String totalSleepTime) 
    {
        this.totalSleepTime = totalSleepTime;
    }

    public String getTotalSleepTime() 
    {
        return totalSleepTime;
    }

    public void setDeepSleepTime(String deepSleepTime) 
    {
        this.deepSleepTime = deepSleepTime;
    }

    public String getDeepSleepTime() 
    {
        return deepSleepTime;
    }

    public void setLightSleepTime(String lightSleepTime) 
    {
        this.lightSleepTime = lightSleepTime;
    }

    public String getLightSleepTime() 
    {
        return lightSleepTime;
    }

    public void setRemSleepTime(String remSleepTime) 
    {
        this.remSleepTime = remSleepTime;
    }

    public String getRemSleepTime() 
    {
        return remSleepTime;
    }

    public void setAverageHeartRate(String averageHeartRate) 
    {
        this.averageHeartRate = averageHeartRate;
    }

    public String getAverageHeartRate() 
    {
        return averageHeartRate;
    }

    public void setAverageOxygenSaturation(String averageOxygenSaturation) 
    {
        this.averageOxygenSaturation = averageOxygenSaturation;
    }

    public String getAverageOxygenSaturation() 
    {
        return averageOxygenSaturation;
    }

    public void setAverageBreathingRate(String averageBreathingRate) 
    {
        this.averageBreathingRate = averageBreathingRate;
    }

    public String getAverageBreathingRate() 
    {
        return averageBreathingRate;
    }

    public void setSleepQualityScore(String sleepQualityScore) 
    {
        this.sleepQualityScore = sleepQualityScore;
    }

    public String getSleepQualityScore() 
    {
        return sleepQualityScore;
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
            .append("recordDate", getRecordDate())
            .append("totalSleepTime", getTotalSleepTime())
            .append("deepSleepTime", getDeepSleepTime())
            .append("lightSleepTime", getLightSleepTime())
            .append("remSleepTime", getRemSleepTime())
            .append("averageHeartRate", getAverageHeartRate())
            .append("averageOxygenSaturation", getAverageOxygenSaturation())
            .append("averageBreathingRate", getAverageBreathingRate())
            .append("sleepQualityScore", getSleepQualityScore())
            .append("userid", getUserid())
            .toString();
    }
}
