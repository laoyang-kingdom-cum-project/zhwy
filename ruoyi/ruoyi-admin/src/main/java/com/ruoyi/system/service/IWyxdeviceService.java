package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Wyxdevice;

/**
 * 设备列表Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16 19:31:38
 */
public interface IWyxdeviceService 
{
    /**
     * 查询设备列表
     * 
     * @param id 设备列表主键
     * @return 设备列表
     */
    public Wyxdevice selectWyxdeviceById(Long id);

    /**
     * 查询设备列表列表
     * 
     * @param wyxdevice 设备列表
     * @return 设备列表集合
     */
    public List<Wyxdevice> selectWyxdeviceList(Wyxdevice wyxdevice);

    /**
     * 新增设备列表
     * 
     * @param wyxdevice 设备列表
     * @return 结果
     */
    public int insertWyxdevice(Wyxdevice wyxdevice);

    /**
     * 修改设备列表
     * 
     * @param wyxdevice 设备列表
     * @return 结果
     */
    public int updateWyxdevice(Wyxdevice wyxdevice);

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的设备列表主键集合
     * @return 结果
     */
    public int deleteWyxdeviceByIds(Long[] ids);

    /**
     * 删除设备列表信息
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    public int deleteWyxdeviceById(Long id);
}
