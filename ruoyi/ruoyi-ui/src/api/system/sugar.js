import request from '@/utils/request'

// 查询血糖列表
export function listSugar(query) {
  return request({
    url: '/system/sugar/list',
    method: 'get',
    params: query
  })
}

// 查询血糖详细
export function getSugar(id) {
  return request({
    url: '/system/sugar/' + id,
    method: 'get'
  })
}

// 新增血糖
export function addSugar(data) {
  return request({
    url: '/system/sugar',
    method: 'post',
    data: data
  })
}

// 修改血糖
export function updateSugar(data) {
  return request({
    url: '/system/sugar',
    method: 'put',
    data: data
  })
}

// 删除血糖
export function delSugar(id) {
  return request({
    url: '/system/sugar/' + id,
    method: 'delete'
  })
}
