package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FamilyHealth;

/**
 * 家人健康Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-23 10:25:07
 */
public interface FamilyHealthMapper 
{
    /**
     * 查询家人健康
     * 
     * @param id 家人健康主键
     * @return 家人健康
     */
    public FamilyHealth selectFamilyHealthById(Long id);

    /**
     * 查询家人健康列表
     * 
     * @param familyHealth 家人健康
     * @return 家人健康集合
     */
    public List<FamilyHealth> selectFamilyHealthList(FamilyHealth familyHealth);

    /**
     * 新增家人健康
     * 
     * @param familyHealth 家人健康
     * @return 结果
     */
    public int insertFamilyHealth(FamilyHealth familyHealth);

    /**
     * 修改家人健康
     * 
     * @param familyHealth 家人健康
     * @return 结果
     */
    public int updateFamilyHealth(FamilyHealth familyHealth);

    /**
     * 删除家人健康
     * 
     * @param id 家人健康主键
     * @return 结果
     */
    public int deleteFamilyHealthById(Long id);

    /**
     * 批量删除家人健康
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyHealthByIds(Long[] ids);
}
