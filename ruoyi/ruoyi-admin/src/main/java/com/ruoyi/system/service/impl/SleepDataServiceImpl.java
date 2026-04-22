package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SleepDataMapper;
import com.ruoyi.system.domain.SleepData;
import com.ruoyi.system.service.ISleepDataService;

/**
 * 睡眠数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@Service
public class SleepDataServiceImpl implements ISleepDataService 
{
    @Autowired
    private SleepDataMapper sleepDataMapper;

    /**
     * 查询睡眠数据
     * 
     * @param id 睡眠数据主键
     * @return 睡眠数据
     */
    @Override
    public SleepData selectSleepDataById(Long id)
    {
        return sleepDataMapper.selectSleepDataById(id);
    }

    /**
     * 查询睡眠数据列表
     * 
     * @param sleepData 睡眠数据
     * @return 睡眠数据
     */
    @Override
    public List<SleepData> selectSleepDataList(SleepData sleepData)
    {
        return sleepDataMapper.selectSleepDataList(sleepData);
    }

    /**
     * 新增睡眠数据
     * 
     * @param sleepData 睡眠数据
     * @return 结果
     */
    @Override
    public int insertSleepData(SleepData sleepData)
    {
        return sleepDataMapper.insertSleepData(sleepData);
    }

    /**
     * 修改睡眠数据
     * 
     * @param sleepData 睡眠数据
     * @return 结果
     */
    @Override
    public int updateSleepData(SleepData sleepData)
    {
        return sleepDataMapper.updateSleepData(sleepData);
    }

    /**
     * 批量删除睡眠数据
     * 
     * @param ids 需要删除的睡眠数据主键
     * @return 结果
     */
    @Override
    public int deleteSleepDataByIds(Long[] ids)
    {
        return sleepDataMapper.deleteSleepDataByIds(ids);
    }

    /**
     * 删除睡眠数据信息
     * 
     * @param id 睡眠数据主键
     * @return 结果
     */
    @Override
    public int deleteSleepDataById(Long id)
    {
        return sleepDataMapper.deleteSleepDataById(id);
    }
}
