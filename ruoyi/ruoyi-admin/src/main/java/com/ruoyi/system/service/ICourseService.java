package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Course;

/**
 * 课程信息主Service接口
 * 
 * @author ruoyi
 * @date 2026-01-19 15:00:52
 */
public interface ICourseService 
{
    /**
     * 查询课程信息主
     * 
     * @param id 课程信息主主键
     * @return 课程信息主
     */
    public Course selectCourseById(Long id);

    /**
     * 查询课程信息主列表
     * 
     * @param course 课程信息主
     * @return 课程信息主集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 新增课程信息主
     * 
     * @param course 课程信息主
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 修改课程信息主
     * 
     * @param course 课程信息主
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 批量删除课程信息主
     * 
     * @param ids 需要删除的课程信息主主键集合
     * @return 结果
     */
    public int deleteCourseByIds(Long[] ids);

    /**
     * 删除课程信息主信息
     * 
     * @param id 课程信息主主键
     * @return 结果
     */
    public int deleteCourseById(Long id);
}
