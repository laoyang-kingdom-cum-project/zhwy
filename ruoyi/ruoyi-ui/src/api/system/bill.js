import request from '@/utils/request'

// 查询缴费账单列表
export function listBill(query) {
  return request({
    url: '/system/bill/list',
    method: 'get',
    params: query
  })
}

// 查询缴费账单详细
export function getBill(id) {
  return request({
    url: '/system/bill/' + id,
    method: 'get'
  })
}

// 新增缴费账单
export function addBill(data) {
  return request({
    url: '/system/bill',
    method: 'post',
    data: data
  })
}

// 修改缴费账单
export function updateBill(data) {
  return request({
    url: '/system/bill',
    method: 'put',
    data: data
  })
}

// 删除缴费账单
export function delBill(id) {
  return request({
    url: '/system/bill/' + id,
    method: 'delete'
  })
}
