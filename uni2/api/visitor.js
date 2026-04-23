import { get, put } from '@/utils/request.js'

// 获取访客预约列表
export const getVisitorList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/appointment/list', params)
}

// 获取访客预约详情
export const getVisitorDetail = (id) => {
  return get(`/system/appointment/${id}`)
}

// 审核访客预约（通过/拒绝）
export const auditVisitor = (data) => {
  return put('/system/appointment', data)
}
