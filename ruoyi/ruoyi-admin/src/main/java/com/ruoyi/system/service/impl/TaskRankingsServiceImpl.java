package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TaskRankingsMapper;
import com.ruoyi.system.domain.TaskRankings;
import com.ruoyi.system.service.ITaskRankingsService;

/**
 * 任务排行榜Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@Service
public class TaskRankingsServiceImpl implements ITaskRankingsService 
{
    @Autowired
    private TaskRankingsMapper taskRankingsMapper;

    /**
     * 查询任务排行榜
     * 
     * @param id 任务排行榜主键
     * @return 任务排行榜
     */
    @Override
    public TaskRankings selectTaskRankingsById(Long id)
    {
        return taskRankingsMapper.selectTaskRankingsById(id);
    }

    /**
     * 查询任务排行榜列表
     * 
     * @param taskRankings 任务排行榜
     * @return 任务排行榜
     */
    @Override
    public List<TaskRankings> selectTaskRankingsList(TaskRankings taskRankings)
    {
        return taskRankingsMapper.selectTaskRankingsList(taskRankings);
    }

    /**
     * 新增任务排行榜
     * 
     * @param taskRankings 任务排行榜
     * @return 结果
     */
    @Override
    public int insertTaskRankings(TaskRankings taskRankings)
    {
        return taskRankingsMapper.insertTaskRankings(taskRankings);
    }

    /**
     * 修改任务排行榜
     * 
     * @param taskRankings 任务排行榜
     * @return 结果
     */
    @Override
    public int updateTaskRankings(TaskRankings taskRankings)
    {
        return taskRankingsMapper.updateTaskRankings(taskRankings);
    }

    /**
     * 批量删除任务排行榜
     * 
     * @param ids 需要删除的任务排行榜主键
     * @return 结果
     */
    @Override
    public int deleteTaskRankingsByIds(Long[] ids)
    {
        return taskRankingsMapper.deleteTaskRankingsByIds(ids);
    }

    /**
     * 删除任务排行榜信息
     * 
     * @param id 任务排行榜主键
     * @return 结果
     */
    @Override
    public int deleteTaskRankingsById(Long id)
    {
        return taskRankingsMapper.deleteTaskRankingsById(id);
    }
}
