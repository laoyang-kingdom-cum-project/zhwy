package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.RunningRoutes;

/**
 * 跑步路线Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-10 15:38:30
 */
public interface RunningRoutesMapper 
{
    /**
     * 查询跑步路线
     * 
     * @param id 跑步路线主键
     * @return 跑步路线
     */
    public RunningRoutes selectRunningRoutesById(Long id);

    /**
     * 查询跑步路线列表
     * 
     * @param runningRoutes 跑步路线
     * @return 跑步路线集合
     */
    public List<RunningRoutes> selectRunningRoutesList(RunningRoutes runningRoutes);

    /**
     * 新增跑步路线
     * 
     * @param runningRoutes 跑步路线
     * @return 结果
     */
    public int insertRunningRoutes(RunningRoutes runningRoutes);

    /**
     * 修改跑步路线
     * 
     * @param runningRoutes 跑步路线
     * @return 结果
     */
    public int updateRunningRoutes(RunningRoutes runningRoutes);

    /**
     * 删除跑步路线
     * 
     * @param id 跑步路线主键
     * @return 结果
     */
    public int deleteRunningRoutesById(Long id);

    /**
     * 批量删除跑步路线
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRunningRoutesByIds(Long[] ids);
}
