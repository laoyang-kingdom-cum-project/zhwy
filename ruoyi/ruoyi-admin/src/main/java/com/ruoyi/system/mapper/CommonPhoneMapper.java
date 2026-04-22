package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.CommonPhone;

/**
 * 常用电话Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:27
 */
public interface CommonPhoneMapper 
{
    /**
     * 查询常用电话
     * 
     * @param id 常用电话主键
     * @return 常用电话
     */
    public CommonPhone selectCommonPhoneById(Long id);

    /**
     * 查询常用电话列表
     * 
     * @param commonPhone 常用电话
     * @return 常用电话集合
     */
    public List<CommonPhone> selectCommonPhoneList(CommonPhone commonPhone);

    /**
     * 新增常用电话
     * 
     * @param commonPhone 常用电话
     * @return 结果
     */
    public int insertCommonPhone(CommonPhone commonPhone);

    /**
     * 修改常用电话
     * 
     * @param commonPhone 常用电话
     * @return 结果
     */
    public int updateCommonPhone(CommonPhone commonPhone);

    /**
     * 删除常用电话
     * 
     * @param id 常用电话主键
     * @return 结果
     */
    public int deleteCommonPhoneById(Long id);

    /**
     * 批量删除常用电话
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCommonPhoneByIds(Long[] ids);
}
