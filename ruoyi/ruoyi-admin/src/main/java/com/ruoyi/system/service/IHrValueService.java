package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.HrValue;

/**
 * 心率数据Service接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface IHrValueService 
{
    /**
     * 查询心率数据
     * 
     * @param id 心率数据主键
     * @return 心率数据
     */
    public HrValue selectHrValueById(Long id);

    /**
     * 查询心率数据列表
     * 
     * @param hrValue 心率数据
     * @return 心率数据集合
     */
    public List<HrValue> selectHrValueList(HrValue hrValue);

    /**
     * 新增心率数据
     * 
     * @param hrValue 心率数据
     * @return 结果
     */
    public int insertHrValue(HrValue hrValue);

    /**
     * 修改心率数据
     * 
     * @param hrValue 心率数据
     * @return 结果
     */
    public int updateHrValue(HrValue hrValue);

    /**
     * 批量删除心率数据
     * 
     * @param ids 需要删除的心率数据主键集合
     * @return 结果
     */
    public int deleteHrValueByIds(Long[] ids);

    /**
     * 删除心率数据信息
     * 
     * @param id 心率数据主键
     * @return 结果
     */
    public int deleteHrValueById(Long id);
}
