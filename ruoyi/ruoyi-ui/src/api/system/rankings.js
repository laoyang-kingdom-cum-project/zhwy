import request from '@/utils/request'

// 查询任务排行榜列表
export function listRankings(query) {
  return request({
    url: '/system/rankings/list',
    method: 'get',
    params: query
  })
}

// 查询任务排行榜详细
export function getRankings(id) {
  return request({
    url: '/system/rankings/' + id,
    method: 'get'
  })
}

// 新增任务排行榜
export function addRankings(data) {
  return request({
    url: '/system/rankings',
    method: 'post',
    data: data
  })
}

// 修改任务排行榜
export function updateRankings(data) {
  return request({
    url: '/system/rankings',
    method: 'put',
    data: data
  })
}

// 删除任务排行榜
export function delRankings(id) {
  return request({
    url: '/system/rankings/' + id,
    method: 'delete'
  })
}
