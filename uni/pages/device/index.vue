<template>
  <view class="device-container">
    <!-- 房间选择 -->
    <scroll-view class="room-scroll" scroll-x>
      <view class="room-list">
        <view 
          class="room-item" 
          :class="{ active: currentRoom === '' }"
          @click="selectRoom('')"
        >
          <text>全部</text>
        </view>
        <view 
          v-for="(room, index) in rooms" 
          :key="index"
          class="room-item"
          :class="{ active: currentRoom === room.roomId }"
          @click="selectRoom(room.roomId)"
        >
          <text>{{ room.roomName }}</text>
        </view>

      <view style="width: 5px;height: 30px;background-color: black;transition: all 0.3s;flex-shrink: 0;opacity: 0;"></view>


      </view>
    </scroll-view>

    <!-- 设备列表 -->
    <view class="device-list">
      <view 
        class="device-card" 
        v-for="(device, index) in filteredDevices" 
        :key="index"
        @click="controlDevice(device)"
      >
        <view class="device-header">
          <view class="device-icon" :class="device.deviceType">
            <text>{{ getDeviceIcon(device.deviceType) }}</text>
          </view>
          <view class="device-status">
            <text class="status-dot" :class="device.online ? 'online' : 'offline'"></text>
            <text class="status-text">{{ device.online ? '在线' : '离线' }}</text>
          </view>
        </view>
        <view class="device-info">
          <text class="device-name">{{ device.deviceName }}</text>
          <text class="device-room">{{ device.roomName }}</text>
        </view>
        <view class="device-control">
          <switch 
            :checked="device.status === 1" 
            :disabled="!device.online"
            @change="toggleDevice(device, $event)"
            @click.stop
            color="#007AFF"
          />
        </view>
      </view>
    </view>

    <!-- 传感器数据 -->
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
    <custom-tabbar :current-index="1"></custom-tabbar>
  </view>
</template>

<script>
import { getDeviceList, controlDevice } from '@/api/device.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      currentRoom: '',
      rooms: [],
      devices: [],
      sensorData: {
        temperature: 0,
        humidity: 0,
        pm25: 0,
        formaldehyde: 0
      }
    }
  },
  computed: {
    filteredDevices() {
      if (!this.currentRoom) return this.devices
      return this.devices.filter(d => String(d.roomId) === String(this.currentRoom))
    }
  },
  onLoad() {
    this.loadDeviceList()
    this.generateSensorData()
  },
  onShow() {
    // 页面显示时刷新数据
    this.loadDeviceList()
    this.generateSensorData()
  },
  onPullDownRefresh() {
    // 下拉刷新
    this.loadDeviceList().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    // 加载设备列表
    async loadDeviceList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getDeviceList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.devices = res.rows.map(item => ({
            deviceId: item.id,
            deviceName: item.deviceName,
            deviceType: item.deviceType,
            roomId: item.roomId,
            roomName: item.roomName,
            status: item.status === '1' || item.status === 1 ? 1 : 0,
            online: item.online === '1' || item.online === 1 ? 1 : 0,
            value: parseInt(item.value) || 0
          }))
          
          // 提取房间列表
          const roomMap = new Map()
          res.rows.forEach(item => {
            if (!roomMap.has(item.roomId)) {
              roomMap.set(item.roomId, {
                roomId: item.roomId,
                roomName: item.roomName
              })
            }
          })
          this.rooms = Array.from(roomMap.values())
        }
      } catch (e) {
        console.error('获取设备列表失败', e)
      }
    },

    selectRoom(roomId) {
      this.currentRoom = roomId
    },

    // 根据时间戳生成传感器数据
    generateSensorData() {
      const timestamp = Date.now()
      // 使用时间戳生成伪随机数，但保持在合理范围内
      const seed = timestamp % 1000

      // 温度: 20-30度之间波动
      this.sensorData.temperature = (20 + (seed % 100) / 10).toFixed(1)

      // 湿度: 40-70%之间
      this.sensorData.humidity = 40 + (seed % 31)

      // PM2.5: 10-80之间
      this.sensorData.pm25 = 10 + (seed % 71)

      // 甲醛: 0.01-0.15之间
      this.sensorData.formaldehyde = (0.01 + (seed % 15) / 100).toFixed(2)
    },

    getDeviceIcon(type) {
      const icons = {
        light: '💡',
        air_conditioner: '❄️',
        curtain: '🌄',
        switch: '🔘',
        lock: '🔒',
        camera: '📷'
      }
      return icons[type] || '📟'
    },
    async toggleDevice(device, e) {
      const newStatus = e.detail.value ? 1 : 0
      try {
        const res = await controlDevice(device.deviceId, { status: newStatus })
        console.log('控制设备响应:', res)
        if (res.code === 200) {
          device.status = newStatus
          uni.showToast({
            title: `${device.deviceName}已${device.status ? '开启' : '关闭'}`,
            icon: 'none'
          })
        } else {
          uni.showToast({ title: res.msg || '控制失败', icon: 'none' })
        }
      } catch (err) {
        uni.showToast({ title: '控制失败', icon: 'none' })
      }
    },
    controlDevice(device) {
      if (!device.online) {
        uni.showToast({ title: '设备离线', icon: 'none' })
        return
      }
      // 跳转到设备控制详情页
      uni.navigateTo({
        url: `/pages/device/control?id=${device.deviceId}`
      })
    }
  }
}
</script>

<style lang="scss">
.device-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 130rpx;
}

// 房间选择
.room-scroll {
  background: #fff;
  padding: 24rpx 0;
  white-space: nowrap;

  .room-list {
    display: flex;
    padding: 0 24rpx;
    padding-right: 44rpx;

    .room-item {
      padding: 16rpx 36rpx;
      margin-right: 20rpx;
      border-radius: 40rpx;
      background: #f5f7fa;
      border: 2rpx solid transparent;
      transition: all 0.3s;
      flex-shrink: 0;

      &:last-child {
        margin-right: 40rpx;
      }

      text {
        font-size: 28rpx;
        color: #666;
        font-weight: 500;
        white-space: nowrap;
      }

      &.active {
        background: #007AFF;
        border-color: #007AFF;
        box-shadow: 0 4rpx 16rpx rgba(0, 122, 255, 0.3);

        text {
          color: #fff;
          font-weight: 600;
        }
      }

      &:active {
        transform: scale(0.95);
      }
    }
  }
}

// 设备列表
.device-list {
  padding: 20rpx;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .device-card {
    width: calc(50% - 10rpx);
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
    box-sizing: border-box;

    .device-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .device-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 40rpx;

        &.light { background: #FFF8E1; }
        &.air_conditioner { background: #E3F2FD; }
        &.curtain { background: #F3E5F5; }
      }

      .device-status {
        display: flex;
        align-items: center;

        .status-dot {
          width: 16rpx;
          height: 16rpx;
          border-radius: 50%;
          margin-right: 8rpx;

          &.online { background: #4CAF50; }
          &.offline { background: #999; }
        }

        .status-text {
          font-size: 22rpx;
          color: #999;
        }
      }
    }

    .device-info {
      margin-bottom: 20rpx;

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
    }

    .device-control {
      display: flex;
      justify-content: flex-end;
    }
  }
}

// 传感器数据
.sensor-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }

  .sensor-grid {
    display: flex;
    flex-wrap: wrap;

    .sensor-item {
      width: 25%;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx 0;

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
    }
  }
}
</style>
