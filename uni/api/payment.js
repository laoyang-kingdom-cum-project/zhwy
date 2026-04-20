import request from '@/utils/request'

// 获取账单列表
export function getBillList(params) {
  return request({
    url: '/system/bill/list',
    method: 'get',
    params
  })
}

// 支付账单 - 使用PUT方法更新账单状态
export function payBill(data) {
  return request({
    url: '/system/bill',
    method: 'put',
    data
  })
}
