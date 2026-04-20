package com.ruoyi.system.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运动团详情对象 sport_team_press
 * 
 * @author ruoyi
 * @date 2026-01-05
 */
public class SportTeamPress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 团队头像 */
    @Excel(name = "团队头像")
    private String avatar;

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

    /** 更新信息时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新信息时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 总运动人数 */
    @Excel(name = "总运动人数")
    private Long lowerCamelCase;

    /** 总运动时长 */
    @Excel(name = "总运动时长")
    private Long totalSportsTime;

    /** 总消耗 */
    @Excel(name = "总消耗")
    private Long totalConsumption;

    /** 任务时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date taskTime;

    /** 要求打卡天数 */
    @Excel(name = "要求打卡天数")
    private Long taskday;

    /** 任务排行榜 */
    @Excel(name = "任务排行榜")
    private String teamMember;

    /** 运动团队列信息 */
    private List<SportTeam> sportTeamList;

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

    public void setLowerCamelCase(Long lowerCamelCase) 
    {
        this.lowerCamelCase = lowerCamelCase;
    }

    public Long getLowerCamelCase() 
    {
        return lowerCamelCase;
    }

    public void setTotalSportsTime(Long totalSportsTime) 
    {
        this.totalSportsTime = totalSportsTime;
    }

    public Long getTotalSportsTime() 
    {
        return totalSportsTime;
    }

    public void setTotalConsumption(Long totalConsumption) 
    {
        this.totalConsumption = totalConsumption;
    }

    public Long getTotalConsumption() 
    {
        return totalConsumption;
    }

    public void setTaskTime(Date taskTime) 
    {
        this.taskTime = taskTime;
    }

    public Date getTaskTime() 
    {
        return taskTime;
    }

    public void setTaskday(Long taskday) 
    {
        this.taskday = taskday;
    }

    public Long getTaskday() 
    {
        return taskday;
    }

    public void setTeamMember(String teamMember) 
    {
        this.teamMember = teamMember;
    }

    public String getTeamMember() 
    {
        return teamMember;
    }

    public List<SportTeam> getSportTeamList()
    {
        return sportTeamList;
    }

    public void setSportTeamList(List<SportTeam> sportTeamList)
    {
        this.sportTeamList = sportTeamList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("avatar", getAvatar())
            .append("teamName", getTeamName())
            .append("memberCount", getMemberCount())
            .append("category", getCategory())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("lowerCamelCase", getLowerCamelCase())
            .append("totalSportsTime", getTotalSportsTime())
            .append("totalConsumption", getTotalConsumption())
            .append("taskTime", getTaskTime())
            .append("taskday", getTaskday())
            .append("teamMember", getTeamMember())
            .append("sportTeamList", getSportTeamList())
            .toString();
    }
}
