<template>
  <view class="service-container">
    <!-- 服务分类 -->
    <view class="service-grid">
      <view 
        class="service-item" 
        v-for="(item, index) in services" 
        :key="index"
        @click="navigateTo(item.path)"
      >
        <view class="service-icon" :style="{ background: item.bgColor }">
          <text>{{ item.icon }}</text>
        </view>
        <text class="service-name">{{ item.name }}</text>
      </view>
    </view>

    <!-- 我的服务订单 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">我的服务</text>
        <text class="section-more" @click="navigateTo('/pages/service/list')">查看全部 ></text>
      </view>
      <view class="order-list">
        <view 
          class="order-item" 
          v-for="(order, index) in orders" 
          :key="index"
          @click="navigateTo(`/pages/service/detail?id=${order.orderId}`)"
        >
          <view class="order-header">
            <text class="order-type">{{ order.type }}</text>
            <text class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</text>
          </view>
          <view class="order-content">
            <text class="order-desc">{{ order.content }}</text>
          </view>
          <view class="order-footer">
            <text class="order-time">{{ order.createTime }}</text>
            <text class="order-no">{{ order.orderId }}</text>
          </view>
        </view>
        <view class="empty-tip" v-if="orders.length === 0">暂无服务订单</view>
      </view>
    </view>

    <!-- 常用电话 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">常用电话</text>
      </view>
      <view class="phone-list">
        <view class="phone-item" v-for="(item, index) in phones" :key="index" @click="makePhoneCall(item.phone)">
          <view class="phone-info">
            <text class="phone-name">{{ item.name }}</text>
            <text class="phone-number">{{ item.phone }}</text>
          </view>
          <text class="phone-icon">📞</text>
        </view>
        <view class="empty-tip" v-if="phones.length === 0">暂无常用电话</view>
      </view>
    </view>
  </view>
</template>

<script>
import { getOrderList, getPhoneList } from '@/api/service.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      services: [
        { name: '物业报修', icon: '🔧', bgColor: '#FF6B6B', path: '/pages/service/repair' },
        { name: '活动报名', icon: '🎉', bgColor: '#4ECDC4', path: '/pages/activity/index' },
        { name: '缴费服务', icon: '💰', bgColor: '#FFEAA7', path: '/pages/user/payment' },
        { name: '我的订单', icon: '📋', bgColor: '#45B7D1', path: '/pages/service/list' }
      ],
      orders: [],
      phones: []
    }
  },
  onLoad() {
    this.loadOrderList()
    this.loadPhoneList()
  },
  onShow() {
    this.loadOrderList()
    this.loadPhoneList()
  },
  methods: {
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        0: '待处理',
        1: '处理中',
        2: '已完成',
        3: '已取消'
      }
      return statusMap[status] || '未知'
    },
    // 加载服务订单列表
    async loadOrderList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getOrderList({
          pageNum: 1,
          pageSize: 5,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.orders = res.rows
        }
      } catch (e) {
        console.error('获取服务订单失败', e)
      }
    },
    // 加载常用电话列表
    async loadPhoneList() {
      try {
        const res = await getPhoneList({
          pageNum: 1,
          pageSize: 10
        })
        if (res.code === 200 && res.rows) {
          this.phones = res.rows
        }
      } catch (e) {
        console.error('获取常用电话失败', e)
      }
    },
    navigateTo(path) {
      if (!path) {
        uni.showToast({ title: '功能开发中', icon: 'none' })
        return
      }
      uni.navigateTo({ url: path })
    },
    makePhoneCall(phone) {
      uni.makePhoneCall({ phoneNumber: phone })
    }
  }
}
</script>

<style lang="scss">
.service-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 30rpx;
}

// 服务网格
.service-grid {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx;
  background: #fff;
  margin-bottom: 20rpx;

  .service-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;

    .service-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;

      text {
        font-size: 48rpx;
      }
    }

    .service-name {
      font-size: 26rpx;
      color: #333;
    }
  }
}

// 通用区块
.section {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 26rpx;
      color: #999;
    }
  }
}

// 订单列表
.order-list {
  .order-item {
    background: #f8f9fa;
    border-radius: 12rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;

    &:last-child {
      margin-bottom: 0;
    }

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;

      .order-type {
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
      }

      .order-status {
        font-size: 24rpx;
        padding: 6rpx 16rpx;
        border-radius: 8rpx;

        &.status-0 {
          background: #FFF3E0;
          color: #FF9800;
        }

        &.status-1 {
          background: #E3F2FD;
          color: #2196F3;
        }

        &.status-2 {
          background: #E8F5E9;
          color: #4CAF50;
        }

        &.status-3 {
          background: #F5F5F5;
          color: #999;
        }
      }
    }

    .order-content {
      margin-bottom: 16rpx;

      .order-desc {
        font-size: 28rpx;
        color: #666;
        line-height: 1.5;
      }
    }

    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .order-time {
        font-size: 24rpx;
        color: #999;
      }

      .order-no {
        font-size: 24rpx;
        color: #999;
      }
    }
  }

  .empty-tip {
    text-align: center;
    padding: 60rpx 0;
    color: #999;
    font-size: 28rpx;
  }
}

// 电话列表
.phone-list {
  .phone-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .phone-info {
      display: flex;
      flex-direction: column;

      .phone-name {
        font-size: 30rpx;
        color: #333;
        margin-bottom: 8rpx;
      }

      .phone-number {
        font-size: 26rpx;
        color: #007AFF;
      }
    }

    .phone-icon {
      font-size: 40rpx;
    }
  }

  .empty-tip {
    text-align: center;
    padding: 60rpx 0;
    color: #999;
    font-size: 28rpx;
  }
}
</style>
