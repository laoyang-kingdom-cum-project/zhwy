package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FamilyHealthMapper;
import com.ruoyi.system.domain.FamilyHealth;
import com.ruoyi.system.service.IFamilyHealthService;

/**
 * 家人健康Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-17 10:42:51
 */
@Service
public class FamilyHealthServiceImpl implements IFamilyHealthService 
{
    @Autowired
    private FamilyHealthMapper familyHealthMapper;

    /**
     * 查询家人健康
     * 
     * @param id 家人健康主键
     * @return 家人健康
     */
    @Override
    public FamilyHealth selectFamilyHealthById(Long id)
    {
        return familyHealthMapper.selectFamilyHealthById(id);
    }

    /**
     * 查询家人健康列表
     * 
     * @param familyHealth 家人健康
     * @return 家人健康
     */
    @Override
    public List<FamilyHealth> selectFamilyHealthList(FamilyHealth familyHealth)
    {
        return familyHealthMapper.selectFamilyHealthList(familyHealth);
    }

    /**
     * 新增家人健康
     * 
     * @param familyHealth 家人健康
     * @return 结果
     */
    @Override
    public int insertFamilyHealth(FamilyHealth familyHealth)
    {
        return familyHealthMapper.insertFamilyHealth(familyHealth);
    }

    /**
     * 修改家人健康
     * 
     * @param familyHealth 家人健康
     * @return 结果
     */
    @Override
    public int updateFamilyHealth(FamilyHealth familyHealth)
    {
        return familyHealthMapper.updateFamilyHealth(familyHealth);
    }

    /**
     * 批量删除家人健康
     * 
     * @param ids 需要删除的家人健康主键
     * @return 结果
     */
    @Override
    public int deleteFamilyHealthByIds(Long[] ids)
    {
        return familyHealthMapper.deleteFamilyHealthByIds(ids);
    }

    /**
     * 删除家人健康信息
     * 
     * @param id 家人健康主键
     * @return 结果
     */
    @Override
    public int deleteFamilyHealthById(Long id)
    {
        return familyHealthMapper.deleteFamilyHealthById(id);
    }
}
