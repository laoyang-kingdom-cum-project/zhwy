import { get, post } from '@/utils/request.js'

export const getWarningList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/warning/list', params)
}

export const addWarning = (data) => {
  return post('/system/warning', data)
}
