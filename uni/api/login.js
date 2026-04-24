import { get, post, put } from '@/utils/request.js'
import config from '@/config/index.js'

// 登录接口
export const login = (data) => {
  return post('/login', data)
}

// 获取验证码
export const getCaptcha = () => {
  return get('/captchaImage')
}

// 获取用户信息
export const getUserInfo = () => {
  return get('/getInfo')
}

// 更新个人信息
export const updateUserProfile = (data) => {
  return put('/system/user/profile', data)
}

// 修改密码
export const updatePassword = (data) => {
  return put('/system/user/profile/updatePwd', data)
}

// 修改用户头像 - 直接使用 uni.uploadFile 上传文件
export const updateAvatar = (filePath) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')
    uni.uploadFile({
      url: config.baseUrl + config.apiPrefix + '/system/user/profile/avatar',
      filePath: filePath,
      name: 'avatarfile',
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        console.log('头像更新响应:', res)
        if (res.statusCode === 200) {
          try {
            const data = JSON.parse(res.data)
            if (data.code === 200) {
              resolve(data)
            } else {
              uni.showToast({
                title: data.msg || '头像更新失败',
                icon: 'none'
              })
              reject(data)
            }
          } catch (e) {
            console.error('解析响应失败:', res.data)
            uni.showToast({
              title: '服务器响应异常',
              icon: 'none'
            })
            reject(e)
          }
        } else {
          uni.showToast({
            title: '头像更新失败: ' + res.statusCode,
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        console.error('头像更新失败:', err)
        uni.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// 退出登录
export const logout = () => {
  return post('/logout')
}

// 上传图片到若依服务器
export const uploadImage = (filePath) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')
    uni.uploadFile({
      url: config.baseUrl + config.apiPrefix + '/common/upload',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        console.log('上传响应:', res)
        if (res.statusCode === 200) {
          try {
            const data = JSON.parse(res.data)
            if (data.code === 200) {
              resolve(data)
            } else {
              uni.showToast({
                title: data.msg || '上传失败',
                icon: 'none'
              })
              reject(data)
            }
          } catch (e) {
            console.error('解析响应失败:', res.data)
            uni.showToast({
              title: '服务器响应异常',
              icon: 'none'
            })
            reject(e)
          }
        } else {
          uni.showToast({
            title: '上传失败: ' + res.statusCode,
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        console.error('上传失败:', err)
        uni.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}
