import request from '@/utils/request'

// 查询家人健康列表
export function listHealth(query) {
  return request({
    url: '/system/health/list',
    method: 'get',
    params: query
  })
}

// 查询家人健康详细
export function getHealth(id) {
  return request({
    url: '/system/health/' + id,
    method: 'get'
  })
}

// 新增家人健康
export function addHealth(data) {
  return request({
    url: '/system/health',
    method: 'post',
    data: data
  })
}

// 修改家人健康
export function updateHealth(data) {
  return request({
    url: '/system/health',
    method: 'put',
    data: data
  })
}

// 删除家人健康
export function delHealth(id) {
  return request({
    url: '/system/health/' + id,
    method: 'delete'
  })
}
