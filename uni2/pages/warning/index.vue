<template>
  <view class="container">
    <!-- 顶部筛选 -->
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
    
    <!-- 预警列表 -->
    <scroll-view class="warning-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="warning-card" v-for="(item, index) in warningList" :key="index" @click="goToDetail(item)">
        <view class="card-header">
          <view class="level-tag" :class="'level-' + item.level">{{item.levelText}}</view>
          <text class="warning-time">{{item.time}}</text>
        </view>
        <view class="card-body">
          <text class="warning-title">{{item.title}}</text>
          <view class="warning-info">
            <text class="info-item">📍 {{item.location}}</text>
          </view>
        </view>
        <view class="card-footer">
          <view class="status-tag" :class="'status-' + (item.status || '0')">{{item.statusText}}</view>
          <view class="action-btns">
            <button v-if="(item.status || '0') === '0'" class="btn btn-primary" @click.stop="handleWarning(item, '1')">接单</button>
            <button v-if="(item.status || '0') === '1'" class="btn btn-success" @click.stop="handleWarning(item, '2')">完成</button>
            <button class="btn btn-default" @click.stop="goToDetail(item)">查看</button>
          </view>
        </view>
      </view>
      
      <view class="empty-tip" v-if="warningList.length === 0 && !loading">
        <text class="empty-icon">📭</text>
        <text class="empty-text">暂无预警信息</text>
      </view>
      
      <view class="loading-more" v-if="loading">
        <text>加载中...</text>
      </view>
      
      <view class="no-more" v-if="!hasMore && warningList.length > 0">
        <text>没有更多了</text>
      </view>
    </scroll-view>
    
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="1"></custom-tabbar>
  </view>
</template>

<script>
import { getWarningList, handleWarning } from '@/api/warning.js'

export default {
  data() {
    return {
      currentFilter: 'all',
      warningList: [],
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
      this.warningList = []
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
        
        const res = await getWarningList(params)
        if (res.code === 200) {
          const list = (res.rows || []).map(item => ({
            ...item,
            levelText: this.getLevelText(item.level),
            statusText: this.getStatusText(item.status || '0')
          }))
          
          if (this.pageNum === 1) {
            this.warningList = list
          } else {
            this.warningList = this.warningList.concat(list)
          }
          
          this.total = res.total || 0
          this.hasMore = this.warningList.length < this.total
        }
      } catch (e) {
        console.error('加载预警列表失败', e)
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
    
    getLevelText(level) {
      const map = { '1': '紧急', '2': '重要', '3': '一般' }
      return map[level] || '一般'
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    async handleWarning(item, status) {
      const statusText = status === '1' ? '接单' : '完成'
      uni.showModal({
        title: '确认',
        content: `确定要${statusText}该预警吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...item,
                status: status
              }
              const result = await handleWarning(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: `${statusText}成功`,
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
        url: `/pages/warning/detail?id=${item.id}`
      })
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding-bottom: 120rpx;
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

.warning-list {
  height: calc(100vh - 100rpx);
  padding: 20rpx;
}

.warning-card {
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

.level-tag {
  padding: 6rpx 20rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #fff;
}

.level-1 {
  background: #FF6B6B;
}

.level-2 {
  background: #FFA502;
}

.level-3 {
  background: #2ED573;
}

.warning-time {
  font-size: 24rpx;
  color: #999;
}

.card-body {
  margin-bottom: 20rpx;
}

.warning-title {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 16rpx;
}

.warning-info {
  display: flex;
  flex-wrap: wrap;
}

.info-item {
  font-size: 26rpx;
  color: #666;
  margin-right: 30rpx;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.status-tag {
  padding: 6rpx 20rpx;
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

.action-btns {
  display: flex;
}

.btn {
  margin-left: 16rpx;
  padding: 10rpx 30rpx;
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

.btn-default {
  background: #f5f7fa;
  color: #666;
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
