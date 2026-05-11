<template>
  <view class="house-container">
    <!-- 顶部 -->
    <view class="header">
      <view class="header-back" @click="goBack">‹</view>
      <text class="header-title">我的房屋</text>
      <view class="header-room">A栋 101</view>
    </view>

    <!-- Tab切换 -->
    <view class="tab-bar">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'device' }"
        @click="currentTab = 'device'"
      >设备状态</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'sensor' }"
        @click="currentTab = 'sensor'"
      >传感器</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'history' }"
        @click="currentTab = 'history'"
      >历史数据</view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'forecast' }"
        @click="currentTab = 'forecast'"
      >数据预测</view>
    </view>

    <!-- 设备状态 -->
    <view class="content" v-if="currentTab === 'device'">
      <!-- 设备卡片 -->
      <view class="device-grid">
        <view class="device-card" v-for="(device, index) in devices" :key="index" :class="{ offline: !device.online }">
          <view class="device-icon" :style="{ background: device.bgColor }">
            <image :src="device.icon" class="icon-image" />
          </view>
          <view class="device-info">
            <text class="device-name">{{ device.name }}</text>
            <text class="device-room">{{ device.room }}</text>
            <view class="device-status">
              <view class="status-dot" :class="device.online ? 'online' : 'offline'"></view>
              <text>{{ device.online ? '在线' : '离线' }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 告警数据 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title"><image class="inline-icon" src="/static/emojis/emoji_39_warning.png" /> 告警记录</text>
        </view>
        <view class="alarm-list">
          <view class="alarm-item" v-for="(alarm, index) in alarmList" :key="index" @click="showAlarmDetail(alarm)">
            <view class="alarm-icon" :class="'level-' + alarm.level">
              <image :src="alarm.icon" class="icon-image" />
            </view>
            <view class="alarm-info">
              <text class="alarm-type">{{ alarm.type }}</text>
              <text class="alarm-time">{{ alarm.time }}</text>
            </view>
            <view class="alarm-arrow">></view>
          </view>
        </view>
      </view>
    </view>

    <!-- 传感器状态 -->
    <view class="content" v-if="currentTab === 'sensor'">
      <view class="sensor-grid">
        <view class="sensor-card" v-for="(sensor, index) in sensors" :key="index" :class="{ abnormal: sensor.status === '异常' }">
          <view class="sensor-header">
            <view class="sensor-icon" :style="{ background: sensor.bgColor }">
              <image :src="sensor.icon" class="icon-image" />
            </view>
            <view class="sensor-status-tag" :class="'status-' + sensor.status">{{ sensor.status }}</view>
          </view>
          <view class="sensor-name">{{ sensor.name }}</view>
          <view class="sensor-location">{{ sensor.location }}</view>
          <view class="sensor-data" v-if="sensor.value">
            <text class="data-value">{{ sensor.value }}</text>
            <text class="data-unit">{{ sensor.unit }}</text>
          </view>
          <view class="sensor-time">更新: {{ sensor.updateTime }}</view>
        </view>
      </view>

      <!-- 传感器说明 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title"><image class="inline-icon" src="/static/emojis/emoji_36_satellite.png" /> 传感器说明</text>
        </view>
        <view class="sensor-desc-list">
          <view class="sensor-desc-item">
            <image class="desc-icon" src="/static/emojis/emoji_36_satellite.png" />
            <view class="desc-info">
              <text class="desc-name">全屋毫米波雷达</text>
              <text class="desc-text">检测人体存在和活动状态，用于智能联动和跌倒检测</text>
            </view>
          </view>
          <view class="sensor-desc-item">
            <image class="desc-icon" src="/static/emojis/emoji_37_thermometer.png" />
            <view class="desc-info">
              <text class="desc-name">红外传感器</text>
              <text class="desc-text">检测人体红外信号，判断是否有人进入区域</text>
            </view>
          </view>
          <view class="sensor-desc-item">
            <image class="desc-icon" src="/static/emojis/emoji_02_person.png" />
            <view class="desc-info">
              <text class="desc-name">人体存在传感器</text>
              <text class="desc-text">高精度存在感知，判断是否有人持续存在</text>
            </view>
          </view>
          <view class="sensor-desc-item">
            <image class="desc-icon" src="/static/emojis/emoji_34_speaker.png" />
            <view class="desc-info">
              <text class="desc-name">声学异响传感器</text>
              <text class="desc-text">监测异常声响，如摔倒声、玻璃破碎声等</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 历史数据 -->
    <view class="content" v-if="currentTab === 'history'">
      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_09_water.png" /> 用水量 (m³)</text>
          <text class="chart-period">近7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars">
            <view class="bar-item" v-for="(item, index) in waterHistory" :key="index">
              <view class="bar" :style="{ height: item.value / maxWater * 100 + '%' }"></view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">本周用量</text>
            <text class="summary-value">{{ waterTotal }} m³</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">日均用量</text>
            <text class="summary-value">{{ waterAvg }} m³</text>
          </view>
        </view>
      </view>

      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_27_lightning.png" /> 用电量 (kWh)</text>
          <text class="chart-period">近7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars">
            <view class="bar-item" v-for="(item, index) in elecHistory" :key="index">
              <view class="bar elec" :style="{ height: item.value / maxElec * 100 + '%' }"></view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">本周用量</text>
            <text class="summary-value">{{ elecTotal }} kWh</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">日均用量</text>
            <text class="summary-value">{{ elecAvg }} kWh</text>
          </view>
        </view>
      </view>

      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_08_fire.png" /> 用气量 (m³)</text>
          <text class="chart-period">近7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars">
            <view class="bar-item" v-for="(item, index) in gasHistory" :key="index">
              <view class="bar gas" :style="{ height: item.value / maxGas * 100 + '%' }"></view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">本周用量</text>
            <text class="summary-value">{{ gasTotal }} m³</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">日均用量</text>
            <text class="summary-value">{{ gasAvg }} m³</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 数据预测 -->
    <view class="content" v-if="currentTab === 'forecast'">
      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_09_water.png" /> 用水量预测 (m³)</text>
          <text class="chart-period">未来7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars forecast">
            <view class="bar-item" v-for="(item, index) in waterForecast" :key="index">
              <view class="bar forecast-water" :style="{ height: item.value / maxWaterForecast * 100 + '%' }">
                <view class="forecast-flag">预</view>
              </view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">预测总用量</text>
            <text class="summary-value">{{ waterForecastTotal }} m³</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">建议</text>
            <text class="summary-tip">注意节约用水</text>
          </view>
        </view>
      </view>

      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_27_lightning.png" /> 用电量预测 (kWh)</text>
          <text class="chart-period">未来7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars forecast">
            <view class="bar-item" v-for="(item, index) in elecForecast" :key="index">
              <view class="bar forecast-elec" :style="{ height: item.value / maxElecForecast * 100 + '%' }">
                <view class="forecast-flag">预</view>
              </view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">预测总用量</text>
            <text class="summary-value">{{ elecForecastTotal }} kWh</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">预计电费</text>
            <text class="summary-tip">约 ¥{{ (elecForecastTotal * 0.5).toFixed(2) }}</text>
          </view>
        </view>
      </view>

      <view class="chart-section">
        <view class="chart-header">
          <text class="chart-title"><image class="inline-icon" src="/static/emojis/emoji_08_fire.png" /> 用气量预测 (m³)</text>
          <text class="chart-period">未来7天</text>
        </view>
        <view class="chart-box">
          <view class="chart-bars forecast">
            <view class="bar-item" v-for="(item, index) in gasForecast" :key="index">
              <view class="bar forecast-gas" :style="{ height: item.value / maxGasForecast * 100 + '%' }">
                <view class="forecast-flag">预</view>
              </view>
              <text class="bar-label">{{ item.day }}</text>
              <text class="bar-value">{{ item.value }}</text>
            </view>
          </view>
        </view>
        <view class="chart-summary">
          <view class="summary-item">
            <text class="summary-label">预测总用量</text>
            <text class="summary-value">{{ gasForecastTotal }} m³</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">安全提示</text>
            <text class="summary-tip warn">注意用气安全</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 告警详情弹窗 -->
    <view class="modal-mask" v-if="showAlarmModal" @click="showAlarmModal = false">
      <view class="modal-box" @click.stop>
        <view class="modal-header">
          <text class="modal-title">告警详情</text>
          <text class="modal-close" @click="showAlarmModal = false">×</text>
        </view>
        <view class="modal-body" v-if="currentAlarm">
          <view class="modal-row">
            <text class="modal-label">告警类型</text>
            <text class="modal-value">{{ currentAlarm.type }}</text>
          </view>
          <view class="modal-row">
            <text class="modal-label">发生时间</text>
            <text class="modal-value">{{ currentAlarm.time }}</text>
          </view>
          <view class="modal-row">
            <text class="modal-label">发生位置</text>
            <text class="modal-value">{{ currentAlarm.location }}</text>
          </view>
          <view class="modal-row">
            <text class="modal-label">告警等级</text>
            <text class="modal-value">
              <span class="level-tag" :class="'level-' + currentAlarm.level">
                {{ currentAlarm.level === 1 ? '高' : currentAlarm.level === 2 ? '中' : '低' }}
              </span>
            </text>
          </view>
          <view class="modal-row">
            <text class="modal-label">处理状态</text>
            <text class="modal-value">{{ currentAlarm.status }}</text>
          </view>
          <view class="modal-desc">
            <text class="desc-label">详细描述</text>
            <text class="desc-text">{{ currentAlarm.description }}</text>
          </view>
        </view>
        <view class="modal-footer">
          <view class="modal-btn" @click="showAlarmModal = false">关闭</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'device',
      showAlarmModal: false,
      currentAlarm: null,
      
      devices: [
        { name: '小艺音箱', icon: '/static/emojis/emoji_34_speaker.png', room: '客厅', online: true, bgColor: '#E8F5E9' },
        { name: '小艺音箱', icon: '/static/emojis/emoji_34_speaker.png', room: '卧室', online: true, bgColor: '#E8F5E9' },
        { name: '智能灯', icon: '/static/emojis/emoji_25_bulb.png', room: '客厅', online: true, bgColor: '#FFF8E1' },
        { name: '智能灯', icon: '/static/emojis/emoji_25_bulb.png', room: '卧室', online: true, bgColor: '#FFF8E1' },
        { name: '智能灯', icon: '/static/emojis/emoji_25_bulb.png', room: '厨房', online: false, bgColor: '#FFF8E1' },
        { name: '智能窗帘', icon: '/static/emojis/emoji_30_switch.png', room: '客厅', online: true, bgColor: '#E3F2FD' },
        { name: '智能窗帘', icon: '/static/emojis/emoji_30_switch.png', room: '卧室', online: true, bgColor: '#E3F2FD' },
        { name: '智能插座', icon: '/static/emojis/emoji_35_plug.png', room: '客厅', online: true, bgColor: '#FCE4EC' },
        { name: '智能插座', icon: '/static/emojis/emoji_35_plug.png', room: '厨房', online: true, bgColor: '#FCE4EC' },
        { name: '智能插座', icon: '/static/emojis/emoji_35_plug.png', room: '书房', online: false, bgColor: '#FCE4EC' }
      ],
      
      sensors: [
        { name: '全屋毫米波雷达', icon: '/static/emojis/emoji_36_satellite.png', location: '全屋覆盖', status: '正常', value: '3人', unit: '检测中', updateTime: '刚刚', bgColor: '#E3F2FD' },
        { name: '红外传感器', icon: '/static/emojis/emoji_37_thermometer.png', location: '门口', status: '正常', value: '未检测', unit: '', updateTime: '刚刚', bgColor: '#FFEBEE' },
        { name: '人体存在传感器', icon: '/static/emojis/emoji_02_person.png', location: '客厅', status: '正常', value: '有人', unit: '', updateTime: '刚刚', bgColor: '#F3E5F5' },
        { name: '声学异响传感器', icon: '/static/emojis/emoji_34_speaker.png', location: '客厅', status: '正常', value: '安静', unit: '', updateTime: '刚刚', bgColor: '#E0F7FA' },
        { name: '全屋毫米波雷达', icon: '/static/emojis/emoji_36_satellite.png', location: '卧室', status: '正常', value: '1人', unit: '检测中', updateTime: '刚刚', bgColor: '#E3F2FD' },
        { name: '红外传感器', icon: '/static/emojis/emoji_37_thermometer.png', location: '阳台', status: '异常', value: '-', unit: '', updateTime: '5分钟前', bgColor: '#FFEBEE' }
      ],
      
      alarmList: [
        { type: '人员跌倒事件', icon: '/static/emojis/emoji_08_standing.png', time: '2026-04-28 14:30', location: '客厅', level: 1, status: '处理中', description: '检测到客厅区域有人员跌倒动作，已通知家人和物业' },
        { type: '燃气长时间未关', icon: '/static/emojis/emoji_27_fire.png', time: '2026-04-27 20:15', location: '厨房', level: 1, status: '已处理', description: '厨房燃气使用超过2小时未关闭，已自动关闭并通知' },
        { type: '用水量异常', icon: '/static/emojis/emoji_28_droplet.png', time: '2026-04-26 03:20', location: '卫生间', level: 2, status: '已处理', description: '检测到深夜时段用水量异常，疑似漏水' },
        { type: '人员跌倒事件', icon: '/static/emojis/emoji_08_standing.png', time: '2026-04-25 16:45', location: '卧室', level: 1, status: '已处理', description: '检测到卧室区域有人员跌倒动作' },
        { type: '声学异响', icon: '/static/emojis/emoji_34_speaker.png', time: '2026-04-24 22:10', location: '厨房', level: 2, status: '已处理', description: '检测到厨房区域有玻璃破碎声' }
      ],
      
      waterHistory: [
        { day: '周一', value: 3.2 },
        { day: '周二', value: 2.8 },
        { day: '周三', value: 4.1 },
        { day: '周四', value: 3.5 },
        { day: '周五', value: 2.9 },
        { day: '周六', value: 5.2 },
        { day: '周日', value: 4.8 }
      ],
      elecHistory: [
        { day: '周一', value: 12.5 },
        { day: '周二', value: 15.2 },
        { day: '周三', value: 11.8 },
        { day: '周四', value: 14.6 },
        { day: '周五', value: 13.2 },
        { day: '周六', value: 18.5 },
        { day: '周日', value: 16.8 }
      ],
      gasHistory: [
        { day: '周一', value: 1.2 },
        { day: '周二', value: 0.8 },
        { day: '周三', value: 1.5 },
        { day: '周四', value: 1.1 },
        { day: '周五', value: 1.8 },
        { day: '周六', value: 2.2 },
        { day: '周日', value: 1.9 }
      ],
      
      waterForecast: [
        { day: '周一', value: 3.5 },
        { day: '周二', value: 3.0 },
        { day: '周三', value: 4.2 },
        { day: '周四', value: 3.8 },
        { day: '周五', value: 3.2 },
        { day: '周六', value: 5.5 },
        { day: '周日', value: 5.0 }
      ],
      elecForecast: [
        { day: '周一', value: 13.5 },
        { day: '周二', value: 16.0 },
        { day: '周三', value: 12.5 },
        { day: '周四', value: 15.2 },
        { day: '周五', value: 14.0 },
        { day: '周六', value: 19.5 },
        { day: '周日', value: 17.8 }
      ],
      gasForecast: [
        { day: '周一', value: 1.3 },
        { day: '周二', value: 0.9 },
        { day: '周三', value: 1.6 },
        { day: '周四', value: 1.2 },
        { day: '周五', value: 2.0 },
        { day: '周六', value: 2.4 },
        { day: '周日', value: 2.1 }
      ]
    }
  },
  computed: {
    maxWater() { return Math.max(...this.waterHistory.map(i => i.value)) },
    maxElec() { return Math.max(...this.elecHistory.map(i => i.value)) },
    maxGas() { return Math.max(...this.gasHistory.map(i => i.value)) },
    maxWaterForecast() { return Math.max(...this.waterForecast.map(i => i.value)) },
    maxElecForecast() { return Math.max(...this.elecForecast.map(i => i.value)) },
    maxGasForecast() { return Math.max(...this.gasForecast.map(i => i.value)) },
    waterTotal() { return this.waterHistory.reduce((sum, i) => sum + i.value, 0).toFixed(1) },
    waterAvg() { return (this.waterTotal / this.waterHistory.length).toFixed(1) },
    elecTotal() { return this.elecHistory.reduce((sum, i) => sum + i.value, 0).toFixed(1) },
    elecAvg() { return (this.elecTotal / this.elecHistory.length).toFixed(1) },
    gasTotal() { return this.gasHistory.reduce((sum, i) => sum + i.value, 0).toFixed(1) },
    gasAvg() { return (this.gasTotal / this.gasHistory.length).toFixed(1) },
    waterForecastTotal() { return this.waterForecast.reduce((sum, i) => sum + i.value, 0).toFixed(1) },
    elecForecastTotal() { return this.elecForecast.reduce((sum, i) => sum + i.value, 0).toFixed(1) },
    gasForecastTotal() { return this.gasForecast.reduce((sum, i) => sum + i.value, 0).toFixed(1) }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    showAlarmDetail(alarm) {
      this.currentAlarm = alarm
      this.showAlarmModal = true
    }
  }
}
</script>

