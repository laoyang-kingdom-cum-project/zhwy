<template>
  <view class="container">
    <!-- 统计卡片 -->
    <view class="stats-section">
      <view class="stats-card">
        <view class="stats-item">
          <text class="stats-num">{{totalCount}}</text>
          <text class="stats-label">全部订单</text>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <text class="stats-num pending">{{pendingCount}}</text>
          <text class="stats-label">待分配</text>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <text class="stats-num processing">{{processingCount}}</text>
          <text class="stats-label">处理中</text>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <text class="stats-num completed">{{completedCount}}</text>
          <text class="stats-label">已完成</text>
        </view>
      </view>
    </view>

    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{active: currentFilter === 'all'}" @click="setFilter('all')">
        <text>全部</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === 0}" @click="setFilter(0)">
        <text>待分配</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === 1}" @click="setFilter(1)">
        <text>处理中</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === 2}" @click="setFilter(2)">
        <text>已完成</text>
      </view>
    </view>

    <!-- 订单列表 -->
    <scroll-view class="order-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="order-card" v-for="(item, index) in orderList" :key="index" @click="goToDetail(item)">
        <view class="card-header">
          <view class="order-type">
            <view class="type-icon-box" :class="'type-' + getTypeClass(item.type)">
              <text class="type-icon">{{getTypeIcon(item.type)}}</text>
            </view>
            <view class="type-info">
              <text class="type-text">{{item.type}}</text>
              <text class="order-id">订单号: {{item.orderId}}</text>
            </view>
          </view>
          <view class="status-tag" :class="'status-' + item.status">
            {{getStatusText(item.status)}}
          </view>
        </view>
        <view class="card-body">
          <view class="content-box">
            <text class="content-label">服务内容</text>
            <text class="order-content">{{item.content}}</text>
          </view>
          <view class="info-grid">
            <view class="info-item">
              <text class="info-icon">👤</text>
              <view class="info-text">
                <text class="info-label">联系人</text>
                <text class="info-value">{{item.contactName}}</text>
              </view>
            </view>
            <view class="info-item">
              <text class="info-icon">📞</text>
              <view class="info-text">
                <text class="info-label">联系电话</text>
                <text class="info-value">{{item.contactPhone}}</text>
              </view>
            </view>
            <view class="info-item full-width">
              <text class="info-icon">📍</text>
              <view class="info-text">
                <text class="info-label">服务地址</text>
                <text class="info-value">{{item.address}}</text>
              </view>
            </view>
            <view class="info-item">
              <text class="info-icon">🕐</text>
              <view class="info-text">
                <text class="info-label">提交时间</text>
                <text class="info-value">{{item.createTime}}</text>
              </view>
            </view>
          </view>
        </view>
        <view class="card-footer" v-if="item.status !== 2">
          <view class="handler-info" v-if="item.handlerName">
            <text class="handler-label">处理人:</text>
            <text class="handler-name">{{item.handlerName}}</text>
          </view>
          <view class="action-btns">
            <button v-if="item.status === 0" class="btn btn-primary" @click.stop="assignOrder(item)">
              <text class="btn-icon">📋</text>
              <text>分配工单</text>
            </button>
            <button v-if="item.status === 1" class="btn btn-success" @click.stop="completeOrder(item)">
              <text class="btn-icon">✓</text>
              <text>标记完成</text>
            </button>
          </view>
        </view>
        <view class="completed-badge" v-else>
          <text class="badge-icon">✓</text>
          <text>已完成</text>
        </view>
      </view>

      <view class="empty-tip" v-if="orderList.length === 0 && !loading">
        <view class="empty-icon-box">
          <text class="empty-icon">📭</text>
        </view>
        <text class="empty-text">暂无服务订单</text>
        <text class="empty-subtext">暂无相关订单数据</text>
      </view>

      <view class="loading-more" v-if="loading">
        <view class="loading-spinner"></view>
        <text>加载中...</text>
      </view>

      <view class="no-more" v-if="!hasMore && orderList.length > 0">
        <text>没有更多了</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getOrderList, assignOrder, updateOrderStatus } from '@/api/service.js'

