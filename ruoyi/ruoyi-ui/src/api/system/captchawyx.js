import request from '@/utils/request'

// 查询验证码池列表
export function listCaptchawyx(query) {
  return request({
    url: '/system/captchawyx/list',
    method: 'get',
    params: query
  })
}

// 查询验证码池详细
export function getCaptchawyx(id) {
  return request({
    url: '/system/captchawyx/' + id,
    method: 'get'
  })
}

// 新增验证码池
export function addCaptchawyx(data) {
  return request({
    url: '/system/captchawyx',
    method: 'post',
    data: data
  })
}

// 修改验证码池
export function updateCaptchawyx(data) {
  return request({
    url: '/system/captchawyx',
    method: 'put',
    data: data
  })
}

// 删除验证码池
export function delCaptchawyx(id) {
  return request({
    url: '/system/captchawyx/' + id,
    method: 'delete'
  })
}