<style lang="scss">
.house-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;

  .inline-icon {
    width: 28rpx;
    height: 28rpx;
    vertical-align: middle;
  }
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 60rpx 30rpx 30rpx;
  background: linear-gradient(135deg, #007AFF 0%, #00C6FF 100%);
  color: #fff;

  .header-back {
    font-size: 48rpx;
    font-weight: bold;
  }

  .header-title {
    font-size: 34rpx;
    font-weight: 600;
  }

  .header-room {
    font-size: 26rpx;
    background: rgba(255,255,255,0.2);
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
  }
}

.tab-bar {
  display: flex;
  background: #fff;
  padding: 0 20rpx;
  position: sticky;
  top: 0;
  z-index: 99;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 24rpx 0;
    font-size: 28rpx;
    color: #666;
    position: relative;
    transition: all 0.3s;

    &.active {
      color: #007AFF;
      font-weight: 600;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 60rpx;
        height: 4rpx;
        background: #007AFF;
        border-radius: 2rpx;
      }
    }
  }
}

.content {
  padding: 20rpx;
}

.device-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.device-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;

  &.offline {
    opacity: 0.6;

    .device-icon {
      filter: grayscale(100%);
    }
  }

  .device-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16rpx;

    .icon-image {
      width: 40rpx;
      height: 40rpx;
    }
  }

  .device-info {
    flex: 1;

    .device-name {
      font-size: 28rpx;
      font-weight: 600;
      color: #333;
      display: block;
    }

    .device-room {
      font-size: 22rpx;
      color: #999;
      display: block;
      margin-top: 4rpx;
    }

    .device-status {
      display: flex;
      align-items: center;
      margin-top: 8rpx;

      .status-dot {
        width: 8rpx;
        height: 8rpx;
        border-radius: 50%;
        margin-right: 8rpx;

        &.online { background: #52c41a; }
        &.offline { background: #999; }
      }

      text {
        font-size: 22rpx;
        color: #666;
      }
    }
  }
}

.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;

  .section-header {
    margin-bottom: 20rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: 600;
      color: #333;
    }
  }
}

