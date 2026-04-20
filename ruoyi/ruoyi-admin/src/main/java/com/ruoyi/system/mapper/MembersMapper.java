package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Members;

/**
 * 成员Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface MembersMapper 
{
    /**
     * 查询成员
     * 
     * @param id 成员主键
     * @return 成员
     */
    public Members selectMembersById(Long id);

    /**
     * 查询成员列表
     * 
     * @param members 成员
     * @return 成员集合
     */
    public List<Members> selectMembersList(Members members);

    /**
     * 新增成员
     * 
     * @param members 成员
     * @return 结果
     */
    public int insertMembers(Members members);

    /**
     * 修改成员
     * 
     * @param members 成员
     * @return 结果
     */
    public int updateMembers(Members members);

    /**
     * 删除成员
     * 
     * @param id 成员主键
     * @return 结果
     */
    public int deleteMembersById(Long id);

    /**
     * 批量删除成员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMembersByIds(Long[] ids);
}
