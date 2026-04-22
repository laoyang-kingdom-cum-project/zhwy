<template>
  <view class="warning-container">
    <!-- 预警统计 -->
    <view class="warning-stats">
      <view class="stat-item urgent">
        <text class="stat-num">{{ stats.urgent }}</text>
        <text class="stat-label">紧急</text>
      </view>
      <view class="stat-item important">
        <text class="stat-num">{{ stats.important }}</text>
        <text class="stat-label">重要</text>
      </view>
      <view class="stat-item normal">
        <text class="stat-num">{{ stats.normal }}</text>
        <text class="stat-label">一般</text>
      </view>
    </view>

    <!-- 预警列表 -->
    <view class="warning-list">
      <view 
        class="warning-card" 
        v-for="(item, index) in warnings" 
        :key="index"
        @click="navigateToDetail(item)"
      >
        <view class="warning-header">
          <view class="warning-level" :class="'level-' + item.level">
            {{ item.level === 1 ? '紧急' : item.level === 2 ? '重要' : '一般' }}
          </view>
        </view>
        <view class="warning-body">
          <text class="warning-title">{{ item.title }}</text>
        </view>
        <view class="warning-footer">
          <view class="warning-location">
            <text class="icon">📍</text>
            <text>{{ item.location }}</text>
          </view>
          <text class="warning-time">{{ item.time }}</text>
        </view>
      </view>
    </view>
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="2"></custom-tabbar>
  </view>
</template>

<script>
import { getWarningList } from '@/api/warning.js'

export default {
  data() {
    return {
      warnings: [],
      stats: {
        urgent: 0,
        important: 0,
        normal: 0
      }
    }
  },
  onLoad() {
    this.loadWarningList()
  },
  onShow() {
    this.loadWarningList()
  },
  methods: {
    // 加载预警列表
    async loadWarningList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getWarningList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.warnings = res.rows.map(item => ({
            warningId: item.id,
            title: item.title,
            location: item.location,
            time: item.time ? item.time.substring(0, 16) : '',
            level: parseInt(item.level) || 1
          }))
          
          // 统计各等级数量
          this.stats.urgent = this.warnings.filter(w => w.level === 1).length
          this.stats.important = this.warnings.filter(w => w.level === 2).length
          this.stats.normal = this.warnings.filter(w => w.level === 3).length
        }
      } catch (e) {
        console.error('获取预警列表失败', e)
      }
    },

    navigateToDetail(item) {
      uni.navigateTo({
        url: `/pages/warning/detail?id=${item.warningId}`
      })
    }
  }
}
</script>

<style lang="scss">
.warning-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 130rpx;
}

// 预警统计
.warning-stats {
  display: flex;
  background: #fff;
  padding: 30rpx;

  .stat-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
    border-radius: 12rpx;
    margin: 0 10rpx;

    &.urgent {
      background: #FFEBEE;
      .stat-num { color: #D32F2F; }
      .stat-label { color: #D32F2F; }
    }

    &.important {
      background: #FFF3E0;
      .stat-num { color: #F57C00; }
      .stat-label { color: #F57C00; }
    }

    &.normal {
      background: #E8F5E9;
      .stat-num { color: #388E3C; }
      .stat-label { color: #388E3C; }
    }

    .stat-num {
      font-size: 48rpx;
      font-weight: bold;
      margin-bottom: 8rpx;
    }

    .stat-label {
      font-size: 26rpx;
    }
  }
}

// 筛选标签
.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 30rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;

  .tab-item {
    padding: 12rpx 30rpx;
    margin-right: 20rpx;
    border-radius: 30rpx;
    font-size: 28rpx;
    color: #666;
    background: #f5f5f5;

    &.active {
      background: #007AFF;
      color: #fff;
    }
  }
}

// 预警列表
.warning-list {
  padding: 20rpx;

  .warning-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .warning-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .warning-level {
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
        font-size: 24rpx;

        &.level-1 {
          background: #FFEBEE;
          color: #D32F2F;
        }

        &.level-2 {
          background: #FFF3E0;
          color: #F57C00;
        }

        &.level-3 {
          background: #E8F5E9;
          color: #388E3C;
        }
      }

      .warning-status {
        font-size: 26rpx;

        &.status-0 { color: #F57C00; }
        &.status-1 { color: #388E3C; }
      }
    }

    .warning-body {
      margin-bottom: 20rpx;

      .warning-title {
        display: block;
        font-size: 32rpx;
        font-weight: 500;
        color: #333;
        margin-bottom: 12rpx;
      }

      .warning-desc {
        font-size: 28rpx;
        color: #666;
      }
    }

    .warning-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .warning-location {
        display: flex;
        align-items: center;
        font-size: 26rpx;
        color: #999;

        .icon {
          margin-right: 8rpx;
        }
      }

      .warning-time {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}
</style>
