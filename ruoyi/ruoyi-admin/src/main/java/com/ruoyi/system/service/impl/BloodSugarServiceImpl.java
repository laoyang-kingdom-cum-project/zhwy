package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BloodSugarMapper;
import com.ruoyi.system.domain.BloodSugar;
import com.ruoyi.system.service.IBloodSugarService;

/**
 * 血糖Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class BloodSugarServiceImpl implements IBloodSugarService 
{
    @Autowired
    private BloodSugarMapper bloodSugarMapper;

    /**
     * 查询血糖
     * 
     * @param id 血糖主键
     * @return 血糖
     */
    @Override
    public BloodSugar selectBloodSugarById(Long id)
    {
        return bloodSugarMapper.selectBloodSugarById(id);
    }

    /**
     * 查询血糖列表
     * 
     * @param bloodSugar 血糖
     * @return 血糖
     */
    @Override
    public List<BloodSugar> selectBloodSugarList(BloodSugar bloodSugar)
    {
        return bloodSugarMapper.selectBloodSugarList(bloodSugar);
    }

    /**
     * 新增血糖
     * 
     * @param bloodSugar 血糖
     * @return 结果
     */
    @Override
    public int insertBloodSugar(BloodSugar bloodSugar)
    {
        return bloodSugarMapper.insertBloodSugar(bloodSugar);
    }

    /**
     * 修改血糖
     * 
     * @param bloodSugar 血糖
     * @return 结果
     */
    @Override
    public int updateBloodSugar(BloodSugar bloodSugar)
    {
        return bloodSugarMapper.updateBloodSugar(bloodSugar);
    }

    /**
     * 批量删除血糖
     * 
     * @param ids 需要删除的血糖主键
     * @return 结果
     */
    @Override
    public int deleteBloodSugarByIds(Long[] ids)
    {
        return bloodSugarMapper.deleteBloodSugarByIds(ids);
    }

    /**
     * 删除血糖信息
     * 
     * @param id 血糖主键
     * @return 结果
     */
    @Override
    public int deleteBloodSugarById(Long id)
    {
        return bloodSugarMapper.deleteBloodSugarById(id);
    }
}
