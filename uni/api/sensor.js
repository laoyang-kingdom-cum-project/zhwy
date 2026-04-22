import { get } from '@/utils/request.js'

// 获取传感器数据
export const getSensorData = (params = {}) => {
  return get('/system/sensor/data', params)
}

// 获取环境数据历史记录
export const getSensorHistory = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/sensor/history', params)
}
