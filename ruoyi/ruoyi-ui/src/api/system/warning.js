import request from '@/utils/request'

// 查询安全预警列表
export function listWarning(query) {
  return request({
    url: '/system/warning/list',
    method: 'get',
    params: query
  })
}

// 查询安全预警详细
export function getWarning(id) {
  return request({
    url: '/system/warning/' + id,
    method: 'get'
  })
}

// 新增安全预警
export function addWarning(data) {
  return request({
    url: '/system/warning',
    method: 'post',
    data: data
  })
}

// 修改安全预警
export function updateWarning(data) {
  return request({
    url: '/system/warning',
    method: 'put',
    data: data
  })
}

// 删除安全预警
export function delWarning(id) {
  return request({
    url: '/system/warning/' + id,
    method: 'delete'
  })
}
