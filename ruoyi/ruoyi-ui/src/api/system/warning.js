import request from '@/utils/request'

// 查询预警处理列表
export function listWarning(query) {
  return request({
    url: '/system/warning/list',
    method: 'get',
    params: query
  })
}

// 查询预警处理详细
export function getWarning(id) {
  return request({
    url: '/system/warning/' + id,
    method: 'get'
  })
}

// 新增预警处理
export function addWarning(data) {
  return request({
    url: '/system/warning',
    method: 'post',
    data: data
  })
}

// 修改预警处理
export function updateWarning(data) {
  return request({
    url: '/system/warning',
    method: 'put',
    data: data
  })
}

// 删除预警处理
export function delWarning(id) {
  return request({
    url: '/system/warning/' + id,
    method: 'delete'
  })
}
