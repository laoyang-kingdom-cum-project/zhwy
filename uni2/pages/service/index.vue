<template>
  <view class="container">
    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{active: currentFilter === 'all'}" @click="setFilter('all')">
        <text>全部</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '0'}" @click="setFilter('0')">
        <text>待分配</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '1'}" @click="setFilter('1')">
        <text>处理中</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '2'}" @click="setFilter('2')">
        <text>已完成</text>
      </view>
    </view>
    
    <!-- 订单列表 -->
    <scroll-view class="order-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="order-card" v-for="(item, index) in orderList" :key="index" @click="goToDetail(item)">
        <view class="card-header">
          <view class="order-type">
            <text class="type-icon">{{getTypeIcon(item.orderType)}}</text>
            <text class="type-text">{{item.orderType}}</text>
          </view>
          <view class="status-tag" :class="'status-' + (item.status || '0')">
            {{getStatusText(item.status || '0')}}
          </view>
        </view>
        <view class="card-body">
          <text class="order-desc">{{item.description}}</text>
          <view class="info-row">
            <text class="info-label">报修人</text>
            <text class="info-value">{{item.userName}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">联系电话</text>
            <text class="info-value">{{item.phone}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">报修地址</text>
            <text class="info-value">{{item.address}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">提交时间</text>
            <text class="info-value">{{item.createTime}}</text>
          </view>
        </view>
        <view class="card-footer" v-if="(item.status || '0') !== '2'">
          <button v-if="(item.status || '0') === '0'" class="btn btn-primary" @click.stop="assignOrder(item)">分配工单</button>
          <button v-if="(item.status || '0') === '1'" class="btn btn-success" @click.stop="completeOrder(item)">标记完成</button>
        </view>
      </view>
      
      <view class="empty-tip" v-if="orderList.length === 0 && !loading">
        <text class="empty-icon">📭</text>
        <text class="empty-text">暂无服务订单</text>
      </view>
      
      <view class="loading-more" v-if="loading">
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
      hasMore: true
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    setFilter(filter) {
      this.currentFilter = filter
      this.pageNum = 1
      this.orderList = []
      this.hasMore = true
      this.loadData()
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
    },
    
    loadMore() {
      if (!this.hasMore || this.loading) return
      this.pageNum++
      this.loadData()
    },
    
    getTypeIcon(type) {
      const map = {
        '报修': '🔧',
        '投诉': '✍️',
        '建议': '💡',
        '其他': '📋'
      }
      return map[type] || '📋'
    },
    
    getStatusText(status) {
      const map = { '0': '待分配', '1': '处理中', '2': '已完成' }
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
              status: '1',
              handler: handlers[res.tapIndex]
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
                status: '2'
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
        url: `/pages/service/detail?id=${item.id}`
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
}

.filter-item text {
  font-size: 28rpx;
  color: #666;
}

.filter-item.active {
  background: #667eea;
}

.filter-item.active text {
  color: #fff;
}

.order-list {
  height: calc(100vh - 100rpx);
  padding: 20rpx;
}

.order-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.order-type {
  display: flex;
  align-items: center;
}

.type-icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.type-text {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.status-tag {
  padding: 8rpx 20rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
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

.card-body {
  padding: 20rpx 0;
  border-top: 1rpx solid #f5f7fa;
  border-bottom: 1rpx solid #f5f7fa;
}

.order-desc {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
  line-height: 1.5;
}

.info-row {
  display: flex;
  padding: 8rpx 0;
}

.info-label {
  width: 160rpx;
  font-size: 26rpx;
  color: #666;
}

.info-value {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 20rpx;
}

.btn {
  padding: 12rpx 40rpx;
  font-size: 26rpx;
  border-radius: 8rpx;
  border: none;
  line-height: 1.5;
}

.btn-primary {
  background: #667eea;
  color: #fff;
}

.btn-success {
  background: #2ED573;
  color: #fff;
}

.empty-tip {
  text-align: center;
  padding: 100rpx 0;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.loading-more, .no-more {
  text-align: center;
  padding: 30rpx 0;
  font-size: 26rpx;
  color: #999;
}
</style>
