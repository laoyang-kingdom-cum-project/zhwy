import { get } from '@/utils/request.js'

// 获取关怀对象列表
export const getCareList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/health/list', params)
}

// 获取关怀对象详情
export const getCareDetail = (id) => {
  return get(`/system/health/${id}`)
}
