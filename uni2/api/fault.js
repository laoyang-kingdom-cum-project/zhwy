import { get, put } from '@/utils/request.js'

// 获取故障列表
export const getFaultList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/fault/list', params)
}

// 获取故障详情
export const getFaultDetail = (id) => {
  return get(`/system/fault/${id}`)
}

// 处理故障
export const handleFault = (data) => {
  return put('/system/fault', data)
}
