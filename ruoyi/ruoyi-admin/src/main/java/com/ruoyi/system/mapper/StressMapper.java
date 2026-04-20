package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Stress;

/**
 * 压力Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface StressMapper 
{
    /**
     * 查询压力
     * 
     * @param id 压力主键
     * @return 压力
     */
    public Stress selectStressById(Long id);

    /**
     * 查询压力列表
     * 
     * @param stress 压力
     * @return 压力集合
     */
    public List<Stress> selectStressList(Stress stress);

    /**
     * 新增压力
     * 
     * @param stress 压力
     * @return 结果
     */
    public int insertStress(Stress stress);

    /**
     * 修改压力
     * 
     * @param stress 压力
     * @return 结果
     */
    public int updateStress(Stress stress);

    /**
     * 删除压力
     * 
     * @param id 压力主键
     * @return 结果
     */
    public int deleteStressById(Long id);

    /**
     * 批量删除压力
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStressByIds(Long[] ids);
}
