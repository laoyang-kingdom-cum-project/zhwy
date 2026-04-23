<template>
  <view class="device-control-container">
    <!-- 设备信息卡片 -->
    <view class="device-card">
      <view class="device-icon" :class="device.deviceType">
        <text>{{ getDeviceIcon(device.deviceType) }}</text>
      </view>
      <text class="device-name">{{ device.deviceName }}</text>
      <text class="device-status" :class="getStatusClass(device.status)">
        {{ getStatusText(device.status) }}
      </text>
    </view>

    <!-- 设备信息 -->
    <view class="info-section">
      <view class="info-title">设备信息</view>
      <view class="info-item">
        <text class="label">设备编号</text>
        <text class="value">{{ device.deviceCode }}</text>
      </view>
      <view class="info-item">
        <text class="label">安装位置</text>
        <text class="value">{{ device.location }}</text>
      </view>
      <view class="info-item">
        <text class="label">设备类型</text>
        <text class="value">{{ getTypeText(device.deviceType) }}</text>
      </view>
      <view class="info-item" v-if="device.ipAddress">
        <text class="label">IP地址</text>
        <text class="value">{{ device.ipAddress }}</text>
      </view>
      <view class="info-item" v-if="device.batteryLevel">
        <text class="label">电池电量</text>
        <view class="battery-wrap">
          <view class="battery-bar">
            <view class="battery-fill" :style="{width: device.batteryLevel + '%'}" :class="{low: device.batteryLevel < 30}"></view>
          </view>
          <text class="battery-text">{{ device.batteryLevel }}%</text>
        </view>
      </view>
      <view class="info-item">
        <text class="label">最后在线</text>
        <text class="value">{{ device.lastOnlineTime || '未知' }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="device.status !== '0'">
      <button class="action-btn btn-primary" @click="reportFault">报修设备</button>
    </view>
  </view>
</template>

<script>
import { getDeviceDetail } from '@/api/device.js'

export default {
  data() {
    return {
      deviceId: null,
      device: {
        deviceName: '',
        deviceCode: '',
        deviceType: '',
        location: '',
        status: '0',
        ipAddress: '',
        batteryLevel: 0,
        lastOnlineTime: ''
      }
    }
  },
  onLoad(options) {
    if (options.id) {
      this.deviceId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({ title: '加载中...' })
      try {
        const res = await getDeviceDetail(this.deviceId)
        if (res.code === 200 && res.data) {
          this.device = res.data
        }
      } catch (e) {
        console.error('加载设备详情失败', e)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
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

    getTypeText(type) {
      const map = {
        sensor: '传感器',
        camera: '摄像头',
        elevator: '电梯',
        door: '门禁'
      }
      return map[type] || '未知设备'
    },

    reportFault() {
      uni.navigateTo({
        url: `/pages/fault/report?deviceId=${this.device.id}&deviceName=${this.device.deviceName}`
      })
    }
  }
}
</script>

<style>
.device-control-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

/* 设备卡片 */
.device-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  padding: 60rpx 40rpx;
}

.device-icon {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80rpx;
  margin-bottom: 30rpx;
}

.device-icon.sensor { background: #E8F5E9; }
.device-icon.camera { background: #E3F2FD; }
.device-icon.elevator { background: #FFF3E0; }
.device-icon.door { background: #F3E5F5; }

.device-name {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 16rpx;
}

.device-status {
  font-size: 28rpx;
  padding: 8rpx 24rpx;
  border-radius: 30rpx;
}

.device-status.online {
  background: #E8F5E9;
  color: #388E3C;
}

.device-status.fault {
  background: #FFEBEE;
  color: #D32F2F;
}

.device-status.offline {
  background: #f5f5f5;
  color: #999;
}

/* 设备信息 */
.info-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.info-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
}

.info-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  align-items: center;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  width: 180rpx;
  font-size: 28rpx;
  color: #999;
}

.info-item .value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.battery-wrap {
  flex: 1;
  display: flex;
  align-items: center;
}

.battery-bar {
  width: 120rpx;
  height: 16rpx;
  background: #e0e0e0;
  border-radius: 8rpx;
  overflow: hidden;
  margin-right: 12rpx;
}

.battery-fill {
  height: 100%;
  background: #4CAF50;
  border-radius: 8rpx;
  transition: width 0.3s;
}

.battery-fill.low {
  background: #FF6B6B;
}

.battery-text {
  font-size: 24rpx;
  color: #666;
}

/* 操作按钮 */
.action-section {
  margin: 40rpx 20rpx;
}

.action-btn {
  width: 100%;
  height: 100rpx;
  line-height: 100rpx;
  background: #667eea;
  color: #fff;
  font-size: 32rpx;
  border-radius: 50rpx;
  border: none;
  font-weight: 500;
}

.action-btn:active {
  opacity: 0.8;
}
</style>
