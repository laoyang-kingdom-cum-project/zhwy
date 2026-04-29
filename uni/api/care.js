import { get, post, put, del } from '@/utils/request.js'

export const getCareList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/health/list', params)
}

export const getCareDetail = (id) => {
  return get(`/system/health/${id}`)
}

export const bindFamily = (data) => {
  return post('/system/health', data)
}

export const updateFamily = (data) => {
  return put('/system/health', data)
}

export const unbindFamily = (ids) => {
  return del(`/system/health/${ids}`)
}
