<template>
  <view class="device-control-container">
    <!-- 设备信息卡片 -->
    <view class="device-card">
      <view class="device-icon" :class="device.deviceType">
        <text>{{ getDeviceIcon(device.deviceType) }}</text>
      </view>
      <text class="device-name">{{ device.deviceName }}</text>
      <text class="device-status" :class="device.online ? 'online' : 'offline'">
        {{ device.online ? '在线' : '离线' }}
      </text>
    </view>

    <!-- 开关控制 -->
    <view class="control-section">
      <view class="control-row">
        <text class="control-label">电源开关</text>
        <switch 
          :checked="device.status === 1" 
          :disabled="!device.online"
          @change="toggleDevice"
          color="#007AFF"
        />
      </view>
    </view>

    <!-- 亮度控制（灯光设备） -->
    <view class="control-section" v-if="device.deviceType === 'light'">
      <view class="control-row">
        <text class="control-label">亮度调节</text>
        <text class="control-value">{{ device.value }}%</text>
      </view>
      <slider 
        :value="device.value" 
        @change="changeBrightness"
        :min="0" 
        :max="100"
        :disabled="!device.online || device.status !== 1"
        activeColor="#007AFF"
        backgroundColor="#e0e0e0"
        block-size="28"
      />
    </view>

    <!-- 温度控制（空调设备） -->
    <view class="control-section" v-if="device.deviceType === 'air_conditioner'">
      <view class="control-row">
        <text class="control-label">温度设置</text>
        <view class="temp-control">
          <text class="temp-btn" @click="changeTemp(-1)">-</text>
          <text class="temp-value">{{ device.value }}°C</text>
          <text class="temp-btn" @click="changeTemp(1)">+</text>
        </view>
      </view>
    </view>

    <!-- 模式选择 -->
    <view class="control-section" v-if="device.deviceType === 'air_conditioner'">
      <view class="section-title">模式选择</view>
      <view class="mode-grid">
        <view 
          class="mode-item" 
          v-for="(mode, index) in modes" 
          :key="index"
          :class="{ active: currentMode === mode.value }"
          @click="selectMode(mode.value)"
        >
          <text class="mode-icon">{{ mode.icon }}</text>
          <text class="mode-name">{{ mode.name }}</text>
        </view>
      </view>
    </view>

    <!-- 设备信息 -->
    <view class="info-section">
      <view class="info-title">设备信息</view>
      <view class="info-item">
        <text class="label">设备ID</text>
        <text class="value">{{ device.deviceId }}</text>
      </view>
      <view class="info-item">
        <text class="label">所在房间</text>
        <text class="value">{{ device.roomName }}</text>
      </view>
      <view class="info-item">
        <text class="label">设备类型</text>
        <text class="value">{{ getDeviceTypeName(device.deviceType) }}</text>
      </view>
      <view class="info-item">
        <text class="label">添加时间</text>
        <text class="value">2024-01-01</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getDeviceDetail, controlDevice } from '@/api/device.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      deviceId: '',
      device: {
        deviceId: '',
        deviceName: '',
        deviceType: 'light',
        roomName: '',
        status: 0,
        online: 0,
        value: 0
      },
      currentMode: 'cool',
      modes: [
        { name: '制冷', value: 'cool', icon: '❄️' },
        { name: '制热', value: 'heat', icon: '🔥' },
        { name: '除湿', value: 'dry', icon: '💧' },
        { name: '送风', value: 'fan', icon: '🌪️' }
      ]
    }
  },
  onLoad(options) {
    this.deviceId = options.id
    this.loadDeviceDetail()
  },
  methods: {
    // 加载设备详情
    loadDeviceDetail() {
      if (!this.deviceId) return
      getDeviceDetail(this.deviceId).then(res => {
        if (res.code === 200 && res.data) {
          const data = res.data
          this.device = {
            deviceId: data.id,
            deviceName: data.deviceName,
            deviceType: data.deviceType,
            roomName: data.roomName,
            status: data.status === '1' || data.status === 1 ? 1 : 0,
            online: data.online === '1' || data.online === 1 ? 1 : 0,
            value: parseInt(data.value) || 0
          }
        }
      }).catch(() => {
        uni.showToast({ title: '获取设备详情失败', icon: 'none' })
      })
    },

    getDeviceIcon(type) {
      const icons = {
        light: '💡',
        air_conditioner: '❄️',
        curtain: '🪟',
        switch: '🔘',
        lock: '🔒',
        camera: '📷'
      }
      return icons[type] || '📟'
    },
    getDeviceTypeName(type) {
      const names = {
        light: '智能灯光',
        air_conditioner: '空调',
        curtain: '智能窗帘',
        switch: '智能开关',
        lock: '智能门锁',
        camera: '摄像头'
      }
      return names[type] || '未知设备'
    },
    async toggleDevice(e) {
      const newStatus = e.detail.value ? 1 : 0
      try {
        const res = await controlDevice(this.deviceId, { status: newStatus })
        if (res.code === 200) {
          this.device.status = newStatus
          uni.showToast({
            title: this.device.status ? '设备已开启' : '设备已关闭',
            icon: 'none'
          })
        } else {
          uni.showToast({ title: res.msg || '控制失败', icon: 'none' })
        }
      } catch (err) {
        uni.showToast({ title: '控制失败', icon: 'none' })
      }
    },
    changeBrightness(e) {
      this.device.value = e.detail.value
    },
    changeTemp(delta) {
      if (!this.device.online) {
        uni.showToast({ title: '设备离线', icon: 'none' })
        return
      }
      const newTemp = this.device.value + delta
      if (newTemp >= 16 && newTemp <= 30) {
        this.device.value = newTemp
      }
    },
    selectMode(mode) {
      if (!this.device.online) {
        uni.showToast({ title: '设备离线', icon: 'none' })
        return
      }
      this.currentMode = mode
      uni.showToast({ title: '模式已切换', icon: 'none' })
    }
  }
}
</script>

