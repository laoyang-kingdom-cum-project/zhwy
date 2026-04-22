package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运动团队列对象 sport_team
 * 
 * @author ruoyi
 * @date 2026-01-15 15:26:47
 */
public class SportTeam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 团队头像URL（正方形） */
    @Excel(name = "团队头像URL", readConverterExp = "正=方形")
    private String avatar;

    /** 简介 */
    @Excel(name = "简介")
    private String content;

    /** 团队名称 */
    @Excel(name = "团队名称")
    private String teamName;

    /** 团队人数 */
    @Excel(name = "团队人数")
    private String memberCount;

    /** 团队分类 */
    @Excel(name = "团队分类")
    private String category;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 总运动人数 */
    @Excel(name = "总运动人数")
    private String lowerCamelase;

    /** 总运动时长 */
    @Excel(name = "总运动时长")
    private String toralSportsTime;

    /** 总消耗 */
    @Excel(name = "总消耗")
    private String totalConsumption;

    /** 运动天数 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "运动天数", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date tasKTime;

    /** 打卡天数 */
    @Excel(name = "打卡天数")
    private String taskday;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setTeamName(String teamName) 
    {
        this.teamName = teamName;
    }

    public String getTeamName() 
    {
        return teamName;
    }

    public void setMemberCount(String memberCount) 
    {
        this.memberCount = memberCount;
    }

    public String getMemberCount() 
    {
        return memberCount;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
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

    public void setLowerCamelase(String lowerCamelase) 
    {
        this.lowerCamelase = lowerCamelase;
    }

    public String getLowerCamelase() 
    {
        return lowerCamelase;
    }

    public void setToralSportsTime(String toralSportsTime) 
    {
        this.toralSportsTime = toralSportsTime;
    }

    public String getToralSportsTime() 
    {
        return toralSportsTime;
    }

    public void setTotalConsumption(String totalConsumption) 
    {
        this.totalConsumption = totalConsumption;
    }

    public String getTotalConsumption() 
    {
        return totalConsumption;
    }

    public void setTasKTime(Date tasKTime) 
    {
        this.tasKTime = tasKTime;
    }

    public Date getTasKTime() 
    {
        return tasKTime;
    }

    public void setTaskday(String taskday) 
    {
        this.taskday = taskday;
    }

    public String getTaskday() 
    {
        return taskday;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("avatar", getAvatar())
            .append("content", getContent())
            .append("teamName", getTeamName())
            .append("memberCount", getMemberCount())
            .append("category", getCategory())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("lowerCamelase", getLowerCamelase())
            .append("toralSportsTime", getToralSportsTime())
            .append("totalConsumption", getTotalConsumption())
            .append("tasKTime", getTasKTime())
            .append("taskday", getTaskday())
            .toString();
    }
}
