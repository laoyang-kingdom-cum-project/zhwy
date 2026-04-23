import request from '@/utils/request'

// 查询设备故障记录列表
export function listFault(query) {
  return request({
    url: '/system/fault/list',
    method: 'get',
    params: query
  })
}

// 查询设备故障记录详细
export function getFault(id) {
  return request({
    url: '/system/fault/' + id,
    method: 'get'
  })
}

// 新增设备故障记录
export function addFault(data) {
  return request({
    url: '/system/fault',
    method: 'post',
    data: data
  })
}

// 修改设备故障记录
export function updateFault(data) {
  return request({
    url: '/system/fault',
    method: 'put',
    data: data
  })
}

// 删除设备故障记录
export function delFault(id) {
  return request({
    url: '/system/fault/' + id,
    method: 'delete'
  })
}
