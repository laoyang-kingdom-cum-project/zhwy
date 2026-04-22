package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Wyxdevice;

/**
 * 设备列表Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-16 19:31:38
 */
public interface WyxdeviceMapper 
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
     * 删除设备列表
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    public int deleteWyxdeviceById(Long id);

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWyxdeviceByIds(Long[] ids);
}
