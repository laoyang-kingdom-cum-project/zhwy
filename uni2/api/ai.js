import { get, post } from '@/utils/request.js'

// 发送AI对话消息
export const sendMessage = (data) => {
  return post('/system/ai/chat', data)
}

// 获取应急方案
export const getEmergencyPlan = (type) => {
  return get('/system/emergency/plan', { type })
}

// 获取周边应急资源
export const getEmergencyResources = (params) => {
  return get('/system/emergency/resources', params)
}
