import request from '@/utils/request'

// 查询压力列表
export function listStress(query) {
  return request({
    url: '/system/stress/list',
    method: 'get',
    params: query
  })
}

// 查询压力详细
export function getStress(id) {
  return request({
    url: '/system/stress/' + id,
    method: 'get'
  })
}

// 新增压力
export function addStress(data) {
  return request({
    url: '/system/stress',
    method: 'post',
    data: data
  })
}

// 修改压力
export function updateStress(data) {
  return request({
    url: '/system/stress',
    method: 'put',
    data: data
  })
}

// 删除压力
export function delStress(id) {
  return request({
    url: '/system/stress/' + id,
    method: 'delete'
  })
}
