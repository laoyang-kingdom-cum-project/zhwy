package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家人健康对象 family_health
 * 
 * @author ruoyi
 * @date 2026-04-17 10:42:51
 */
public class FamilyHealth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 住址 */
    @Excel(name = "住址")
    private String room;

    /** 状态 */
    @Excel(name = "状态")
    private String healthStatus;

    /** 最后活动 */
    @Excel(name = "最后活动")
    private String lastActive;

    /** 紧急联系人+手机号 */
    @Excel(name = "紧急联系人+手机号")
    private String emergencyContact;

    /** 步数 */
    @Excel(name = "步数")
    private String steps;

    /** 睡眠时间 */
    @Excel(name = "睡眠时间")
    private String sleep;

    /** 心率 */
    @Excel(name = "心率")
    private String heartRate;

    /** 血压 */
    @Excel(name = "血压")
    private String pressure;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }

    public void setRoom(String room) 
    {
        this.room = room;
    }

    public String getRoom() 
    {
        return room;
    }

    public void setHealthStatus(String healthStatus) 
    {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() 
    {
        return healthStatus;
    }

    public void setLastActive(String lastActive) 
    {
        this.lastActive = lastActive;
    }

    public String getLastActive() 
    {
        return lastActive;
    }

    public void setEmergencyContact(String emergencyContact) 
    {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContact() 
    {
        return emergencyContact;
    }

    public void setSteps(String steps) 
    {
        this.steps = steps;
    }

    public String getSteps() 
    {
        return steps;
    }

    public void setSleep(String sleep) 
    {
        this.sleep = sleep;
    }

    public String getSleep() 
    {
        return sleep;
    }

    public void setHeartRate(String heartRate) 
    {
        this.heartRate = heartRate;
    }

    public String getHeartRate() 
    {
        return heartRate;
    }

    public void setPressure(String pressure) 
    {
        this.pressure = pressure;
    }

    public String getPressure() 
    {
        return pressure;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("age", getAge())
            .append("room", getRoom())
            .append("healthStatus", getHealthStatus())
            .append("lastActive", getLastActive())
            .append("emergencyContact", getEmergencyContact())
            .append("steps", getSteps())
            .append("sleep", getSleep())
            .append("heartRate", getHeartRate())
            .append("pressure", getPressure())
            .toString();
    }
}
