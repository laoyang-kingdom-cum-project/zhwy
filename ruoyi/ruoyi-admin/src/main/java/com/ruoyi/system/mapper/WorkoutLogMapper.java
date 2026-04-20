package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WorkoutLog;

/**
 * 运动记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface WorkoutLogMapper 
{
    /**
     * 查询运动记录
     * 
     * @param id 运动记录主键
     * @return 运动记录
     */
    public WorkoutLog selectWorkoutLogById(Long id);

    /**
     * 查询运动记录列表
     * 
     * @param workoutLog 运动记录
     * @return 运动记录集合
     */
    public List<WorkoutLog> selectWorkoutLogList(WorkoutLog workoutLog);

    /**
     * 新增运动记录
     * 
     * @param workoutLog 运动记录
     * @return 结果
     */
    public int insertWorkoutLog(WorkoutLog workoutLog);

    /**
     * 修改运动记录
     * 
     * @param workoutLog 运动记录
     * @return 结果
     */
    public int updateWorkoutLog(WorkoutLog workoutLog);

    /**
     * 删除运动记录
     * 
     * @param id 运动记录主键
     * @return 结果
     */
    public int deleteWorkoutLogById(Long id);

    /**
     * 批量删除运动记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkoutLogByIds(Long[] ids);
}
