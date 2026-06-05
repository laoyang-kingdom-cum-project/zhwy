import { post } from '@/utils/request.js'

export const registerPushClientId = (data) => {
  return post('/system/wyxdevice/pushClientId', data)
}
