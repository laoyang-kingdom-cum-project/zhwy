import request from '@/utils/request'

// 查询课程目录列表
export function listCatalog(query) {
  return request({
    url: '/system/catalog/list',
    method: 'get',
    params: query
  })
}

// 查询课程目录详细
export function getCatalog(id) {
  return request({
    url: '/system/catalog/' + id,
    method: 'get'
  })
}

// 新增课程目录
export function addCatalog(data) {
  return request({
    url: '/system/catalog',
    method: 'post',
    data: data
  })
}

// 修改课程目录
export function updateCatalog(data) {
  return request({
    url: '/system/catalog',
    method: 'put',
    data: data
  })
}

// 删除课程目录
export function delCatalog(id) {
  return request({
    url: '/system/catalog/' + id,
    method: 'delete'
  })
}
