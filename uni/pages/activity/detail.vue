<template>
  <view class="activity-detail-container">
    <!-- 活动图片 -->
    <image class="activity-image" :src="getImageUrl(activity.image)" mode="aspectFill"></image>
    
    <!-- 活动信息 -->
    <view class="activity-info">
      <text class="activity-title">{{ activity.title }}</text>
      <view class="info-item">
        <text class="info-label">活动时间</text>
        <text class="info-value">{{ activity.time }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">活动地点</text>
        <text class="info-value">{{ activity.location }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">活动状�?/text>
        <text class="info-value status" :class="'status-' + activity.status">{{ getStatusText(activity.status) }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">报名人数</text>
        <text class="info-value">{{ activity.joinCount }}/{{ activity.maxCount }}�?/text>
      </view>
    </view>

    <!-- 活动详情 -->
    <view class="activity-content">
      <text class="content-title">活动详情</text>
      <text class="content-text">{{ activity.content || '暂无活动详情' }}</text>
    </view>

    <!-- 报名按钮 -->
    <view class="action-section">
      <button 
        class="join-btn" 
        :class="{ disabled: !canJoin }"
        :disabled="!canJoin"
        @click="joinActivity"
      >
        {{ getBtnText() }}
      </button>
    </view>
  </view>
</template>

<script>
import { getActivityDetail, joinActivity, getJoinedActivityList } from '@/api/activity.js'
import config from '@/config/index.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      activityId: null,
      activity: {
        title: '',
        time: '',
        location: '',
        status: 0,
        joinCount: 0,
        maxCount: 0,
        content: '',
        image: ''
      },
      joinedActivityIds: [],
      userId: 1
    }
  },
  computed: {
    canJoin() {
      // 可以报名的条件：状态为报名中、未满员、未报名�?      return this.activity.status === 0 && 
             this.activity.joinCount < this.activity.maxCount &&
             !this.joinedActivityIds.includes(this.activityId)
    }
  },
  onLoad(options) {
    this.activityId = parseInt(options.id)
    const userInfo = uni.getStorageSync('userInfo')
    this.userId = userInfo ? userInfo.userId : 1
    this.loadActivityDetail()
    this.loadJoinedActivities()
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        0: '报名�?,
        1: '进行�?,
        2: '已结�?
      }
      return statusMap[status] || '未知'
    },
    getImageUrl(image) {
      if (!image) return '/static/logo.png'
      if (image.startsWith('http')) return image
      return config.baseUrl + config.apiPrefix + image
    },
    getBtnText() {
      if (this.joinedActivityIds.includes(this.activityId)) return '已报�?
      if (this.activity.status === 2) return '活动已结�?
      if (this.activity.status === 1) return '活动进行�?
      if (this.activity.joinCount >= this.activity.maxCount) return '名额已满'
      return '立即报名'
    },
    async loadActivityDetail() {
      try {
        const res = await getActivityDetail(this.activityId)
        if (res.code === 200 && res.data) {
          this.activity = res.data
        }
      } catch (e) {
        console.error('获取活动详情失败', e)
      }
    },
    async loadJoinedActivities() {
      try {
        const res = await getJoinedActivityList({
          pageNum: 1,
          pageSize: 100,
          userId: this.userId
        })
        if (res.code === 200 && res.rows) {
          // 提取已报名的活动ID列表
          this.joinedActivityIds = res.rows.map(item => item.activityId)
        }
      } catch (e) {
        console.error('获取已报名活动失�?, e)
      }
    },
    async joinActivity() {
      if (!this.canJoin) return
      
      uni.showModal({
        title: '确认报名',
        content: `确认报名参加"${this.activity.title}"吗？`,
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '报名�?..' })
            try {
              const joinRes = await joinActivity({
                activityId: this.activityId.toString(),
                userId: this.userId.toString(),
                joinTime: new Date().toISOString().split('T')[0] + ' 00:00:00',
                status: 1
              })
              uni.hideLoading()
              
              if (joinRes.code === 200) {
                uni.showToast({ title: '报名成功', icon: 'success' })
                this.joinedActivityIds.push(this.activityId)
                this.activity.joinCount++
              } else {
                uni.showToast({ title: joinRes.msg || '报名失败', icon: 'none' })
              }
            } catch (e) {
              uni.hideLoading()
              console.error('报名失败', e)
              uni.showToast({ title: '报名失败', icon: 'none' })
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.activity-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;

  .activity-image {
    width: 100%;
    height: 400rpx;
  }

  .activity-info {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .activity-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 24rpx;
    }

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16rpx 0;
      border-bottom: 1rpx solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .info-label {
        font-size: 28rpx;
        color: #999;
      }

      .info-value {
        font-size: 28rpx;
        color: #333;

        &.status {
          padding: 6rpx 16rpx;
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
      }
    }
  }

  .activity-content {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .content-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 20rpx;
    }

    .content-text {
      font-size: 28rpx;
      color: #666;
      line-height: 1.6;
    }
  }

  .action-section {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx 30rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.1);

    .join-btn {
      width: 100%;
      height: 90rpx;
      line-height: 90rpx;
      background: #007AFF;
      color: #fff;
      font-size: 32rpx;
      border-radius: 12rpx;
      border: none;

      &:active {
        opacity: 0.8;
      }

      &.disabled {
        background: #ccc;
      }
    }
  }
}
</style>




