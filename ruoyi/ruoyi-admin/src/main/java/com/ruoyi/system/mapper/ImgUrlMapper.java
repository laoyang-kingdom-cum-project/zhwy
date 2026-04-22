package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ImgUrl;

/**
 * 图片上传Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface ImgUrlMapper 
{
    /**
     * 查询图片上传
     * 
     * @param id 图片上传主键
     * @return 图片上传
     */
    public ImgUrl selectImgUrlById(Long id);

    /**
     * 查询图片上传列表
     * 
     * @param imgUrl 图片上传
     * @return 图片上传集合
     */
    public List<ImgUrl> selectImgUrlList(ImgUrl imgUrl);

    /**
     * 新增图片上传
     * 
     * @param imgUrl 图片上传
     * @return 结果
     */
    public int insertImgUrl(ImgUrl imgUrl);

    /**
     * 修改图片上传
     * 
     * @param imgUrl 图片上传
     * @return 结果
     */
    public int updateImgUrl(ImgUrl imgUrl);

    /**
     * 删除图片上传
     * 
     * @param id 图片上传主键
     * @return 结果
     */
    public int deleteImgUrlById(Long id);

    /**
     * 批量删除图片上传
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteImgUrlByIds(Long[] ids);
}
