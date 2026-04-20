import request from '@/utils/request'

// 查询跑步路线列表
export function listRoutes(query) {
  return request({
    url: '/system/routes/list',
    method: 'get',
    params: query
  })
}

// 查询跑步路线详细
export function getRoutes(id) {
  return request({
    url: '/system/routes/' + id,
    method: 'get'
  })
}

// 新增跑步路线
export function addRoutes(data) {
  return request({
    url: '/system/routes',
    method: 'post',
    data: data
  })
}

// 修改跑步路线
export function updateRoutes(data) {
  return request({
    url: '/system/routes',
    method: 'put',
    data: data
  })
}

// 删除跑步路线
export function delRoutes(id) {
  return request({
    url: '/system/routes/' + id,
    method: 'delete'
  })
}
