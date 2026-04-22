import { get } from '@/utils/request.js'

// 获取预警列表
export const getWarningList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/warning/list', params)
}
