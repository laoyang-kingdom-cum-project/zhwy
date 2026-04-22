package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PlanMapper;
import com.ruoyi.system.domain.Plan;
import com.ruoyi.system.service.IPlanService;

/**
 * 计划Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-20 21:21:50
 */
@Service
public class PlanServiceImpl implements IPlanService 
{
    @Autowired
    private PlanMapper planMapper;

    /**
     * 查询计划
     * 
     * @param id 计划主键
     * @return 计划
     */
    @Override
    public Plan selectPlanById(Long id)
    {
        return planMapper.selectPlanById(id);
    }

    /**
     * 查询计划列表
     * 
     * @param plan 计划
     * @return 计划
     */
    @Override
    public List<Plan> selectPlanList(Plan plan)
    {
        return planMapper.selectPlanList(plan);
    }

    /**
     * 新增计划
     * 
     * @param plan 计划
     * @return 结果
     */
    @Override
    public int insertPlan(Plan plan)
    {
        return planMapper.insertPlan(plan);
    }

    /**
     * 修改计划
     * 
     * @param plan 计划
     * @return 结果
     */
    @Override
    public int updatePlan(Plan plan)
    {
        return planMapper.updatePlan(plan);
    }

    /**
     * 批量删除计划
     * 
     * @param ids 需要删除的计划主键
     * @return 结果
     */
    @Override
    public int deletePlanByIds(Long[] ids)
    {
        return planMapper.deletePlanByIds(ids);
    }

    /**
     * 删除计划信息
     * 
     * @param id 计划主键
     * @return 结果
     */
    @Override
    public int deletePlanById(Long id)
    {
        return planMapper.deletePlanById(id);
    }
}
