import { get, put } from '@/utils/request.js'

// 获取设备列表
export const getDeviceList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/wyxdevice/list', params)
}

// 获取设备详情
export const getDeviceDetail = (deviceId) => {
  return get(`/system/wyxdevice/${deviceId}`)
}

// 控制设备开关
export const controlDevice = (deviceId, data) => {
  return put('/system/wyxdevice', { id: deviceId, ...data })
}
