<template>
  <view class="container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <view class="avatar">
          <text>👤</text>
        </view>
        <view class="info">
          <text class="name">{{userInfo.userName || '物业管理员'}}</text>
          <text class="role">{{userInfo.role || '物业管理员'}}</text>
        </view>
      </view>
    </view>
    
    <!-- 数据统计 -->
    <view class="stats-section">
      <view class="stats-item" @click="goToWarning">
        <text class="stats-num">{{stats.warning}}</text>
        <text class="stats-label">待处理预警</text>
      </view>
      <view class="stats-item" @click="goToFault">
        <text class="stats-num">{{stats.fault}}</text>
        <text class="stats-label">待处理故障</text>
      </view>
      <view class="stats-item" @click="goToService">
        <text class="stats-num">{{stats.service}}</text>
        <text class="stats-label">待分配订单</text>
      </view>
      <view class="stats-item" @click="goToVisitor">
        <text class="stats-num">{{stats.visitor}}</text>
        <text class="stats-label">待审核访客</text>
      </view>
    </view>
    
    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" @click="goToProfile">
        <view class="menu-left">
          <text class="menu-icon">👤</text>
          <text class="menu-text">个人信息</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goToPassword">
        <view class="menu-left">
          <text class="menu-icon">🔒</text>
          <text class="menu-text">修改密码</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goToHelp">
        <view class="menu-left">
          <text class="menu-icon">❓</text>
          <text class="menu-text">帮助中心</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goToAbout">
        <view class="menu-left">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-text">关于我们</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>
    
    <!-- 退出登录 -->
    <view class="logout-section">
      <button class="logout-btn" @click="logout">退出登录</button>
    </view>
    
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="3"></custom-tabbar>
  </view>
</template>

<script>
import { getUserInfo, logout } from '@/api/login.js'
import { getWarningList } from '@/api/warning.js'
import { getFaultList } from '@/api/fault.js'
import { getOrderList } from '@/api/service.js'
import { getVisitorList } from '@/api/visitor.js'

export default {
  data() {
    return {
      userInfo: {},
      stats: {
        warning: 0,
        fault: 0,
        service: 0,
        visitor: 0
      }
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadStats()
  },
  onShow() {
    this.loadStats()
  },
  methods: {
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200 && res.data) {
          this.userInfo = res.data
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },
    
    async loadStats() {
      try {
        // 获取待处理预警数量
        const warningRes = await getWarningList({ pageNum: 1, pageSize: 1, status: '0' })
        if (warningRes.code === 200) {
          this.stats.warning = warningRes.total || 0
        }
        
        // 获取待处理故障数量
        const faultRes = await getFaultList({ pageNum: 1, pageSize: 1, handleStatus: '0' })
        if (faultRes.code === 200) {
          this.stats.fault = faultRes.total || 0
        }
        
        // 获取待分配订单数量
        const serviceRes = await getOrderList({ pageNum: 1, pageSize: 1, status: '0' })
        if (serviceRes.code === 200) {
          this.stats.service = serviceRes.total || 0
        }
        
        // 获取待审核访客数量
        const visitorRes = await getVisitorList({ pageNum: 1, pageSize: 1, status: '0' })
        if (visitorRes.code === 200) {
          this.stats.visitor = visitorRes.total || 0
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    
    goToWarning() {
      uni.switchTab({
        url: '/pages/warning/index'
      })
    },
    
    goToFault() {
      uni.navigateTo({
        url: '/pages/fault/index'
      })
    },
    
    goToService() {
      uni.navigateTo({
        url: '/pages/service/index'
      })
    },
    
    goToVisitor() {
      uni.navigateTo({
        url: '/pages/visitor/index'
      })
    },
    
    goToProfile() {
      uni.navigateTo({
        url: '/pages/user/profile'
      })
    },
    
    goToPassword() {
      uni.navigateTo({
        url: '/pages/user/password'
      })
    },
    
    goToHelp() {
      uni.navigateTo({
        url: '/pages/user/help'
      })
    },
    
    goToAbout() {
      uni.navigateTo({
        url: '/pages/user/about'
      })
    },
    
    logout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            logout()
            uni.removeStorageSync('token')
            uni.reLaunch({
              url: '/pages/login/index'
            })
          }
        }
      })
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding-bottom: 120rpx;
}

.user-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.avatar text {
  font-size: 60rpx;
}

.info {
  flex: 1;
}

.name {
  display: block;
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
}

.role {
  display: block;
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 10rpx;
}

.stats-section {
  display: flex;
  background: #fff;
  margin: -40rpx 30rpx 30rpx;
  border-radius: 16rpx;
  padding: 30rpx 0;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.stats-item {
  flex: 1;
  text-align: center;
  border-right: 1rpx solid #f0f0f0;
}

.stats-item:last-child {
  border-right: none;
}

.stats-num {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #667eea;
}

.stats-label {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-top: 8rpx;
}

.menu-section {
  background: #fff;
  margin: 0 30rpx;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f7fa;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-left {
  display: flex;
  align-items: center;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.menu-text {
  font-size: 30rpx;
  color: #333;
}

.arrow {
  font-size: 28rpx;
  color: #999;
}

.logout-section {
  margin: 60rpx 30rpx;
}

.logout-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: #FF6B6B;
  color: #fff;
  font-size: 32rpx;
  border-radius: 45rpx;
  border: none;
}
</style>
