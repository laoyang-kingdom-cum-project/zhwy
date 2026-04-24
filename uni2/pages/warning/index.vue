<template>
  <view class="container">
    <!-- 顶部统计卡片 -->
    <view class="stats-section">
      <view class="stat-card urgent" @click="setFilter('1')">
        <view class="stat-icon">🚨</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.urgent}}</text>
          <text class="stat-label">紧急预警</text>
        </view>
      </view>
      <view class="stat-card important" @click="setFilter('2')">
        <view class="stat-icon">⚠️</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.important}}</text>
          <text class="stat-label">重要预警</text>
        </view>
      </view>
      <view class="stat-card normal" @click="setFilter('3')">
        <view class="stat-icon">ℹ️</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.normal}}</text>
          <text class="stat-label">一般预警</text>
        </view>
      </view>
    </view>

    <!-- 筛选标签 -->
    <view class="filter-section">
      <scroll-view class="filter-scroll" scroll-x>
        <view class="filter-list">
          <view class="filter-item" :class="{active: currentFilter === 'all'}" @click="setFilter('all')">
            <text class="filter-text">全部</text>
            <text class="filter-count">{{stats.total}}</text>
          </view>
          <view class="filter-item" :class="{active: currentFilter === '0'}" @click="setFilter('0')">
            <text class="filter-text">待处理</text>
            <text class="filter-badge" v-if="stats.pending > 0">{{stats.pending}}</text>
          </view>
          <view class="filter-item" :class="{active: currentFilter === '1'}" @click="setFilter('1')">
            <text class="filter-text">处理中</text>
          </view>
          <view class="filter-item" :class="{active: currentFilter === '2'}" @click="setFilter('2')">
            <text class="filter-text">已处理</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 预警列表 -->
    <scroll-view class="warning-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="list-content">
        <view class="warning-card" v-for="(item, index) in warningList" :key="index" @click="goToDetail(item)">
          <!-- 左侧等级指示条 -->
          <view class="level-indicator" :class="'level-' + item.level"></view>
          
          <view class="card-content">
            <!-- 头部：等级和状态 -->
            <view class="card-header">
              <view class="level-tag" :class="'level-' + item.level">
                <text class="level-icon">{{item.level === '1' ? '🚨' : item.level === '2' ? '⚠️' : 'ℹ️'}}</text>
                <text>{{item.levelText}}</text>
              </view>
              <view class="status-tag" :class="'status-' + (item.state || '0')">{{item.statusText}}</view>
            </view>
            
            <!-- 标题 -->
            <view class="card-title">
              <text>{{item.title}}</text>
            </view>
            
            <!-- 底部信息 -->
            <view class="card-footer">
              <view class="info-item">
                <text class="info-icon">📍</text>
                <text class="info-text">{{item.location}}</text>
              </view>
              <view class="info-item">
                <text class="info-icon">🕐</text>
                <text class="info-text">{{item.time}}</text>
              </view>
            </view>
          </view>
          
          <!-- 右侧操作区 -->
          <view class="card-actions">
            <view v-if="(item.state || '0') === '0'" class="action-btn handle" @click.stop="handleWarning(item, '1')">
              <text>接单</text>
            </view>
            <view v-else-if="(item.state || '0') === '1'" class="action-btn complete" @click.stop="handleWarning(item, '2')">
              <text>完成</text>
            </view>
            <view v-else class="action-btn view" @click.stop="goToDetail(item)">
              <text>查看</text>
            </view>
            <text class="arrow-icon">›</text>
          </view>
        </view>
        
        <!-- 加载更多 -->
        <view class="load-more" v-if="loading">
          <text>加载中...</text>
        </view>
        <view class="no-more" v-else-if="!hasMore && warningList.length > 0">
          <text>没有更多了</text>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="warningList.length === 0 && !loading">
        <view class="empty-icon">🛡️</view>
        <text class="empty-title">暂无预警信息</text>
        <text class="empty-desc">当前没有{{getFilterText()}}的预警</text>
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
      hasMore: true,
      stats: {
        urgent: 0,
        important: 0,
        normal: 0,
        total: 0,
        pending: 0
      }
    }
  },
  onLoad() {
    this.loadData()
  },
  onShow() {
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
    
    getFilterText() {
      const map = { 'all': '', '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[this.currentFilter] || ''
    },
    
    async loadData() {
      await this.loadStats()
      await this.loadWarningList()
    },
    
    async loadStats() {
      try {
        // 获取各等级预警数量
        const urgentRes = await getWarningList({ pageNum: 1, pageSize: 1, level: '1' })
        const importantRes = await getWarningList({ pageNum: 1, pageSize: 1, level: '2' })
        const normalRes = await getWarningList({ pageNum: 1, pageSize: 1, level: '3' })
        const pendingRes = await getWarningList({ pageNum: 1, pageSize: 1, state: '0' })
        const totalRes = await getWarningList({ pageNum: 1, pageSize: 1 })
        
        this.stats.urgent = urgentRes.total || 0
        this.stats.important = importantRes.total || 0
        this.stats.normal = normalRes.total || 0
        this.stats.pending = pendingRes.total || 0
        this.stats.total = totalRes.total || 0
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    
    async loadWarningList() {
      if (this.loading || !this.hasMore) return
      this.loading = true
      
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        if (this.currentFilter !== 'all') {
          params.state = this.currentFilter
        }
        
        const res = await getWarningList(params)
        if (res.code === 200) {
          const list = (res.rows || []).map(item => ({
            ...item,
            levelText: this.getLevelText(item.level),
            statusText: this.getStatusText(item.state)
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
        uni.showToast({ title: '加载失败', icon: 'none' })
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
      if (this.hasMore && !this.loading) {
        this.pageNum++
        this.loadWarningList()
      }
    },
    
    getLevelText(level) {
      const map = { '1': '紧急', '2': '重要', '3': '一般' }
      return map[level] || '一般'
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    async handleWarning(item, state) {
      const actionText = state === '1' ? '接单' : '完成'
      uni.showModal({
        title: '确认' + actionText,
        content: `确定要${actionText}该预警吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const result = await handleWarning({
                id: item.id,
                state: state
              })
              if (result.code === 200) {
                uni.showToast({ title: actionText + '成功', icon: 'success' })
                this.loadData()
              } else {
                uni.showToast({ title: result.msg || '操作失败', icon: 'none' })
              }
            } catch (e) {
              uni.showToast({ title: '操作失败', icon: 'none' })
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
}

/* 顶部统计卡片 */
.stats-section {
  display: flex;
  padding: 20rpx;
  gap: 16rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card {
  flex: 1;
  background: rgba(255,255,255,0.95);
  border-radius: 16rpx;
  padding: 24rpx 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.1);
}

.stat-card.urgent {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
}

.stat-card.important {
  background: linear-gradient(135deg, #ffa502 0%, #ff6348 100%);
}

.stat-card.normal {
  background: linear-gradient(135deg, #2ed573 0%, #7bed9f 100%);
}

.stat-icon {
  font-size: 40rpx;
  margin-bottom: 12rpx;
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-num {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 4rpx;
}

.stat-label {
  font-size: 22rpx;
  color: rgba(255,255,255,0.9);
}

/* 筛选区域 */
.filter-section {
  background: #fff;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.filter-scroll {
  white-space: nowrap;
}

.filter-list {
  display: inline-flex;
  padding: 0 20rpx;
  gap: 16rpx;
}

.filter-item {
  display: flex;
  align-items: center;
  padding: 16rpx 28rpx;
  background: #f5f7fa;
  border-radius: 32rpx;
  transition: all 0.3s;
}

.filter-item.active {
  background: #667eea;
}

.filter-text {
  font-size: 28rpx;
  color: #666;
}

.filter-item.active .filter-text {
  color: #fff;
}

.filter-count {
  font-size: 22rpx;
  color: #667eea;
  background: #fff;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  margin-left: 12rpx;
}

.filter-badge {
  font-size: 22rpx;
  color: #fff;
  background: #ff6b6b;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  margin-left: 12rpx;
}

/* 预警列表 */
.warning-list {
  height: calc(100vh - 320rpx - 120rpx);
}

.list-content {
  padding: 20rpx;
}

.warning-card {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06);
}

.level-indicator {
  width: 8rpx;
  flex-shrink: 0;
}

.level-indicator.level-1 {
  background: #ff6b6b;
}

.level-indicator.level-2 {
  background: #ffa502;
}

.level-indicator.level-3 {
  background: #2ed573;
}

.card-content {
  flex: 1;
  padding: 24rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.level-tag {
  display: flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.level-tag.level-1 {
  background: #ffebee;
  color: #d32f2f;
}

.level-tag.level-2 {
  background: #fff3e0;
  color: #f57c00;
}

.level-tag.level-3 {
  background: #e8f5e9;
  color: #388e3c;
}

.level-icon {
  margin-right: 8rpx;
}

.status-tag {
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.status-tag.status-0 {
  background: #fff3e0;
  color: #f57c00;
}

.status-tag.status-1 {
  background: #e3f2fd;
  color: #1976d2;
}

.status-tag.status-2 {
  background: #e8f5e9;
  color: #388e3c;
}

.card-title {
  margin-bottom: 20rpx;
}

.card-title text {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
}

.card-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.info-text {
  font-size: 26rpx;
  color: #999;
}

.card-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24rpx;
  border-left: 1rpx solid #f5f7fa;
  min-width: 120rpx;
}

.action-btn {
  padding: 12rpx 28rpx;
  border-radius: 28rpx;
  font-size: 26rpx;
  margin-bottom: 16rpx;
}

.action-btn.handle {
  background: #667eea;
  color: #fff;
}

.action-btn.complete {
  background: #2ed573;
  color: #fff;
}

.action-btn.view {
  background: #f5f7fa;
  color: #666;
}

.arrow-icon {
  font-size: 40rpx;
  color: #ccc;
}

/* 加载更多 */
.load-more, .no-more {
  text-align: center;
  padding: 40rpx;
}

.load-more text, .no-more text {
  font-size: 26rpx;
  color: #999;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-title {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 28rpx;
  color: #999;
}
</style>
