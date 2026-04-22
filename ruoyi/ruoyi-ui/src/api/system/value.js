import request from '@/utils/request'

// 查询心率数据列表
export function listValue(query) {
  return request({
    url: '/system/value/list',
    method: 'get',
    params: query
  })
}

// 查询心率数据详细
export function getValue(id) {
  return request({
    url: '/system/value/' + id,
    method: 'get'
  })
}

// 新增心率数据
export function addValue(data) {
  return request({
    url: '/system/value',
    method: 'post',
    data: data
  })
}

// 修改心率数据
export function updateValue(data) {
  return request({
    url: '/system/value',
    method: 'put',
    data: data
  })
}

// 删除心率数据
export function delValue(id) {
  return request({
    url: '/system/value/' + id,
    method: 'delete'
  })
}
