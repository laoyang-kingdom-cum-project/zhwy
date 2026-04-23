package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.VisitorAppointmentMapper;
import com.ruoyi.system.domain.VisitorAppointment;
import com.ruoyi.system.service.IVisitorAppointmentService;

/**
 * 访客预约Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-23 11:04:09
 */
@Service
public class VisitorAppointmentServiceImpl implements IVisitorAppointmentService 
{
    @Autowired
    private VisitorAppointmentMapper visitorAppointmentMapper;

    /**
     * 查询访客预约
     * 
     * @param id 访客预约主键
     * @return 访客预约
     */
    @Override
    public VisitorAppointment selectVisitorAppointmentById(Long id)
    {
        return visitorAppointmentMapper.selectVisitorAppointmentById(id);
    }

    /**
     * 查询访客预约列表
     * 
     * @param visitorAppointment 访客预约
     * @return 访客预约
     */
    @Override
    public List<VisitorAppointment> selectVisitorAppointmentList(VisitorAppointment visitorAppointment)
    {
        return visitorAppointmentMapper.selectVisitorAppointmentList(visitorAppointment);
    }

    /**
     * 新增访客预约
     * 
     * @param visitorAppointment 访客预约
     * @return 结果
     */
    @Override
    public int insertVisitorAppointment(VisitorAppointment visitorAppointment)
    {
        visitorAppointment.setCreateTime(DateUtils.getNowDate());
        return visitorAppointmentMapper.insertVisitorAppointment(visitorAppointment);
    }

    /**
     * 修改访客预约
     * 
     * @param visitorAppointment 访客预约
     * @return 结果
     */
    @Override
    public int updateVisitorAppointment(VisitorAppointment visitorAppointment)
    {
        visitorAppointment.setUpdateTime(DateUtils.getNowDate());
        return visitorAppointmentMapper.updateVisitorAppointment(visitorAppointment);
    }

    /**
     * 批量删除访客预约
     * 
     * @param ids 需要删除的访客预约主键
     * @return 结果
     */
    @Override
    public int deleteVisitorAppointmentByIds(Long[] ids)
    {
        return visitorAppointmentMapper.deleteVisitorAppointmentByIds(ids);
    }

    /**
     * 删除访客预约信息
     * 
     * @param id 访客预约主键
     * @return 结果
     */
    @Override
    public int deleteVisitorAppointmentById(Long id)
    {
        return visitorAppointmentMapper.deleteVisitorAppointmentById(id);
    }
}
