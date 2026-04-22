import request from '@/utils/request'

// 查询运动团队列列表
export function listTeam(query) {
  return request({
    url: '/system/team/list',
    method: 'get',
    params: query
  })
}

// 查询运动团队列详细
export function getTeam(id) {
  return request({
    url: '/system/team/' + id,
    method: 'get'
  })
}

// 新增运动团队列
export function addTeam(data) {
  return request({
    url: '/system/team',
    method: 'post',
    data: data
  })
}

// 修改运动团队列
export function updateTeam(data) {
  return request({
    url: '/system/team',
    method: 'put',
    data: data
  })
}

// 删除运动团队列
export function delTeam(id) {
  return request({
    url: '/system/team/' + id,
    method: 'delete'
  })
}
