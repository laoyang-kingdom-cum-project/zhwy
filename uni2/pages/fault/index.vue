<template>
  <view class="container">
    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{active: currentFilter === 'all'}" @click="setFilter('all')">
        <text>全部</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '0'}" @click="setFilter('0')">
        <text>待处理</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '1'}" @click="setFilter('1')">
        <text>处理中</text>
      </view>
      <view class="filter-item" :class="{active: currentFilter === '2'}" @click="setFilter('2')">
        <text>已处理</text>
      </view>
    </view>
    
    <!-- 故障列表 -->
    <scroll-view class="fault-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="fault-card" v-for="(item, index) in faultList" :key="index" @click="goToDetail(item)">
        <view class="card-header">
          <view class="device-info">
            <text class="device-name">{{item.deviceName}}</text>
            <text class="fault-type">{{item.faultType}}</text>
          </view>
          <view class="status-tag" :class="'status-' + (item.handleStatus || '0')">
            {{getStatusText(item.handleStatus || '0')}}
          </view>
        </view>
        <view class="card-body">
          <text class="fault-desc">{{item.faultDesc}}</text>
          <view class="info-row">
            <text class="info-label">上报时间</text>
            <text class="info-value">{{item.reportTime}}</text>
          </view>
          <view class="info-row" v-if="item.handleTime">
            <text class="info-label">处理时间</text>
            <text class="info-value">{{item.handleTime}}</text>
          </view>
        </view>
        <view class="card-footer" v-if="(item.handleStatus || '0') !== '2'">
          <button v-if="(item.handleStatus || '0') === '0'" class="btn btn-primary" @click.stop="handleFault(item, '1')">开始处理</button>
          <button v-if="(item.handleStatus || '0') === '1'" class="btn btn-success" @click.stop="completeFault(item)">完成处理</button>
        </view>
      </view>
      
      <view class="empty-tip" v-if="faultList.length === 0 && !loading">
        <text class="empty-icon">📭</text>
        <text class="empty-text">暂无故障记录</text>
      </view>
      
      <view class="loading-more" v-if="loading">
        <text>加载中...</text>
      </view>
      
      <view class="no-more" v-if="!hasMore && faultList.length > 0">
        <text>没有更多了</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getFaultList, handleFault } from '@/api/fault.js'

export default {
  data() {
    return {
      currentFilter: 'all',
      faultList: [],
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
      this.faultList = []
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
          params.handleStatus = this.currentFilter
        }
        
        const res = await getFaultList(params)
        if (res.code === 200) {
          const list = res.rows || []
          
          if (this.pageNum === 1) {
            this.faultList = list
          } else {
            this.faultList = this.faultList.concat(list)
          }
          
          this.total = res.total || 0
          this.hasMore = this.faultList.length < this.total
        }
      } catch (e) {
        console.error('加载故障列表失败', e)
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
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    async handleFault(item, status) {
      const statusText = status === '1' ? '开始处理' : '完成处理'
      uni.showModal({
        title: '确认',
        content: `确定要${statusText}该故障吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...item,
                handleStatus: status
              }
              if (status === '1') {
                updateData.handleTime = new Date().toISOString()
              }
              const result = await handleFault(updateData)
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
    
    completeFault(item) {
      uni.showModal({
        title: '处理结果',
        editable: true,
        placeholderText: '请输入处理结果...',
        success: async (res) => {
          if (res.confirm && res.content) {
            try {
              const updateData = {
                ...item,
                handleStatus: '2',
                handleResult: res.content
              }
              const result = await handleFault(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: '处理完成',
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
        url: `/pages/fault/detail?id=${item.id}`
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

.fault-list {
  height: calc(100vh - 100rpx);
  padding: 20rpx;
}

.fault-card {
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

.device-info {
  flex: 1;
}

.device-name {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
}

.fault-type {
  display: block;
  font-size: 24rpx;
  color: #FF6B6B;
  margin-top: 6rpx;
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

.fault-desc {
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
  margin-left: 16rpx;
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
