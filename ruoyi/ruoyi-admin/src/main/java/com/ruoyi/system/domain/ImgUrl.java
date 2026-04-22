package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图片上传对象 img_url
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public class ImgUrl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 上传图片 */
    @Excel(name = "上传图片")
    private String img;

    /** 主键 */
    private Long id;

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("img", getImg())
            .append("id", getId())
            .toString();
    }
}
