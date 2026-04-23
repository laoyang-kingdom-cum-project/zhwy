<template>
  <view class="visitor-container">
    <!-- 新增按钮 -->
    <view class="add-section">
      <view class="add-btn" @click="navigateToAppointment">
        <text class="add-icon">+</text>
        <text>新增预约</text>
      </view>
    </view>

    <!-- 状态筛选 -->
    <view class="filter-bar">
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === '' }"
        @click="selectStatus('')"
      >
        <text>全部</text>
      </view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === '0' }"
        @click="selectStatus('0')"
      >
        <text>待审核</text>
      </view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === '1' }"
        @click="selectStatus('1')"
      >
        <text>已通过</text>
      </view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === '2' }"
        @click="selectStatus('2')"
      >
        <text>已拒绝</text>
      </view>
    </view>

    <!-- 访客列表 -->
    <scroll-view class="visitor-list" scroll-y @scrolltolower="loadMore">
      <view 
        class="visitor-card" 
        v-for="(item, index) in visitorList" 
        :key="index"
        @click="viewDetail(item)"
      >
        <view class="card-header">
          <view class="visitor-info">
            <text class="visitor-name">{{ item.visitorName }}</text>
            <text class="visitor-phone">{{ item.visitorPhone }}</text>
          </view>
          <view class="status-tag" :class="getStatusClass(item.status)">
            {{ getStatusText(item.status) }}
          </view>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">访问日期</text>
            <text class="info-value">{{ item.visitDate }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">访问时间</text>
            <text class="info-value">{{ formatTime(item.visitTimeStart) }} - {{ formatTime(item.visitTimeEnd) }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">访客人数</text>
            <text class="info-value">{{ item.visitorCount }}人</text>
          </view>
          <view class="info-row" v-if="item.visitReason">
            <text class="info-label">访问事由</text>
            <text class="info-value">{{ item.visitReason }}</text>
          </view>
        </view>
        <view class="card-footer" v-if="item.status === '0'">
          <view class="action-btn cancel" @click.stop="cancelAppointment(item)">
            <text>取消预约</text>
          </view>
        </view>
      </view>

      <!-- 空状态 -->
      <view class="empty-state" v-if="visitorList.length === 0 && !loading">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无访客预约记录</text>
        <text class="empty-tip">点击右上角新建预约</text>
      </view>

      <!-- 加载更多 -->
      <view class="load-more" v-if="visitorList.length > 0">
        <text v-if="loading">加载中...</text>
        <text v-else-if="noMore">没有更多了</text>
      </view>
    </scroll-view>

  </view>
</template>

<script>
import { getVisitorList, updateVisitor } from '@/api/visitor.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      currentStatus: '',
      visitorList: [],
      loading: false,
      noMore: false,
      pageNum: 1,
      pageSize: 10
    }
  },
  onLoad() {
    this.loadVisitorList()
  },
  onShow() {
    this.loadVisitorList()
  },
  methods: {
    // 加载访客列表
    async loadVisitorList() {
      if (this.loading) return
      this.loading = true
      
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: userId
        }
        if (this.currentStatus !== '') {
          params.status = this.currentStatus
        }
        
        const res = await getVisitorList(params)
        if (res.code === 200) {
          if (this.pageNum === 1) {
            this.visitorList = res.rows || []
          } else {
            this.visitorList = this.visitorList.concat(res.rows || [])
          }
          this.noMore = this.visitorList.length >= res.total
        }
      } catch (e) {
        console.error('获取访客列表失败', e)
        uni.showToast({ title: '获取数据失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    
    // 加载更多
    loadMore() {
      if (this.noMore || this.loading) return
      this.pageNum++
      this.loadVisitorList()
    },
    
    // 筛选状态
    selectStatus(status) {
      this.currentStatus = status
      this.pageNum = 1
      this.loadVisitorList()
    },
    
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        '0': '待审核',
        '1': '已通过',
        '2': '已拒绝',
        '3': '已过期',
        '4': '已取消'
      }
      return statusMap[status] || '未知'
    },
    
    // 获取状态样式类
    getStatusClass(status) {
      const classMap = {
        '0': 'pending',
        '1': 'approved',
        '2': 'rejected',
        '3': 'expired',
        '4': 'cancelled'
      }
      return classMap[status] || ''
    },

    // 格式化时间，去掉日期部分
    formatTime(timeStr) {
      if (!timeStr) return ''
      // 如果包含日期部分（如 "1970-01-01 11:20:00"），只返回时间部分
      if (timeStr.includes(' ')) {
        return timeStr.split(' ')[1].substring(0, 5) // 返回 "11:20"
      }
      // 如果只有时间（如 "11:20:00"），去掉秒
      if (timeStr.includes(':')) {
        return timeStr.substring(0, 5) // 返回 "11:20"
      }
      return timeStr
    },

    // 跳转到新建预约
    navigateToAppointment() {
      uni.navigateTo({
        url: '/pages/visitor/appointment'
      })
    },
    
    // 查看详情
    viewDetail(item) {
      uni.navigateTo({
        url: `/pages/visitor/detail?id=${item.id}`
      })
    },
    
    // 取消预约
    async cancelAppointment(item) {
      uni.showModal({
        title: '提示',
        content: '确定要取消该预约吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const result = await updateVisitor({
                id: item.id,
                status: '4'
              })
              if (result.code === 200) {
                uni.showToast({ title: '取消成功', icon: 'success' })
                this.loadVisitorList()
              } else {
                uni.showToast({ title: result.msg || '取消失败', icon: 'none' })
              }
            } catch (e) {
              uni.showToast({ title: '取消失败', icon: 'none' })
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.visitor-container {
  min-height: 100vh;
  background: #f5f5f5;
}

// 新增按钮
.add-section {
  padding: 30rpx;

  .add-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    background: #007AFF;
    color: #fff;
    padding: 24rpx;
    border-radius: 12rpx;
    font-size: 30rpx;

    .add-icon {
      font-size: 36rpx;
      margin-right: 12rpx;
    }
  }
}

// 状态筛选
.filter-bar {
  display: flex;
  background: #fff;
  padding: 0 30rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  
  .filter-item {
    padding: 12rpx 30rpx;
    margin-right: 20rpx;
    background: #f5f5f5;
    border-radius: 30rpx;
    font-size: 26rpx;
    color: #666;
    
    &.active {
      background: #007AFF;
      color: #fff;
    }
  }
}

// 访客列表
.visitor-list {
  padding: 20rpx;
  
  .visitor-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #f5f5f5;
      
      .visitor-info {
        display: flex;
        flex-direction: column;
        
        .visitor-name {
          font-size: 32rpx;
          font-weight: 500;
          color: #333;
          margin-bottom: 8rpx;
        }
        
        .visitor-phone {
          font-size: 26rpx;
          color: #999;
        }
      }
      
      .status-tag {
        padding: 8rpx 20rpx;
        border-radius: 30rpx;
        font-size: 24rpx;
        
        &.pending {
          background: #FFF3E0;
          color: #FF9800;
        }
        
        &.approved {
          background: #E8F5E9;
          color: #4CAF50;
        }
        
        &.rejected {
          background: #FFEBEE;
          color: #F44336;
        }
        
        &.expired {
          background: #f5f5f5;
          color: #999;
        }
        
        &.cancelled {
          background: #ECEFF1;
          color: #607D8B;
        }
      }
    }
    
    .card-body {
      padding: 30rpx;
      
      .info-row {
        display: flex;
        margin-bottom: 16rpx;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .info-label {
          width: 140rpx;
          font-size: 28rpx;
          color: #999;
        }
        
        .info-value {
          flex: 1;
          font-size: 28rpx;
          color: #333;
        }
      }
    }
    
    .card-footer {
      display: flex;
      justify-content: flex-end;
      padding: 20rpx 30rpx;
      border-top: 1rpx solid #f5f5f5;
      
      .action-btn {
        padding: 12rpx 30rpx;
        border-radius: 30rpx;
        font-size: 26rpx;
        
        &.cancel {
          background: #f5f5f5;
          color: #666;
        }
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
    font-size: 120rpx;
    margin-bottom: 20rpx;
  }
  
  .empty-text {
    font-size: 32rpx;
    color: #666;
    margin-bottom: 10rpx;
  }
  
  .empty-tip {
    font-size: 26rpx;
    color: #999;
  }
}

// 加载更多
.load-more {
  text-align: center;
  padding: 30rpx;
  font-size: 26rpx;
  color: #999;
}
</style>
