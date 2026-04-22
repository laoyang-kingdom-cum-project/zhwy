package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ActivityJoinMapper;
import com.ruoyi.system.domain.ActivityJoin;
import com.ruoyi.system.service.IActivityJoinService;

/**
 * 活动报名Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-17 15:48:32
 */
@Service
public class ActivityJoinServiceImpl implements IActivityJoinService 
{
    @Autowired
    private ActivityJoinMapper activityJoinMapper;

    /**
     * 查询活动报名
     * 
     * @param id 活动报名主键
     * @return 活动报名
     */
    @Override
    public ActivityJoin selectActivityJoinById(Long id)
    {
        return activityJoinMapper.selectActivityJoinById(id);
    }

    /**
     * 查询活动报名列表
     * 
     * @param activityJoin 活动报名
     * @return 活动报名
     */
    @Override
    public List<ActivityJoin> selectActivityJoinList(ActivityJoin activityJoin)
    {
        return activityJoinMapper.selectActivityJoinList(activityJoin);
    }

    /**
     * 新增活动报名
     * 
     * @param activityJoin 活动报名
     * @return 结果
     */
    @Override
    public int insertActivityJoin(ActivityJoin activityJoin)
    {
        return activityJoinMapper.insertActivityJoin(activityJoin);
    }

    /**
     * 修改活动报名
     * 
     * @param activityJoin 活动报名
     * @return 结果
     */
    @Override
    public int updateActivityJoin(ActivityJoin activityJoin)
    {
        return activityJoinMapper.updateActivityJoin(activityJoin);
    }

    /**
     * 批量删除活动报名
     * 
     * @param ids 需要删除的活动报名主键
     * @return 结果
     */
    @Override
    public int deleteActivityJoinByIds(Long[] ids)
    {
        return activityJoinMapper.deleteActivityJoinByIds(ids);
    }

    /**
     * 删除活动报名信息
     * 
     * @param id 活动报名主键
     * @return 结果
     */
    @Override
    public int deleteActivityJoinById(Long id)
    {
        return activityJoinMapper.deleteActivityJoinById(id);
    }
}