.alarm-list {
  .alarm-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .alarm-icon {
      width: 70rpx;
      height: 70rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16rpx;

      .icon-image {
        width: 36rpx;
        height: 36rpx;
      }

      &.level-1 { background: #FFEBEE; }
      &.level-2 { background: #FFF3E0; }
    }

    .alarm-info {
      flex: 1;

      .alarm-type {
        font-size: 28rpx;
        color: #333;
        display: block;
        font-weight: 500;
      }

      .alarm-time {
        font-size: 22rpx;
        color: #999;
        display: block;
        margin-top: 4rpx;
      }
    }

    .alarm-arrow {
      font-size: 28rpx;
      color: #ccc;
    }
  }
}

.sensor-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.sensor-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;

  &.abnormal {
    background: #FFF5F5;
    border: 1rpx solid #FFCDD2;
  }

  .sensor-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 12rpx;

    .sensor-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .icon-image {
        width: 32rpx;
        height: 32rpx;
      }
    }

    .sensor-status-tag {
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 8rpx;

      &.status-正常 { background: #E8F5E9; color: #52c41a; }
      &.status-异常 { background: #FFEBEE; color: #f5222d; }
    }
  }

  .sensor-name {
    font-size: 26rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 4rpx;
  }

  .sensor-location {
    font-size: 22rpx;
    color: #999;
    margin-bottom: 8rpx;
  }

  .sensor-data {
    .data-value {
      font-size: 32rpx;
      font-weight: 700;
      color: #007AFF;
    }

    .data-unit {
      font-size: 22rpx;
      color: #999;
      margin-left: 4rpx;
    }
  }

  .sensor-time {
    font-size: 20rpx;
    color: #bbb;
    margin-top: 8rpx;
  }
}

