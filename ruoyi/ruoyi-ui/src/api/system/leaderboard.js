import request from '@/utils/request'

// 查询打卡排行榜列表
export function listLeaderboard(query) {
  return request({
    url: '/system/leaderboard/list',
    method: 'get',
    params: query
  })
}

// 查询打卡排行榜详细
export function getLeaderboard(id) {
  return request({
    url: '/system/leaderboard/' + id,
    method: 'get'
  })
}

// 新增打卡排行榜
export function addLeaderboard(data) {
  return request({
    url: '/system/leaderboard',
    method: 'post',
    data: data
  })
}

// 修改打卡排行榜
export function updateLeaderboard(data) {
  return request({
    url: '/system/leaderboard',
    method: 'put',
    data: data
  })
}

// 删除打卡排行榜
export function delLeaderboard(id) {
  return request({
    url: '/system/leaderboard/' + id,
    method: 'delete'
  })
}
