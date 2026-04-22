package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成绩排行榜对象 performance_leaderboardwyx
 * 
 * @author ruoyi
 * @date 2026-01-19 16:27:45
 */
public class PerformanceLeaderboardwyx extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 设备  */
    @Excel(name = "设备 ")
    private String device;

    /** 时间  */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间 ", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

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

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setDevice(String device) 
    {
        this.device = device;
    }

    public String getDevice() 
    {
        return device;
    }

    public void setTimestamp(Date timestamp) 
    {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() 
    {
        return timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("avatar", getAvatar())
            .append("nickname", getNickname())
            .append("device", getDevice())
            .append("timestamp", getTimestamp())
            .toString();
    }
}
