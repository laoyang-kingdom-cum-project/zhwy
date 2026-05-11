<template>
  <view class="profile-container">
    <!-- 头像 -->
    <view class="form-item">
      <text class="label">头像</text>
      <view class="avatar-section" @click="chooseAvatar">
        <image class="avatar" :src="userInfo.avatar ? baseUrl + '/por-api' + userInfo.avatar : '/static/logo.png'" mode="aspectFill"></image>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 昵称 -->
    <view class="form-item">
      <text class="label">昵称</text>
      <input class="input" v-model="form.nickName" placeholder="请输入昵�? />
    </view>

    <!-- 手机�?-->
    <view class="form-item">
      <text class="label">手机�?/text>
      <input class="input" v-model="form.phonenumber" placeholder="请输入手机号" type="number" disabled />
    </view>

    <!-- 邮箱 -->
    <view class="form-item">
      <text class="label">邮箱</text>
      <input class="input" v-model="form.email" placeholder="请输入邮�? />
    </view>

    <!-- 性别 -->
    <view class="form-item">
      <text class="label">性别</text>
      <view class="radio-group">
        <view class="radio-item" @click="form.sex = '0'">
          <view class="radio" :class="{ active: form.sex === '0' }"></view>
          <text>�?/text>
        </view>
        <view class="radio-item" @click="form.sex = '1'">
          <view class="radio" :class="{ active: form.sex === '1' }"></view>
          <text>�?/text>
        </view>
      </view>
    </view>

    <!-- 保存按钮 -->
    <view class="submit-section">
      <button class="submit-btn" @click="saveProfile">保存</button>
    </view>
  </view>
</template>

<script>
import { getUserInfo, updateUserProfile } from '@/api/login.js'
import config from '@/config/index.js'

export default {
  data() {
    return {
      baseUrl: config.baseUrl,
      userInfo: {},
      form: {
        nickName: '',
        phonenumber: '',
        email: '',
        sex: '0'
      }
    }
  },
  onLoad() {
    // 先从本地缓存加载用户信息作为默认�?    this.loadLocalUserInfo()
    // 再从接口获取最新信�?    this.loadUserInfo()
  },
  methods: {
    // 从本地缓存加载用户信�?    loadLocalUserInfo() {
      const userInfo = uni.getStorageSync('userInfo') || {}
      this.userInfo = userInfo
      // 填充表单
      this.form.nickName = userInfo.nickName || userInfo.userName || ''
      this.form.phonenumber = userInfo.phonenumber || ''
      this.form.email = userInfo.email || ''
      this.form.sex = userInfo.sex || '0'
    },

    // 从接口加载用户信�?    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200 && res.user) {
          this.userInfo = res.user
          // 填充表单
          this.form.nickName = res.user.nickName || res.user.userName || ''
          this.form.phonenumber = res.user.phonenumber || ''
          this.form.email = res.user.email || ''
          this.form.sex = res.user.sex || '0'
          // 更新本地缓存
          uni.setStorageSync('userInfo', res.user)
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },

    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          uni.showToast({ title: '头像上传功能需要后端支�?, icon: 'none' })
        }
      })
    },

    // 保存个人信息
    async saveProfile() {
      // 表单验证
      if (!this.form.nickName.trim()) {
        uni.showToast({ title: '请输入昵�?, icon: 'none' })
        return
      }
      if (!this.form.email.trim()) {
        uni.showToast({ title: '请输入邮�?, icon: 'none' })
        return
      }

      uni.showLoading({ title: '保存�?..' })

      try {
        // 调用接口更新用户信息
        const res = await updateUserProfile({
          nickName: this.form.nickName,
          phonenumber: this.form.phonenumber,
          email: this.form.email,
          sex: this.form.sex
        })

        if (res.code === 200) {
          uni.hideLoading()
          uni.showToast({ title: '保存成功', icon: 'success' })

          // 更新本地用户信息
          const userInfo = uni.getStorageSync('userInfo') || {}
          userInfo.nickName = this.form.nickName
          userInfo.email = this.form.email
          userInfo.sex = this.form.sex
          uni.setStorageSync('userInfo', userInfo)

          // 返回上一�?          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.hideLoading()
          uni.showToast({ title: res.msg || '保存失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        uni.showToast({ title: '保存失败', icon: 'none' })
        console.error('保存失败', e)
      }
    }
  }
}
</script>

<style>
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.form-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  border-bottom: 1rpx solid #f0f0f0;
}

.form-item .label {
  font-size: 30rpx;
  color: #333;
  width: 140rpx;
}

.avatar-section {
  display: flex;
  align-items: center;
}

.avatar-section .avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.avatar-section .arrow {
  font-size: 28rpx;
  color: #999;
}

.input {
  flex: 1;
  text-align: right;
  font-size: 30rpx;
  color: #333;
}

.radio-group {
  display: flex;
  gap: 40rpx;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.radio {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  border: 2rpx solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
}

.radio.active {
  border-color: #667eea;
  background: #667eea;
}

.radio.active::after {
  content: '';
  width: 12rpx;
  height: 12rpx;
  background: #fff;
  border-radius: 50%;
}

.submit-section {
  margin: 60rpx 30rpx;
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: #667eea;
  color: #fff;
  font-size: 32rpx;
  border-radius: 45rpx;
  border: none;
}
</style>

