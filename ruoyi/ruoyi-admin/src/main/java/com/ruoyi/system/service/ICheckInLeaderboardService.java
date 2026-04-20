package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CheckInLeaderboard;

/**
 * 打卡排行榜Service接口
 * 
 * @author ruoyi
 * @date 2026-01-10 17:25:39
 */
public interface ICheckInLeaderboardService 
{
    /**
     * 查询打卡排行榜
     * 
     * @param id 打卡排行榜主键
     * @return 打卡排行榜
     */
    public CheckInLeaderboard selectCheckInLeaderboardById(Long id);

    /**
     * 查询打卡排行榜列表
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 打卡排行榜集合
     */
    public List<CheckInLeaderboard> selectCheckInLeaderboardList(CheckInLeaderboard checkInLeaderboard);

    /**
     * 新增打卡排行榜
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 结果
     */
    public int insertCheckInLeaderboard(CheckInLeaderboard checkInLeaderboard);

    /**
     * 修改打卡排行榜
     * 
     * @param checkInLeaderboard 打卡排行榜
     * @return 结果
     */
    public int updateCheckInLeaderboard(CheckInLeaderboard checkInLeaderboard);

    /**
     * 批量删除打卡排行榜
     * 
     * @param ids 需要删除的打卡排行榜主键集合
     * @return 结果
     */
    public int deleteCheckInLeaderboardByIds(Long[] ids);

    /**
     * 删除打卡排行榜信息
     * 
     * @param id 打卡排行榜主键
     * @return 结果
     */
    public int deleteCheckInLeaderboardById(Long id);
}
