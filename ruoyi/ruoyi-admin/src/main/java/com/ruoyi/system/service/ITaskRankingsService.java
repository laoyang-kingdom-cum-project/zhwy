package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TaskRankings;

/**
 * 任务排行榜Service接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface ITaskRankingsService 
{
    /**
     * 查询任务排行榜
     * 
     * @param id 任务排行榜主键
     * @return 任务排行榜
     */
    public TaskRankings selectTaskRankingsById(Long id);

    /**
     * 查询任务排行榜列表
     * 
     * @param taskRankings 任务排行榜
     * @return 任务排行榜集合
     */
    public List<TaskRankings> selectTaskRankingsList(TaskRankings taskRankings);

    /**
     * 新增任务排行榜
     * 
     * @param taskRankings 任务排行榜
     * @return 结果
     */
    public int insertTaskRankings(TaskRankings taskRankings);

    /**
     * 修改任务排行榜
     * 
     * @param taskRankings 任务排行榜
     * @return 结果
     */
    public int updateTaskRankings(TaskRankings taskRankings);

    /**
     * 批量删除任务排行榜
     * 
     * @param ids 需要删除的任务排行榜主键集合
     * @return 结果
     */
    public int deleteTaskRankingsByIds(Long[] ids);

    /**
     * 删除任务排行榜信息
     * 
     * @param id 任务排行榜主键
     * @return 结果
     */
    public int deleteTaskRankingsById(Long id);
}
