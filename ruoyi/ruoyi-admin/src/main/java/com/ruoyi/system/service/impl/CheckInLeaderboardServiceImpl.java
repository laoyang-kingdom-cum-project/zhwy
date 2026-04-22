package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CheckInLeaderboardMapper;
import com.ruoyi.system.domain.CheckInLeaderboard;
import com.ruoyi.system.service.ICheckInLeaderboardService;

/**
 * 打卡排行榜Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-10 17:25:39
 */
@Service
public class CheckInLeaderboardServiceImpl implements ICheckInLeaderboardService 
{
    @Autowired
    private CheckInLeaderboardMapper checkInLeaderboardMapper;

    /**
     * 查询打卡排行榜
     * 
     * @param id 打卡排行榜主键
     * @return 打卡排行榜
     */
    @Override
    public CheckInLeaderboard selectCheckInLeaderboardById(Long id)
    {
        return checkInLeaderboardMapper.selectCheckInLeaderboardById(id);
    }

    /**
     * 查询打卡排行榜列表
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 打卡排行榜
     */
    @Override
    public List<CheckInLeaderboard> selectCheckInLeaderboardList(CheckInLeaderboard checkInLeaderboard)
    {
        return checkInLeaderboardMapper.selectCheckInLeaderboardList(checkInLeaderboard);
    }

    /**
     * 新增打卡排行榜
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 结果
     */
    @Override
    public int insertCheckInLeaderboard(CheckInLeaderboard checkInLeaderboard)
    {
        return checkInLeaderboardMapper.insertCheckInLeaderboard(checkInLeaderboard);
    }

    /**
     * 修改打卡排行榜
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 结果
     */
    @Override
    public int updateCheckInLeaderboard(CheckInLeaderboard checkInLeaderboard)
    {
        return checkInLeaderboardMapper.updateCheckInLeaderboard(checkInLeaderboard);
    }

    /**
     * 批量删除打卡排行榜
     * 
     * @param ids 需要删除的打卡排行榜主键
     * @return 结果
     */
    @Override
    public int deleteCheckInLeaderboardByIds(Long[] ids)
    {
        return checkInLeaderboardMapper.deleteCheckInLeaderboardByIds(ids);
    }

    /**
     * 删除打卡排行榜信息
     * 
     * @param id 打卡排行榜主键
     * @return 结果
     */
    @Override
    public int deleteCheckInLeaderboardById(Long id)
    {
        return checkInLeaderboardMapper.deleteCheckInLeaderboardById(id);
    }
}
