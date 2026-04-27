<template>
  <view class="container">
    <!-- 顶部背景+用户信息 -->
    <view class="header">
      <view class="header-bg">
        <view class="header-wave"></view>
      </view>
      <view class="header-content">
        <view class="user-info">
          <view class="avatar-wrap">
            <text class="avatar-icon">👮</text>
          </view>
          <view class="user-text">
            <text class="greeting">{{greetingText}}，管理员</text>
            <text class="date">{{currentDate}}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 数据概览卡片 -->
    <view class="stats-section">
      <view class="stats-card warning" @click="goToWarning">
        <view class="stats-bg-icon">⚠️</view>
        <view class="stats-content">
          <text class="stats-num">{{stats.pendingWarnings}}</text>
          <text class="stats-label">待处理预警</text>
        </view>
        <view class="stats-trend up">
          <text class="trend-text">+3 今日</text>
        </view>
      </view>
      <view class="stats-card device" @click="goToDevice">
        <view class="stats-bg-icon">🔧</view>
        <view class="stats-content">
          <text class="stats-num">{{stats.faultDevices}}</text>
          <text class="stats-label">设备故障</text>
        </view>
        <view class="stats-trend down">
          <text class="trend-text">-1 今日</text>
        </view>
      </view>
    </view>

    <!-- 快捷功能入口 -->
    <view class="quick-section">
      <view class="section-header">
        <text class="section-title">快捷功能</text>
      </view>
      <view class="quick-grid">
        <view class="quick-item" @click="goToWarning">
          <view class="quick-icon warning-bg">
            <text class="icon-text">🚨</text>
          </view>
          <text class="quick-text">预警处置</text>
        </view>
        <view class="quick-item" @click="goToDevice">
          <view class="quick-icon device-bg">
            <text class="icon-text">📹</text>
          </view>
          <text class="quick-text">设备监控</text>
        </view>
        <view class="quick-item" @click="goToFault">
          <view class="quick-icon fault-bg">
            <text class="icon-text">🔨</text>
          </view>
          <text class="quick-text">故障维修</text>
        </view>
        <view class="quick-item" @click="goToService">
          <view class="quick-icon service-bg">
            <text class="icon-text">📋</text>
          </view>
          <text class="quick-text">服务订单</text>
        </view>
        <view class="quick-item" @click="goToVisitor">
          <view class="quick-icon visitor-bg">
            <text class="icon-text">📝</text>
          </view>
          <text class="quick-text">访客审核</text>
        </view>
        <view class="quick-item" @click="goToAI">
          <view class="quick-icon ai-bg">
            <text class="icon-text">🤖</text>
          </view>
          <text class="quick-text">AI助手</text>
        </view>
      </view>
    </view>

    <!-- 最近预警 -->
    <view class="recent-section">
      <view class="section-header">
        <view class="section-title-wrap">
          <view class="title-bar"></view>
          <text class="section-title">最近预警</text>
        </view>
        <text class="more" @click="goToWarning">查看更多</text>
      </view>
      <view class="warning-list">
        <view class="warning-item" v-for="(item, index) in recentWarnings" :key="index" @click="goToWarningDetail(item.id)">
          <view class="warning-left">
            <view class="warning-level" :class="'level-' + item.level">{{item.levelText}}</view>
          </view>
          <view class="warning-body">
            <text class="warning-title">{{item.title}}</text>
            <view class="warning-meta">
              <text class="warning-location">📍 {{item.location}}</text>
              <text class="warning-time">{{item.time}}</text>
            </view>
          </view>
          <view class="warning-right">
            <view class="warning-status" :class="'status-' + item.status">{{item.statusText}}</view>
          </view>
        </view>
        <view class="empty-tip" v-if="recentWarnings.length === 0">
          <view class="empty-icon">📭</view>
          <text class="empty-text">暂无预警信息</text>
        </view>
      </view>
    </view>

    <!-- 底部安全间距 -->
    <view class="safe-bottom"></view>

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
      recentWarnings: [],
      greetingText: '',
      currentDate: ''
    }
  },
  onLoad() {
    this.loadData()
    this.updateDateTime()
  },
  onShow() {
    this.loadData()
    this.updateDateTime()
  },
  methods: {
    async loadData() {
      await this.loadStats()
      await this.loadRecentWarnings()
    },

    async loadStats() {
      try {
        const warningRes = await getWarningList({ pageNum: 1, pageSize: 1, state: '0' })
        if (warningRes.code === 200) {
          this.stats.pendingWarnings = warningRes.total || 0
        }

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
      uni.reLaunch({ url: '/pages/warning/index' })
    },

    goToWarningDetail(id) {
      uni.navigateTo({ url: `/pages/warning/detail?id=${id}` })
    },

    goToDevice() {
      uni.reLaunch({ url: '/pages/device/index' })
    },

    goToFault() {
      uni.navigateTo({ url: '/pages/fault/index' })
    },

    goToService() {
      uni.navigateTo({ url: '/pages/service/index' })
    },

    goToVisitor() {
      uni.navigateTo({ url: '/pages/visitor/index' })
    },

    goToAI() {
      uni.navigateTo({ url: '/pages/ai/index' })
    },

    updateDateTime() {
      const now = new Date()
      const hour = now.getHours()
      if (hour < 6) {
        this.greetingText = '凌晨好'
      } else if (hour < 9) {
        this.greetingText = '早上好'
      } else if (hour < 12) {
        this.greetingText = '上午好'
      } else if (hour < 14) {
        this.greetingText = '中午好'
      } else if (hour < 18) {
        this.greetingText = '下午好'
      } else {
        this.greetingText = '晚上好'
      }

      const year = now.getFullYear()
      const month = now.getMonth() + 1
      const day = now.getDate()
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
      const weekDay = weekDays[now.getDay()]
      this.currentDate = `${year}年${month}月${day}日 ${weekDay}`
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f8f9fb;
  padding-bottom: 120rpx;
}

/* 顶部区域 */
.header {
  position: relative;
  overflow: hidden;
}

.header-bg {
  position: relative;
  height: 280rpx;
  background: linear-gradient(135deg, #1a5fb4 0%, #3584e4 50%, #62a0ea 100%);
  border-radius: 0 0 40rpx 40rpx;
}

.header-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60rpx;
  background: #f8f9fb;
  border-radius: 50% 50% 0 0 / 100% 100% 0 0;
  transform: scaleX(1.5);
}

.header-content {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 60rpx 30rpx 0;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-wrap {
  width: 88rpx;
  height: 88rpx;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10rpx);
  border: 2rpx solid rgba(255,255,255,0.3);
}

.avatar-icon {
  font-size: 44rpx;
}

.user-text {
  margin-left: 20rpx;
}

.greeting {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}

.date {
  display: block;
  font-size: 24rpx;
  color: rgba(255,255,255,0.75);
  margin-top: 6rpx;
}

/* 统计卡片 */
.stats-section {
  display: flex;
  padding: 0 30rpx;
  margin-top: -80rpx;
  position: relative;
  z-index: 1;
}

.stats-card {
  flex: 1;
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin: 0 10rpx;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8rpx 32rpx rgba(26, 95, 180, 0.08);
}

.stats-bg-icon {
  position: absolute;
  right: 10rpx;
  top: 10rpx;
  font-size: 80rpx;
  opacity: 0.06;
}

.stats-content {
  position: relative;
  z-index: 1;
}

.stats-num {
  display: block;
  font-size: 44rpx;
  font-weight: 700;
  color: #1c1c1e;
}

.stats-label {
  display: block;
  font-size: 24rpx;
  color: #8e8e93;
  margin-top: 8rpx;
}

.stats-trend {
  margin-top: 16rpx;
  display: inline-flex;
  align-items: center;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.stats-trend.up {
  background: #fff2f0;
}

.stats-trend.down {
  background: #f0fff4;
}

.trend-text {
  font-size: 20rpx;
  font-weight: 500;
}

.stats-trend.up .trend-text {
  color: #ff4d4f;
}

.stats-trend.down .trend-text {
  color: #52c41a;
}

/* 快捷功能 */
.quick-section {
  margin: 30rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.04);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title-wrap {
  display: flex;
  align-items: center;
}

.title-bar {
  width: 8rpx;
  height: 32rpx;
  background: linear-gradient(180deg, #3584e4, #62a0ea);
  border-radius: 4rpx;
  margin-right: 12rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1c1c1e;
}

.more {
  font-size: 24rpx;
  color: #3584e4;
  font-weight: 500;
}

.quick-grid {
  display: flex;
  flex-wrap: wrap;
}

.quick-item {
  width: 33.33%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 0;
}

.quick-icon {
  width: 96rpx;
  height: 96rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.warning-bg { background: linear-gradient(135deg, #ff6b6b, #ff8e8e); }
.device-bg { background: linear-gradient(135deg, #4ecdc4, #6ee7df); }
.fault-bg { background: linear-gradient(135deg, #f093fb, #f5a3fd); }
.service-bg { background: linear-gradient(135deg, #4facfe, #74c0fc); }
.visitor-bg { background: linear-gradient(135deg, #43e97b, #6ef9a8); }
.ai-bg { background: linear-gradient(135deg, #fa709a, #fb9eb8); }

.icon-text {
  font-size: 44rpx;
}

.quick-text {
  font-size: 26rpx;
  color: #3a3a3c;
  font-weight: 500;
}

/* 最近预警 */
.recent-section {
  margin: 0 30rpx 30rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.04);
}

.warning-list {
  margin-top: 10rpx;
}

.warning-item {
  display: flex;
  align-items: flex-start;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f2f2f7;
}

.warning-item:last-child {
  border-bottom: none;
}

.warning-left {
  margin-right: 20rpx;
  padding-top: 4rpx;
}

.warning-level {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 20rpx;
  font-weight: 600;
  color: #fff;
}

.level-1 { background: linear-gradient(135deg, #ff4d4f, #ff7875); }
.level-2 { background: linear-gradient(135deg, #faad14, #ffc53d); }
.level-3 { background: linear-gradient(135deg, #52c41a, #73d13d); }

.warning-body {
  flex: 1;
  min-width: 0;
}

.warning-title {
  display: block;
  font-size: 28rpx;
  color: #1c1c1e;
  font-weight: 500;
  line-height: 1.4;
}

.warning-meta {
  display: flex;
  align-items: center;
  margin-top: 10rpx;
  flex-wrap: wrap;
}

.warning-location {
  font-size: 22rpx;
  color: #8e8e93;
  margin-right: 16rpx;
}

.warning-time {
  font-size: 22rpx;
  color: #c7c7cc;
}

.warning-right {
  margin-left: 16rpx;
  padding-top: 4rpx;
}

.warning-status {
  padding: 6rpx 14rpx;
  border-radius: 8rpx;
  font-size: 20rpx;
  font-weight: 500;
  white-space: nowrap;
}

.status-0 {
  background: #fff2f0;
  color: #ff4d4f;
}

.status-1 {
  background: #fffbe6;
  color: #faad14;
}

.status-2 {
  background: #f6ffed;
  color: #52c41a;
}

/* 空状态 */
.empty-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
  opacity: 0.5;
}

.empty-text {
  font-size: 28rpx;
  color: #c7c7cc;
}

.safe-bottom {
  height: 40rpx;
}
</style>
