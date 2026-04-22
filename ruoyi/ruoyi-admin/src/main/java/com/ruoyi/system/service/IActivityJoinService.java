package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ActivityJoin;

/**
 * 活动报名Service接口
 * 
 * @author ruoyi
 * @date 2026-04-17 15:48:32
 */
public interface IActivityJoinService 
{
    /**
     * 查询活动报名
     * 
     * @param id 活动报名主键
     * @return 活动报名
     */
    public ActivityJoin selectActivityJoinById(Long id);

    /**
     * 查询活动报名列表
     * 
     * @param activityJoin 活动报名
     * @return 活动报名集合
     */
    public List<ActivityJoin> selectActivityJoinList(ActivityJoin activityJoin);

    /**
     * 新增活动报名
     * 
     * @param activityJoin 活动报名
     * @return 结果
     */
    public int insertActivityJoin(ActivityJoin activityJoin);

    /**
     * 修改活动报名
     * 
     * @param activityJoin 活动报名
     * @return 结果
     */
    public int updateActivityJoin(ActivityJoin activityJoin);

    /**
     * 批量删除活动报名
     * 
     * @param ids 需要删除的活动报名主键集合
     * @return 结果
     */
    public int deleteActivityJoinByIds(Long[] ids);

    /**
     * 删除活动报名信息
     * 
     * @param id 活动报名主键
     * @return 结果
     */
    public int deleteActivityJoinById(Long id);
}
