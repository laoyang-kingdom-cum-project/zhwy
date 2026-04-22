package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 打卡排行榜对象 check_in_leaderboard
 * 
 * @author ruoyi
 * @date 2026-01-10 17:25:39
 */
public class CheckInLeaderboard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户头像 */
    @Excel(name = "用户头像")
    private String img;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickname;

    /** 设备 */
    @Excel(name = "设备")
    private String device;

    /** 打卡次数 */
    @Excel(name = "打卡次数")
    private String checkInCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
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

    public void setCheckInCount(String checkInCount) 
    {
        this.checkInCount = checkInCount;
    }

    public String getCheckInCount() 
    {
        return checkInCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("img", getImg())
            .append("nickname", getNickname())
            .append("device", getDevice())
            .append("checkInCount", getCheckInCount())
            .toString();
    }
}
