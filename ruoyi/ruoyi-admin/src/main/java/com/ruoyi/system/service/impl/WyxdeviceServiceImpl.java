package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WyxdeviceMapper;
import com.ruoyi.system.domain.Wyxdevice;
import com.ruoyi.system.service.IWyxdeviceService;

/**
 * 设备列表Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16 19:31:38
 */
@Service
public class WyxdeviceServiceImpl implements IWyxdeviceService 
{
    @Autowired
    private WyxdeviceMapper wyxdeviceMapper;

    /**
     * 查询设备列表
     * 
     * @param id 设备列表主键
     * @return 设备列表
     */
    @Override
    public Wyxdevice selectWyxdeviceById(Long id)
    {
        return wyxdeviceMapper.selectWyxdeviceById(id);
    }

    /**
     * 查询设备列表列表
     * 
     * @param wyxdevice 设备列表
     * @return 设备列表
     */
    @Override
    public List<Wyxdevice> selectWyxdeviceList(Wyxdevice wyxdevice)
    {
        return wyxdeviceMapper.selectWyxdeviceList(wyxdevice);
    }

    /**
     * 新增设备列表
     * 
     * @param wyxdevice 设备列表
     * @return 结果
     */
    @Override
    public int insertWyxdevice(Wyxdevice wyxdevice)
    {
        return wyxdeviceMapper.insertWyxdevice(wyxdevice);
    }

    /**
     * 修改设备列表
     * 
     * @param wyxdevice 设备列表
     * @return 结果
     */
    @Override
    public int updateWyxdevice(Wyxdevice wyxdevice)
    {
        return wyxdeviceMapper.updateWyxdevice(wyxdevice);
    }

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的设备列表主键
     * @return 结果
     */
    @Override
    public int deleteWyxdeviceByIds(Long[] ids)
    {
        return wyxdeviceMapper.deleteWyxdeviceByIds(ids);
    }

    /**
     * 删除设备列表信息
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    @Override
    public int deleteWyxdeviceById(Long id)
    {
        return wyxdeviceMapper.deleteWyxdeviceById(id);
    }
}
