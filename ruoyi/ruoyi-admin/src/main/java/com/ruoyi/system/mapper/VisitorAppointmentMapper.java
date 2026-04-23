package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.VisitorAppointment;

/**
 * 访客预约Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-23 11:04:09
 */
public interface VisitorAppointmentMapper 
{
    /**
     * 查询访客预约
     * 
     * @param id 访客预约主键
     * @return 访客预约
     */
    public VisitorAppointment selectVisitorAppointmentById(Long id);

    /**
     * 查询访客预约列表
     * 
     * @param visitorAppointment 访客预约
     * @return 访客预约集合
     */
    public List<VisitorAppointment> selectVisitorAppointmentList(VisitorAppointment visitorAppointment);

    /**
     * 新增访客预约
     * 
     * @param visitorAppointment 访客预约
     * @return 结果
     */
    public int insertVisitorAppointment(VisitorAppointment visitorAppointment);

    /**
     * 修改访客预约
     * 
     * @param visitorAppointment 访客预约
     * @return 结果
     */
    public int updateVisitorAppointment(VisitorAppointment visitorAppointment);

    /**
     * 删除访客预约
     * 
     * @param id 访客预约主键
     * @return 结果
     */
    public int deleteVisitorAppointmentById(Long id);

    /**
     * 批量删除访客预约
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitorAppointmentByIds(Long[] ids);
}
