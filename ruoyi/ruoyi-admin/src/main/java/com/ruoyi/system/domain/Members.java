package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成员对象 members
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public class Members extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 团队id */
    @Excel(name = "团队id")
    private Long teamId;

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

    public void setTeamId(Long teamId) 
    {
        this.teamId = teamId;
    }

    public Long getTeamId() 
    {
        return teamId;
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
            .append("teamId", getTeamId())
            .append("userid", getUserid())
            .toString();
    }
}
