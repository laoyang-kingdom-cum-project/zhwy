import request from '@/utils/request'

// 查询心率数据列表
export function listHrValue(query) {
  return request({
    url: '/system/HrValue/list',
    method: 'get',
    params: query
  })
}

// 查询心率数据详细
export function getHrValue(id) {
  return request({
    url: '/system/HrValue/' + id,
    method: 'get'
  })
}

// 新增心率数据
export function addHrValue(data) {
  return request({
    url: '/system/HrValue',
    method: 'post',
    data: data
  })
}

// 修改心率数据
export function updateHrValue(data) {
  return request({
    url: '/system/HrValue',
    method: 'put',
    data: data
  })
}

// 删除心率数据
export function delHrValue(id) {
  return request({
    url: '/system/HrValue/' + id,
    method: 'delete'
  })
}
