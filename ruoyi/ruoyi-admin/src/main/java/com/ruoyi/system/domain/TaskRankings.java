package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务排行榜对象 task_rankings
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public class TaskRankings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 用户头像 */
    @Excel(name = "用户头像")
    private String img;

    /** 打卡天数 */
    @Excel(name = "打卡天数")
    private String checkInDays;

    /** 运动时长 */
    @Excel(name = "运动时长")
    private String exerciseDuration;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    public void setCheckInDays(String checkInDays) 
    {
        this.checkInDays = checkInDays;
    }

    public String getCheckInDays() 
    {
        return checkInDays;
    }

    public void setExerciseDuration(String exerciseDuration) 
    {
        this.exerciseDuration = exerciseDuration;
    }

    public String getExerciseDuration() 
    {
        return exerciseDuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("nickname", getNickname())
            .append("img", getImg())
            .append("checkInDays", getCheckInDays())
            .append("exerciseDuration", getExerciseDuration())
            .toString();
    }
}
