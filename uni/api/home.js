import { get } from '@/utils/request.js'

// 获取轮播图列表
export const getCarouselList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/carousel/list', params)
}

// 获取公告列表
export const getNoticeList = (params = { pageNum: 1, pageSize: 10 }) => {
  return get('/system/notice/list', params)
}

// 获取公告详情
export const getNoticeDetail = (id) => {
  return get(`/system/notice/${id}`)
}
