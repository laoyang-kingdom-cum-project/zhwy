import request from '@/utils/request'

// 查询体重列表
export function listWeight(query) {
  return request({
    url: '/system/weight/list',
    method: 'get',
    params: query
  })
}

// 查询体重详细
export function getWeight(id) {
  return request({
    url: '/system/weight/' + id,
    method: 'get'
  })
}

// 新增体重
export function addWeight(data) {
  return request({
    url: '/system/weight',
    method: 'post',
    data: data
  })
}

// 修改体重
export function updateWeight(data) {
  return request({
    url: '/system/weight',
    method: 'put',
    data: data
  })
}

// 删除体重
export function delWeight(id) {
  return request({
    url: '/system/weight/' + id,
    method: 'delete'
  })
}
