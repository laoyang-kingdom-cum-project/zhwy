<template>
  <!-- 页面根容器 -->
  <view class="device-control-container">
    <!-- 设备信息卡片 -->
    <view class="device-card">
      <!-- 设备图标 -->
      <view class="device-icon" :class="device.deviceType">
        <!-- 设备图标图片 -->
        <image class="device-icon-img" :src="getDeviceIcon(device.deviceType)" />
      </view>
      <!-- 设备名称 -->
      <text class="device-name">{{ device.deviceName }}</text>
      <!-- 设备状态 -->
      <text class="device-status" :class="device.online ? 'online' : 'offline'">
        <!-- 根据在线状态显示文字 -->
        {{ device.online ? '在线' : '离线' }}
      </text>
    </view>

    <!-- 开关控制区域 -->
    <view class="control-section">
      <!-- 控制行 -->
      <view class="control-row">
        <!-- 控制标签 -->
        <text class="control-label">电源开关</text>
        <!-- 开关组件：绑定状态、禁用控制、切换事件 -->
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
      <!-- 控制行 -->
      <view class="control-row">
        <!-- 控制标签 -->
        <text class="control-label">亮度调节</text>
        <!-- 亮度值显示 -->
        <text class="control-value">{{ device.value }}%</text>
      </view>
      <!-- 滑块组件：绑定亮度、改变事件、范围限制、禁用控制 -->
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
      <!-- 控制行 -->
      <view class="control-row">
        <!-- 控制标签 -->
        <text class="control-label">温度设置</text>
        <!-- 温度控制容器 -->
        <view class="temp-control">
          <!-- 减温度按钮 -->
          <text class="temp-btn" @click="changeTemp(-1)">-</text>
          <!-- 温度值显示 -->
          <text class="temp-value">{{ device.value }}°C</text>
          <!-- 加温度按钮 -->
          <text class="temp-btn" @click="changeTemp(1)">+</text>
        </view>
      </view>
    </view>

    <!-- 模式选择 -->
    <view class="control-section" v-if="device.deviceType === 'air_conditioner'">
      <!-- 区块标题 -->
      <view class="section-title">模式选择</view>
      <!-- 模式网格 -->
      <view class="mode-grid">
        <!-- 遍历模式列表：模式项、active类绑定、点击事件 -->
        <view 
          class="mode-item" 
          v-for="(mode, index) in modes" 
          :key="index"
          :class="{ active: currentMode === mode.value }"
          @click="selectMode(mode.value)"
        >
          <!-- 模式图标 -->
          <image class="mode-icon" :src="mode.icon" />
          <!-- 模式名称 -->
          <text class="mode-name">{{ mode.name }}</text>
        </view>
      </view>
    </view>

    <!-- 设备信息 -->
    <view class="info-section">
      <!-- 信息标题 -->
      <view class="info-title">设备信息</view>
      <!-- 信息项：设备ID -->
      <view class="info-item">
        <!-- 标签 -->
        <text class="label">设备ID</text>
        <!-- 值 -->
        <text class="value">{{ device.deviceId }}</text>
      </view>
      <!-- 信息项：所在房间 -->
      <view class="info-item">
        <!-- 标签 -->
        <text class="label">所在房间</text>
        <!-- 值 -->
        <text class="value">{{ device.roomName }}</text>
      </view>
      <!-- 信息项：设备类型 -->
      <view class="info-item">
        <!-- 标签 -->
        <text class="label">设备类型</text>
        <!-- 值 -->
        <text class="value">{{ getDeviceTypeName(device.deviceType) }}</text>
      </view>
      <!-- 信息项：添加时间 -->
      <view class="info-item">
        <!-- 标签 -->
        <text class="label">添加时间</text>
        <!-- 值 -->
        <text class="value">2024-01-01</text>
      </view>
    </view>
  </view>
</template>

<script>
// 导入设备API
import { getDeviceDetail, controlDevice } from '@/api/device.js'
// 导入适老化mixin
import careModeMixin from '@/mixins/careMode.js'

