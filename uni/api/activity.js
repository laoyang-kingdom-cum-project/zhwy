import request from '@/utils/request'

// 获取活动列表
export function getActivityList(params) {
  return request({
    url: '/system/activity/list',
    method: 'get',
    params
  })
}

// 获取活动详情
export function getActivityDetail(id) {
  return request({
    url: `/system/activity/${id}`,
    method: 'get'
  })
}

// 获取用户已报名的活动列表
export function getJoinedActivityList(params) {
  return request({
    url: '/system/join/list',
    method: 'get',
    params
  })
}

// 报名活动
export function joinActivity(data) {
  return request({
    url: '/system/join',
    method: 'post',
    data
  })
}
