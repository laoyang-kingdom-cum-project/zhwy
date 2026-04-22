<template>
  <view class="dashboard-container">
    <!-- 顶部标题 -->
    <view class="header">
      <text class="title">智慧社区数据大屏</text>
      <text class="subtitle">{{ currentTime }}</text>
    </view>

    <!-- 数据概览卡片 -->
    <view class="stats-cards">
      <view class="stat-card">
        <view class="stat-icon device">📱</view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalDevices }}</text>
          <text class="stat-label">设备总数</text>
        </view>
        <view class="stat-trend">
          <text class="trend-value">{{ stats.onlineDevices }} 在线</text>
        </view>
      </view>

      <view class="stat-card">
        <view class="stat-icon warning">⚠️</view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalWarnings }}</text>
          <text class="stat-label">预警总数</text>
        </view>
        <view class="stat-trend">
          <text class="trend-value warning">{{ stats.pendingWarnings }} 待处理</text>
        </view>
      </view>

      <view class="stat-card">
        <view class="stat-icon order">📋</view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalOrders }}</text>
          <text class="stat-label">服务订单</text>
        </view>
        <view class="stat-trend">
          <text class="trend-value">{{ stats.pendingOrders }} 待处理</text>
        </view>
      </view>

      <view class="stat-card">
        <view class="stat-icon member">👥</view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalMembers }}</text>
          <text class="stat-label">社区居民</text>
        </view>
        <view class="stat-trend">
          <text class="trend-value">{{ stats.elderlyCount }} 老人</text>
        </view>
      </view>
    </view>

    <!-- 图表区域 -->
    <view class="charts-section">
      <!-- 左侧：设备状态和设备类型 -->
      <view class="chart-left">
        <!-- 设备在线率 -->
        <view class="chart-card">
          <view class="chart-header">
            <text class="chart-title">设备在线状态</text>
          </view>
          <view class="device-status-chart">
            <view class="progress-ring">
              <view class="ring-bg"></view>
              <view class="ring-progress" :style="{ background: `conic-gradient(#52c41a ${stats.deviceOnlineRate * 3.6}deg, #f0f0f0 0deg)` }"></view>
              <view class="ring-inner">
                <text class="ring-value">{{ stats.deviceOnlineRate }}%</text>
                <text class="ring-label">在线率</text>
              </view>
            </view>
            <view class="device-legend">
              <view class="legend-item">
                <view class="legend-dot online"></view>
                <text class="legend-text">在线 {{ stats.onlineDevices }}</text>
              </view>
              <view class="legend-item">
                <view class="legend-dot offline"></view>
                <text class="legend-text">离线 {{ stats.offlineDevices }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 设备类型分布 -->
        <view class="chart-card">
          <view class="chart-header">
            <text class="chart-title">设备类型分布</text>
          </view>
          <view class="device-type-list">
            <view class="type-item" v-for="(item, index) in deviceTypeStats" :key="index">
              <text class="type-name">{{ item.name }}</text>
              <view class="type-bar">
                <view class="type-progress" :style="{ width: getTypePercent(item.value) + '%' }"></view>
              </view>
              <text class="type-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 中间：预警趋势和实时预警 -->
      <view class="chart-center">
        <!-- 预警趋势 -->
        <view class="chart-card">
          <view class="chart-header">
            <text class="chart-title">近7天预警趋势</text>
          </view>
          <view class="trend-chart">
            <view class="trend-bars">
              <view class="trend-bar-item" v-for="(item, index) in warningTrend" :key="index">
                <view class="trend-bar" :style="{ height: getTrendHeight(item.count) + '%' }"></view>
                <text class="trend-day">{{ item.day }}</text>
                <text class="trend-count">{{ item.count }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 实时预警 -->
        <view class="chart-card warning-list-card">
          <view class="chart-header">
            <text class="chart-title">实时预警</text>
            <view class="refresh-btn" @click="refreshData">
              <text class="refresh-icon">🔄</text>
            </view>
          </view>
          <view class="warning-list">
            <view class="warning-item" v-for="(item, index) in recentWarnings" :key="index" :class="'level-' + (item.level || 1)">
              <view class="warning-icon">⚠️</view>
              <view class="warning-info">
                <text class="warning-title">{{ item.title }}</text>
                <text class="warning-location">📍 {{ item.location || '未知位置' }}</text>
              </view>
              <text class="warning-time">{{ formatTime(item.createTime) }}</text>
            </view>
            <view class="empty-warning" v-if="recentWarnings.length === 0">
              <text>暂无预警信息</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 右侧：房间分布和特殊群体 -->
      <view class="chart-right">
        <!-- 房间设备分布 -->
        <view class="chart-card">
          <view class="chart-header">
            <text class="chart-title">房间设备分布</text>
          </view>
          <view class="room-list">
            <view class="room-item" v-for="(item, index) in roomDeviceStats" :key="index">
              <view class="room-icon">🏠</view>
              <view class="room-info">
                <text class="room-name">{{ item.room }}</text>
                <view class="room-bar">
                  <view class="room-progress" :style="{ width: getRoomPercent(item.count) + '%' }"></view>
                </view>
              </view>
              <text class="room-count">{{ item.count }}个</text>
            </view>
          </view>
        </view>

        <!-- 特殊群体统计 -->
        <view class="chart-card">
          <view class="chart-header">
            <text class="chart-title">特殊群体关怀</text>
          </view>
          <view class="special-group">
            <view class="group-item">
              <view class="group-icon elderly">👴</view>
              <view class="group-info">
                <text class="group-value">{{ stats.elderlyCount }}</text>
                <text class="group-label">老人</text>
              </view>
            </view>
            <view class="group-item">
              <view class="group-icon child">👶</view>
              <view class="group-info">
                <text class="group-value">{{ stats.childCount }}</text>
                <text class="group-label">儿童</text>
              </view>
            </view>
            <view class="group-item">
              <view class="group-icon activity">🎉</view>
              <view class="group-info">
                <text class="group-value">{{ stats.ongoingActivities }}</text>
                <text class="group-label">进行中活动</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getDashboardStats, getDeviceTypeStats, getWarningTrend, getRecentWarnings, getRoomDeviceStats } from '@/api/dashboard.js'

export default {
  data() {
    return {
      currentTime: '',
      stats: {
        totalDevices: 0,
        onlineDevices: 0,
        offlineDevices: 0,
        deviceOnlineRate: 0,
        totalWarnings: 0,
        pendingWarnings: 0,
        processedWarnings: 0,
        totalOrders: 0,
        pendingOrders: 0,
        processingOrders: 0,
        completedOrders: 0,
        totalMembers: 0,
        elderlyCount: 0,
        childCount: 0,
        adultCount: 0,
        ongoingActivities: 0
      },
      deviceTypeStats: [],
      warningTrend: [],
      recentWarnings: [],
      roomDeviceStats: [],
      refreshTimer: null
    }
  },
  onLoad() {
    this.updateTime()
    this.loadAllData()
    // 每30秒自动刷新
    this.refreshTimer = setInterval(() => {
      this.loadAllData()
    }, 30000)
  },
  onUnload() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  methods: {
    // 更新时间
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = (now.getMonth() + 1).toString().padStart(2, '0')
      const day = now.getDate().toString().padStart(2, '0')
      const hours = now.getHours().toString().padStart(2, '0')
      const minutes = now.getMinutes().toString().padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}`
      setTimeout(() => this.updateTime(), 60000)
    },

    // 加载所有数据
    async loadAllData() {
      await Promise.all([
        this.loadStats(),
        this.loadDeviceTypeStats(),
        this.loadWarningTrend(),
        this.loadRecentWarnings(),
        this.loadRoomDeviceStats()
      ])
    },

    // 加载统计数据
    async loadStats() {
      try {
        const res = await getDashboardStats()
        if (res.code === 200 && res.data) {
          this.stats = { ...this.stats, ...res.data }
        }
      } catch (e) {
        console.error('获取统计数据失败', e)
      }
    },

    // 加载设备类型统计
    async loadDeviceTypeStats() {
      try {
        const res = await getDeviceTypeStats()
        if (res.code === 200 && res.data) {
          this.deviceTypeStats = res.data
        }
      } catch (e) {
        console.error('获取设备类型统计失败', e)
      }
    },

    // 加载预警趋势
    async loadWarningTrend() {
      try {
        const res = await getWarningTrend()
        if (res.code === 200 && res.data) {
          this.warningTrend = res.data
        }
      } catch (e) {
        console.error('获取预警趋势失败', e)
      }
    },

    // 加载最近预警
    async loadRecentWarnings() {
      try {
        const res = await getRecentWarnings()
        if (res.code === 200 && res.data) {
          this.recentWarnings = res.data
        }
      } catch (e) {
        console.error('获取最近预警失败', e)
      }
    },

    // 加载房间设备统计
    async loadRoomDeviceStats() {
      try {
        const res = await getRoomDeviceStats()
        if (res.code === 200 && res.data) {
          this.roomDeviceStats = res.data
        }
      } catch (e) {
        console.error('获取房间设备统计失败', e)
      }
    },

    // 刷新数据
    refreshData() {
      uni.showLoading({ title: '刷新中...' })
      this.loadAllData().finally(() => {
        uni.hideLoading()
        uni.showToast({ title: '刷新成功', icon: 'success' })
      })
    },

    // 计算类型百分比
    getTypePercent(value) {
      const max = Math.max(...this.deviceTypeStats.map(item => item.value), 1)
      return (value / max) * 100
    },

    // 计算趋势高度
    getTrendHeight(count) {
      const max = Math.max(...this.warningTrend.map(item => item.count), 1)
      return (count / max) * 80 + 20
    },

    // 计算房间百分比
    getRoomPercent(count) {
      const max = Math.max(...this.roomDeviceStats.map(item => item.count), 1)
      return (count / max) * 100
    },

    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    }
  }
}
</script>

<style lang="scss">
.dashboard-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1f3c 0%, #0d1225 100%);
  padding: 20rpx;
  color: #fff;
}

// 顶部标题
.header {
  text-align: center;
  padding: 20rpx 0 40rpx;

  .title {
    font-size: 48rpx;
    font-weight: bold;
    background: linear-gradient(90deg, #00d4ff, #00ff88);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    display: block;
    margin-bottom: 10rpx;
  }

  .subtitle {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.6);
  }
}

// 数据概览卡片
.stats-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 30rpx;

  .stat-card {
    flex: 1;
    min-width: 200rpx;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 16rpx;
    padding: 30rpx;
    display: flex;
    align-items: center;
    border: 1px solid rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);

    .stat-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 40rpx;
      margin-right: 20rpx;

      &.device { background: rgba(0, 212, 255, 0.2); }
      &.warning { background: rgba(255, 77, 79, 0.2); }
      &.order { background: rgba(250, 173, 20, 0.2); }
      &.member { background: rgba(82, 196, 26, 0.2); }
    }

    .stat-info {
      flex: 1;

      .stat-value {
        font-size: 40rpx;
        font-weight: bold;
        color: #fff;
        display: block;
      }

      .stat-label {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.6);
      }
    }

    .stat-trend {
      .trend-value {
        font-size: 22rpx;
        color: #52c41a;
        background: rgba(82, 196, 26, 0.1);
        padding: 6rpx 16rpx;
        border-radius: 20rpx;

        &.warning {
          color: #ff4d4f;
          background: rgba(255, 77, 79, 0.1);
        }
      }
    }
  }
}

// 图表区域
.charts-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;

  .chart-left,
  .chart-center,
  .chart-right {
    flex: 1;
    min-width: 300rpx;
    display: flex;
    flex-direction: column;
    gap: 20rpx;
  }
}

// 图表卡片
.chart-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16rpx;
  padding: 30rpx;
  border: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .chart-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #fff;
    }

    .refresh-btn {
      padding: 10rpx;

      .refresh-icon {
        font-size: 32rpx;
      }
    }
  }
}

// 设备状态图表
.device-status-chart {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 20rpx 0;

  .progress-ring {
    width: 200rpx;
    height: 200rpx;
    position: relative;

    .ring-bg,
    .ring-progress {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }

    .ring-bg {
      background: rgba(255, 255, 255, 0.1);
    }

    .ring-inner {
      position: absolute;
      top: 20rpx;
      left: 20rpx;
      right: 20rpx;
      bottom: 20rpx;
      background: #1a1f3c;
      border-radius: 50%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .ring-value {
        font-size: 48rpx;
        font-weight: bold;
        color: #52c41a;
      }

      .ring-label {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }

  .device-legend {
    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;

      .legend-dot {
        width: 16rpx;
        height: 16rpx;
        border-radius: 50%;
        margin-right: 12rpx;

        &.online { background: #52c41a; }
        &.offline { background: #ff4d4f; }
      }

      .legend-text {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

// 设备类型列表
.device-type-list {
  .type-item {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;

    .type-name {
      width: 120rpx;
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.8);
    }

    .type-bar {
      flex: 1;
      height: 16rpx;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 8rpx;
      margin: 0 16rpx;
      overflow: hidden;

      .type-progress {
        height: 100%;
        background: linear-gradient(90deg, #00d4ff, #00ff88);
        border-radius: 8rpx;
        transition: width 0.5s ease;
      }
    }

    .type-value {
      width: 60rpx;
      font-size: 24rpx;
      color: #fff;
      text-align: right;
    }
  }
}

// 趋势图表
.trend-chart {
  height: 300rpx;
  display: flex;
  align-items: flex-end;

  .trend-bars {
    display: flex;
    justify-content: space-around;
    align-items: flex-end;
    width: 100%;
    height: 100%;

    .trend-bar-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      flex: 1;

      .trend-bar {
        width: 40rpx;
        background: linear-gradient(180deg, #00d4ff, #00ff88);
        border-radius: 8rpx 8rpx 0 0;
        transition: height 0.5s ease;
        min-height: 20rpx;
      }

      .trend-day {
        font-size: 22rpx;
        color: rgba(255, 255, 255, 0.6);
        margin-top: 10rpx;
      }

      .trend-count {
        font-size: 20rpx;
        color: #00d4ff;
        margin-top: 4rpx;
      }
    }
  }
}

// 预警列表
.warning-list-card {
  flex: 1;

  .warning-list {
    max-height: 400rpx;
    overflow-y: auto;

    .warning-item {
      display: flex;
      align-items: center;
      padding: 20rpx;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12rpx;
      margin-bottom: 16rpx;
      border-left: 4rpx solid #ff4d4f;

      &.level-1 { border-left-color: #ff4d4f; }
      &.level-2 { border-left-color: #faad14; }
      &.level-3 { border-left-color: #52c41a; }

      .warning-icon {
        font-size: 36rpx;
        margin-right: 16rpx;
      }

      .warning-info {
        flex: 1;

        .warning-title {
          font-size: 26rpx;
          color: #fff;
          display: block;
          margin-bottom: 8rpx;
        }

        .warning-location {
          font-size: 22rpx;
          color: rgba(255, 255, 255, 0.6);
        }
      }

      .warning-time {
        font-size: 22rpx;
        color: rgba(255, 255, 255, 0.5);
      }
    }

    .empty-warning {
      text-align: center;
      padding: 60rpx;
      color: rgba(255, 255, 255, 0.5);
      font-size: 26rpx;
    }
  }
}

// 房间列表
.room-list {
  .room-item {
    display: flex;
    align-items: center;
    padding: 16rpx 0;
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);

    &:last-child {
      border-bottom: none;
    }

    .room-icon {
      font-size: 36rpx;
      margin-right: 16rpx;
    }

    .room-info {
      flex: 1;

      .room-name {
        font-size: 26rpx;
        color: #fff;
        display: block;
        margin-bottom: 8rpx;
      }

      .room-bar {
        height: 8rpx;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 4rpx;
        overflow: hidden;

        .room-progress {
          height: 100%;
          background: linear-gradient(90deg, #faad14, #ff7a45);
          border-radius: 4rpx;
          transition: width 0.5s ease;
        }
      }
    }

    .room-count {
      font-size: 24rpx;
      color: #faad14;
      margin-left: 16rpx;
    }
  }
}

// 特殊群体
.special-group {
  display: flex;
  justify-content: space-around;
  padding: 20rpx 0;

  .group-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    .group-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 48rpx;
      margin-bottom: 16rpx;

      &.elderly { background: rgba(255, 77, 79, 0.2); }
      &.child { background: rgba(24, 144, 255, 0.2); }
      &.activity { background: rgba(82, 196, 26, 0.2); }
    }

    .group-info {
      text-align: center;

      .group-value {
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        display: block;
      }

      .group-label {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
}
</style>