// 导出组件
export default {
  // 混入适老化功能
  mixins: [careModeMixin],
  // 数据定义
  data() {
    // 返回数据对象
    return {
      // 设备ID
      deviceId: '',
      // 设备对象
      device: {
        // 设备ID
        deviceId: '',
        // 设备名称
        deviceName: '',
        // 设备类型
        deviceType: 'light',
        // 房间名称
        roomName: '',
        // 状态
        status: 0,
        // 在线状态
        online: 0,
        // 数值
        value: 0
      },
      // 当前模式
      currentMode: 'cool',
      // 模式列表
      modes: [
        // 制冷模式
        { name: '制冷', value: 'cool', icon: '/static/emojis/emoji_26_snowflake.png' },
        // 制热模式
        { name: '制热', value: 'heat', icon: '/static/emojis/emoji_27_fire.png' },
        // 除湿模式
        { name: '除湿', value: 'dry', icon: '/static/emojis/emoji_28_droplet.png' },
        // 送风模式
        { name: '送风', value: 'fan', icon: '/static/emojis/emoji_29_tornado.png' }
      ]
    }
  },
  // 页面加载时
  onLoad(options) {
    // 获取设备ID
    this.deviceId = options.id
    // 加载设备详情
    this.loadDeviceDetail()
  },
  // 方法定义
  methods: {
    // 加载设备详情
    loadDeviceDetail() {
      // 校验设备ID是否存在
      if (!this.deviceId) return
      // 调用API获取设备详情
      getDeviceDetail(this.deviceId).then(res => {
        // 判断响应是否成功
        if (res.code === 200 && res.data) {
          // 获取响应数据
          const data = res.data
          // 映射API数据到本地设备对象
          this.device = {
            // 映射设备ID
            deviceId: data.id,
            // 映射设备名称
            deviceName: data.deviceName,
            // 映射设备类型
            deviceType: data.deviceType,
            // 映射房间名称
            roomName: data.roomName,
            // 映射状态并转换
            status: data.status === '1' || data.status === 1 ? 1 : 0,
            // 映射在线状态并转换
            online: data.online === '1' || data.online === 1 ? 1 : 0,
            // 映射数值并转换
            value: parseInt(data.value) || 0
          }
        }
      }).catch(() => {
        // 显示获取失败提示
        uni.showToast({ title: '获取设备详情失败', icon: 'none' })
      })
    },
    // 切换设备开关状态
    async toggleDevice(e) {
      // 获取开关新状态
      const newStatus = e.detail.value ? 1 : 0
      // 开始try块
      try {
        // 调用API发送控制指令
        const res = await controlDevice(this.deviceId, { status: newStatus })
        // 判断响应是否成功
        if (res.code === 200) {
          // 更新本地状态
          this.device.status = newStatus
          // 显示操作成功提示
          uni.showToast({
            // 根据状态显示不同文字
            title: this.device.status ? '设备已开启' : '设备已关闭',
            // 无图标
            icon: 'none'
          })
        } else {
          // 显示控制失败提示
          uni.showToast({ title: res.msg || '控制失败', icon: 'none' })
        }
      } catch (err) {
        // 显示控制失败提示
        uni.showToast({ title: '控制失败', icon: 'none' })
      }
    },

    // 改变亮度值
    changeBrightness(e) {
      // 更新亮度值
      this.device.value = e.detail.value
    },

    // 改变温度值
    changeTemp(delta) {
      // 检查设备是否在线
      if (!this.device.online) {
        // 显示离线提示
        uni.showToast({ title: '设备离线', icon: 'none' })
        // 返回
        return
      }
      // 计算新温度
      const newTemp = this.device.value + delta
      // 校验温度范围(16-30°C)
      if (newTemp >= 16 && newTemp <= 30) {
        // 更新温度值
        this.device.value = newTemp
      }
    },

    // 选择运行模式
    selectMode(mode) {
      // 检查设备是否在线
      if (!this.device.online) {
        // 显示离线提示
        uni.showToast({ title: '设备离线', icon: 'none' })
        // 返回
        return
      }
      // 切换当前模式
      this.currentMode = mode
      // 显示切换成功提示
      uni.showToast({ title: '模式已切换', icon: 'none' })
    },

        // 根据设备类型获取图标
    getDeviceIcon(type) {
      // 定义图标映射表
      const icons = {
        // 灯光图标
        light: '/static/emojis/emoji_25_bulb.png',
        // 空调图标
        air_conditioner: '/static/emojis/emoji_26_snowflake.png',
        // 窗帘图标
        curtain: '/static/emojis/emoji_30_switch.png',
        // 开关图标
        switch: '/static/emojis/emoji_30_switch.png',
        // 门锁图标
        lock: '/static/emojis/emoji_31_lock.png',
        // 摄像头图标
        camera: '/static/emojis/emoji_32_camera.png'
      }
      // 返回对应图标或默认图标
      return icons[type] || '/static/emojis/emoji_33_pager.png'
    },

    // 根据设备类型获取中文名称
    getDeviceTypeName(type) {
      // 定义名称映射表
      const names = {
        // 灯光名称
        light: '智能灯光',
        // 空调名称
        air_conditioner: '空调',
        // 窗帘名称
        curtain: '智能窗帘',
        // 开关名称
        switch: '智能开关',
        // 门锁名称
        lock: '智能门锁',
        // 摄像头名称
        camera: '摄像头'
      }
      // 返回对应名称或默认名称
      return names[type] || '未知设备'
    },

  }
}
</script>

<style lang="scss">
// 页面根容器
.device-control-container {
  // 最小高度为视口高度
  min-height: 100vh;
  // 背景色
  background: #f5f5f5;
  // 底部内边距
  padding-bottom: 40rpx;
}

