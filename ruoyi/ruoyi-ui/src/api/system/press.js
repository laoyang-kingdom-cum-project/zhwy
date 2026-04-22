import request from '@/utils/request'

// 查询运动团详情列表
export function listPress(query) {
  return request({
    url: '/system/press/list',
    method: 'get',
    params: query
  })
}

// 查询运动团详情详细
export function getPress(id) {
  return request({
    url: '/system/press/' + id,
    method: 'get'
  })
}

// 新增运动团详情
export function addPress(data) {
  return request({
    url: '/system/press',
    method: 'post',
    data: data
  })
}

// 修改运动团详情
export function updatePress(data) {
  return request({
    url: '/system/press',
    method: 'put',
    data: data
  })
}

// 删除运动团详情
export function delPress(id) {
  return request({
    url: '/system/press/' + id,
    method: 'delete'
  })
}
