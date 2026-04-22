import request from '@/utils/request'

// 查询验证码池列表
export function listCaptcha(query) {
  return request({
    url: '/system/captcha/list',
    method: 'get',
    params: query
  })
}

// 查询验证码池详细
export function getCaptcha(id) {
  return request({
    url: '/system/captcha/' + id,
    method: 'get'
  })
}

// 新增验证码池
export function addCaptcha(data) {
  return request({
    url: '/system/captcha',
    method: 'post',
    data: data
  })
}

// 修改验证码池
export function updateCaptcha(data) {
  return request({
    url: '/system/captcha',
    method: 'put',
    data: data
  })
}

// 删除验证码池
export function delCaptcha(id) {
  return request({
    url: '/system/captcha/' + id,
    method: 'delete'
  })
}
