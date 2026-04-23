<template>
  <view class="service-list-container">
    <!-- 状态筛选 -->
    <view class="filter-tabs">
      <view 
        class="tab-item" 
        v-for="(tab, index) in tabs" 
        :key="index"
        :class="{ active: currentTab === index }"
        @click="selectTab(index)"
      >
        {{ tab.name }}
      </view>
    </view>

    <!-- 订单列表 -->
    <view class="order-list">
      <view 
        class="order-item" 
        v-for="(order, index) in filteredOrders" 
        :key="index"
        @click="navigateToDetail(order)"
      >
        <view class="order-header">
          <text class="order-type">{{ order.type }}</text>
          <text class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</text>
        </view>
        <view class="order-content">
          <text class="order-desc">{{ order.content }}</text>
        </view>
        <view class="order-footer">
          <text class="order-no">{{ order.orderId }}</text>
          <text class="order-time">{{ order.createTime }}</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="filteredOrders.length === 0">
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无订单</text>
    </view>
  </view>
</template>

<script>
import { getOrderList } from '@/api/service.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      currentTab: 0,
      tabs: [
        { name: '全部', value: -1 },
        { name: '待处理', value: 0 },
        { name: '处理中', value: 1 },
        { name: '已完成', value: 2 }
      ],
      orders: []
    }
  },
  computed: {
    filteredOrders() {
      if (this.currentTab === 0) return this.orders
      const status = this.tabs[this.currentTab].value
      return this.orders.filter(order => order.status === status)
    }
  },
  onLoad() {
    this.loadOrderList()
  },
  onShow() {
    this.loadOrderList()
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
    // 加载订单列表
    async loadOrderList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getOrderList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.orders = res.rows
        }
      } catch (e) {
        console.error('获取服务订单失败', e)
      }
    },
    selectTab(index) {
      this.currentTab = index
    },
    navigateToDetail(order) {
      uni.navigateTo({
        url: `/pages/service/detail?id=${order.orderId}`
      })
    }
  }
}
</script>

<style lang="scss">
.service-list-container {
  min-height: 100vh;
  background: #f5f5f5;
}

// 筛选标签
.filter-tabs {
  display: flex;
  background: #fff;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    font-size: 28rpx;
    color: #666;
    position: relative;

    &.active {
      color: #007AFF;
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 40rpx;
        height: 4rpx;
        background: #007AFF;
        border-radius: 2rpx;
      }
    }
  }
}

// 订单列表
.order-list {
  padding: 20rpx;

  .order-item {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .order-type {
        font-size: 32rpx;
        font-weight: 500;
        color: #333;
      }

      .order-status {
        font-size: 26rpx;
        padding: 6rpx 16rpx;
        border-radius: 8rpx;

        &.status-0 {
          background: #FFF3E0;
          color: #F57C00;
        }

        &.status-1 {
          background: #E3F2FD;
          color: #1976D2;
        }

        &.status-2 {
          background: #E8F5E9;
          color: #388E3C;
        }
      }
    }

    .order-content {
      margin-bottom: 20rpx;

      .order-desc {
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
      }
    }

    .order-footer {
      display: flex;
      justify-content: space-between;

      .order-no, .order-time {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}

// 空状态
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;

  .empty-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
  }

  .empty-text {
    font-size: 30rpx;
    color: #999;
  }
}
</style>
