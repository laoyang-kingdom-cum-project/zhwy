package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WarningMapper;
import com.ruoyi.system.domain.Warning;
import com.ruoyi.system.service.IWarningService;

/**
 * 预警处理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-24 14:48:46
 */
@Service
public class WarningServiceImpl implements IWarningService 
{
    @Autowired
    private WarningMapper warningMapper;

    /**
     * 查询预警处理
     * 
     * @param id 预警处理主键
     * @return 预警处理
     */
    @Override
    public Warning selectWarningById(Long id)
    {
        return warningMapper.selectWarningById(id);
    }

    /**
     * 查询预警处理列表
     * 
     * @param warning 预警处理
     * @return 预警处理
     */
    @Override
    public List<Warning> selectWarningList(Warning warning)
    {
        return warningMapper.selectWarningList(warning);
    }

    /**
     * 新增预警处理
     * 
     * @param warning 预警处理
     * @return 结果
     */
    @Override
    public int insertWarning(Warning warning)
    {
        return warningMapper.insertWarning(warning);
    }

    /**
     * 修改预警处理
     * 
     * @param warning 预警处理
     * @return 结果
     */
    @Override
    public int updateWarning(Warning warning)
    {
        return warningMapper.updateWarning(warning);
    }

    /**
     * 批量删除预警处理
     * 
     * @param ids 需要删除的预警处理主键
     * @return 结果
     */
    @Override
    public int deleteWarningByIds(Long[] ids)
    {
        return warningMapper.deleteWarningByIds(ids);
    }

    /**
     * 删除预警处理信息
     * 
     * @param id 预警处理主键
     * @return 结果
     */
    @Override
    public int deleteWarningById(Long id)
    {
        return warningMapper.deleteWarningById(id);
    }
}
