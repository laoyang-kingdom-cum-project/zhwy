import { get, post, put } from '@/utils/request.js'

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

// 退出登录
export const logout = () => {
  return post('/logout')
}
