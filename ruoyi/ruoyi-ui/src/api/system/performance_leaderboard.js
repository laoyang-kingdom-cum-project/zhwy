import request from '@/utils/request'

// 查询成绩排行榜列表
export function listPerformance_leaderboard(query) {
  return request({
    url: '/system/performance_leaderboard/list',
    method: 'get',
    params: query
  })
}

// 查询成绩排行榜详细
export function getPerformance_leaderboard(id) {
  return request({
    url: '/system/performance_leaderboard/' + id,
    method: 'get'
  })
}

// 新增成绩排行榜
export function addPerformance_leaderboard(data) {
  return request({
    url: '/system/performance_leaderboard',
    method: 'post',
    data: data
  })
}

// 修改成绩排行榜
export function updatePerformance_leaderboard(data) {
  return request({
    url: '/system/performance_leaderboard',
    method: 'put',
    data: data
  })
}

// 删除成绩排行榜
export function delPerformance_leaderboard(id) {
  return request({
    url: '/system/performance_leaderboard/' + id,
    method: 'delete'
  })
}