<style lang="scss">
.device-control-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

// 设备卡片
.device-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  padding: 60rpx 40rpx;

  .device-icon {
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 80rpx;
    margin-bottom: 30rpx;

    &.light { background: #FFF8E1; }
    &.air_conditioner { background: #E3F2FD; }
    &.curtain { background: #F3E5F5; }
  }

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

    &.online {
      background: #E8F5E9;
      color: #388E3C;
    }

    &.offline {
      background: #f5f5f5;
      color: #999;
    }
  }
}

// 控制区块
.control-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 20rpx;
  }

  .control-row {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .control-label {
      font-size: 30rpx;
      color: #333;
    }

    .control-value {
      font-size: 30rpx;
      color: #007AFF;
    }

    .temp-control {
      display: flex;
      align-items: center;

      .temp-btn {
        width: 60rpx;
        height: 60rpx;
        background: #f5f5f5;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 36rpx;
        color: #333;
      }

      .temp-value {
        font-size: 40rpx;
        font-weight: bold;
        color: #333;
        margin: 0 40rpx;
      }
    }
  }
}

// 模式网格
.mode-grid {
  display: flex;
  flex-wrap: wrap;

  .mode-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;

    .mode-icon {
      font-size: 48rpx;
      margin-bottom: 12rpx;
    }

    .mode-name {
      font-size: 26rpx;
      color: #666;
    }

    &.active {
      .mode-name {
        color: #007AFF;
        font-weight: 500;
      }
    }
  }
}

// 设备信息
.info-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

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

    &:last-child {
      border-bottom: none;
    }

    .label {
      width: 180rpx;
      font-size: 28rpx;
      color: #999;
    }

    .value {
      flex: 1;
      font-size: 28rpx;
      color: #333;
    }
  }
}
</style>
