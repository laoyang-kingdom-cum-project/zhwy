package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Plan;

/**
 * 计划Service接口
 * 
 * @author ruoyi
 * @date 2026-01-20 21:21:50
 */
public interface IPlanService 
{
    /**
     * 查询计划
     * 
     * @param id 计划主键
     * @return 计划
     */
    public Plan selectPlanById(Long id);

    /**
     * 查询计划列表
     * 
     * @param plan 计划
     * @return 计划集合
     */
    public List<Plan> selectPlanList(Plan plan);

    /**
     * 新增计划
     * 
     * @param plan 计划
     * @return 结果
     */
    public int insertPlan(Plan plan);

    /**
     * 修改计划
     * 
     * @param plan 计划
     * @return 结果
     */
    public int updatePlan(Plan plan);

    /**
     * 批量删除计划
     * 
     * @param ids 需要删除的计划主键集合
     * @return 结果
     */
    public int deletePlanByIds(Long[] ids);

    /**
     * 删除计划信息
     * 
     * @param id 计划主键
     * @return 结果
     */
    public int deletePlanById(Long id);
}
