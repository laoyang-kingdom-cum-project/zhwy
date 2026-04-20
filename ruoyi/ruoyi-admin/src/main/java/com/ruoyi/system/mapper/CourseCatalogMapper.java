package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.CourseCatalog;

/**
 * 课程目录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-21 15:23:49
 */
public interface CourseCatalogMapper 
{
    /**
     * 查询课程目录
     * 
     * @param id 课程目录主键
     * @return 课程目录
     */
    public CourseCatalog selectCourseCatalogById(Long id);

    /**
     * 查询课程目录列表
     * 
     * @param courseCatalog 课程目录
     * @return 课程目录集合
     */
    public List<CourseCatalog> selectCourseCatalogList(CourseCatalog courseCatalog);

    /**
     * 新增课程目录
     * 
     * @param courseCatalog 课程目录
     * @return 结果
     */
    public int insertCourseCatalog(CourseCatalog courseCatalog);

    /**
     * 修改课程目录
     * 
     * @param courseCatalog 课程目录
     * @return 结果
     */
    public int updateCourseCatalog(CourseCatalog courseCatalog);

    /**
     * 删除课程目录
     * 
     * @param id 课程目录主键
     * @return 结果
     */
    public int deleteCourseCatalogById(Long id);

    /**
     * 批量删除课程目录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseCatalogByIds(Long[] ids);
}
