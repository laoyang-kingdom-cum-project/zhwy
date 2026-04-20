package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SportTeamPress;
import com.ruoyi.system.domain.SportTeam;

/**
 * 运动团详情Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SportTeamPressMapper 
{
    /**
     * 查询运动团详情
     * 
     * @param id 运动团详情主键
     * @return 运动团详情
     */
    public SportTeamPress selectSportTeamPressById(Long id);

    /**
     * 查询运动团详情列表
     * 
     * @param sportTeamPress 运动团详情
     * @return 运动团详情集合
     */
    public List<SportTeamPress> selectSportTeamPressList(SportTeamPress sportTeamPress);

    /**
     * 新增运动团详情
     * 
     * @param sportTeamPress 运动团详情
     * @return 结果
     */
    public int insertSportTeamPress(SportTeamPress sportTeamPress);

    /**
     * 修改运动团详情
     * 
     * @param sportTeamPress 运动团详情
     * @return 结果
     */
    public int updateSportTeamPress(SportTeamPress sportTeamPress);

    /**
     * 删除运动团详情
     * 
     * @param id 运动团详情主键
     * @return 结果
     */
    public int deleteSportTeamPressById(Long id);

    /**
     * 批量删除运动团详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSportTeamPressByIds(Long[] ids);

    /**
     * 批量删除运动团队列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSportTeamByIds(Long[] ids);
    
    /**
     * 批量新增运动团队列
     * 
     * @param sportTeamList 运动团队列列表
     * @return 结果
     */
    public int batchSportTeam(List<SportTeam> sportTeamList);
    

    /**
     * 通过运动团详情主键删除运动团队列信息
     * 
     * @param id 运动团详情ID
     * @return 结果
     */
    public int deleteSportTeamById(Long id);
}