.sensor-desc-list {
  .sensor-desc-item {
    display: flex;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .desc-icon {
      width: 36rpx;
      height: 36rpx;
      margin-right: 16rpx;
    }

    .desc-info {
      flex: 1;

      .desc-name {
        font-size: 26rpx;
        font-weight: 500;
        color: #333;
        display: block;
      }

      .desc-text {
        font-size: 22rpx;
        color: #999;
        display: block;
        margin-top: 4rpx;
        line-height: 1.4;
      }
    }
  }
}

.chart-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .chart-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #333;
    }

    .inline-icon {
      width: 28rpx;
      height: 28rpx;
      vertical-align: middle;
      margin-right: 4rpx;
    }

    .chart-period {
      font-size: 22rpx;
      color: #999;
    }
  }

  .chart-box {
    height: 240rpx;
    margin-bottom: 16rpx;
  }

  .chart-bars {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    height: 200rpx;
    padding-top: 20rpx;

    .bar-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 100%;

      .bar {
        width: 36rpx;
        background: linear-gradient(180deg, #007AFF 0%, #00C6FF 100%);
        border-radius: 6rpx 6rpx 0 0;
        margin-top: auto;
        position: relative;
        min-height: 10rpx;
        transition: height 0.3s;

        &.elec {
          background: linear-gradient(180deg, #FFD93D 0%, #FF9F43 100%);
        }

        &.gas {
          background: linear-gradient(180deg, #FF6B6B 0%, #EE5A24 100%);
        }

        &.forecast-water {
          background: linear-gradient(180deg, #007AFF 0%, #00C6FF 100%);
          opacity: 0.7;
        }

        &.forecast-elec {
          background: linear-gradient(180deg, #FFD93D 0%, #FF9F43 100%);
          opacity: 0.7;
        }

        &.forecast-gas {
          background: linear-gradient(180deg, #FF6B6B 0%, #EE5A24 100%);
          opacity: 0.7;
        }

        .forecast-flag {
          position: absolute;
          top: -30rpx;
          left: 50%;
          transform: translateX(-50%);
          font-size: 18rpx;
          color: #999;
        }
      }

      .bar-label {
        font-size: 20rpx;
        color: #999;
        margin-top: 8rpx;
      }

      .bar-value {
        font-size: 18rpx;
        color: #666;
        margin-top: 2rpx;
      }
    }
  }

  .chart-summary {
    display: flex;
    justify-content: space-around;
    padding-top: 16rpx;
    border-top: 1rpx solid #f0f0f0;

    .summary-item {
      text-align: center;

      .summary-label {
        font-size: 22rpx;
        color: #999;
        display: block;
      }

      .summary-value {
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
        display: block;
        margin-top: 4rpx;
      }

      .summary-tip {
        font-size: 22rpx;
        color: #52c41a;
        display: block;
        margin-top: 4rpx;

        &.warn { color: #ff6b6b; }
      }
    }
  }
}

.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-box {
  width: 600rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;

  .modal-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
  }

  .modal-close {
    font-size: 48rpx;
    color: #999;
    line-height: 1;
  }
}

.modal-body {
  padding: 30rpx;

  .modal-row {
    display: flex;
    padding: 16rpx 0;
    border-bottom: 1rpx solid #f5f5f5;

    .modal-label {
      width: 160rpx;
      font-size: 26rpx;
      color: #999;
    }

    .modal-value {
      flex: 1;
      font-size: 26rpx;
      color: #333;

      .level-tag {
        padding: 4rpx 16rpx;
        border-radius: 8rpx;
        font-size: 22rpx;

        &.level-1 { background: #FFEBEE; color: #f5222d; }
        &.level-2 { background: #FFF3E0; color: #fa8c16; }
        &.level-3 { background: #E8F5E9; color: #52c41a; }
      }
    }
  }

  .modal-desc {
    padding: 16rpx 0;

    .desc-label {
      font-size: 26rpx;
      color: #999;
      display: block;
      margin-bottom: 8rpx;
    }

    .desc-text {
      font-size: 26rpx;
      color: #333;
      line-height: 1.6;
    }
  }
}

.modal-footer {
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #f0f0f0;

  .modal-btn {
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    background: #f5f5f5;
    border-radius: 16rpx;
    font-size: 28rpx;
    color: #666;
  }
}
</style>