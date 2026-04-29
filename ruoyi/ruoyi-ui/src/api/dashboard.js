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

// 获取基本信息饼图数据（接入户数、在线户数）
export function getFamilyStats() {
  return request({
    url: '/system/dashboard/familyStats',
    method: 'get'
  })
}

// 获取实时日志流
export function getRealTimeLogs() {
  return request({
    url: '/system/dashboard/realTimeLogs',
    method: 'get'
  })
}

// 获取告警趋势数据
export function getAlarmTrend() {
  return request({
    url: '/system/dashboard/alarmTrend',
    method: 'get'
  })
}

// 获取传感器状态数据
export function getSensorTrend() {
  return request({
    url: '/system/dashboard/sensorTrend',
    method: 'get'
  })
}

// 获取需关注事件列表
export function getAttentionEvents() {
  return request({
    url: '/system/dashboard/attentionEvents',
    method: 'get'
  })
}

// 获取事件详细信息
export function getEventDetail(id) {
  return request({
    url: '/system/dashboard/eventDetail',
    method: 'get',
    params: { id }
  })
}
