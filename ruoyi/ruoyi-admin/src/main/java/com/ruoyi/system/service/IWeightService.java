package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Weight;

/**
 * 体重Service接口
 * 
 * @author ruoyi
 * @date 2026-01-15 15:18:23
 */
public interface IWeightService 
{
    /**
     * 查询体重
     * 
     * @param id 体重主键
     * @return 体重
     */
    public Weight selectWeightById(Long id);

    /**
     * 查询体重列表
     * 
     * @param weight 体重
     * @return 体重集合
     */
    public List<Weight> selectWeightList(Weight weight);

    /**
     * 新增体重
     * 
     * @param weight 体重
     * @return 结果
     */
    public int insertWeight(Weight weight);

    /**
     * 修改体重
     * 
     * @param weight 体重
     * @return 结果
     */
    public int updateWeight(Weight weight);

    /**
     * 批量删除体重
     * 
     * @param ids 需要删除的体重主键集合
     * @return 结果
     */
    public int deleteWeightByIds(Long[] ids);

    /**
     * 删除体重信息
     * 
     * @param id 体重主键
     * @return 结果
     */
    public int deleteWeightById(Long id);
}
