import request from '@/utils/request'

// 查询验证码池列表
export function listMycaptcha(query) {
  return request({
    url: '/system/mycaptcha/list',
    method: 'get',
    params: query
  })
}

// 查询验证码池详细
export function getMycaptcha(id) {
  return request({
    url: '/system/mycaptcha/' + id,
    method: 'get'
  })
}

// 新增验证码池
export function addMycaptcha(data) {
  return request({
    url: '/system/mycaptcha',
    method: 'post',
    data: data
  })
}

// 修改验证码池
export function updateMycaptcha(data) {
  return request({
    url: '/system/mycaptcha',
    method: 'put',
    data: data
  })
}

// 删除验证码池
export function delMycaptcha(id) {
  return request({
    url: '/system/mycaptcha/' + id,
    method: 'delete'
  })
}