export default {
  data() {
    return {
      currentFilter: 'all',
      orderList: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      refreshing: false,
      hasMore: true,
      totalCount: 0,
      pendingCount: 0,
      processingCount: 0,
      completedCount: 0
    }
  },
  onLoad() {
    this.loadData()
    this.loadStats()
  },
  methods: {
    setFilter(filter) {
      this.currentFilter = filter
      this.pageNum = 1
      this.orderList = []
      this.hasMore = true
      this.loadData()
    },

    async loadStats() {
      try {
        const res = await getOrderList({ pageNum: 1, pageSize: 1 })
        if (res.code === 200) {
          this.totalCount = res.total || 0
        }
        const pendingRes = await getOrderList({ pageNum: 1, pageSize: 1, status: 0 })
        if (pendingRes.code === 200) {
          this.pendingCount = pendingRes.total || 0
        }
        const processingRes = await getOrderList({ pageNum: 1, pageSize: 1, status: 1 })
        if (processingRes.code === 200) {
          this.processingCount = processingRes.total || 0
        }
        const completedRes = await getOrderList({ pageNum: 1, pageSize: 1, status: 2 })
        if (completedRes.code === 200) {
          this.completedCount = completedRes.total || 0
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },

    async loadData() {
      if (this.loading) return
      this.loading = true

      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        if (this.currentFilter !== 'all') {
          params.status = this.currentFilter
        }

        const res = await getOrderList(params)
        if (res.code === 200) {
          const list = res.rows || []

          if (this.pageNum === 1) {
            this.orderList = list
          } else {
            this.orderList = this.orderList.concat(list)
          }

          this.total = res.total || 0
          this.hasMore = this.orderList.length < this.total
        }
      } catch (e) {
        console.error('加载订单列表失败', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
        this.refreshing = false
      }
    },

    onRefresh() {
      this.refreshing = true
      this.pageNum = 1
      this.hasMore = true
      this.loadData()
      this.loadStats()
    },

    loadMore() {
      if (!this.hasMore || this.loading) return
      this.pageNum++
      this.loadData()
    },

    getTypeIcon(type) {
      const map = {
        '保洁服务': '🧹',
        '维修服务': '🔧',
        '搬家服务': '🚚',
        '洗衣服务': '👕',
        '其他': '📋'
      }
      return map[type] || '📋'
    },

    getTypeClass(type) {
      const map = {
        '保洁服务': 'clean',
        '维修服务': 'repair',
        '搬家服务': 'move',
        '洗衣服务': 'wash',
        '其他': 'other'
      }
      return map[type] || 'other'
    },

    getStatusText(status) {
      const map = { 0: '待分配', 1: '处理中', 2: '已完成' }
      return map[status] || '待分配'
    },

    assignOrder(item) {
      uni.showActionSheet({
        itemList: ['分配给维修组A', '分配给维修组B', '分配给维修组C'],
        success: async (res) => {
          const handlers = ['维修组A', '维修组B', '维修组C']
          try {
            const updateData = {
              ...item,
              status: 1,
              handlerId: res.tapIndex + 1,
              handlerName: handlers[res.tapIndex]
            }
            const result = await assignOrder(updateData)
            if (result.code === 200) {
              uni.showToast({
                title: '分配成功',
                icon: 'success'
              })
              this.onRefresh()
            }
          } catch (e) {
            uni.showToast({
              title: '操作失败',
              icon: 'none'
            })
          }
        }
      })
    },

    completeOrder(item) {
      uni.showModal({
        title: '确认',
        content: '确定要标记该订单为已完成吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...item,
                status: 2
              }
              const result = await updateOrderStatus(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: '操作成功',
                  icon: 'success'
                })
                this.onRefresh()
              }
            } catch (e) {
              uni.showToast({
                title: '操作失败',
                icon: 'none'
              })
            }
          }
        }
      })
    },

    goToDetail(item) {
      uni.navigateTo({
        url: `/pages/service/detail?id=${item.orderId}`
      })
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 统计卡片 */
.stats-section {
  padding: 20rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-card {
  display: flex;
  background: rgba(255,255,255,0.95);
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(102, 126, 234, 0.2);
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-num {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 8rpx;
}

.stats-num.pending { color: #FF6B6B; }
.stats-num.processing { color: #FFA502; }
.stats-num.completed { color: #2ED573; }

.stats-label {
  font-size: 24rpx;
  color: #666;
}

.stats-divider {
  width: 2rpx;
  background: #e8e8e8;
  margin: 0 10rpx;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  background: #fff;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.filter-item {
  flex: 1;
  text-align: center;
  padding: 16rpx 0;
  margin: 0 10rpx;
  border-radius: 30rpx;
  background: #f5f7fa;
  transition: all 0.3s;
}

.filter-item text {
  font-size: 28rpx;
  color: #666;
}

.filter-item.active {
  background: #667eea;
  transform: scale(1.02);
}

.filter-item.active text {
  color: #fff;
}

/* 订单列表 */
.order-list {
  height: calc(100vh - 280rpx);
  padding: 20rpx;
}

.order-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.order-card:active {
  transform: scale(0.98);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.order-type {
  display: flex;
  align-items: center;
}

.type-icon-box {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.type-icon-box.type-clean { background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%); }
.type-icon-box.type-repair { background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%); }
.type-icon-box.type-move { background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%); }
.type-icon-box.type-wash { background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%); }
.type-icon-box.type-other { background: linear-gradient(135deg, #F5F5F5 0%, #E0E0E0 100%); }

.type-icon {
  font-size: 40rpx;
}

.type-info {
  display: flex;
  flex-direction: column;
}

.type-text {
  font-size: 32rpx;
  color: #333;
  font-weight: 600;
  margin-bottom: 6rpx;
}

.order-id {
  font-size: 24rpx;
  color: #999;
}

.status-tag {
  padding: 10rpx 24rpx;
  border-radius: 24rpx;
  font-size: 24rpx;
  font-weight: 500;
}

.status-0 {
  background: #FFE5E5;
  color: #FF6B6B;
}

.status-1 {
  background: #FFF4E5;
  color: #FFA502;
}

.status-2 {
  background: #E5F5E5;
  color: #2ED573;
}

/* 卡片内容 */
.card-body {
  padding: 24rpx 0;
  border-top: 1rpx solid #f5f7fa;
  border-bottom: 1rpx solid #f5f7fa;
}

.content-box {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.content-label {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.order-content {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  line-height: 1.5;
}

/* 信息网格 */
.info-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.info-item {
  display: flex;
  align-items: flex-start;
  width: calc(50% - 8rpx);
  background: #fafafa;
  border-radius: 12rpx;
  padding: 16rpx;
}

.info-item.full-width {
  width: 100%;
}

.info-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
  margin-top: 4rpx;
}

.info-text {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
}

.info-label {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
}

.info-value {
  font-size: 26rpx;
  color: #333;
  font-weight: 500;
  word-break: break-all;
}

/* 卡片底部 */
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24rpx;
}

.handler-info {
  display: flex;
  align-items: center;
}

.handler-label {
  font-size: 24rpx;
  color: #999;
  margin-right: 8rpx;
}

.handler-name {
  font-size: 26rpx;
  color: #667eea;
  font-weight: 500;
}

.action-btns {
  display: flex;
  gap: 16rpx;
}

.btn {
  display: flex;
  align-items: center;
  padding: 16rpx 32rpx;
  font-size: 26rpx;
  border-radius: 30rpx;
  border: none;
  line-height: 1;
}

.btn-icon {
  margin-right: 8rpx;
  font-size: 28rpx;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 4rpx 16rpx rgba(102, 126, 234, 0.3);
}

.btn-success {
  background: linear-gradient(135deg, #2ED573 0%, #1e9c52 100%);
  color: #fff;
  box-shadow: 0 4rpx 16rpx rgba(46, 213, 115, 0.3);
}

/* 已完成标识 */
.completed-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  padding-top: 24rpx;
  border-top: 1rpx dashed #e0e0e0;
  margin-top: 24rpx;
}

.badge-icon {
  width: 40rpx;
  height: 40rpx;
  background: #2ED573;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24rpx;
  margin-right: 12rpx;
}

.completed-badge text:last-child {
  font-size: 28rpx;
  color: #2ED573;
  font-weight: 500;
}

/* 空状态 */
.empty-tip {
  text-align: center;
  padding: 120rpx 0;
}

.empty-icon-box {
  width: 160rpx;
  height: 160rpx;
  background: #f5f7fa;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
}

.empty-icon {
  font-size: 80rpx;
}

.empty-text {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 12rpx;
}

.empty-subtext {
  display: block;
  font-size: 26rpx;
  color: #999;
}

/* 加载更多 */
.loading-more, .no-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx 0;
  font-size: 26rpx;
  color: #999;
}

.loading-spinner {
  width: 32rpx;
  height: 32rpx;
  border: 4rpx solid #f0f0f0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 12rpx;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
