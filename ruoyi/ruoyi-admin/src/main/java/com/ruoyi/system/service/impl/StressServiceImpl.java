package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StressMapper;
import com.ruoyi.system.domain.Stress;
import com.ruoyi.system.service.IStressService;

/**
 * 压力Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@Service
public class StressServiceImpl implements IStressService 
{
    @Autowired
    private StressMapper stressMapper;

    /**
     * 查询压力
     * 
     * @param id 压力主键
     * @return 压力
     */
    @Override
    public Stress selectStressById(Long id)
    {
        return stressMapper.selectStressById(id);
    }

    /**
     * 查询压力列表
     * 
     * @param stress 压力
     * @return 压力
     */
    @Override
    public List<Stress> selectStressList(Stress stress)
    {
        return stressMapper.selectStressList(stress);
    }

    /**
     * 新增压力
     * 
     * @param stress 压力
     * @return 结果
     */
    @Override
    public int insertStress(Stress stress)
    {
        return stressMapper.insertStress(stress);
    }

    /**
     * 修改压力
     * 
     * @param stress 压力
     * @return 结果
     */
    @Override
    public int updateStress(Stress stress)
    {
        return stressMapper.updateStress(stress);
    }

    /**
     * 批量删除压力
     * 
     * @param ids 需要删除的压力主键
     * @return 结果
     */
    @Override
    public int deleteStressByIds(Long[] ids)
    {
        return stressMapper.deleteStressByIds(ids);
    }

    /**
     * 删除压力信息
     * 
     * @param id 压力主键
     * @return 结果
     */
    @Override
    public int deleteStressById(Long id)
    {
        return stressMapper.deleteStressById(id);
    }
}
