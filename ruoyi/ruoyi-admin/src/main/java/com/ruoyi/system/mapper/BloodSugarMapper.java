package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BloodSugar;

/**
 * 血糖Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface BloodSugarMapper 
{
    /**
     * 查询血糖
     * 
     * @param id 血糖主键
     * @return 血糖
     */
    public BloodSugar selectBloodSugarById(Long id);

    /**
     * 查询血糖列表
     * 
     * @param bloodSugar 血糖
     * @return 血糖集合
     */
    public List<BloodSugar> selectBloodSugarList(BloodSugar bloodSugar);

    /**
     * 新增血糖
     * 
     * @param bloodSugar 血糖
     * @return 结果
     */
    public int insertBloodSugar(BloodSugar bloodSugar);

    /**
     * 修改血糖
     * 
     * @param bloodSugar 血糖
     * @return 结果
     */
    public int updateBloodSugar(BloodSugar bloodSugar);

    /**
     * 删除血糖
     * 
     * @param id 血糖主键
     * @return 结果
     */
    public int deleteBloodSugarById(Long id);

    /**
     * 批量删除血糖
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBloodSugarByIds(Long[] ids);
}
