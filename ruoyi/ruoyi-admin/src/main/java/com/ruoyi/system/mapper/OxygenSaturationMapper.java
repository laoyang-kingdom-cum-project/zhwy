package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OxygenSaturation;

/**
 * 血氧饱和度Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface OxygenSaturationMapper 
{
    /**
     * 查询血氧饱和度
     * 
     * @param id 血氧饱和度主键
     * @return 血氧饱和度
     */
    public OxygenSaturation selectOxygenSaturationById(Long id);

    /**
     * 查询血氧饱和度列表
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 血氧饱和度集合
     */
    public List<OxygenSaturation> selectOxygenSaturationList(OxygenSaturation oxygenSaturation);

    /**
     * 新增血氧饱和度
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 结果
     */
    public int insertOxygenSaturation(OxygenSaturation oxygenSaturation);

    /**
     * 修改血氧饱和度
     * 
     * @param oxygenSaturation 血氧饱和度
     * @return 结果
     */
    public int updateOxygenSaturation(OxygenSaturation oxygenSaturation);

    /**
     * 删除血氧饱和度
     * 
     * @param id 血氧饱和度主键
     * @return 结果
     */
    public int deleteOxygenSaturationById(Long id);

    /**
     * 批量删除血氧饱和度
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOxygenSaturationByIds(Long[] ids);
}
