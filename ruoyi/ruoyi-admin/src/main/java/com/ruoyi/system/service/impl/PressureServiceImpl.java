package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PressureMapper;
import com.ruoyi.system.domain.Pressure;
import com.ruoyi.system.service.IPressureService;

/**
 * 血压Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class PressureServiceImpl implements IPressureService 
{
    @Autowired
    private PressureMapper pressureMapper;

    /**
     * 查询血压
     * 
     * @param id 血压主键
     * @return 血压
     */
    @Override
    public Pressure selectPressureById(Long id)
    {
        return pressureMapper.selectPressureById(id);
    }

    /**
     * 查询血压列表
     * 
     * @param pressure 血压
     * @return 血压
     */
    @Override
    public List<Pressure> selectPressureList(Pressure pressure)
    {
        return pressureMapper.selectPressureList(pressure);
    }

    /**
     * 新增血压
     * 
     * @param pressure 血压
     * @return 结果
     */
    @Override
    public int insertPressure(Pressure pressure)
    {
        return pressureMapper.insertPressure(pressure);
    }

    /**
     * 修改血压
     * 
     * @param pressure 血压
     * @return 结果
     */
    @Override
    public int updatePressure(Pressure pressure)
    {
        return pressureMapper.updatePressure(pressure);
    }

    /**
     * 批量删除血压
     * 
     * @param ids 需要删除的血压主键
     * @return 结果
     */
    @Override
    public int deletePressureByIds(Long[] ids)
    {
        return pressureMapper.deletePressureByIds(ids);
    }

    /**
     * 删除血压信息
     * 
     * @param id 血压主键
     * @return 结果
     */
    @Override
    public int deletePressureById(Long id)
    {
        return pressureMapper.deletePressureById(id);
    }
}
