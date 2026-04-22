package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SleepData;

/**
 * 睡眠数据Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
public interface SleepDataMapper 
{
    /**
     * 查询睡眠数据
     * 
     * @param id 睡眠数据主键
     * @return 睡眠数据
     */
    public SleepData selectSleepDataById(Long id);

    /**
     * 查询睡眠数据列表
     * 
     * @param sleepData 睡眠数据
     * @return 睡眠数据集合
     */
    public List<SleepData> selectSleepDataList(SleepData sleepData);

    /**
     * 新增睡眠数据
     * 
     * @param sleepData 睡眠数据
     * @return 结果
     */
    public int insertSleepData(SleepData sleepData);

    /**
     * 修改睡眠数据
     * 
     * @param sleepData 睡眠数据
     * @return 结果
     */
    public int updateSleepData(SleepData sleepData);

    /**
     * 删除睡眠数据
     * 
     * @param id 睡眠数据主键
     * @return 结果
     */
    public int deleteSleepDataById(Long id);

    /**
     * 批量删除睡眠数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSleepDataByIds(Long[] ids);
}
