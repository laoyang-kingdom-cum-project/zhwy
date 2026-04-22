import request from '@/utils/request'

// 查询常用电话列表
export function listPhone(query) {
  return request({
    url: '/system/phone/list',
    method: 'get',
    params: query
  })
}

// 查询常用电话详细
export function getPhone(id) {
  return request({
    url: '/system/phone/' + id,
    method: 'get'
  })
}

// 新增常用电话
export function addPhone(data) {
  return request({
    url: '/system/phone',
    method: 'post',
    data: data
  })
}

// 修改常用电话
export function updatePhone(data) {
  return request({
    url: '/system/phone',
    method: 'put',
    data: data
  })
}

// 删除常用电话
export function delPhone(id) {
  return request({
    url: '/system/phone/' + id,
    method: 'delete'
  })
}
