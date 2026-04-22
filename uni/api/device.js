import { get, put } from '@/utils/request.js'

// 获取设备列表
export const getDeviceList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/wyxdevice/list', params)
}

// 控制设备开关
export const controlDevice = (deviceId, data) => {
  return put('/system/wyxdevice', { id: deviceId, ...data })
}
