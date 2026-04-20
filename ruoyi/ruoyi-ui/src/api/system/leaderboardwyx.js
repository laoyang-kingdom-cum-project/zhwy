import request from '@/utils/request'

// 查询成绩排行榜列表
export function listLeaderboardwyx(query) {
  return request({
    url: '/system/leaderboardwyx/list',
    method: 'get',
    params: query
  })
}

// 查询成绩排行榜详细
export function getLeaderboardwyx(id) {
  return request({
    url: '/system/leaderboardwyx/' + id,
    method: 'get'
  })
}

// 新增成绩排行榜
export function addLeaderboardwyx(data) {
  return request({
    url: '/system/leaderboardwyx',
    method: 'post',
    data: data
  })
}

// 修改成绩排行榜
export function updateLeaderboardwyx(data) {
  return request({
    url: '/system/leaderboardwyx',
    method: 'put',
    data: data
  })
}

// 删除成绩排行榜
export function delLeaderboardwyx(id) {
  return request({
    url: '/system/leaderboardwyx/' + id,
    method: 'delete'
  })
}
