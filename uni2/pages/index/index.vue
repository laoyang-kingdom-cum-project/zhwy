<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="header">
      <text class="title">物业管理中心</text>
      <text class="subtitle">智慧社区卫士系统</text>
    </view>
    
    <!-- 数据概览卡片 -->
    <view class="stats-section">
      <view class="stats-card warning" @click="goToWarning">
        <view class="stats-icon">⚠️</view>
        <view class="stats-info">
          <text class="stats-num">{{stats.pendingWarnings}}</text>
          <text class="stats-label">待处理预警</text>
        </view>
      </view>
      <view class="stats-card device" @click="goToDevice">
        <view class="stats-icon">🔧</view>
        <view class="stats-info">
          <text class="stats-num">{{stats.faultDevices}}</text>
          <text class="stats-label">设备故障</text>
        </view>
      </view>
    </view>
    
    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="section-title">功能菜单</view>
      <view class="menu-grid">
        <view class="menu-item" @click="goToWarning" style="background: linear-gradient(135deg, #FF6B6B, #EE5A6F);">
          <text class="menu-icon">🚨</text>
          <text class="menu-text">预警处置</text>
        </view>
        <view class="menu-item" @click="goToDevice" style="background: linear-gradient(135deg, #4ECDC4, #44A08D);">
          <text class="menu-icon">📹</text>
          <text class="menu-text">设备监控</text>
        </view>
        <view class="menu-item" @click="goToFault" style="background: linear-gradient(135deg, #F093FB, #F5576C);">
          <text class="menu-icon">🔨</text>
          <text class="menu-text">故障维修</text>
        </view>
        <view class="menu-item" @click="goToService" style="background: linear-gradient(135deg, #4FACFE, #00F2FE);">
          <text class="menu-icon">📋</text>
          <text class="menu-text">服务订单</text>
        </view>
        <view class="menu-item" @click="goToVisitor" style="background: linear-gradient(135deg, #43E97B, #38F9D7);">
          <text class="menu-icon">📝</text>
          <text class="menu-text">访客审核</text>
        </view>
        <view class="menu-item" @click="goToAI" style="background: linear-gradient(135deg, #FA709A, #FEE140);">
          <text class="menu-icon">🤖</text>
          <text class="menu-text">AI助手</text>
        </view>
      </view>
    </view>
    
    <!-- 最近预警 -->
    <view class="recent-section">
      <view class="section-header">
        <text class="section-title">最近预警</text>
        <text class="more" @click="goToWarning">查看更多 ></text>
      </view>
      <view class="warning-list">
        <view class="warning-item" v-for="(item, index) in recentWarnings" :key="index" @click="goToWarningDetail(item.id)">
          <view class="warning-level" :class="'level-' + item.level">{{item.levelText}}</view>
          <view class="warning-content">
            <text class="warning-title">{{item.title}}</text>
            <text class="warning-location">📍 {{item.location}}</text>
            <text class="warning-time">{{item.time}}</text>
          </view>
          <view class="warning-status" :class="'status-' + item.status">{{item.statusText}}</view>
        </view>
        <view class="empty-tip" v-if="recentWarnings.length === 0">
          <text>暂无预警信息</text>
        </view>
      </view>
    </view>
    
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="0"></custom-tabbar>
  </view>
</template>

<script>
import { getDashboardStats, getRecentWarnings } from '@/api/dashboard.js'
import { getWarningList } from '@/api/warning.js'
import { getFaultList } from '@/api/fault.js'

export default {
  data() {
    return {
      stats: {
        pendingWarnings: 0,
        faultDevices: 0
      },
      recentWarnings: []
    }
  },
  onLoad() {
    this.loadData()
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      await this.loadStats()
      await this.loadRecentWarnings()
    },
    
    async loadStats() {
      try {
        // 获取待处理预警数量
        const warningRes = await getWarningList({ pageNum: 1, pageSize: 1, state: '0' })
        if (warningRes.code === 200) {
          this.stats.pendingWarnings = warningRes.total || 0
        }
        
        // 获取故障设备数量
        const faultRes = await getFaultList({ pageNum: 1, pageSize: 1, handleStatus: '0' })
        if (faultRes.code === 200) {
          this.stats.faultDevices = faultRes.total || 0
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    
    async loadRecentWarnings() {
      try {
        const res = await getWarningList({ pageNum: 1, pageSize: 5 })
        if (res.code === 200 && res.rows) {
          this.recentWarnings = res.rows.map(item => ({
            id: item.id,
            title: item.title,
            location: item.location,
            time: item.time,
            level: item.level,
            levelText: this.getLevelText(item.level),
            status: item.state || '0',
            statusText: this.getStatusText(item.state || '0')
          }))
        }
      } catch (e) {
        console.error('加载最近预警失败', e)
      }
    },
    
    getLevelText(level) {
      const map = { '1': '紧急', '2': '重要', '3': '一般' }
      return map[level] || '一般'
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    goToWarning() {
      uni.reLaunch({
        url: '/pages/warning/index'
      })
    },
    
    goToWarningDetail(id) {
      uni.navigateTo({
        url: `/pages/warning/detail?id=${id}`
      })
    },
    
    goToDevice() {
      uni.reLaunch({
        url: '/pages/device/index'
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
    
    goToAI() {
      uni.navigateTo({
        url: '/pages/ai/index'
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

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx 80rpx;
  text-align: center;
}

.title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.subtitle {
  display: block;
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 10rpx;
}

.stats-section {
  display: flex;
  padding: 0 30rpx;
  margin-top: -40rpx;
}

.stats-card {
  flex: 1;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin: 0 10rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.stats-card.warning {
  border-left: 6rpx solid #FF6B6B;
}

.stats-card.device {
  border-left: 6rpx solid #4ECDC4;
}

.stats-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.stats-info {
  flex: 1;
}

.stats-num {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.stats-label {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-top: 6rpx;
}

.menu-section {
  margin: 30rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.menu-grid {
  display: flex;
  flex-wrap: wrap;
}

.menu-item {
  width: calc(33.33% - 20rpx);
  margin: 10rpx;
  border-radius: 16rpx;
  padding: 30rpx 0;
  text-align: center;
}

.menu-icon {
  display: block;
  font-size: 48rpx;
  margin-bottom: 10rpx;
}

.menu-text {
  display: block;
  font-size: 24rpx;
  color: #fff;
}

.recent-section {
  margin: 30rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.more {
  font-size: 24rpx;
  color: #667eea;
}

.warning-list {
  margin-top: 10rpx;
}

.warning-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.warning-item:last-child {
  border-bottom: none;
}

.warning-level {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  color: #fff;
  margin-right: 20rpx;
}

.level-1 {
  background: #FF6B6B;
}

.level-2 {
  background: #FFA502;
}

.level-3 {
  background: #2ED573;
}

.warning-content {
  flex: 1;
}

.warning-title {
  display: block;
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.warning-location {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-top: 6rpx;
}

.warning-time {
  display: block;
  font-size: 22rpx;
  color: #999;
  margin-top: 4rpx;
}

.warning-status {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
}

.status-0 {
  background: #FFE5E5;
  color: #FF6B6B;
}

.status-1 {
  background: #FFF4E5;
  color: #FFA502;
}

.status-2 {
  background: #E5F5E5;
  color: #2ED573;
}

.empty-tip {
  text-align: center;
  padding: 60rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
