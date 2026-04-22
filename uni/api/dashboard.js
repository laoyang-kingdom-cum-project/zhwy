import { get } from '@/utils/request.js'

// 获取大屏统计数据
export const getDashboardStats = () => {
  return get('/system/dashboard/stats')
}

// 获取设备类型分布
export const getDeviceTypeStats = () => {
  return get('/system/dashboard/deviceTypeStats')
}

// 获取预警趋势
export const getWarningTrend = () => {
  return get('/system/dashboard/warningTrend')
}

// 获取最近预警列表
export const getRecentWarnings = () => {
  return get('/system/dashboard/recentWarnings')
}

// 获取房间设备分布
export const getRoomDeviceStats = () => {
  return get('/system/dashboard/roomDeviceStats')
}
