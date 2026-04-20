package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.captchawyxMapper;
import com.ruoyi.system.domain.captchawyx;
import com.ruoyi.system.service.IcaptchawyxService;

/**
 * 验证码池Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class captchawyxServiceImpl implements IcaptchawyxService 
{
    @Autowired
    private captchawyxMapper captchawyxMapper;

    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    @Override
    public captchawyx selectcaptchawyxById(Long id)
    {
        return captchawyxMapper.selectcaptchawyxById(id);
    }

    /**
     * 查询验证码池列表
     * 
     * @param captchawyx 验证码池
     * @return 验证码池
     */
    @Override
    public List<captchawyx> selectcaptchawyxList(captchawyx captchawyx)
    {
        return captchawyxMapper.selectcaptchawyxList(captchawyx);
    }

    /**
     * 新增验证码池
     * 
     * @param captchawyx 验证码池
     * @return 结果
     */
    @Override
    public int insertcaptchawyx(captchawyx captchawyx)
    {
        return captchawyxMapper.insertcaptchawyx(captchawyx);
    }

    /**
     * 修改验证码池
     * 
     * @param captchawyx 验证码池
     * @return 结果
     */
    @Override
    public int updatecaptchawyx(captchawyx captchawyx)
    {
        return captchawyxMapper.updatecaptchawyx(captchawyx);
    }

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的验证码池主键
     * @return 结果
     */
    @Override
    public int deletecaptchawyxByIds(Long[] ids)
    {
        return captchawyxMapper.deletecaptchawyxByIds(ids);
    }

    /**
     * 删除验证码池信息
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    @Override
    public int deletecaptchawyxById(Long id)
    {
        return captchawyxMapper.deletecaptchawyxById(id);
    }
}
