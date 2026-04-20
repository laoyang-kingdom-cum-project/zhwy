import request from '@/utils/request'

// 查询血氧饱和度列表
export function listSaturation(query) {
  return request({
    url: '/system/saturation/list',
    method: 'get',
    params: query
  })
}

// 查询血氧饱和度详细
export function getSaturation(id) {
  return request({
    url: '/system/saturation/' + id,
    method: 'get'
  })
}

// 新增血氧饱和度
export function addSaturation(data) {
  return request({
    url: '/system/saturation',
    method: 'post',
    data: data
  })
}

// 修改血氧饱和度
export function updateSaturation(data) {
  return request({
    url: '/system/saturation',
    method: 'put',
    data: data
  })
}

// 删除血氧饱和度
export function delSaturation(id) {
  return request({
    url: '/system/saturation/' + id,
    method: 'delete'
  })
}
