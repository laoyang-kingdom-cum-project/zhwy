package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PerformanceLeaderboardwyx;

/**
 * 成绩排行榜Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-19 16:27:45
 */
public interface PerformanceLeaderboardwyxMapper 
{
    /**
     * 查询成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 成绩排行榜
     */
    public PerformanceLeaderboardwyx selectPerformanceLeaderboardwyxById(Long id);

    /**
     * 查询成绩排行榜列表
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 成绩排行榜集合
     */
    public List<PerformanceLeaderboardwyx> selectPerformanceLeaderboardwyxList(PerformanceLeaderboardwyx performanceLeaderboardwyx);

    /**
     * 新增成绩排行榜
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 结果
     */
    public int insertPerformanceLeaderboardwyx(PerformanceLeaderboardwyx performanceLeaderboardwyx);

    /**
     * 修改成绩排行榜
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 结果
     */
    public int updatePerformanceLeaderboardwyx(PerformanceLeaderboardwyx performanceLeaderboardwyx);

    /**
     * 删除成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 结果
     */
    public int deletePerformanceLeaderboardwyxById(Long id);

    /**
     * 批量删除成绩排行榜
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerformanceLeaderboardwyxByIds(Long[] ids);
}
