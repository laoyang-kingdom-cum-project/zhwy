package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.RunningRouteFavoriteStatus;

/**
 * 路线收藏状态Service接口
 * 
 * @author ruoyi
 * @date 2026-01-10 17:31:13
 */
public interface IRunningRouteFavoriteStatusService 
{
    /**
     * 查询路线收藏状态
     * 
     * @param id 路线收藏状态主键
     * @return 路线收藏状态
     */
    public RunningRouteFavoriteStatus selectRunningRouteFavoriteStatusById(Long id);

    /**
     * 查询路线收藏状态列表
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 路线收藏状态集合
     */
    public List<RunningRouteFavoriteStatus> selectRunningRouteFavoriteStatusList(RunningRouteFavoriteStatus runningRouteFavoriteStatus);

    /**
     * 新增路线收藏状态
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 结果
     */
    public int insertRunningRouteFavoriteStatus(RunningRouteFavoriteStatus runningRouteFavoriteStatus);

    /**
     * 修改路线收藏状态
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 结果
     */
    public int updateRunningRouteFavoriteStatus(RunningRouteFavoriteStatus runningRouteFavoriteStatus);

    /**
     * 批量删除路线收藏状态
     * 
     * @param ids 需要删除的路线收藏状态主键集合
     * @return 结果
     */
    public int deleteRunningRouteFavoriteStatusByIds(Long[] ids);

    /**
     * 删除路线收藏状态信息
     * 
     * @param id 路线收藏状态主键
     * @return 结果
     */
    public int deleteRunningRouteFavoriteStatusById(Long id);
}
