import { get, post, put } from '@/utils/request.js'

// 获取服务订单列表
export const getOrderList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/order/list', params)
}

// 获取服务订单详情
export const getOrderDetail = (orderId) => {
  return get(`/system/order/${orderId}`)
}

// 分配订单
export const assignOrder = (data) => {
  return put('/system/order', data)
}

// 更新订单状态
export const updateOrderStatus = (data) => {
  return put('/system/order', data)
}

// 获取常用电话列表
export const getPhoneList = (params = {}) => {
  return get('/system/phone/list', params)
}
