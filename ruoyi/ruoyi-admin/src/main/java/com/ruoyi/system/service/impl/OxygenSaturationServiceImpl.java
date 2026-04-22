package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OxygenSaturationMapper;
import com.ruoyi.system.domain.OxygenSaturation;
import com.ruoyi.system.service.IOxygenSaturationService;

/**
 * 血氧饱和度Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class OxygenSaturationServiceImpl implements IOxygenSaturationService 
{
    @Autowired
    private OxygenSaturationMapper oxygenSaturationMapper;

    /**
     * 查询血氧饱和度
     * 
     * @param id 血氧饱和度主键
     * @return 血氧饱和度
     */
    @Override
    public OxygenSaturation selectOxygenSaturationById(Long id)
    {
        return oxygenSaturationMapper.selectOxygenSaturationById(id);
    }

    /**
     * 查询血氧饱和度列表
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 血氧饱和度
     */
    @Override
    public List<OxygenSaturation> selectOxygenSaturationList(OxygenSaturation oxygenSaturation)
    {
        return oxygenSaturationMapper.selectOxygenSaturationList(oxygenSaturation);
    }

    /**
     * 新增血氧饱和度
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 结果
     */
    @Override
    public int insertOxygenSaturation(OxygenSaturation oxygenSaturation)
    {
        return oxygenSaturationMapper.insertOxygenSaturation(oxygenSaturation);
    }

    /**
     * 修改血氧饱和度
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 结果
     */
    @Override
    public int updateOxygenSaturation(OxygenSaturation oxygenSaturation)
    {
        return oxygenSaturationMapper.updateOxygenSaturation(oxygenSaturation);
    }

    /**
     * 批量删除血氧饱和度
     * 
     * @param ids 需要删除的血氧饱和度主键
     * @return 结果
     */
    @Override
    public int deleteOxygenSaturationByIds(Long[] ids)
    {
        return oxygenSaturationMapper.deleteOxygenSaturationByIds(ids);
    }

    /**
     * 删除血氧饱和度信息
     * 
     * @param id 血氧饱和度主键
     * @return 结果
     */
    @Override
    public int deleteOxygenSaturationById(Long id)
    {
        return oxygenSaturationMapper.deleteOxygenSaturationById(id);
    }
}
