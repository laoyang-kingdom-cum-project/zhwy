package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CourseCatalogMapper;
import com.ruoyi.system.domain.CourseCatalog;
import com.ruoyi.system.service.ICourseCatalogService;

/**
 * 课程目录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-21 15:23:49
 */
@Service
public class CourseCatalogServiceImpl implements ICourseCatalogService 
{
    @Autowired
    private CourseCatalogMapper courseCatalogMapper;

    /**
     * 查询课程目录
     * 
     * @param id 课程目录主键
     * @return 课程目录
     */
    @Override
    public CourseCatalog selectCourseCatalogById(Long id)
    {
        return courseCatalogMapper.selectCourseCatalogById(id);
    }

    /**
     * 查询课程目录列表
     * 
     * @param courseCatalog 课程目录
     * @return 课程目录
     */
    @Override
    public List<CourseCatalog> selectCourseCatalogList(CourseCatalog courseCatalog)
    {
        return courseCatalogMapper.selectCourseCatalogList(courseCatalog);
    }

    /**
     * 新增课程目录
     * 
     * @param courseCatalog 课程目录
     * @return 结果
     */
    @Override
    public int insertCourseCatalog(CourseCatalog courseCatalog)
    {
        return courseCatalogMapper.insertCourseCatalog(courseCatalog);
    }

    /**
     * 修改课程目录
     * 
     * @param courseCatalog 课程目录
     * @return 结果
     */
    @Override
    public int updateCourseCatalog(CourseCatalog courseCatalog)
    {
        return courseCatalogMapper.updateCourseCatalog(courseCatalog);
    }

    /**
     * 批量删除课程目录
     * 
     * @param ids 需要删除的课程目录主键
     * @return 结果
     */
    @Override
    public int deleteCourseCatalogByIds(Long[] ids)
    {
        return courseCatalogMapper.deleteCourseCatalogByIds(ids);
    }

    /**
     * 删除课程目录信息
     * 
     * @param id 课程目录主键
     * @return 结果
     */
    @Override
    public int deleteCourseCatalogById(Long id)
    {
        return courseCatalogMapper.deleteCourseCatalogById(id);
    }
}
