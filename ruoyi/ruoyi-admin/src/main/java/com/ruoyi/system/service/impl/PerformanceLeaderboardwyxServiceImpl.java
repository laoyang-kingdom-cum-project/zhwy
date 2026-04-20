package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PerformanceLeaderboardwyxMapper;
import com.ruoyi.system.domain.PerformanceLeaderboardwyx;
import com.ruoyi.system.service.IPerformanceLeaderboardwyxService;

/**
 * 成绩排行榜Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-19 16:27:45
 */
@Service
public class PerformanceLeaderboardwyxServiceImpl implements IPerformanceLeaderboardwyxService 
{
    @Autowired
    private PerformanceLeaderboardwyxMapper performanceLeaderboardwyxMapper;

    /**
     * 查询成绩排行榜
     * 
     * @param id 成绩排行榜主键
     * @return 成绩排行榜
     */
    @Override
    public PerformanceLeaderboardwyx selectPerformanceLeaderboardwyxById(Long id)
    {
        return performanceLeaderboardwyxMapper.selectPerformanceLeaderboardwyxById(id);
    }

    /**
     * 查询成绩排行榜列表
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 成绩排行榜
     */
    @Override
    public List<PerformanceLeaderboardwyx> selectPerformanceLeaderboardwyxList(PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        return performanceLeaderboardwyxMapper.selectPerformanceLeaderboardwyxList(performanceLeaderboardwyx);
    }

    /**
     * 新增成绩排行榜
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 结果
     */
    @Override
    public int insertPerformanceLeaderboardwyx(PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        return performanceLeaderboardwyxMapper.insertPerformanceLeaderboardwyx(performanceLeaderboardwyx);
    }

    /**
     * 修改成绩排行榜
     * 
     * @param performanceLeaderboardwyx 成绩排行榜
     * @return 结果
     */
    @Override
    public int updatePerformanceLeaderboardwyx(PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        return performanceLeaderboardwyxMapper.updatePerformanceLeaderboardwyx(performanceLeaderboardwyx);
    }

    /**
     * 批量删除成绩排行榜
     * 
     * @param ids 需要删除的成绩排行榜主键
     * @return 结果
     */
    @Override
    public int deletePerformanceLeaderboardwyxByIds(Long[] ids)
    {
        return performanceLeaderboardwyxMapper.deletePerformanceLeaderboardwyxByIds(ids);
    }

    /**
     * 删除成绩排行榜信息
     * 
     * @param id 成绩排行榜主键
     * @return 结果
     */
    @Override
    public int deletePerformanceLeaderboardwyxById(Long id)
    {
        return performanceLeaderboardwyxMapper.deletePerformanceLeaderboardwyxById(id);
    }
}
