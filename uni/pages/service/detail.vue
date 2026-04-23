<template>
  <view class="service-detail-container">
    <!-- 状态卡片 -->
    <view class="status-card">
      <view class="status-icon" :class="'status-' + order.status">📝</view>
      <text class="status-title">{{ getStatusText(order.status) }}</text>
      <text class="order-no">订单号: {{ order.orderId }}</text>
    </view>

    <!-- 服务信息 -->
    <view class="detail-section">
      <view class="section-title">服务信息</view>
      <view class="detail-item">
        <text class="label">服务类型</text>
        <text class="value">{{ order.type }}</text>
      </view>
      <view class="detail-item">
        <text class="label">报修内容</text>
        <text class="value">{{ order.content }}</text>
      </view>
      <view class="detail-item">
        <text class="label">报修地址</text>
        <text class="value">{{ order.address }}</text>
      </view>
      <view class="detail-item">
        <text class="label">联系电话</text>
        <text class="value">{{ order.contactPhone }}</text>
      </view>
      <view class="detail-item" v-if="order.contactName">
        <text class="label">联系人</text>
        <text class="value">{{ order.contactName }}</text>
      </view>
      <view class="detail-item" v-if="order.appointmentTime">
        <text class="label">预约时间</text>
        <text class="value">{{ order.appointmentTime }}</text>
      </view>
    </view>

    <!-- 处理进度 -->
    <view class="detail-section">
      <view class="section-title">处理进度</view>
      <view class="timeline">
        <view class="timeline-item" :class="{ active: true }">
          <view class="timeline-dot"></view>
          <view class="timeline-content">
            <text class="timeline-title">提交报修</text>
            <text class="timeline-time">{{ order.createTime }}</text>
          </view>
        </view>
        <view class="timeline-item" :class="{ active: order.status >= 1 }">
          <view class="timeline-dot"></view>
          <view class="timeline-content">
            <text class="timeline-title">物业受理</text>
            <text class="timeline-time">{{ order.status >= 1 ? order.updateTime : '' }}</text>
          </view>
        </view>
        <view class="timeline-item" :class="{ active: order.status >= 2 }">
          <view class="timeline-dot"></view>
          <view class="timeline-content">
            <text class="timeline-title">处理完成</text>
            <text class="timeline-time">{{ order.status >= 2 ? order.updateTime : '' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 处理结果 -->
    <view class="detail-section" v-if="order.status == 2 && order.handleResult">
      <view class="section-title">处理结果</view>
      <view class="result-content">
        <text>{{ order.handleResult }}</text>
      </view>
    </view>

    <!-- 处理人员 -->
    <view class="detail-section" v-if="order.handlerName">
      <view class="section-title">处理人员</view>
      <view class="detail-item">
        <text class="label">处理人</text>
        <text class="value">{{ order.handlerName }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getOrderDetail } from '@/api/service.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      orderId: null,
      order: {
        orderId: '',
        type: '',
        content: '',
        address: '',
        contactPhone: '',
        contactName: '',
        appointmentTime: '',
        status: 0,
        createTime: '',
        updateTime: '',
        handleResult: '',
        handlerName: ''
      }
    }
  },
  onLoad(options) {
    this.orderId = options.id
    this.loadOrderDetail()
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        0: '待处理',
        1: '处理中',
        2: '已完成',
        3: '已取消'
      }
      return statusMap[status] || '未知'
    },
    async loadOrderDetail() {
      try {
        const res = await getOrderDetail(this.orderId)
        if (res.code === 200 && res.data) {
          this.order = res.data
        }
      } catch (e) {
        console.error('获取订单详情失败', e)
      }
    }
  }
}
</script>

<style lang="scss">
.service-detail-container {
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
  background: #fff;
  margin-bottom: 20rpx;

  .status-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60rpx;
    margin-bottom: 20rpx;

    &.status-0 {
      background: #FFF3E0;
    }

    &.status-1 {
      background: #E3F2FD;
    }

    &.status-2 {
      background: #E8F5E9;
    }
  }

  .status-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 12rpx;
  }

  .order-no {
    font-size: 26rpx;
    color: #999;
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

// 时间线
.timeline {
  padding: 20rpx 0;

  .timeline-item {
    display: flex;
    padding: 20rpx 0;
    position: relative;
    opacity: 0.5;

    &.active {
      opacity: 1;
    }

    &::before {
      content: '';
      position: absolute;
      left: 15rpx;
      top: 50rpx;
      width: 2rpx;
      height: calc(100% - 20rpx);
      background: #e0e0e0;
    }

    &:last-child::before {
      display: none;
    }

    .timeline-dot {
      width: 32rpx;
      height: 32rpx;
      border-radius: 50%;
      background: #007AFF;
      margin-right: 20rpx;
      flex-shrink: 0;
    }

    .timeline-content {
      flex: 1;

      .timeline-title {
        font-size: 30rpx;
        color: #333;
        display: block;
        margin-bottom: 8rpx;
      }

      .timeline-time {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}

// 处理结果
.result-content {
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}
</style>
