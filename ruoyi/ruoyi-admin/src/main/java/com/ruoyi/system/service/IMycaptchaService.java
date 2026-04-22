package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Mycaptcha;

/**
 * 验证码池Service接口
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public interface IMycaptchaService 
{
    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    public Mycaptcha selectMycaptchaById(Long id);

    /**
     * 查询验证码池列表
     * 
     * @param mycaptcha 验证码池
     * @return 验证码池集合
     */
    public List<Mycaptcha> selectMycaptchaList(Mycaptcha mycaptcha);

    /**
     * 新增验证码池
     * 
     * @param mycaptcha 验证码池
     * @return 结果
     */
    public int insertMycaptcha(Mycaptcha mycaptcha);

    /**
     * 修改验证码池
     * 
     * @param mycaptcha 验证码池
     * @return 结果
     */
    public int updateMycaptcha(Mycaptcha mycaptcha);

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的验证码池主键集合
     * @return 结果
     */
    public int deleteMycaptchaByIds(Long[] ids);

    /**
     * 删除验证码池信息
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    public int deleteMycaptchaById(Long id);
}
