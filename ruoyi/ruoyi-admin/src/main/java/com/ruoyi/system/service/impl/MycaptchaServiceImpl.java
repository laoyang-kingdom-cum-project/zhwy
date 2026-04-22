package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MycaptchaMapper;
import com.ruoyi.system.domain.Mycaptcha;
import com.ruoyi.system.service.IMycaptchaService;

/**
 * 验证码池Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class MycaptchaServiceImpl implements IMycaptchaService 
{
    @Autowired
    private MycaptchaMapper mycaptchaMapper;

    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    @Override
    public Mycaptcha selectMycaptchaById(Long id)
    {
        return mycaptchaMapper.selectMycaptchaById(id);
    }

    /**
     * 查询验证码池列表
     * 
     * @param mycaptcha 验证码池
     * @return 验证码池
     */
    @Override
    public List<Mycaptcha> selectMycaptchaList(Mycaptcha mycaptcha)
    {
        return mycaptchaMapper.selectMycaptchaList(mycaptcha);
    }

    /**
     * 新增验证码池
     * 
     * @param mycaptcha 验证码池
     * @return 结果
     */
    @Override
    public int insertMycaptcha(Mycaptcha mycaptcha)
    {
        return mycaptchaMapper.insertMycaptcha(mycaptcha);
    }

    /**
     * 修改验证码池
     * 
     * @param mycaptcha 验证码池
     * @return 结果
     */
    @Override
    public int updateMycaptcha(Mycaptcha mycaptcha)
    {
        return mycaptchaMapper.updateMycaptcha(mycaptcha);
    }

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的验证码池主键
     * @return 结果
     */
    @Override
    public int deleteMycaptchaByIds(Long[] ids)
    {
        return mycaptchaMapper.deleteMycaptchaByIds(ids);
    }

    /**
     * 删除验证码池信息
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    @Override
    public int deleteMycaptchaById(Long id)
    {
        return mycaptchaMapper.deleteMycaptchaById(id);
    }
}
