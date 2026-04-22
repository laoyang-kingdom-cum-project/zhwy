import request from '@/utils/request'

// 查询图片上传列表
export function listUrl(query) {
  return request({
    url: '/system/url/list',
    method: 'get',
    params: query
  })
}

// 查询图片上传详细
export function getUrl(id) {
  return request({
    url: '/system/url/' + id,
    method: 'get'
  })
}

// 新增图片上传
export function addUrl(data) {
  return request({
    url: '/system/url',
    method: 'post',
    data: data
  })
}

// 修改图片上传
export function updateUrl(data) {
  return request({
    url: '/system/url',
    method: 'put',
    data: data
  })
}

// 删除图片上传
export function delUrl(id) {
  return request({
    url: '/system/url/' + id,
    method: 'delete'
  })
}
