<template>
  <view class="warning-detail-container">
    <!-- 预警状态卡片 -->
    <view class="status-card" :class="'level-' + warning.level">
      <view class="status-icon">⚠️</view>
      <text class="status-title">{{ warning.levelText }}预警</text>
    </view>

    <!-- 预警详情 -->
    <view class="detail-section">
      <view class="detail-item">
        <text class="label">预警标题</text>
        <text class="value">{{ warning.title }}</text>
      </view>
      <view class="detail-item">
        <text class="label">预警类型</text>
        <text class="value">{{ warning.type }}</text>
      </view>
      <view class="detail-item">
        <text class="label">预警内容</text>
        <text class="value">{{ warning.content }}</text>
      </view>
      <view class="detail-item">
        <text class="label">发生位置</text>
        <text class="value">{{ warning.location }}</text>
      </view>
      <view class="detail-item">
        <text class="label">设备名称</text>
        <text class="value">{{ warning.deviceName }}</text>
      </view>
      <view class="detail-item">
        <text class="label">发生时间</text>
        <text class="value">{{ warning.createTime }}</text>
      </view>
    </view>

    <!-- 处理信息 -->
    <view class="detail-section" v-if="warning.status !== 0">
      <view class="section-title">处理信息</view>
      <view class="detail-item">
        <text class="label">处理时间</text>
        <text class="value">{{ warning.handleTime }}</text>
      </view>
      <view class="detail-item">
        <text class="label">处理人</text>
        <text class="value">{{ warning.handler }}</text>
      </view>
      <view class="detail-item">
        <text class="label">处理结果</text>
        <text class="value">{{ warning.handleResult }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="warning.status === 0">
      <button class="confirm-btn" @click="confirmWarning">确认预警</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      warning: {
        warningId: 'W001',
        title: '烟雾报警器触发',
        level: 1,
        levelText: '紧急',
        type: '火灾',
        content: '检测到烟雾浓度超标，请及时查看处理',
        location: '1栋1单元101客厅',
        deviceName: '烟雾传感器',
        status: 0,
        statusText: '未处理',
        createTime: '2024-01-15 14:30:00',
        handleTime: '',
        handler: '',
        handleResult: ''
      }
    }
  },
  onLoad(options) {
    // 根据id加载详情
    console.log('预警ID:', options.id)
  },
  methods: {
    confirmWarning() {
      uni.showModal({
        title: '确认预警',
        content: '确认已收到此预警信息？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '确认成功', icon: 'success' })
            this.warning.status = 1
            this.warning.statusText = '已确认'
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.warning-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

// 状态卡片
.status-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 40rpx;
  margin-bottom: 20rpx;

  &.level-1 {
    background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
  }

  &.level-2 {
    background: linear-gradient(135deg, #FFA502 0%, #FFC107 100%);
  }

  &.level-3 {
    background: linear-gradient(135deg, #2ED573 0%, #7BED9F 100%);
  }

  .status-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
  }

  .status-title {
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 12rpx;
  }

  .status-text {
    font-size: 28rpx;
    color: rgba(255,255,255,0.9);
  }
}

// 详情区块
.detail-section {
  background: #fff;
  margin-bottom: 20rpx;
  padding: 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }

  .detail-item {
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
      line-height: 1.6;
    }
  }
}

// 操作按钮
.action-section {
  padding: 40rpx 30rpx;

  .confirm-btn {
    width: 100%;
    height: 90rpx;
    line-height: 90rpx;
    background: #007AFF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 12rpx;
    border: none;

    &:active {
      opacity: 0.8;
    }
  }
}
</style>
