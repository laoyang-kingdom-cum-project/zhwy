package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.SportTeam;
import com.ruoyi.system.mapper.SportTeamPressMapper;
import com.ruoyi.system.domain.SportTeamPress;
import com.ruoyi.system.service.ISportTeamPressService;

/**
 * 运动团详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SportTeamPressServiceImpl implements ISportTeamPressService 
{
    @Autowired
    private SportTeamPressMapper sportTeamPressMapper;

    /**
     * 查询运动团详情
     * 
     * @param id 运动团详情主键
     * @return 运动团详情
     */
    @Override
    public SportTeamPress selectSportTeamPressById(Long id)
    {
        return sportTeamPressMapper.selectSportTeamPressById(id);
    }

    /**
     * 查询运动团详情列表
     * 
     * @param sportTeamPress 运动团详情
     * @return 运动团详情
     */
    @Override
    public List<SportTeamPress> selectSportTeamPressList(SportTeamPress sportTeamPress)
    {
        return sportTeamPressMapper.selectSportTeamPressList(sportTeamPress);
    }

    /**
     * 新增运动团详情
     * 
     * @param sportTeamPress 运动团详情
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSportTeamPress(SportTeamPress sportTeamPress)
    {
        int rows = sportTeamPressMapper.insertSportTeamPress(sportTeamPress);
        insertSportTeam(sportTeamPress);
        return rows;
    }

    /**
     * 修改运动团详情
     * 
     * @param sportTeamPress 运动团详情
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSportTeamPress(SportTeamPress sportTeamPress)
    {
        sportTeamPressMapper.deleteSportTeamById(sportTeamPress.getId());
        insertSportTeam(sportTeamPress);
        return sportTeamPressMapper.updateSportTeamPress(sportTeamPress);
    }

    /**
     * 批量删除运动团详情
     * 
     * @param ids 需要删除的运动团详情主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSportTeamPressByIds(Long[] ids)
    {
        sportTeamPressMapper.deleteSportTeamByIds(ids);
        return sportTeamPressMapper.deleteSportTeamPressByIds(ids);
    }

    /**
     * 删除运动团详情信息
     * 
     * @param id 运动团详情主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSportTeamPressById(Long id)
    {
        sportTeamPressMapper.deleteSportTeamById(id);
        return sportTeamPressMapper.deleteSportTeamPressById(id);
    }

    /**
     * 新增运动团队列信息
     * 
     * @param sportTeamPress 运动团详情对象
     */
    public void insertSportTeam(SportTeamPress sportTeamPress)
    {
        List<SportTeam> sportTeamList = sportTeamPress.getSportTeamList();
        Long id = sportTeamPress.getId();
        if (StringUtils.isNotNull(sportTeamList))
        {
            List<SportTeam> list = new ArrayList<SportTeam>();
            for (SportTeam sportTeam : sportTeamList)
            {
                sportTeam.setId(id);
                list.add(sportTeam);
            }
            if (list.size() > 0)
            {
                sportTeamPressMapper.batchSportTeam(list);
            }
        }
    }
}
