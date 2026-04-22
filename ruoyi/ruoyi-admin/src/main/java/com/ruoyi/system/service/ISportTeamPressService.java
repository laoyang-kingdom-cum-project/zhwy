package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SportTeamPress;

/**
 * 运动团详情Service接口
 * 
 * @author ruoyi
 * @date 2026-01-05
 */
public interface ISportTeamPressService 
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
     * 批量删除运动团详情
     * 
     * @param ids 需要删除的运动团详情主键集合
     * @return 结果
     */
    public int deleteSportTeamPressByIds(Long[] ids);

    /**
     * 删除运动团详情信息
     * 
     * @param id 运动团详情主键
     * @return 结果
     */
    public int deleteSportTeamPressById(Long id);
}