// 设备卡片
.device-card {
  // 弹性布局
  display: flex;
  // 垂直排列
  flex-direction: column;
  // 居中对齐
  align-items: center;
  // 白色背景
  background: #fff;
  // 内边距
  padding: 60rpx 40rpx;

  // 设备图标容器（圆形）
  .device-icon {
    // 宽度
    width: 160rpx;
    // 高度
    height: 160rpx;
    // 圆形
    border-radius: 50%;
    // 弹性布局
    display: flex;
    // 垂直居中
    align-items: center;
    // 水平居中
    justify-content: center;

    // 设备图标图片
    .device-icon-img {
      // 宽度
      width: 80rpx;
      // 高度
      height: 80rpx;
    }

    // 灯光设备背景色
    &.light { background: #FFF8E1; }
    // 空调设备背景色
    &.air_conditioner { background: #E3F2FD; }
    // 窗帘设备背景色
    &.curtain { background: #F3E5F5; }
  }

  // 设备名称
  .device-name {
    // 字体大小
    font-size: 40rpx;
    // 加粗
    font-weight: bold;
    // 字体颜色
    color: #333;
    // 底部外边距
    margin-bottom: 16rpx;
  }

  // 设备在线/离线状态标签
  .device-status {
    // 字体大小
    font-size: 28rpx;
    // 内边距
    padding: 8rpx 24rpx;
    // 圆角
    border-radius: 30rpx;

    // 在线状态样式
    &.online {
      // 背景色
      background: #E8F5E9;
      // 字体颜色
      color: #388E3C;
    }

    // 离线状态样式
    &.offline {
      // 背景色
      background: #f5f5f5;
      // 字体颜色
      color: #999;
    }
  }
}

// 控制区块
.control-section {
  // 白色背景
  background: #fff;
  // 外边距
  margin: 20rpx;
  // 圆角
  border-radius: 16rpx;
  // 内边距
  padding: 30rpx;

  // 区块标题
  .section-title {
    // 字体大小
    font-size: 30rpx;
    // 字体粗细
    font-weight: 500;
    // 字体颜色
    color: #333;
    // 底部外边距
    margin-bottom: 20rpx;
  }

  // 控制行
  .control-row {
    // 弹性布局
    display: flex;
    // 两端对齐
    justify-content: space-between;
    // 垂直居中
    align-items: center;

    // 控制标签
    .control-label {
      // 字体大小
      font-size: 30rpx;
      // 字体颜色
      color: #333;
    }

    // 控制值
    .control-value {
      // 字体大小
      font-size: 30rpx;
      // 字体颜色
      color: #007AFF;
    }

    // 温度加减按钮容器
    .temp-control {
      // 弹性布局
      display: flex;
      // 垂直居中
      align-items: center;

      // 温度按钮
      .temp-btn {
        // 宽度
        width: 60rpx;
        // 高度
        height: 60rpx;
        // 背景色
        background: #f5f5f5;
        // 圆形
        border-radius: 50%;
        // 弹性布局
        display: flex;
        // 垂直居中
        align-items: center;
        // 水平居中
        justify-content: center;
        // 字体大小
        font-size: 36rpx;
        // 字体颜色
        color: #333;
      }

      // 温度值
      .temp-value {
        // 字体大小
        font-size: 40rpx;
        // 加粗
        font-weight: bold;
        // 字体颜色
        color: #333;
        // 水平外边距
        margin: 0 40rpx;
      }
    }
  }
}

// 模式网格
.mode-grid {
  // 弹性布局
  display: flex;
  // 允许换行
  flex-wrap: wrap;

  // 模式项
  .mode-item {
    // 宽度25%
    width: 25%;
    // 弹性布局
    display: flex;
    // 垂直排列
    flex-direction: column;
    // 居中对齐
    align-items: center;
    // 垂直内边距
    padding: 20rpx 0;

    // 模式图标
    .mode-icon {
      // 宽度
      width: 48rpx;
      // 高度
      height: 48rpx;
      // 底部外边距
      margin-bottom: 12rpx;
    }

    // 模式名称
    .mode-name {
      // 字体大小
      font-size: 26rpx;
      // 字体颜色
      color: #666;
    }

    // 激活状态
    &.active {
      // 模式名称
      .mode-name {
        // 字体颜色
        color: #007AFF;
        // 字体粗细
        font-weight: 500;
      }
    }
  }
}

// 设备信息
.info-section {
  // 白色背景
  background: #fff;
  // 外边距
  margin: 20rpx;
  // 圆角
  border-radius: 16rpx;
  // 内边距
  padding: 30rpx;

  // 信息标题
  .info-title {
    // 字体大小
    font-size: 30rpx;
    // 字体粗细
    font-weight: 500;
    // 字体颜色
    color: #333;
    // 底部外边距
    margin-bottom: 20rpx;
  }

  // 信息项
  .info-item {
    // 弹性布局
    display: flex;
    // 垂直内边距
    padding: 20rpx 0;
    // 底部边框
    border-bottom: 1rpx solid #f0f0f0;

    // 最后一个无下边框
    &:last-child {
      // 无边框
      border-bottom: none;
    }

    // 标签
    .label {
      // 固定宽度
      width: 180rpx;
      // 字体大小
      font-size: 28rpx;
      // 字体颜色
      color: #999;
    }

    // 值
    .value {
      // 占据剩余空间
      flex: 1;
      // 字体大小
      font-size: 28rpx;
      // 字体颜色
      color: #333;
    }
  }
}
</style>
