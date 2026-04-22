package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.RunningRouteFavoriteStatusMapper;
import com.ruoyi.system.domain.RunningRouteFavoriteStatus;
import com.ruoyi.system.service.IRunningRouteFavoriteStatusService;

/**
 * 路线收藏状态Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-10 17:31:13
 */
@Service
public class RunningRouteFavoriteStatusServiceImpl implements IRunningRouteFavoriteStatusService 
{
    @Autowired
    private RunningRouteFavoriteStatusMapper runningRouteFavoriteStatusMapper;

    /**
     * 查询路线收藏状态
     * 
     * @param id 路线收藏状态主键
     * @return 路线收藏状态
     */
    @Override
    public RunningRouteFavoriteStatus selectRunningRouteFavoriteStatusById(Long id)
    {
        return runningRouteFavoriteStatusMapper.selectRunningRouteFavoriteStatusById(id);
    }

    /**
     * 查询路线收藏状态列表
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 路线收藏状态
     */
    @Override
    public List<RunningRouteFavoriteStatus> selectRunningRouteFavoriteStatusList(RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        return runningRouteFavoriteStatusMapper.selectRunningRouteFavoriteStatusList(runningRouteFavoriteStatus);
    }

    /**
     * 新增路线收藏状态
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 结果
     */
    @Override
    public int insertRunningRouteFavoriteStatus(RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        return runningRouteFavoriteStatusMapper.insertRunningRouteFavoriteStatus(runningRouteFavoriteStatus);
    }

    /**
     * 修改路线收藏状态
     * 
     * @param runningRouteFavoriteStatus 路线收藏状态
     * @return 结果
     */
    @Override
    public int updateRunningRouteFavoriteStatus(RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        return runningRouteFavoriteStatusMapper.updateRunningRouteFavoriteStatus(runningRouteFavoriteStatus);
    }

    /**
     * 批量删除路线收藏状态
     * 
     * @param ids 需要删除的路线收藏状态主键
     * @return 结果
     */
    @Override
    public int deleteRunningRouteFavoriteStatusByIds(Long[] ids)
    {
        return runningRouteFavoriteStatusMapper.deleteRunningRouteFavoriteStatusByIds(ids);
    }

    /**
     * 删除路线收藏状态信息
     * 
     * @param id 路线收藏状态主键
     * @return 结果
     */
    @Override
    public int deleteRunningRouteFavoriteStatusById(Long id)
    {
        return runningRouteFavoriteStatusMapper.deleteRunningRouteFavoriteStatusById(id);
    }
}
