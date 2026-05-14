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
  // methods对象包含页面所有的业务逻辑方法
  // 用于处理用户交互、API调用、数据处理等功能
  methods: {
    // 加载设备详情方法
    // 作用：通过API获取设备详细信息并更新页面数据
    loadDeviceDetail() {
      // 如果设备ID为空，则不执行后续操作，防止无效请求
      if (!this.deviceId) return
      // 调用API获取设备详情，传入设备ID作为参数
      // then方法处理请求成功后的回调函数
      getDeviceDetail(this.deviceId).then(res => {
        // 判断响应状态码是否为200表示成功，且返回数据存在
        if (res.code === 200 && res.data) {
          // 将返回的data数据暂存到本地变量，减少重复访问
          const data = res.data
          // 更新页面设备对象的数据，实现数据的双向绑定
          // 将API返回的设备信息映射到本地设备对象
          this.device = {
            // 设备唯一标识符
            deviceId: data.id,
            // 设备名称，用于界面显示
            deviceName: data.deviceName,
            // 设备类型，如light、air_conditioner等
            deviceType: data.deviceType,
            // 设备所在房间名称
            roomName: data.roomName,
            // 设备状态：1表示开启，0表示关闭
            // 兼容字符串'1'和数字1两种格式
            status: data.status === '1' || data.status === 1 ? 1 : 0,
            // 设备在线状态：1表示在线，0表示离线
            // 兼容字符串'1'和数字1两种格式
            online: data.online === '1' || data.online === 1 ? 1 : 0,
            // 设备当前值（如亮度百分比、温度值等）
            // parseInt将字符串转换为整数，|| 0处理非数字情况
            value: parseInt(data.value) || 0
          } // 结束设备对象赋值
        } // 结束数据验证判断
      }) // 结束then回调
      // catch方法处理请求失败或出现异常的情况
      .catch(() => {
        // 调用UniApp的showToast方法显示错误提示
        // icon设为'none'表示不显示图标，只显示文字
        uni.showToast({ title: '获取设备详情失败', icon: 'none' })
      }) // 结束catch回调
    }, // 结束loadDeviceDetail方法

    // 根据设备类型获取对应的图标路径
    // 参数type：设备类型字符串，如'light'、'air_conditioner'等
    // 返回值：设备类型对应的图标文件路径
    getDeviceIcon(type) {
      // 定义设备类型与图标路径的映射关系对象
      // key为设备类型，value为对应的图标资源路径
      const icons = {
        // 智能灯光设备对应的图标
        light: '/static/emojis/emoji_25_bulb.png',
        // 空调设备对应的图标（雪花图案）
        air_conditioner: '/static/emojis/emoji_26_snowflake.png',
        // 智能窗帘设备对应的图标
        curtain: '/static/emojis/emoji_30_switch.png',
        // 智能开关设备对应的图标
        switch: '/static/emojis/emoji_30_switch.png',
        // 智能门锁设备对应的图标
        lock: '/static/emojis/emoji_31_lock.png',
        // 摄像头设备对应的图标
        camera: '/static/emojis/emoji_32_camera.png'
      } // 结束图标映射对象
      // 根据传入的设备类型获取对应图标
      // 如果找不到对应类型则返回默认图标（emoji_33_pager.png）
      return icons[type] || '/static/emojis/emoji_33_pager.png'
    }, // 结束getDeviceIcon方法

    // 根据设备类型获取对应的中文名称
    // 参数type：设备类型字符串
    // 返回值：设备类型对应的中文名称
    getDeviceTypeName(type) {
      // 定义设备类型与中文名称的映射关系对象
      const names = {
        // 智能灯光的中文名称
        light: '智能灯光',
        // 空调的中文名称
        air_conditioner: '空调',
        // 智能窗帘的中文名称
        curtain: '智能窗帘',
        // 智能开关的中文名称
        switch: '智能开关',
        // 智能门锁的中文名称
        lock: '智能门锁',
        // 摄像头的中文名称
        camera: '摄像头'
      } // 结束名称映射对象
      // 根据传入的设备类型获取对应中文名称
      // 如果找不到对应类型则返回'未知设备'作为默认值
      return names[type] || '未知设备'
    }, // 结束getDeviceTypeName方法

    // 切换设备开关状态（异步方法）
    // 参数e：开关组件的事件对象，包含开关的新状态
    async toggleDevice(e) {
      // 从事件对象中获取开关的新状态值
      // e.detail.value为true表示开启，false表示关闭
      // 转换为1表示开启，0表示关闭的数值格式
      const newStatus = e.detail.value ? 1 : 0
      // 使用try-catch包裹异步操作，捕获可能的异常
      try {
        // 调用API发送控制指令，传入设备ID和新的状态值
        // await等待异步请求完成
        const res = await controlDevice(this.deviceId, { status: newStatus })
        // 判断响应状态码是否为200表示成功
        if (res.code === 200) {
          // 更新本地设备状态，实现UI的实时更新
          this.device.status = newStatus
          // 调用showToast显示操作成功的提示信息
          // 根据设备状态显示不同的提示文字
          uni.showToast({
            // 三元运算符：状态为1显示'设备已开启'，否则显示'设备已关闭'
            title: this.device.status ? '设备已开启' : '设备已关闭',
            // icon设为'none'表示不显示图标
            icon: 'none'
          }) // 结束showToast调用
        } else {
          // 请求成功但业务逻辑失败（如设备不在线）
          // 显示服务器返回的错误信息或默认提示
          uni.showToast({ title: res.msg || '控制失败', icon: 'none' })
        } // 结束业务逻辑判断
      } catch (err) {
        // 捕获网络异常或API调用失败的情况
        // 显示通用的错误提示信息
        uni.showToast({ title: '控制失败', icon: 'none' })
      } // 结束try-catch块
    }, // 结束toggleDevice方法

    // 改变设备亮度值
    // 参数e：滑块组件的事件对象，包含滑块的新值
    changeBrightness(e) {
      // 从事件对象中获取滑块的当前值
      // e.detail.value为0-100的数值，表示亮度百分比
      // 直接更新设备的value属性，实现数据的双向绑定
      this.device.value = e.detail.value
    }, // 结束changeBrightness方法

    // 改变空调温度值
    // 参数delta：温度变化量，正数表示升温，负数表示降温
    changeTemp(delta) {
      // 判断设备是否在线，离线状态下不能调节温度
      if (!this.device.online) {
        // 显示设备离线提示，icon设为'none'只显示文字
        uni.showToast({ title: '设备离线', icon: 'none' })
        // 提前返回，不执行后续的温度调节逻辑
        return
      } // 结束设备在线判断
      // 计算新的温度值：当前温度加上变化量
      // delta为正时升温，为负时降温
      const newTemp = this.device.value + delta
      // 判断新温度是否在允许的范围内（16°C到30°C）
      if (newTemp >= 16 && newTemp <= 30) {
        // 温度在有效范围内，更新设备温度值
        this.device.value = newTemp
      } // 结束温度范围判断
    }, // 结束changeTemp方法

    // 选择设备运行模式
    // 参数mode：选择的模式值，如'cool'、'heat'、'dry'、'fan'等
    selectMode(mode) {
      // 判断设备是否在线，离线状态下不能切换模式
      if (!this.device.online) {
        // 显示设备离线提示
        uni.showToast({ title: '设备离线', icon: 'none' })
        // 提前返回，不执行后续的模式切换逻辑
        return
      } // 结束设备在线判断
      // 将当前模式更新为传入的新模式
      this.currentMode = mode
      // 显示模式切换成功的提示信息
      uni.showToast({ title: '模式已切换', icon: 'none' })
    } // 结束selectMode方法
  } // 结束methods对象
}
</script>

