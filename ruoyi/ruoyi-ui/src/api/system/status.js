import request from '@/utils/request'

// 查询路线收藏状态列表
export function listStatus(query) {
  return request({
    url: '/system/status/list',
    method: 'get',
    params: query
  })
}

// 查询路线收藏状态详细
export function getStatus(id) {
  return request({
    url: '/system/status/' + id,
    method: 'get'
  })
}

// 新增路线收藏状态
export function addStatus(data) {
  return request({
    url: '/system/status',
    method: 'post',
    data: data
  })
}

// 修改路线收藏状态
export function updateStatus(data) {
  return request({
    url: '/system/status',
    method: 'put',
    data: data
  })
}

// 删除路线收藏状态
export function delStatus(id) {
  return request({
    url: '/system/status/' + id,
    method: 'delete'
  })
}
