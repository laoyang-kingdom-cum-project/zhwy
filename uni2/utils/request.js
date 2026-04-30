import config from '@/config/index.js'

// 请求拦截
const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')
    
    uni.request({
      url: config.baseUrl + config.apiPrefix + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : '',
        ...options.header
      },
      timeout: config.timeout,
      success: (res) => {
        // 请求成功
        if (res.statusCode === 200) {
          const data = res.data
          // RuoYi返回码处理
          if (data.code === 200) {
            resolve(data)
          } else if (data.code === 401) {
            // 未登录或token过期
            uni.removeStorageSync('token')
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            })
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/login/index'
              })
            }, 1500)
            reject(data)
          } else {
            uni.showToast({
              title: data.msg || '请求失败',
              icon: 'none'
            })
            reject(data)
          }
        } else {
          uni.showToast({
            title: '网络错误: ' + res.statusCode,
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// GET请求
export const get = (url, params = {}) => {
  return request({
    url,
    method: 'GET',
    data: params
  })
}

// POST请求
export const post = (url, data = {}) => {
  return request({
    url,
    method: 'POST',
    data
  })
}

// PUT请求
export const put = (url, data = {}) => {
  return request({
    url,
    method: 'PUT',
    data
  })
}

// DELETE请求
export const del = (url, params = {}) => {
  return request({
    url,
    method: 'DELETE',
    data: params
  })
}

export default request
