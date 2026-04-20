package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.RunningRoutesMapper;
import com.ruoyi.system.domain.RunningRoutes;
import com.ruoyi.system.service.IRunningRoutesService;

/**
 * 跑步路线Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-10 15:38:30
 */
@Service
public class RunningRoutesServiceImpl implements IRunningRoutesService 
{
    @Autowired
    private RunningRoutesMapper runningRoutesMapper;

    /**
     * 查询跑步路线
     * 
     * @param id 跑步路线主键
     * @return 跑步路线
     */
    @Override
    public RunningRoutes selectRunningRoutesById(Long id)
    {
        return runningRoutesMapper.selectRunningRoutesById(id);
    }

    /**
     * 查询跑步路线列表
     * 
     * @param runningRoutes 跑步路线
     * @return 跑步路线
     */
    @Override
    public List<RunningRoutes> selectRunningRoutesList(RunningRoutes runningRoutes)
    {
        return runningRoutesMapper.selectRunningRoutesList(runningRoutes);
    }

    /**
     * 新增跑步路线
     * 
     * @param runningRoutes 跑步路线
     * @return 结果
     */
    @Override
    public int insertRunningRoutes(RunningRoutes runningRoutes)
    {
        return runningRoutesMapper.insertRunningRoutes(runningRoutes);
    }

    /**
     * 修改跑步路线
     * 
     * @param runningRoutes 跑步路线
     * @return 结果
     */
    @Override
    public int updateRunningRoutes(RunningRoutes runningRoutes)
    {
        return runningRoutesMapper.updateRunningRoutes(runningRoutes);
    }

    /**
     * 批量删除跑步路线
     * 
     * @param ids 需要删除的跑步路线主键
     * @return 结果
     */
    @Override
    public int deleteRunningRoutesByIds(Long[] ids)
    {
        return runningRoutesMapper.deleteRunningRoutesByIds(ids);
    }

    /**
     * 删除跑步路线信息
     * 
     * @param id 跑步路线主键
     * @return 结果
     */
    @Override
    public int deleteRunningRoutesById(Long id)
    {
        return runningRoutesMapper.deleteRunningRoutesById(id);
    }
}
