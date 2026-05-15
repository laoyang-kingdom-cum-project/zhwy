<template>
  <view class="device-control-container">
    <!-- 设备信息卡片 -->
    <view class="device-card">
      <view class="device-icon" :class="device.deviceType">
        <image class="device-icon-img" :src="getDeviceIcon(device.deviceType)" />
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
          <image class="mode-icon" :src="mode.icon" />
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
        { name: '制冷', value: 'cool', icon: '/static/emojis/emoji_26_snowflake.png' },
        { name: '制热', value: 'heat', icon: '/static/emojis/emoji_27_fire.png' },
        { name: '除湿', value: 'dry', icon: '/static/emojis/emoji_28_droplet.png' },
        { name: '送风', value: 'fan', icon: '/static/emojis/emoji_29_tornado.png' }
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
      // 第一步：校验设备ID
      if (!this.deviceId) return
      // 第二步：调用API获取设备详情
      getDeviceDetail(this.deviceId).then(res => {
        if (res.code === 200 && res.data) {
          const data = res.data
          // 第三步：映射API数据到本地设备对象
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

    // 根据设备类型获取图标
    getDeviceIcon(type) {
      const icons = {
        light: '/static/emojis/emoji_25_bulb.png',
        air_conditioner: '/static/emojis/emoji_26_snowflake.png',
        curtain: '/static/emojis/emoji_30_switch.png',
        switch: '/static/emojis/emoji_30_switch.png',
        lock: '/static/emojis/emoji_31_lock.png',
        camera: '/static/emojis/emoji_32_camera.png'
      }
      return icons[type] || '/static/emojis/emoji_33_pager.png'
    },

    // 根据设备类型获取中文名称
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

    // 切换设备开关状态
    async toggleDevice(e) {
      // 第一步：获取开关新状态
      const newStatus = e.detail.value ? 1 : 0
      try {
        // 第二步：调用API发送控制指令
        const res = await controlDevice(this.deviceId, { status: newStatus })
        if (res.code === 200) {
          // 第三步：更新本地状态并提示
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

    // 改变亮度值
    changeBrightness(e) {
      this.device.value = e.detail.value
    },

    // 改变温度值
    changeTemp(delta) {
      // 第一步：检查设备是否在线
      if (!this.device.online) {
        uni.showToast({ title: '设备离线', icon: 'none' })
        return
      }
      // 第二步：计算新温度并校验范围(16-30°C)
      const newTemp = this.device.value + delta
      if (newTemp >= 16 && newTemp <= 30) {
        this.device.value = newTemp
      }
    },

    // 选择运行模式
    selectMode(mode) {
      // 第一步：检查设备是否在线
      if (!this.device.online) {
        uni.showToast({ title: '设备离线', icon: 'none' })
        return
      }
      // 第二步：切换模式并提示
      this.currentMode = mode
      uni.showToast({ title: '模式已切换', icon: 'none' })
    }
  }
}
</script>

<style lang="scss">
// 页面根容器
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

  // 设备图标容器（圆形）
  .device-icon {
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;

    .device-icon-img {
      width: 80rpx;
      height: 80rpx;
    }

    // 不同设备类型的背景色
    &.light { background: #FFF8E1; }
    &.air_conditioner { background: #E3F2FD; }
    &.curtain { background: #F3E5F5; }
  }

  // 设备名称
  .device-name {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
  }

  // 设备在线/离线状态标签
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

    // 温度加减按钮
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
      width: 48rpx;
      height: 48rpx;
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
