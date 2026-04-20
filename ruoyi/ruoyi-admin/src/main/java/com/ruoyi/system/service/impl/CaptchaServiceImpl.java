package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CaptchaMapper;
import com.ruoyi.system.domain.Captcha;
import com.ruoyi.system.service.ICaptchaService;

/**
 * 验证码池Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService 
{
    @Autowired
    private CaptchaMapper captchaMapper;

    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    @Override
    public Captcha selectCaptchaById(Long id)
    {
        return captchaMapper.selectCaptchaById(id);
    }

    /**
     * 查询验证码池列表
     * 
     * @param captcha 验证码池
     * @return 验证码池
     */
    @Override
    public List<Captcha> selectCaptchaList(Captcha captcha)
    {
        return captchaMapper.selectCaptchaList(captcha);
    }

    /**
     * 新增验证码池
     * 
     * @param captcha 验证码池
     * @return 结果
     */
    @Override
    public int insertCaptcha(Captcha captcha)
    {
        return captchaMapper.insertCaptcha(captcha);
    }

    /**
     * 修改验证码池
     * 
     * @param captcha 验证码池
     * @return 结果
     */
    @Override
    public int updateCaptcha(Captcha captcha)
    {
        return captchaMapper.updateCaptcha(captcha);
    }

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的验证码池主键
     * @return 结果
     */
    @Override
    public int deleteCaptchaByIds(Long[] ids)
    {
        return captchaMapper.deleteCaptchaByIds(ids);
    }

    /**
     * 删除验证码池信息
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    @Override
    public int deleteCaptchaById(Long id)
    {
        return captchaMapper.deleteCaptchaById(id);
    }
}
