package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Pressure;

/**
 * 血压Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface PressureMapper 
{
    /**
     * 查询血压
     * 
     * @param id 血压主键
     * @return 血压
     */
    public Pressure selectPressureById(Long id);

    /**
     * 查询血压列表
     * 
     * @param pressure 血压
     * @return 血压集合
     */
    public List<Pressure> selectPressureList(Pressure pressure);

    /**
     * 新增血压
     * 
     * @param pressure 血压
     * @return 结果
     */
    public int insertPressure(Pressure pressure);

    /**
     * 修改血压
     * 
     * @param pressure 血压
     * @return 结果
     */
    public int updatePressure(Pressure pressure);

    /**
     * 删除血压
     * 
     * @param id 血压主键
     * @return 结果
     */
    public int deletePressureById(Long id);

    /**
     * 批量删除血压
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePressureByIds(Long[] ids);
}
