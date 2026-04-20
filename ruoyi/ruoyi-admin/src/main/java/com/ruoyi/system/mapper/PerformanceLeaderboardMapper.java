package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PerformanceLeaderboard;

/**
 * 成绩排行榜Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-10 20:09:55
 */
public interface PerformanceLeaderboardMapper 
{
    /**
     * 查询成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 成绩排行榜
     */
    public PerformanceLeaderboard selectPerformanceLeaderboardById(Long id);

    /**
     * 查询成绩排行榜列表
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 成绩排行榜集合
     */
    public List<PerformanceLeaderboard> selectPerformanceLeaderboardList(PerformanceLeaderboard performanceLeaderboard);

    /**
     * 新增成绩排行榜
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 结果
     */
    public int insertPerformanceLeaderboard(PerformanceLeaderboard performanceLeaderboard);

    /**
     * 修改成绩排行榜
     * 
     * @param performanceLeaderboard 成绩排行榜
     * @return 结果
     */
    public int updatePerformanceLeaderboard(PerformanceLeaderboard performanceLeaderboard);

    /**
     * 删除成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 结果
     */
    public int deletePerformanceLeaderboardById(Long id);

    /**
     * 批量删除成绩排行榜
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerformanceLeaderboardByIds(Long[] ids);
}
