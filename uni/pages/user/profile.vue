<template>
  <view class="profile-container">
    <!-- 头像 -->
    <view class="form-item">
      <text class="label">头像</text>
      <view class="avatar-section" @click="chooseAvatar">
        <image class="avatar" :src="userInfo.avatar || '/static/logo.png'" mode="aspectFill"></image>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 昵称 -->
    <view class="form-item">
      <text class="label">昵称</text>
      <input class="input" v-model="form.nickName" placeholder="请输入昵称" />
    </view>

    <!-- 手机号 -->
    <view class="form-item">
      <text class="label">手机号</text>
      <input class="input" v-model="form.phonenumber" placeholder="请输入手机号" type="number" disabled />
    </view>

    <!-- 邮箱 -->
    <view class="form-item">
      <text class="label">邮箱</text>
      <input class="input" v-model="form.email" placeholder="请输入邮箱" />
    </view>

    <!-- 性别 -->
    <view class="form-item">
      <text class="label">性别</text>
      <view class="radio-group">
        <view class="radio-item" @click="form.sex = '0'">
          <view class="radio" :class="{ active: form.sex === '0' }"></view>
          <text>男</text>
        </view>
        <view class="radio-item" @click="form.sex = '1'">
          <view class="radio" :class="{ active: form.sex === '1' }"></view>
          <text>女</text>
        </view>
      </view>
    </view>

    <!-- 住址 -->
    <view class="form-item">
      <text class="label">住址</text>
      <input class="input" v-model="form.address" placeholder="请输入住址" />
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
      userInfo: {},
      form: {
        nickName: '',
        phonenumber: '',
        email: '',
        sex: '0',
        address: ''
      }
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadAddress()
  },
  methods: {
    // 加载用户信息
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200 && res.user) {
          this.userInfo = {
            ...res.user,
            avatar: res.user.avatar ? config.baseUrl + '/por-api' + res.user.avatar : '/static/logo.png'
          }
          // 填充表单
          this.form.nickName = res.user.nickName || ''
          this.form.phonenumber = res.user.phonenumber || ''
          this.form.email = res.user.email || ''
          this.form.sex = res.user.sex || '0'
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },
    // 加载本地保存的住址
    loadAddress() {
      const address = uni.getStorageSync('userAddress')
      if (address) {
        this.form.address = address
      }
    },
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          uni.showToast({ title: '头像已更新', icon: 'success' })
        }
      })
    },
    // 保存个人信息
    async saveProfile() {
      // 表单验证
      if (!this.form.nickName.trim()) {
        uni.showToast({ title: '请输入昵称', icon: 'none' })
        return
      }
      if (!this.form.email.trim()) {
        uni.showToast({ title: '请输入邮箱', icon: 'none' })
        return
      }

      uni.showLoading({ title: '保存中...' })

      try {
        // 调用接口更新用户信息
        const res = await updateUserProfile({
          nickName: this.form.nickName,
          phonenumber: this.form.phonenumber,
          email: this.form.email,
          sex: this.form.sex
        })

        if (res.code === 200) {
          // 保存住址到本地
          uni.setStorageSync('userAddress', this.form.address)

          uni.hideLoading()
          uni.showToast({ title: '保存成功', icon: 'success' })

          // 更新本地用户信息
          const userInfo = uni.getStorageSync('userInfo') || {}
          userInfo.nickName = this.form.nickName
          userInfo.email = this.form.email
          userInfo.sex = this.form.sex
          uni.setStorageSync('userInfo', userInfo)

          // 返回上一页
          setTimeout(() => {
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

<style lang="scss">
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

  .label {
    font-size: 30rpx;
    color: #333;
    width: 140rpx;
  }

  .avatar-section {
    display: flex;
    align-items: center;

    .avatar {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      margin-right: 20rpx;
    }

    .arrow {
      font-size: 28rpx;
      color: #ccc;
    }
  }

  .input {
    flex: 1;
    text-align: right;
    font-size: 30rpx;
    color: #333;

    &[disabled] {
      color: #999;
    }
  }

  .radio-group {
    flex: 1;
    display: flex;
    justify-content: flex-end;
    gap: 40rpx;

    .radio-item {
      display: flex;
      align-items: center;
      font-size: 30rpx;
      color: #333;

      .radio {
        width: 32rpx;
        height: 32rpx;
        border-radius: 50%;
        border: 2rpx solid #ccc;
        margin-right: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;

        &.active {
          border-color: #007AFF;
          background: #007AFF;

          &::after {
            content: '';
            width: 12rpx;
            height: 12rpx;
            background: #fff;
            border-radius: 50%;
          }
        }
      }
    }
  }
}

.submit-section {
  padding: 60rpx 30rpx;

  .submit-btn {
    width: 100%;
    height: 90rpx;
    line-height: 90rpx;
    background: #007AFF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 12rpx;
    border: none;

    &:active {
      opacity: 0.8;
    }
  }
}
</style>
