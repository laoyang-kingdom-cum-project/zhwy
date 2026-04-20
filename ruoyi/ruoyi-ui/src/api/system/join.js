import request from '@/utils/request'

// 查询活动报名列表
export function listJoin(query) {
  return request({
    url: '/system/join/list',
    method: 'get',
    params: query
  })
}

// 查询活动报名详细
export function getJoin(id) {
  return request({
    url: '/system/join/' + id,
    method: 'get'
  })
}

// 新增活动报名
export function addJoin(data) {
  return request({
    url: '/system/join',
    method: 'post',
    data: data
  })
}

// 修改活动报名
export function updateJoin(data) {
  return request({
    url: '/system/join',
    method: 'put',
    data: data
  })
}

// 删除活动报名
export function delJoin(id) {
  return request({
    url: '/system/join/' + id,
    method: 'delete'
  })
}
