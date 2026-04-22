import request from '@/utils/request'

// 获取大屏统计数据
export function getDashboardStats() {
  return request({
    url: '/system/dashboard/stats',
    method: 'get'
  })
}

// 获取设备类型分布
export function getDeviceTypeStats() {
  return request({
    url: '/system/dashboard/deviceTypeStats',
    method: 'get'
  })
}

// 获取预警趋势
export function getWarningTrend() {
  return request({
    url: '/system/dashboard/warningTrend',
    method: 'get'
  })
}

// 获取最近预警
export function getRecentWarnings() {
  return request({
    url: '/system/dashboard/recentWarnings',
    method: 'get'
  })
}

// 获取房间设备分布
export function getRoomDeviceStats() {
  return request({
    url: '/system/dashboard/roomDeviceStats',
    method: 'get'
  })
}
