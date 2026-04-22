package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Warning;

/**
 * 安全预警Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-16 20:50:36
 */
public interface WarningMapper 
{
    /**
     * 查询安全预警
     * 
     * @param id 安全预警主键
     * @return 安全预警
     */
    public Warning selectWarningById(Long id);

    /**
     * 查询安全预警列表
     * 
     * @param warning 安全预警
     * @return 安全预警集合
     */
    public List<Warning> selectWarningList(Warning warning);

    /**
     * 新增安全预警
     * 
     * @param warning 安全预警
     * @return 结果
     */
    public int insertWarning(Warning warning);

    /**
     * 修改安全预警
     * 
     * @param warning 安全预警
     * @return 结果
     */
    public int updateWarning(Warning warning);

    /**
     * 删除安全预警
     * 
     * @param id 安全预警主键
     * @return 结果
     */
    public int deleteWarningById(Long id);

    /**
     * 批量删除安全预警
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarningByIds(Long[] ids);
}
