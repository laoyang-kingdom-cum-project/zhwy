<template>
  <view class="care-container">
    <!-- 关怀对象列表 -->
    <view class="care-list">
      <view 
        class="care-card" 
        v-for="(item, index) in careList" 
        :key="index"
        @click="navigateToDetail(item)"
      >
        <view class="care-header">
          <image class="care-avatar" src="/static/logo.png" mode="aspectFill"></image>
          <view class="care-info">
            <text class="care-name">{{ item.name }}</text>
            <text class="care-room">{{ item.room }}</text>
          </view>
          <view class="care-status" :class="item.healthStatus === '0' ? 'normal' : 'warning'">
            {{ item.healthStatus === '0' ? '正常' : item.healthStatus === '1' ? '关注' : '异常' }}
          </view>
        </view>
        <view class="care-body">
          <view class="care-stats">
            <view class="stat">
              <text class="stat-icon">👣</text>
              <text class="stat-value">{{ item.steps }}</text>
              <text class="stat-label">步数</text>
            </view>
            <view class="stat">
              <text class="stat-icon">😴</text>
              <text class="stat-value">{{ item.sleep }}小时</text>
              <text class="stat-label">睡眠</text>
            </view>
            <view class="stat">
              <text class="stat-icon">❤️</text>
              <text class="stat-value">{{ item.heartRate }}</text>
              <text class="stat-label">心率</text>
            </view>
          </view>
        </view>
        <view class="care-footer">
          <text class="last-active">最后活动: {{ formatTime(item.lastActive) }}</text>
          <text class="emergency-contact">紧急联系人: {{ item.emergencyContact }}</text>
        </view>
      </view>
    </view>
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="3"></custom-tabbar>
  </view>
</template>

<script>
import { getCareList } from '@/api/care.js'

export default {
  data() {
    return {
      careList: []
    }
  },
  onLoad() {
    this.loadCareList()
  },
  onShow() {
    this.loadCareList()
  },
  methods: {
    // 加载关怀对象列表
    async loadCareList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getCareList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.careList = res.rows
        }
      } catch (e) {
        console.error('获取关怀列表失败', e)
        uni.showToast({ title: '获取数据失败', icon: 'none' })
      }
    },
    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      const now = new Date()
      const diff = now - date
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(diff / 3600000)
      const days = Math.floor(diff / 86400000)
      
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return `${minutes}分钟前`
      if (hours < 24) return `${hours}小时前`
      if (days < 30) return `${days}天前`
      return timeStr.substring(0, 10)
    },
    navigateToDetail(item) {
      uni.navigateTo({
        url: `/pages/care/detail?id=${item.id}`
      })
    }
  }
}
</script>

<style lang="scss">
.care-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 130rpx;
}

.care-list {
  .care-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .care-header {
      display: flex;
      align-items: center;
      margin-bottom: 24rpx;

      .care-avatar {
        width: 100rpx;
        height: 100rpx;
        border-radius: 50%;
        margin-right: 20rpx;
      }

      .care-info {
        flex: 1;

        .care-name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          display: block;
          margin-bottom: 8rpx;
        }

        .care-room {
          font-size: 26rpx;
          color: #999;
        }
      }

      .care-status {
        padding: 8rpx 20rpx;
        border-radius: 24rpx;
        font-size: 24rpx;

        &.normal {
          background: #E8F5E9;
          color: #2ED573;
        }

        &.warning {
          background: #FFF3E0;
          color: #FF9800;
        }
      }
    }

    .care-body {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;

      .care-stats {
        display: flex;
        justify-content: space-around;

        .stat {
          display: flex;
          flex-direction: column;
          align-items: center;

          .stat-icon {
            font-size: 40rpx;
            margin-bottom: 8rpx;
          }

          .stat-value {
            font-size: 32rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 4rpx;
          }

          .stat-label {
            font-size: 24rpx;
            color: #999;
          }
        }
      }
    }

    .care-footer {
      .last-active {
        font-size: 26rpx;
        color: #666;
        display: block;
        margin-bottom: 8rpx;
      }

      .emergency-contact {
        font-size: 26rpx;
        color: #007AFF;
      }
    }
  }
}
</style>
