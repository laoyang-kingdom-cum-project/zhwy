package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ServiceOrderMapper;
import com.ruoyi.system.domain.ServiceOrder;
import com.ruoyi.system.service.IServiceOrderService;

/**
 * 服务订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:33
 */
@Service
public class ServiceOrderServiceImpl implements IServiceOrderService 
{
    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    /**
     * 查询服务订单
     * 
     * @param orderId 服务订单主键
     * @return 服务订单
     */
    @Override
    public ServiceOrder selectServiceOrderByOrderId(String orderId)
    {
        return serviceOrderMapper.selectServiceOrderByOrderId(orderId);
    }

    /**
     * 查询服务订单列表
     * 
     * @param serviceOrder 服务订单
     * @return 服务订单
     */
    @Override
    public List<ServiceOrder> selectServiceOrderList(ServiceOrder serviceOrder)
    {
        return serviceOrderMapper.selectServiceOrderList(serviceOrder);
    }

    /**
     * 新增服务订单
     * 
     * @param serviceOrder 服务订单
     * @return 结果
     */
    @Override
    public int insertServiceOrder(ServiceOrder serviceOrder)
    {
        serviceOrder.setCreateTime(DateUtils.getNowDate());
        return serviceOrderMapper.insertServiceOrder(serviceOrder);
    }

    /**
     * 修改服务订单
     * 
     * @param serviceOrder 服务订单
     * @return 结果
     */
    @Override
    public int updateServiceOrder(ServiceOrder serviceOrder)
    {
        serviceOrder.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderMapper.updateServiceOrder(serviceOrder);
    }

    /**
     * 批量删除服务订单
     * 
     * @param orderIds 需要删除的服务订单主键
     * @return 结果
     */
    @Override
    public int deleteServiceOrderByOrderIds(String[] orderIds)
    {
        return serviceOrderMapper.deleteServiceOrderByOrderIds(orderIds);
    }

    /**
     * 删除服务订单信息
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    @Override
    public int deleteServiceOrderByOrderId(String orderId)
    {
        return serviceOrderMapper.deleteServiceOrderByOrderId(orderId);
    }
}
