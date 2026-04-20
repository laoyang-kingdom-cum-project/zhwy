import request from '@/utils/request'

// 查询成员列表
export function listMembers(query) {
  return request({
    url: '/system/members/list',
    method: 'get',
    params: query
  })
}

// 查询成员详细
export function getMembers(id) {
  return request({
    url: '/system/members/' + id,
    method: 'get'
  })
}

// 新增成员
export function addMembers(data) {
  return request({
    url: '/system/members',
    method: 'post',
    data: data
  })
}

// 修改成员
export function updateMembers(data) {
  return request({
    url: '/system/members',
    method: 'put',
    data: data
  })
}

// 删除成员
export function delMembers(id) {
  return request({
    url: '/system/members/' + id,
    method: 'delete'
  })
}
