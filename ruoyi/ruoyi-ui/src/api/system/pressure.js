import request from '@/utils/request'

// 查询血压列表
export function listPressure(query) {
  return request({
    url: '/system/pressure/list',
    method: 'get',
    params: query
  })
}

// 查询血压详细
export function getPressure(id) {
  return request({
    url: '/system/pressure/' + id,
    method: 'get'
  })
}

// 新增血压
export function addPressure(data) {
  return request({
    url: '/system/pressure',
    method: 'post',
    data: data
  })
}

// 修改血压
export function updatePressure(data) {
  return request({
    url: '/system/pressure',
    method: 'put',
    data: data
  })
}

// 删除血压
export function delPressure(id) {
  return request({
    url: '/system/pressure/' + id,
    method: 'delete'
  })
}
