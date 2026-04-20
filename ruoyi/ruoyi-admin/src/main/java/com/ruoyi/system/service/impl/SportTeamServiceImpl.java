package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SportTeamMapper;
import com.ruoyi.system.domain.SportTeam;
import com.ruoyi.system.service.ISportTeamService;

/**
 * 运动团队列Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-15 15:26:47
 */
@Service
public class SportTeamServiceImpl implements ISportTeamService 
{
    @Autowired
    private SportTeamMapper sportTeamMapper;

    /**
     * 查询运动团队列
     * 
     * @param id 运动团队列主键
     * @return 运动团队列
     */
    @Override
    public SportTeam selectSportTeamById(Long id)
    {
        return sportTeamMapper.selectSportTeamById(id);
    }

    /**
     * 查询运动团队列列表
     * 
     * @param sportTeam 运动团队列
     * @return 运动团队列
     */
    @Override
    public List<SportTeam> selectSportTeamList(SportTeam sportTeam)
    {
        return sportTeamMapper.selectSportTeamList(sportTeam);
    }

    /**
     * 新增运动团队列
     * 
     * @param sportTeam 运动团队列
     * @return 结果
     */
    @Override
    public int insertSportTeam(SportTeam sportTeam)
    {
        return sportTeamMapper.insertSportTeam(sportTeam);
    }

    /**
     * 修改运动团队列
     * 
     * @param sportTeam 运动团队列
     * @return 结果
     */
    @Override
    public int updateSportTeam(SportTeam sportTeam)
    {
        return sportTeamMapper.updateSportTeam(sportTeam);
    }

    /**
     * 批量删除运动团队列
     * 
     * @param ids 需要删除的运动团队列主键
     * @return 结果
     */
    @Override
    public int deleteSportTeamByIds(Long[] ids)
    {
        return sportTeamMapper.deleteSportTeamByIds(ids);
    }

    /**
     * 删除运动团队列信息
     * 
     * @param id 运动团队列主键
     * @return 结果
     */
    @Override
    public int deleteSportTeamById(Long id)
    {
        return sportTeamMapper.deleteSportTeamById(id);
    }
}
