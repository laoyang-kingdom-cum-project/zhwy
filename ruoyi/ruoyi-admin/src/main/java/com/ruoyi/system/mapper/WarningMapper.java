package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Warning;

/**
 * 预警处理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-24 14:48:46
 */
public interface WarningMapper 
{
    /**
     * 查询预警处理
     * 
     * @param id 预警处理主键
     * @return 预警处理
     */
    public Warning selectWarningById(Long id);

    /**
     * 查询预警处理列表
     * 
     * @param warning 预警处理
     * @return 预警处理集合
     */
    public List<Warning> selectWarningList(Warning warning);

    /**
     * 新增预警处理
     * 
     * @param warning 预警处理
     * @return 结果
     */
    public int insertWarning(Warning warning);

    /**
     * 修改预警处理
     * 
     * @param warning 预警处理
     * @return 结果
     */
    public int updateWarning(Warning warning);

    /**
     * 删除预警处理
     * 
     * @param id 预警处理主键
     * @return 结果
     */
    public int deleteWarningById(Long id);

    /**
     * 批量删除预警处理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarningByIds(Long[] ids);
}
