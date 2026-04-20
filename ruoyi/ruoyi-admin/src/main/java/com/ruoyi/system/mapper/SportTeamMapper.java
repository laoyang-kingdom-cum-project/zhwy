package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SportTeam;

/**
 * 运动团队列Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-15 15:26:47
 */
public interface SportTeamMapper 
{
    /**
     * 查询运动团队列
     * 
     * @param id 运动团队列主键
     * @return 运动团队列
     */
    public SportTeam selectSportTeamById(Long id);

    /**
     * 查询运动团队列列表
     * 
     * @param sportTeam 运动团队列
     * @return 运动团队列集合
     */
    public List<SportTeam> selectSportTeamList(SportTeam sportTeam);

    /**
     * 新增运动团队列
     * 
     * @param sportTeam 运动团队列
     * @return 结果
     */
    public int insertSportTeam(SportTeam sportTeam);

    /**
     * 修改运动团队列
     * 
     * @param sportTeam 运动团队列
     * @return 结果
     */
    public int updateSportTeam(SportTeam sportTeam);

    /**
     * 删除运动团队列
     * 
     * @param id 运动团队列主键
     * @return 结果
     */
    public int deleteSportTeamById(Long id);

    /**
     * 批量删除运动团队列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSportTeamByIds(Long[] ids);
}
