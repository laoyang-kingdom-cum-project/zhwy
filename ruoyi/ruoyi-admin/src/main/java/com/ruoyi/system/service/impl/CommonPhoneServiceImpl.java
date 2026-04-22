package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CommonPhoneMapper;
import com.ruoyi.system.domain.CommonPhone;
import com.ruoyi.system.service.ICommonPhoneService;

/**
 * 常用电话Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:27
 */
@Service
public class CommonPhoneServiceImpl implements ICommonPhoneService 
{
    @Autowired
    private CommonPhoneMapper commonPhoneMapper;

    /**
     * 查询常用电话
     * 
     * @param id 常用电话主键
     * @return 常用电话
     */
    @Override
    public CommonPhone selectCommonPhoneById(Long id)
    {
        return commonPhoneMapper.selectCommonPhoneById(id);
    }

    /**
     * 查询常用电话列表
     * 
     * @param commonPhone 常用电话
     * @return 常用电话
     */
    @Override
    public List<CommonPhone> selectCommonPhoneList(CommonPhone commonPhone)
    {
        return commonPhoneMapper.selectCommonPhoneList(commonPhone);
    }

    /**
     * 新增常用电话
     * 
     * @param commonPhone 常用电话
     * @return 结果
     */
    @Override
    public int insertCommonPhone(CommonPhone commonPhone)
    {
        commonPhone.setCreateTime(DateUtils.getNowDate());
        return commonPhoneMapper.insertCommonPhone(commonPhone);
    }

    /**
     * 修改常用电话
     * 
     * @param commonPhone 常用电话
     * @return 结果
     */
    @Override
    public int updateCommonPhone(CommonPhone commonPhone)
    {
        commonPhone.setUpdateTime(DateUtils.getNowDate());
        return commonPhoneMapper.updateCommonPhone(commonPhone);
    }

    /**
     * 批量删除常用电话
     * 
     * @param ids 需要删除的常用电话主键
     * @return 结果
     */
    @Override
    public int deleteCommonPhoneByIds(Long[] ids)
    {
        return commonPhoneMapper.deleteCommonPhoneByIds(ids);
    }

    /**
     * 删除常用电话信息
     * 
     * @param id 常用电话主键
     * @return 结果
     */
    @Override
    public int deleteCommonPhoneById(Long id)
    {
        return commonPhoneMapper.deleteCommonPhoneById(id);
    }
}
