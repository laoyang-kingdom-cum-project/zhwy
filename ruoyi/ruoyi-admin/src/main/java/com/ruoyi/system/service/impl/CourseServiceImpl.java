package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CourseMapper;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.ICourseService;

/**
 * 课程信息主Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-19 15:00:52
 */
@Service
public class CourseServiceImpl implements ICourseService 
{
    @Autowired
    private CourseMapper courseMapper;

    /**
     * 查询课程信息主
     * 
     * @param id 课程信息主主键
     * @return 课程信息主
     */
    @Override
    public Course selectCourseById(Long id)
    {
        return courseMapper.selectCourseById(id);
    }

    /**
     * 查询课程信息主列表
     * 
     * @param course 课程信息主
     * @return 课程信息主
     */
    @Override
    public List<Course> selectCourseList(Course course)
    {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程信息主
     * 
     * @param course 课程信息主
     * @return 结果
     */
    @Override
    public int insertCourse(Course course)
    {
        return courseMapper.insertCourse(course);
    }

    /**
     * 修改课程信息主
     * 
     * @param course 课程信息主
     * @return 结果
     */
    @Override
    public int updateCourse(Course course)
    {
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程信息主
     * 
     * @param ids 需要删除的课程信息主主键
     * @return 结果
     */
    @Override
    public int deleteCourseByIds(Long[] ids)
    {
        return courseMapper.deleteCourseByIds(ids);
    }

    /**
     * 删除课程信息主信息
     * 
     * @param id 课程信息主主键
     * @return 结果
     */
    @Override
    public int deleteCourseById(Long id)
    {
        return courseMapper.deleteCourseById(id);
    }
}
