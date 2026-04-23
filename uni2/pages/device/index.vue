<template>
  <view class="device-container">
    <!-- 设备类型筛选 -->
    <scroll-view class="room-scroll" scroll-x>
      <view class="room-list">
        <view 
          class="room-item" 
          :class="{ active: currentType === 'all' }"
          @click="setType('all')"
        >
          <text>全部</text>
        </view>
        <view 
          class="room-item"
          :class="{ active: currentType === 'sensor' }"
          @click="setType('sensor')"
        >
          <text>传感器</text>
        </view>
        <view 
          class="room-item"
          :class="{ active: currentType === 'camera' }"
          @click="setType('camera')"
        >
          <text>摄像头</text>
        </view>
        <view 
          class="room-item"
          :class="{ active: currentType === 'elevator' }"
          @click="setType('elevator')"
        >
          <text>电梯</text>
        </view>
        <view 
          class="room-item"
          :class="{ active: currentType === 'door' }"
          @click="setType('door')"
        >
          <text>门禁</text>
        </view>
        <view style="width: 5px;height: 30px;background-color: black;transition: all 0.3s;flex-shrink: 0;opacity: 0;"></view>
      </view>
    </scroll-view>

    <!-- 设备列表 -->
    <view class="device-list">
      <view 
        class="device-card" 
        v-for="(device, index) in deviceList" 
        :key="index"
        @click="goToDetail(device)"
      >
        <view class="device-header">
          <view class="device-icon" :class="device.deviceType">
            <text>{{ getDeviceIcon(device.deviceType) }}</text>
          </view>
          <view class="device-status">
            <text class="status-dot" :class="getStatusClass(device.status)"></text>
            <text class="status-text">{{ getStatusText(device.status) }}</text>
          </view>
        </view>
        <view class="device-info">
          <text class="device-name">{{ device.deviceName }}</text>
          <text class="device-room">{{ device.location }}</text>
        </view>
      </view>
    </view>

    <!-- 环境数据 -->
    <view class="sensor-section">
      <view class="section-title">环境数据</view>
      <view class="sensor-grid">
        <view class="sensor-item">
          <text class="sensor-icon">🌡️</text>
          <text class="sensor-value">{{ sensorData.temperature }}°C</text>
          <text class="sensor-label">温度</text>
        </view>
        <view class="sensor-item">
          <text class="sensor-icon">💧</text>
          <text class="sensor-value">{{ sensorData.humidity }}%</text>
          <text class="sensor-label">湿度</text>
        </view>
        <view class="sensor-item">
          <text class="sensor-icon">🌫️</text>
          <text class="sensor-value">{{ sensorData.pm25 }}</text>
          <text class="sensor-label">PM2.5</text>
        </view>
        <view class="sensor-item">
          <text class="sensor-icon">🔥</text>
          <text class="sensor-value">{{ sensorData.formaldehyde }}</text>
          <text class="sensor-label">甲醛</text>
        </view>
      </view>
    </view>

    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="2"></custom-tabbar>
  </view>
</template>

<script>
import { getDeviceList } from '@/api/device.js'

export default {
  data() {
    return {
      currentType: 'all',
      deviceList: [],
      pageNum: 1,
      pageSize: 100,
      loading: false,
      sensorData: {
        temperature: 0,
        humidity: 0,
        pm25: 0,
        formaldehyde: 0
      }
    }
  },
  onLoad() {
    this.loadData()
    this.generateSensorData()
  },
  onShow() {
    this.loadData()
    this.generateSensorData()
  },
  onPullDownRefresh() {
    this.loadData().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    setType(type) {
      this.currentType = type
      this.loadData()
    },

    async loadData() {
      if (this.loading) return
      this.loading = true

      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        if (this.currentType !== 'all') {
          params.deviceType = this.currentType
        }

        const res = await getDeviceList(params)
        if (res.code === 200) {
          this.deviceList = res.rows || []
        }
      } catch (e) {
        console.error('获取设备列表失败', e)
      } finally {
        this.loading = false
      }
    },

    generateSensorData() {
      const timestamp = Date.now()
      const seed = timestamp % 1000

      this.sensorData.temperature = (20 + (seed % 100) / 10).toFixed(1)
      this.sensorData.humidity = 40 + (seed % 31)
      this.sensorData.pm25 = 10 + (seed % 71)
      this.sensorData.formaldehyde = (0.01 + (seed % 15) / 100).toFixed(2)
    },

    getDeviceIcon(type) {
      const icons = {
        sensor: '📡',
        camera: '📷',
        elevator: '🛗',
        door: '🚪'
      }
      return icons[type] || '📟'
    },

    getStatusClass(status) {
      const map = { '0': 'online', '1': 'fault', '2': 'offline' }
      return map[status] || 'offline'
    },

    getStatusText(status) {
      const map = { '0': '正常', '1': '故障', '2': '离线' }
      return map[status] || '未知'
    },

    goToDetail(device) {
      uni.navigateTo({
        url: `/pages/device/detail?id=${device.id}`
      })
    }
  }
}
</script>

<style>
.device-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 100rpx;
}

/* 类型筛选 */
.room-scroll {
  background: #fff;
  padding: 24rpx 0;
  white-space: nowrap;
}

.room-list {
  display: flex;
  padding: 0 24rpx;
  padding-right: 44rpx;
}

.room-item {
  padding: 16rpx 36rpx;
  margin-right: 20rpx;
  border-radius: 40rpx;
  background: #f5f7fa;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  flex-shrink: 0;
}

.room-item text {
  font-size: 28rpx;
  color: #666;
  font-weight: 500;
  white-space: nowrap;
}

.room-item.active {
  background: #667eea;
  border-color: #667eea;
  box-shadow: 0 4rpx 16rpx rgba(102, 126, 234, 0.3);
}

.room-item.active text {
  color: #fff;
  font-weight: 600;
}

.room-item:active {
  transform: scale(0.95);
}

/* 设备列表 */
.device-list {
  padding: 20rpx;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.device-card {
  width: calc(50% - 10rpx);
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
  box-sizing: border-box;
}

.device-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.device-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
}

.device-icon.sensor { background: #E8F5E9; }
.device-icon.camera { background: #E3F2FD; }
.device-icon.elevator { background: #FFF3E0; }
.device-icon.door { background: #F3E5F5; }

.device-status {
  display: flex;
  align-items: center;
}

.status-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  margin-right: 8rpx;
}

.status-dot.online { background: #4CAF50; }
.status-dot.fault { background: #FF6B6B; }
.status-dot.offline { background: #999; }

.status-text {
  font-size: 22rpx;
  color: #999;
}

.device-info {
  margin-bottom: 10rpx;
}

.device-name {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 8rpx;
}

.device-room {
  font-size: 24rpx;
  color: #999;
}

/* 环境数据 */
.sensor-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.sensor-grid {
  display: flex;
  flex-wrap: wrap;
}

.sensor-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.sensor-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.sensor-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.sensor-label {
  font-size: 24rpx;
  color: #999;
}
</style>
