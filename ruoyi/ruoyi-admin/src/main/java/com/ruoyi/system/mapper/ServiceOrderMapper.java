package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ServiceOrder;

/**
 * 服务订单Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-17 11:36:33
 */
public interface ServiceOrderMapper 
{
    /**
     * 查询服务订单
     * 
     * @param orderId 服务订单主键
     * @return 服务订单
     */
    public ServiceOrder selectServiceOrderByOrderId(String orderId);

    /**
     * 查询服务订单列表
     * 
     * @param serviceOrder 服务订单
     * @return 服务订单集合
     */
    public List<ServiceOrder> selectServiceOrderList(ServiceOrder serviceOrder);

    /**
     * 新增服务订单
     * 
     * @param serviceOrder 服务订单
     * @return 结果
     */
    public int insertServiceOrder(ServiceOrder serviceOrder);

    /**
     * 修改服务订单
     * 
     * @param serviceOrder 服务订单
     * @return 结果
     */
    public int updateServiceOrder(ServiceOrder serviceOrder);

    /**
     * 删除服务订单
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    public int deleteServiceOrderByOrderId(String orderId);

    /**
     * 批量删除服务订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceOrderByOrderIds(String[] orderIds);
}
