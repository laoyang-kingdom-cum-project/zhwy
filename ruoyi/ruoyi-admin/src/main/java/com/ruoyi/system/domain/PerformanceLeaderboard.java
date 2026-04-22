package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成绩排行榜对象 performance_leaderboard
 * 
 * @author ruoyi
 * @date 2026-01-10 20:09:55
 */
public class PerformanceLeaderboard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 头像 */
    @Excel(name = "头像")
    private String Avatar;

    /** 昵称 */
    @Excel(name = "昵称")
    private String Nickname;

    /** 设备  */
    @Excel(name = "设备 ")
    private String Device;

    /** 时间  */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间 ", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date Timestamp;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAvatar(String Avatar) 
    {
        this.Avatar = Avatar;
    }

    public String getAvatar() 
    {
        return Avatar;
    }

    public void setNickname(String Nickname) 
    {
        this.Nickname = Nickname;
    }

    public String getNickname() 
    {
        return Nickname;
    }

    public void setDevice(String Device) 
    {
        this.Device = Device;
    }

    public String getDevice() 
    {
        return Device;
    }

    public void setTimestamp(Date Timestamp) 
    {
        this.Timestamp = Timestamp;
    }

    public Date getTimestamp() 
    {
        return Timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("Avatar", getAvatar())
            .append("Nickname", getNickname())
            .append("Device", getDevice())
            .append("Timestamp", getTimestamp())
            .toString();
    }
}
