package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WorkoutLogMapper;
import com.ruoyi.system.domain.WorkoutLog;
import com.ruoyi.system.service.IWorkoutLogService;

/**
 * 运动记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class WorkoutLogServiceImpl implements IWorkoutLogService 
{
    @Autowired
    private WorkoutLogMapper workoutLogMapper;

    /**
     * 查询运动记录
     * 
     * @param id 运动记录主键
     * @return 运动记录
     */
    @Override
    public WorkoutLog selectWorkoutLogById(Long id)
    {
        return workoutLogMapper.selectWorkoutLogById(id);
    }

    /**
     * 查询运动记录列表
     * 
     * @param workoutLog 运动记录
     * @return 运动记录
     */
    @Override
    public List<WorkoutLog> selectWorkoutLogList(WorkoutLog workoutLog)
    {
        return workoutLogMapper.selectWorkoutLogList(workoutLog);
    }

    /**
     * 新增运动记录
     * 
     * @param workoutLog 运动记录
     * @return 结果
     */
    @Override
    public int insertWorkoutLog(WorkoutLog workoutLog)
    {
        return workoutLogMapper.insertWorkoutLog(workoutLog);
    }

    /**
     * 修改运动记录
     * 
     * @param workoutLog 运动记录
     * @return 结果
     */
    @Override
    public int updateWorkoutLog(WorkoutLog workoutLog)
    {
        return workoutLogMapper.updateWorkoutLog(workoutLog);
    }

    /**
     * 批量删除运动记录
     * 
     * @param ids 需要删除的运动记录主键
     * @return 结果
     */
    @Override
    public int deleteWorkoutLogByIds(Long[] ids)
    {
        return workoutLogMapper.deleteWorkoutLogByIds(ids);
    }

    /**
     * 删除运动记录信息
     * 
     * @param id 运动记录主键
     * @return 结果
     */
    @Override
    public int deleteWorkoutLogById(Long id)
    {
        return workoutLogMapper.deleteWorkoutLogById(id);
    }
}
