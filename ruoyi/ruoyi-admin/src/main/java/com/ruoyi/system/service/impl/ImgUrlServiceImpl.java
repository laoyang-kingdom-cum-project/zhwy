package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ImgUrlMapper;
import com.ruoyi.system.domain.ImgUrl;
import com.ruoyi.system.service.IImgUrlService;

/**
 * 图片上传Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@Service
public class ImgUrlServiceImpl implements IImgUrlService 
{
    @Autowired
    private ImgUrlMapper imgUrlMapper;

    /**
     * 查询图片上传
     * 
     * @param id 图片上传主键
     * @return 图片上传
     */
    @Override
    public ImgUrl selectImgUrlById(Long id)
    {
        return imgUrlMapper.selectImgUrlById(id);
    }

    /**
     * 查询图片上传列表
     * 
     * @param imgUrl 图片上传
     * @return 图片上传
     */
    @Override
    public List<ImgUrl> selectImgUrlList(ImgUrl imgUrl)
    {
        return imgUrlMapper.selectImgUrlList(imgUrl);
    }

    /**
     * 新增图片上传
     * 
     * @param imgUrl 图片上传
     * @return 结果
     */
    @Override
    public int insertImgUrl(ImgUrl imgUrl)
    {
        return imgUrlMapper.insertImgUrl(imgUrl);
    }

    /**
     * 修改图片上传
     * 
     * @param imgUrl 图片上传
     * @return 结果
     */
    @Override
    public int updateImgUrl(ImgUrl imgUrl)
    {
        return imgUrlMapper.updateImgUrl(imgUrl);
    }

    /**
     * 批量删除图片上传
     * 
     * @param ids 需要删除的图片上传主键
     * @return 结果
     */
    @Override
    public int deleteImgUrlByIds(Long[] ids)
    {
        return imgUrlMapper.deleteImgUrlByIds(ids);
    }

    /**
     * 删除图片上传信息
     * 
     * @param id 图片上传主键
     * @return 结果
     */
    @Override
    public int deleteImgUrlById(Long id)
    {
        return imgUrlMapper.deleteImgUrlById(id);
    }
}
