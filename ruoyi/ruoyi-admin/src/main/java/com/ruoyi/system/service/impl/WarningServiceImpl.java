package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WarningMapper;
import com.ruoyi.system.domain.Warning;
import com.ruoyi.system.service.IWarningService;

/**
 * 安全预警Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16 20:50:36
 */
@Service
public class WarningServiceImpl implements IWarningService 
{
    @Autowired
    private WarningMapper warningMapper;

    /**
     * 查询安全预警
     * 
     * @param id 安全预警主键
     * @return 安全预警
     */
    @Override
    public Warning selectWarningById(Long id)
    {
        return warningMapper.selectWarningById(id);
    }

    /**
     * 查询安全预警列表
     * 
     * @param warning 安全预警
     * @return 安全预警
     */
    @Override
    public List<Warning> selectWarningList(Warning warning)
    {
        return warningMapper.selectWarningList(warning);
    }

    /**
     * 新增安全预警
     * 
     * @param warning 安全预警
     * @return 结果
     */
    @Override
    public int insertWarning(Warning warning)
    {
        return warningMapper.insertWarning(warning);
    }

    /**
     * 修改安全预警
     * 
     * @param warning 安全预警
     * @return 结果
     */
    @Override
    public int updateWarning(Warning warning)
    {
        return warningMapper.updateWarning(warning);
    }

    /**
     * 批量删除安全预警
     * 
     * @param ids 需要删除的安全预警主键
     * @return 结果
     */
    @Override
    public int deleteWarningByIds(Long[] ids)
    {
        return warningMapper.deleteWarningByIds(ids);
    }

    /**
     * 删除安全预警信息
     * 
     * @param id 安全预警主键
     * @return 结果
     */
    @Override
    public int deleteWarningById(Long id)
    {
        return warningMapper.deleteWarningById(id);
    }
}