<style lang="scss">
// 页面容器样式定义
// lang="scss" 表示使用 SCSS 预处理器，支持嵌套、变量等特性
// 设备控制页面的根容器
// 负责设置页面整体背景和布局
.device-control-container {
  // min-height: 100vh 确保容器至少占满整个视口高度
  // 防止内容不足时页面底部出现空白
  min-height: 100vh;
  // background: #f5f5f5 设置浅灰色背景，是移动端常见的页面背景色
  background: #f5f5f5;
  // padding-bottom: 40rpx 底部内边距，避免内容被底部导航栏遮挡
  padding-bottom: 40rpx;
} // 结束.device-control-container样式

// 设备卡片样式
// 用于展示设备信息的卡片容器
.device-card {
  // display: flex 将卡片设为弹性布局
  display: flex;
  // flex-direction: column 子元素垂直排列
  flex-direction: column;
  // align-items: center 子元素水平居中对齐
  align-items: center;
  // background: #fff 白色背景，形成卡片悬浮效果
  background: #fff;
  // padding: 60rpx 40rpx 上下60rpx、左右40rpx的内边距
  padding: 60rpx 40rpx;

  // 设备图标容器
  .device-icon {
    // width/height: 160rpx 设置图标容器为正方形
    width: 160rpx;
    height: 160rpx;
    // border-radius: 50% 将容器设为圆形
    border-radius: 50%;
    // display: flex 内部图标居中
    display: flex;
    // align-items: center 垂直居中
    align-items: center;
    // justify-content: center 水平居中
    justify-content: center;

    // 设备图标图片
    .device-icon-img {
      // width/height: 80rpx 图标尺寸为容器的一半
      width: 80rpx;
      height: 80rpx;
    } // 结束.device-icon-img样式

    // 不同设备类型的背景色（SCSS嵌套选择器）
    // &.light 表示.device-icon.light组合选择器
    &.light { background: #FFF8E1; } // 灯光设备：淡黄色背景
    &.air_conditioner { background: #E3F2FD; } // 空调设备：淡蓝色背景
    &.curtain { background: #F3E5F5; } // 窗帘设备：淡紫色背景
  } // 结束.device-icon样式

  // 设备名称样式
  .device-name {
    // font-size: 40rpx 较大字体便于老年人阅读
    font-size: 40rpx;
    // font-weight: bold 加粗突出显示
    font-weight: bold;
    // color: #333 深灰色文字
    color: #333;
    // margin-bottom: 16rpx 与下方状态标签保持间距
    margin-bottom: 16rpx;
  } // 结束.device-name样式

  // 设备状态标签样式
  .device-status {
    // font-size: 28rpx 适中字号
    font-size: 28rpx;
    // padding: 8rpx 24rpx 上下8rpx、左右24rpx的内边距
    padding: 8rpx 24rpx;
    // border-radius: 30rpx 圆角设计，使标签更圆润
    border-radius: 30rpx;

    // 在线状态样式
    &.online {
      // background: #E8F5E9 浅绿色背景表示正常状态
      background: #E8F5E9;
      // color: #388E3C 深绿色文字
      color: #388E3C;
    } // 结束.online状态样式

    // 离线状态样式
    &.offline {
      // background: #f5f5f5 灰色背景表示异常状态
      background: #f5f5f5;
      // color: #999 浅灰色文字
      color: #999;
    } // 结束.offline状态样式
  } // 结束.device-status样式
} // 结束.device-card样式

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
