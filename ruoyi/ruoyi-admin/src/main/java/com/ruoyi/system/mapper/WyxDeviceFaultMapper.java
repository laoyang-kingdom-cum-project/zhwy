package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WyxDeviceFault;

/**
 * 设备故障记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-24 16:00:03
 */
public interface WyxDeviceFaultMapper 
{
    /**
     * 查询设备故障记录
     * 
     * @param id 设备故障记录主键
     * @return 设备故障记录
     */
    public WyxDeviceFault selectWyxDeviceFaultById(Long id);

    /**
     * 查询设备故障记录列表
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 设备故障记录集合
     */
    public List<WyxDeviceFault> selectWyxDeviceFaultList(WyxDeviceFault wyxDeviceFault);

    /**
     * 新增设备故障记录
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 结果
     */
    public int insertWyxDeviceFault(WyxDeviceFault wyxDeviceFault);

    /**
     * 修改设备故障记录
     * 
     * @param wyxDeviceFault 设备故障记录
     * @return 结果
     */
    public int updateWyxDeviceFault(WyxDeviceFault wyxDeviceFault);

    /**
     * 删除设备故障记录
     * 
     * @param id 设备故障记录主键
     * @return 结果
     */
    public int deleteWyxDeviceFaultById(Long id);

    /**
     * 批量删除设备故障记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWyxDeviceFaultByIds(Long[] ids);
}
