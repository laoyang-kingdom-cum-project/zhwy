package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PerformanceLeaderboardMapper;
import com.ruoyi.system.domain.PerformanceLeaderboard;
import com.ruoyi.system.service.IPerformanceLeaderboardService;

/**
 * 成绩排行榜Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-10 20:09:55
 */
@Service
public class PerformanceLeaderboardServiceImpl implements IPerformanceLeaderboardService 
{
    @Autowired
    private PerformanceLeaderboardMapper performanceLeaderboardMapper;

    /**
     * 查询成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 成绩排行榜
     */
    @Override
    public PerformanceLeaderboard selectPerformanceLeaderboardById(Long id)
    {
        return performanceLeaderboardMapper.selectPerformanceLeaderboardById(id);
    }

    /**
     * 查询成绩排行榜列表
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 成绩排行榜
     */
    @Override
    public List<PerformanceLeaderboard> selectPerformanceLeaderboardList(PerformanceLeaderboard performanceLeaderboard)
    {
        return performanceLeaderboardMapper.selectPerformanceLeaderboardList(performanceLeaderboard);
    }

    /**
     * 新增成绩排行榜
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 结果
     */
    @Override
    public int insertPerformanceLeaderboard(PerformanceLeaderboard performanceLeaderboard)
    {
        return performanceLeaderboardMapper.insertPerformanceLeaderboard(performanceLeaderboard);
    }

    /**
     * 修改成绩排行榜
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 结果
     */
    @Override
    public int updatePerformanceLeaderboard(PerformanceLeaderboard performanceLeaderboard)
    {
        return performanceLeaderboardMapper.updatePerformanceLeaderboard(performanceLeaderboard);
    }

    /**
     * 批量删除成绩排行榜
     * 
     * @param ids 需要删除的成绩排行榜主键
     * @return 结果
     */
    @Override
    public int deletePerformanceLeaderboardByIds(Long[] ids)
    {
        return performanceLeaderboardMapper.deletePerformanceLeaderboardByIds(ids);
    }

    /**
     * 删除成绩排行榜信息
     * 
     * @param id 成绩排行榜主键
     * @return 结果
     */
    @Override
    public int deletePerformanceLeaderboardById(Long id)
    {
        return performanceLeaderboardMapper.deletePerformanceLeaderboardById(id);
    }
}
