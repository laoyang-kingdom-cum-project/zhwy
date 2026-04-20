package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WeightMapper;
import com.ruoyi.system.domain.Weight;
import com.ruoyi.system.service.IWeightService;

/**
 * 体重Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-15 15:18:23
 */
@Service
public class WeightServiceImpl implements IWeightService 
{
    @Autowired
    private WeightMapper weightMapper;

    /**
     * 查询体重
     * 
     * @param id 体重主键
     * @return 体重
     */
    @Override
    public Weight selectWeightById(Long id)
    {
        return weightMapper.selectWeightById(id);
    }

    /**
     * 查询体重列表
     * 
     * @param weight 体重
     * @return 体重
     */
    @Override
    public List<Weight> selectWeightList(Weight weight)
    {
        return weightMapper.selectWeightList(weight);
    }

    /**
     * 新增体重
     * 
     * @param weight 体重
     * @return 结果
     */
    @Override
    public int insertWeight(Weight weight)
    {
        return weightMapper.insertWeight(weight);
    }

    /**
     * 修改体重
     * 
     * @param weight 体重
     * @return 结果
     */
    @Override
    public int updateWeight(Weight weight)
    {
        return weightMapper.updateWeight(weight);
    }

    /**
     * 批量删除体重
     * 
     * @param ids 需要删除的体重主键
     * @return 结果
     */
    @Override
    public int deleteWeightByIds(Long[] ids)
    {
        return weightMapper.deleteWeightByIds(ids);
    }

    /**
     * 删除体重信息
     * 
     * @param id 体重主键
     * @return 结果
     */
    @Override
    public int deleteWeightById(Long id)
    {
        return weightMapper.deleteWeightById(id);
    }
}
