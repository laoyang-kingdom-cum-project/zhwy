import request from '@/utils/request'

// 查询设备列表列表
export function listWyxdevice(query) {
  return request({
    url: '/system/wyxdevice/list',
    method: 'get',
    params: query
  })
}

// 查询设备列表详细
export function getWyxdevice(id) {
  return request({
    url: '/system/wyxdevice/' + id,
    method: 'get'
  })
}

// 新增设备列表
export function addWyxdevice(data) {
  return request({
    url: '/system/wyxdevice',
    method: 'post',
    data: data
  })
}

// 修改设备列表
export function updateWyxdevice(data) {
  return request({
    url: '/system/wyxdevice',
    method: 'put',
    data: data
  })
}

// 删除设备列表
export function delWyxdevice(id) {
  return request({
    url: '/system/wyxdevice/' + id,
    method: 'delete'
  })
}
