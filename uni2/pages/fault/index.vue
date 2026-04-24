<template>
  <view class="container">
    <!-- 顶部统计卡片 -->
    <view class="stats-section">
      <view class="stat-card pending" @click="setFilter('0')">
        <view class="stat-icon">⏳</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.pending}}</text>
          <text class="stat-label">待处理</text>
        </view>
      </view>
      <view class="stat-card processing" @click="setFilter('1')">
        <view class="stat-icon">🔧</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.processing}}</text>
          <text class="stat-label">处理中</text>
        </view>
      </view>
      <view class="stat-card completed" @click="setFilter('2')">
        <view class="stat-icon">✅</view>
        <view class="stat-info">
          <text class="stat-num">{{stats.completed}}</text>
          <text class="stat-label">已完成</text>
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
            <text class="filter-text">已完成</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 故障列表 -->
    <scroll-view class="fault-list" scroll-y refresher-enabled :refresher-triggered="refreshing" @refresherrefresh="onRefresh" @scrolltolower="loadMore">
      <view class="list-content">
        <view class="fault-card" v-for="(item, index) in faultList" :key="index">
          <!-- 左侧状态指示条 -->
          <view class="status-indicator" :class="'status-' + (item.handleStatus || '0')"></view>
          
          <!-- 中间内容区 - 点击进详情 -->
          <view class="card-content" @click="goToDetail(item)">
            <!-- 头部：设备名和状态 -->
            <view class="card-header">
              <view class="device-tag">
                <text class="device-icon">🔌</text>
                <text>{{item.deviceName}}</text>
              </view>
              <view class="status-tag" :class="'status-' + (item.handleStatus || '0')">
                {{getStatusText(item.handleStatus || '0')}}
              </view>
            </view>
            
            <!-- 故障类型 -->
            <view class="fault-type-row">
              <text class="fault-type">{{item.faultType}}</text>
            </view>
            
            <!-- 故障描述 -->
            <view class="fault-desc">
              <text>{{item.faultDesc}}</text>
            </view>
            
            <!-- 底部信息 -->
            <view class="card-footer">
              <view class="info-item">
                <text class="info-icon">🕐</text>
                <text class="info-text">上报: {{item.reportTime}}</text>
              </view>
              <view class="info-item" v-if="item.handleTime">
                <text class="info-icon">✓</text>
                <text class="info-text">处理: {{item.handleTime}}</text>
              </view>
            </view>
          </view>
          
          <!-- 右侧操作区 - 独立点击 -->
          <view class="card-actions">
            <view class="action-row" v-if="(item.handleStatus || '0') === '0'" @click="handleFault(item, '1')">
              <text class="action-btn handle">处理</text>
              <text class="arrow-icon">›</text>
            </view>
            <view class="action-row" v-else-if="(item.handleStatus || '0') === '1'" @click="showCompleteForm(item)">
              <text class="action-btn complete">完成</text>
              <text class="arrow-icon">›</text>
            </view>
            <view class="action-row" v-else @click="goToDetail(item)">
              <text class="action-btn view">查看</text>
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
        
        <!-- 加载更多 -->
        <view class="loading-more" v-if="loading">
          <text>加载中...</text>
        </view>
        <view class="no-more" v-if="!hasMore && faultList.length > 0">
          <text>没有更多了</text>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="faultList.length === 0 && !loading">
        <view class="empty-icon">🔧</view>
        <text class="empty-text">暂无故障记录</text>
        <text class="empty-subtext">设备运行正常，继续保持！</text>
      </view>
    </scroll-view>
    
    <!-- 完成故障弹窗 -->
    <view class="complete-popup" v-if="showCompletePopup" @click="closeCompletePopup">
      <view class="popup-content" @click.stop>
        <view class="popup-header">
          <text class="popup-title">处理结果</text>
          <text class="popup-close" @click="closeCompletePopup">✕</text>
        </view>
        <view class="popup-body">
          <textarea 
            class="result-input" 
            v-model="completeForm.result"
            placeholder="请输入处理结果..."
            maxlength="200"
          />
          <view class="input-count">{{completeForm.result.length}}/200</view>
        </view>
        <view class="popup-footer">
          <view class="btn btn-cancel" @click="closeCompletePopup">取消</view>
          <view class="btn btn-submit" @click="submitComplete">提交</view>
        </view>
      </view>
    </view>
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
      hasMore: true,
      stats: {
        total: 0,
        pending: 0,
        processing: 0,
        completed: 0
      },
      showCompletePopup: false,
      completeForm: {
        item: null,
        result: ''
      }
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
      this.faultList = []
      this.hasMore = true
      this.loadData()
    },
    
    async loadStats() {
      try {
        // 获取各状态数量
        const pendingRes = await getFaultList({ pageNum: 1, pageSize: 1, handleStatus: '0' })
        const processingRes = await getFaultList({ pageNum: 1, pageSize: 1, handleStatus: '1' })
        const completedRes = await getFaultList({ pageNum: 1, pageSize: 1, handleStatus: '2' })
        const totalRes = await getFaultList({ pageNum: 1, pageSize: 1 })
        
        this.stats = {
          pending: pendingRes.total || 0,
          processing: processingRes.total || 0,
          completed: completedRes.total || 0,
          total: totalRes.total || 0
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
      this.loadStats()
    },
    
    loadMore() {
      if (!this.hasMore || this.loading) return
      this.pageNum++
      this.loadData()
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已完成' }
      return map[status] || '待处理'
    },
    
    async handleFault(item, status) {
      uni.showModal({
        title: '确认',
        content: '确定要开始处理该故障吗？',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '处理中...' })
            try {
              const updateData = {
                id: item.id,
                handleStatus: status,
                handleTime: this.formatDateTime(new Date())
              }
              console.log('更新故障数据:', updateData)
              const result = await handleFault(updateData)
              console.log('更新故障结果:', result)
              uni.hideLoading()
              if (result.code === 200) {
                uni.showToast({ title: '已开始处理', icon: 'success' })
                this.onRefresh()
              } else {
                uni.showToast({ title: result.msg || '操作失败', icon: 'none' })
              }
            } catch (e) {
              uni.hideLoading()
              console.error('处理故障失败:', e)
              uni.showToast({ title: e.message || '操作失败', icon: 'none' })
            }
          }
        }
      })
    },

    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    showCompleteForm(item) {
      this.completeForm.item = item
      this.completeForm.result = ''
      this.showCompletePopup = true
    },

    closeCompletePopup() {
      this.showCompletePopup = false
      this.completeForm.item = null
      this.completeForm.result = ''
    },

    async submitComplete() {
      if (!this.completeForm.result.trim()) {
        uni.showToast({ title: '请输入处理结果', icon: 'none' })
        return
      }

      uni.showLoading({ title: '提交中...' })
      try {
        const updateData = {
          id: this.completeForm.item.id,
          handleStatus: '2',
          handleResult: this.completeForm.result,
          handleTime: this.formatDateTime(new Date())
        }
        console.log('完成故障数据:', updateData)
        const result = await handleFault(updateData)
        console.log('完成故障结果:', result)
        uni.hideLoading()
        if (result.code === 200) {
          uni.showToast({ title: '处理完成', icon: 'success' })
          this.closeCompletePopup()
          this.onRefresh()
        } else {
          uni.showToast({ title: result.msg || '操作失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('完成故障失败:', e)
        uni.showToast({ title: e.message || '操作失败', icon: 'none' })
      }
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

/* 统计卡片 */
.stats-section {
  display: flex;
  padding: 20rpx;
  gap: 16rpx;
  background: #fff;
}

.stat-card {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 24rpx 16rpx;
  border-radius: 16rpx;
  background: #f5f7fa;
}

.stat-card.pending {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%);
}

.stat-card.processing {
  background: linear-gradient(135deg, #ffa502 0%, #ff9500 100%);
}

.stat-card.completed {
  background: linear-gradient(135deg, #2ed573 0%, #26c068 100%);
}

.stat-icon {
  font-size: 48rpx;
  margin-right: 12rpx;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-num {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.stat-label {
  font-size: 24rpx;
  color: rgba(255,255,255,0.9);
  margin-top: 4rpx;
}

/* 筛选标签 */
.filter-section {
  background: #fff;
  padding: 0 20rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.filter-scroll {
  white-space: nowrap;
}

.filter-list {
  display: inline-flex;
  gap: 16rpx;
}

.filter-item {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  background: #f5f7fa;
  border-radius: 30rpx;
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
  color: #999;
  margin-left: 8rpx;
  background: #e8e8e8;
  padding: 2rpx 10rpx;
  border-radius: 10rpx;
}

.filter-item.active .filter-count {
  color: #667eea;
  background: rgba(255,255,255,0.9);
}

.filter-badge {
  font-size: 20rpx;
  color: #fff;
  background: #ff6b6b;
  padding: 2rpx 10rpx;
  border-radius: 10rpx;
  margin-left: 8rpx;
}

/* 故障列表 */
.fault-list {
  height: calc(100vh - 280rpx);
  padding: 20rpx;
}

.list-content {
  padding-bottom: 40rpx;
}

.fault-card {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.06);
}

.status-indicator {
  width: 8rpx;
  flex-shrink: 0;
}

.status-indicator.status-0 {
  background: #ff6b6b;
}

.status-indicator.status-1 {
  background: #ffa502;
}

.status-indicator.status-2 {
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

.device-tag {
  display: flex;
  align-items: center;
  background: #f0f4ff;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.device-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.device-tag text:last-child {
  font-size: 26rpx;
  color: #667eea;
  font-weight: 500;
}

.status-tag {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.status-tag.status-0 {
  background: #ffe5e5;
  color: #ff6b6b;
}

.status-tag.status-1 {
  background: #fff4e5;
  color: #ffa502;
}

.status-tag.status-2 {
  background: #e5f5e5;
  color: #2ed573;
}

.fault-type-row {
  margin-bottom: 12rpx;
}

.fault-type {
  font-size: 32rpx;
  color: #333;
  font-weight: 600;
}

.fault-desc {
  margin-bottom: 16rpx;
}

.fault-desc text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-icon {
  font-size: 22rpx;
  margin-right: 6rpx;
  color: #999;
}

.info-text {
  font-size: 24rpx;
  color: #999;
}

/* 右侧操作区 */
.card-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24rpx;
  border-left: 1rpx solid #f5f7fa;
  min-width: 120rpx;
}

.action-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4rpx;
}

.action-btn {
  padding: 12rpx 20rpx;
  border-radius: 24rpx;
  font-size: 26rpx;
  color: #fff;
  line-height: 1;
}

.action-btn.handle {
  background: #667eea;
}

.action-btn.complete {
  background: #2ed573;
}

.action-btn.view {
  background: #e8e8e8;
  color: #666;
}

.arrow-icon {
  font-size: 32rpx;
  color: #ccc;
  line-height: 1;
}

/* 加载更多 */
.loading-more, .no-more {
  text-align: center;
  padding: 30rpx 0;
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

.empty-text {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 12rpx;
}

.empty-subtext {
  font-size: 26rpx;
  color: #999;
}

/* 完成故障弹窗 */
.complete-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.popup-content {
  width: 80%;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.popup-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.popup-close {
  font-size: 36rpx;
  color: #999;
  padding: 10rpx;
}

.popup-body {
  padding: 30rpx;
}

.result-input {
  width: 100%;
  height: 200rpx;
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.input-count {
  text-align: right;
  font-size: 24rpx;
  color: #999;
  margin-top: 12rpx;
}

.popup-footer {
  display: flex;
  padding: 20rpx 30rpx 30rpx;
  gap: 20rpx;
}

.popup-footer .btn {
  flex: 1;
  padding: 24rpx 0;
  border-radius: 12rpx;
  text-align: center;
  font-size: 30rpx;
}

.btn-cancel {
  background: #f5f7fa;
  color: #666;
}

.btn-submit {
  background: #667eea;
  color: #fff;
}
</style>
