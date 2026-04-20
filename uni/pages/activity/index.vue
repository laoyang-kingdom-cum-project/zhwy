<template>
  <view class="activity-container">
    <!-- 活动列表 -->
    <view class="activity-list">
      <view 
        class="activity-item" 
        v-for="(item, index) in activities" 
        :key="index"
        @click="goToDetail(item)"
      >
        <image class="activity-image" :src="getImageUrl(item.image)" mode="aspectFill"></image>
        <view class="activity-info">
          <text class="activity-title">{{ item.title }}</text>
          <text class="activity-time">⏰ {{ item.time }}</text>
          <text class="activity-location">📍 {{ item.location }}</text>
          <view class="activity-footer">
            <text class="activity-status" :class="'status-' + item.status">{{ getStatusText(item.status) }}</text>
            <text class="activity-count">已报名 {{ item.joinCount }}/{{ item.maxCount }}人</text>
          </view>
        </view>
      </view>
      <view class="empty-tip" v-if="activities.length === 0">暂无活动</view>
    </view>
  </view>
</template>

<script>
import { getActivityList } from '@/api/activity.js'
import config from '@/config/index.js'

export default {
  data() {
    return {
      activities: []
    }
  },
  onLoad() {
    this.loadActivityList()
  },
  onShow() {
    this.loadActivityList()
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        0: '报名中',
        1: '进行中',
        2: '已结束'
      }
      return statusMap[status] || '未知'
    },
    getImageUrl(image) {
      if (!image) return '/static/logo.png'
      if (image.startsWith('http')) return image
      return config.baseUrl + config.apiPrefix + image
    },
    async loadActivityList() {
      try {
        const res = await getActivityList({
          pageNum: 1,
          pageSize: 100
        })
        if (res.code === 200 && res.rows) {
          this.activities = res.rows
        }
      } catch (e) {
        console.error('获取活动列表失败', e)
      }
    },
    goToDetail(item) {
      uni.navigateTo({
        url: `/pages/activity/detail?id=${item.id}`
      })
    }
  }
}
</script>

<style lang="scss">
.activity-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.activity-list {
  .activity-item {
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 20rpx;

    &:active {
      opacity: 0.9;
    }

    .activity-image {
      width: 100%;
      height: 300rpx;
    }

    .activity-info {
      padding: 24rpx;

      .activity-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        display: block;
        margin-bottom: 16rpx;
      }

      .activity-time,
      .activity-location {
        font-size: 26rpx;
        color: #666;
        display: block;
        margin-bottom: 8rpx;
      }

      .activity-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 16rpx;

        .activity-status {
          padding: 8rpx 20rpx;
          border-radius: 8rpx;
          font-size: 24rpx;

          &.status-0 {
            background: #E8F5E9;
            color: #4CAF50;
          }

          &.status-1 {
            background: #E3F2FD;
            color: #2196F3;
          }

          &.status-2 {
            background: #F5F5F5;
            color: #999;
          }
        }

        .activity-count {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
  }

  .empty-tip {
    text-align: center;
    padding: 100rpx 0;
    color: #999;
    font-size: 28rpx;
  }
}
</style>
