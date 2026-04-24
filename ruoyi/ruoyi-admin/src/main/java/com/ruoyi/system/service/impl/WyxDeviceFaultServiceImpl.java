package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WyxDeviceFaultMapper;
import com.ruoyi.system.domain.WyxDeviceFault;
import com.ruoyi.system.service.IWyxDeviceFaultService;

/**
 * 设备故障记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-24 16:00:03
 */
@Service
public class WyxDeviceFaultServiceImpl implements IWyxDeviceFaultService 
{
    @Autowired
    private WyxDeviceFaultMapper wyxDeviceFaultMapper;

    /**
     * 查询设备故障记录
     * 
     * @param id 设备故障记录主键
     * @return 设备故障记录
     */
    @Override
    public WyxDeviceFault selectWyxDeviceFaultById(Long id)
    {
        return wyxDeviceFaultMapper.selectWyxDeviceFaultById(id);
    }

    /**
     * 查询设备故障记录列表
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 设备故障记录
     */
    @Override
    public List<WyxDeviceFault> selectWyxDeviceFaultList(WyxDeviceFault wyxDeviceFault)
    {
        return wyxDeviceFaultMapper.selectWyxDeviceFaultList(wyxDeviceFault);
    }

    /**
     * 新增设备故障记录
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 结果
     */
    @Override
    public int insertWyxDeviceFault(WyxDeviceFault wyxDeviceFault)
    {
        wyxDeviceFault.setCreateTime(DateUtils.getNowDate());
        return wyxDeviceFaultMapper.insertWyxDeviceFault(wyxDeviceFault);
    }

    /**
     * 修改设备故障记录
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 结果
     */
    @Override
    public int updateWyxDeviceFault(WyxDeviceFault wyxDeviceFault)
    {
        wyxDeviceFault.setUpdateTime(DateUtils.getNowDate());
        return wyxDeviceFaultMapper.updateWyxDeviceFault(wyxDeviceFault);
    }

    /**
     * 批量删除设备故障记录
     * 
     * @param ids 需要删除的设备故障记录主键
     * @return 结果
     */
    @Override
    public int deleteWyxDeviceFaultByIds(Long[] ids)
    {
        return wyxDeviceFaultMapper.deleteWyxDeviceFaultByIds(ids);
    }

    /**
     * 删除设备故障记录信息
     * 
     * @param id 设备故障记录主键
     * @return 结果
     */
    @Override
    public int deleteWyxDeviceFaultById(Long id)
    {
        return wyxDeviceFaultMapper.deleteWyxDeviceFaultById(id);
    }
}
