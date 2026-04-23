<template>
  <view class="settings-container">
    <!-- 账号设置 -->
    <view class="settings-group">
      <view class="group-title">账号设置</view>
      <view class="settings-list">
        <view class="settings-item" @click="navigateTo('/pages/user/profile')">
          <text class="item-name">个人信息</text>
          <view class="item-right">
            <text class="item-value">{{ userInfo.nickName || '未设置' }}</text>
            <text class="item-arrow">></text>
          </view>
        </view>
        <view class="settings-item" @click="navigateTo('/pages/user/password')">
          <text class="item-name">修改密码</text>
          <view class="item-right">
            <text class="item-arrow">></text>
          </view>
        </view>
        <view class="settings-item" @click="bindPhone">
          <text class="item-name">绑定手机</text>
          <view class="item-right">
            <text class="item-value">{{ maskPhone(userInfo.phonenumber) }}</text>
            <text class="item-arrow">></text>
          </view>
        </view>
      </view>
    </view>

    <!-- 消息通知 -->
    <view class="settings-group">
      <view class="group-title">消息通知</view>
      <view class="settings-list">
        <view class="settings-item">
          <text class="item-name">接收推送通知</text>
          <switch :checked="settings.pushEnabled" @change="toggleSetting('pushEnabled')" color="#007AFF" />
        </view>
        <view class="settings-item">
          <text class="item-name">预警提醒</text>
          <switch :checked="settings.warningEnabled" @change="toggleSetting('warningEnabled')" color="#007AFF" />
        </view>
        <view class="settings-item">
          <text class="item-name">缴费提醒</text>
          <switch :checked="settings.paymentEnabled" @change="toggleSetting('paymentEnabled')" color="#007AFF" />
        </view>
      </view>
    </view>

    <!-- 通用设置 -->
    <view class="settings-group">
      <view class="group-title">通用设置</view>
      <view class="settings-list">
        <view class="settings-item" @click="clearCache">
          <text class="item-name">清除缓存</text>
          <view class="item-right">
            <text class="item-value">{{ cacheSize }}</text>
            <text class="item-arrow">></text>
          </view>
        </view>
        <view class="settings-item" @click="checkUpdate">
          <text class="item-name">检查更新</text>
          <view class="item-right">
            <text class="item-value">{{ version }}</text>
            <text class="item-arrow">></text>
          </view>
        </view>
        <view class="settings-item">
          <text class="item-name">关于我们</text>
          <view class="item-right">
            <text class="item-arrow">></text>
          </view>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-btn" @click="logout">
      <text>退出登录</text>
    </view>
  </view>
</template>

<script>
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      userInfo: {},
      settings: {
        pushEnabled: true,
        warningEnabled: true,
        paymentEnabled: true
      },
      cacheSize: '12.5MB',
      version: 'v1.0.0'
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadSettings()
  },
  methods: {
    loadUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo) {
        this.userInfo = userInfo
      }
    },
    loadSettings() {
      const settings = uni.getStorageSync('appSettings')
      if (settings) {
        this.settings = { ...this.settings, ...settings }
      }
    },
    maskPhone(phone) {
      if (!phone) return '未绑定'
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    },
    navigateTo(path) {
      uni.navigateTo({ url: path })
    },
    bindPhone() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    toggleSetting(key) {
      this.settings[key] = !this.settings[key]
      uni.setStorageSync('appSettings', this.settings)
      uni.showToast({ title: '设置已保存', icon: 'none' })
    },
    clearCache() {
      uni.showModal({
        title: '提示',
        content: '确定要清除缓存吗？',
        success: (res) => {
          if (res.confirm) {
            this.cacheSize = '0MB'
            uni.showToast({ title: '缓存已清除', icon: 'success' })
          }
        }
      })
    },
    checkUpdate() {
      uni.showLoading({ title: '检查中...' })
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({ title: '已是最新版本', icon: 'none' })
      }, 1000)
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({ url: '/pages/login/index' })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.settings-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.settings-group {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;

  .group-title {
    font-size: 26rpx;
    color: #999;
    padding: 20rpx 30rpx;
    background: #fafafa;
  }
}

.settings-list {
  .settings-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 28rpx 30rpx;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child { border-bottom: none; }

    .item-name {
      font-size: 30rpx;
      color: #333;
    }

    .item-right {
      display: flex;
      align-items: center;

      .item-value {
        font-size: 28rpx;
        color: #999;
        margin-right: 16rpx;
      }

      .item-arrow {
        font-size: 28rpx;
        color: #ccc;
      }
    }
  }
}

.logout-btn {
  margin: 40rpx 30rpx;
  background: #FF6B6B;
  color: #fff;
  text-align: center;
  padding: 28rpx 0;
  border-radius: 12rpx;
  font-size: 32rpx;

  &:active {
    opacity: 0.9;
  }
}
</style>
