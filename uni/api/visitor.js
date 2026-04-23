import { get, post, put, del } from '@/utils/request.js'

// 获取访客预约列表
export const getVisitorList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/appointment/list', params)
}

// 获取访客预约详情
export const getVisitorDetail = (id) => {
  return get(`/system/appointment/${id}`)
}

// 创建访客预约
export const createVisitor = (data) => {
  return post('/system/appointment', data)
}

// 更新访客预约（取消预约）
export const updateVisitor = (data) => {
  return put('/system/appointment', data)
}

// 删除访客预约
export const deleteVisitor = (id) => {
  return del(`/system/appointment/${id}`)
}
