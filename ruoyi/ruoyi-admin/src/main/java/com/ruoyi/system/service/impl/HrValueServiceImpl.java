package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.HrValueMapper;
import com.ruoyi.system.domain.HrValue;
import com.ruoyi.system.service.IHrValueService;

/**
 * 心率数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class HrValueServiceImpl implements IHrValueService 
{
    @Autowired
    private HrValueMapper hrValueMapper;

    /**
     * 查询心率数据
     * 
     * @param id 心率数据主键
     * @return 心率数据
     */
    @Override
    public HrValue selectHrValueById(Long id)
    {
        return hrValueMapper.selectHrValueById(id);
    }

    /**
     * 查询心率数据列表
     * 
     * @param hrValue 心率数据
     * @return 心率数据
     */
    @Override
    public List<HrValue> selectHrValueList(HrValue hrValue)
    {
        return hrValueMapper.selectHrValueList(hrValue);
    }

    /**
     * 新增心率数据
     * 
     * @param hrValue 心率数据
     * @return 结果
     */
    @Override
    public int insertHrValue(HrValue hrValue)
    {
        return hrValueMapper.insertHrValue(hrValue);
    }

    /**
     * 修改心率数据
     * 
     * @param hrValue 心率数据
     * @return 结果
     */
    @Override
    public int updateHrValue(HrValue hrValue)
    {
        return hrValueMapper.updateHrValue(hrValue);
    }

    /**
     * 批量删除心率数据
     * 
     * @param ids 需要删除的心率数据主键
     * @return 结果
     */
    @Override
    public int deleteHrValueByIds(Long[] ids)
    {
        return hrValueMapper.deleteHrValueByIds(ids);
    }

    /**
     * 删除心率数据信息
     * 
     * @param id 心率数据主键
     * @return 结果
     */
    @Override
    public int deleteHrValueById(Long id)
    {
        return hrValueMapper.deleteHrValueById(id);
    }
}
