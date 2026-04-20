package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Captcha;

/**
 * 验证码池Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public interface CaptchaMapper 
{
    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    public Captcha selectCaptchaById(Long id);

    /**
     * 查询验证码池列表
     * 
     * @param captcha 验证码池
     * @return 验证码池集合
     */
    public List<Captcha> selectCaptchaList(Captcha captcha);

    /**
     * 新增验证码池
     * 
     * @param captcha 验证码池
     * @return 结果
     */
    public int insertCaptcha(Captcha captcha);

    /**
     * 修改验证码池
     * 
     * @param captcha 验证码池
     * @return 结果
     */
    public int updateCaptcha(Captcha captcha);

    /**
     * 删除验证码池
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    public int deleteCaptchaById(Long id);

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCaptchaByIds(Long[] ids);
}
