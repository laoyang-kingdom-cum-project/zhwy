import { get, put } from '@/utils/request.js'

// 获取预警列表
export const getWarningList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/warning/list', params)
}

// 获取预警详情
export const getWarningDetail = (id) => {
  return get(`/system/warning/${id}`)
}

// 处理预警（更新状态）
export const handleWarning = (data) => {
  return put('/system/warning', data)
}
