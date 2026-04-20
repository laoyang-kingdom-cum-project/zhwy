package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MembersMapper;
import com.ruoyi.system.domain.Members;
import com.ruoyi.system.service.IMembersService;

/**
 * 成员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class MembersServiceImpl implements IMembersService 
{
    @Autowired
    private MembersMapper membersMapper;

    /**
     * 查询成员
     * 
     * @param id 成员主键
     * @return 成员
     */
    @Override
    public Members selectMembersById(Long id)
    {
        return membersMapper.selectMembersById(id);
    }

    /**
     * 查询成员列表
     * 
     * @param members 成员
     * @return 成员
     */
    @Override
    public List<Members> selectMembersList(Members members)
    {
        return membersMapper.selectMembersList(members);
    }

    /**
     * 新增成员
     * 
     * @param members 成员
     * @return 结果
     */
    @Override
    public int insertMembers(Members members)
    {
        return membersMapper.insertMembers(members);
    }

    /**
     * 修改成员
     * 
     * @param members 成员
     * @return 结果
     */
    @Override
    public int updateMembers(Members members)
    {
        return membersMapper.updateMembers(members);
    }

    /**
     * 批量删除成员
     * 
     * @param ids 需要删除的成员主键
     * @return 结果
     */
    @Override
    public int deleteMembersByIds(Long[] ids)
    {
        return membersMapper.deleteMembersByIds(ids);
    }

    /**
     * 删除成员信息
     * 
     * @param id 成员主键
     * @return 结果
     */
    @Override
    public int deleteMembersById(Long id)
    {
        return membersMapper.deleteMembersById(id);
    }
}
