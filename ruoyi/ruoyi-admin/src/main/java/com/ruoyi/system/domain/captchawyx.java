package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 验证码池对象 captchawyx
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public class captchawyx extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 手机号/邮箱 */
    @Excel(name = "手机号/邮箱")
    private String target;

    /** 6位验证码 */
    @Excel(name = "6位验证码")
    private String code;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTarget(String target) 
    {
        this.target = target;
    }

    public String getTarget() 
    {
        return target;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setExpireAt(Date expireAt) 
    {
        this.expireAt = expireAt;
    }

    public Date getExpireAt() 
    {
        return expireAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("target", getTarget())
            .append("code", getCode())
            .append("expireAt", getExpireAt())
            .toString();
    }
}
