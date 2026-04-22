package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.captchawyx;

/**
 * 验证码池Service接口
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public interface IcaptchawyxService 
{
    /**
     * 查询验证码池
     * 
     * @param id 验证码池主键
     * @return 验证码池
     */
    public captchawyx selectcaptchawyxById(Long id);

    /**
     * 查询验证码池列表
     * 
     * @param captchawyx 验证码池
     * @return 验证码池集合
     */
    public List<captchawyx> selectcaptchawyxList(captchawyx captchawyx);

    /**
     * 新增验证码池
     * 
     * @param captchawyx 验证码池
     * @return 结果
     */
    public int insertcaptchawyx(captchawyx captchawyx);

    /**
     * 修改验证码池
     * 
     * @param captchawyx 验证码池
     * @return 结果
     */
    public int updatecaptchawyx(captchawyx captchawyx);

    /**
     * 批量删除验证码池
     * 
     * @param ids 需要删除的验证码池主键集合
     * @return 结果
     */
    public int deletecaptchawyxByIds(Long[] ids);

    /**
     * 删除验证码池信息
     * 
     * @param id 验证码池主键
     * @return 结果
     */
    public int deletecaptchawyxById(Long id);
}
