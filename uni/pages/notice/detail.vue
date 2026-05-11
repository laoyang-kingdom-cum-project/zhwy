<template>
  <view class="notice-detail-container">
    <!-- 加载�?-->
    <view v-if="loading" class="loading">
      <text>加载�?..</text>
    </view>
    
    <!-- 公告内容 -->
    <view v-else class="content">
      <view class="notice-header">
        <text class="notice-title">{{ notice.noticeTitle }}</text>
        <view class="notice-meta">
          <text class="notice-type" :class="notice.noticeType === '1' ? 'notice' : 'warning'">
            {{ notice.noticeType === '1' ? '通知' : '公告' }}
          </text>
          <text class="notice-time">{{ notice.createTime }}</text>
        </view>
      </view>
      
      <view class="notice-body">
        <rich-text :nodes="notice.noticeContent"></rich-text>
      </view>
    </view>
  </view>
</template>

<script>
import { getNoticeDetail } from '@/api/home.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      noticeId: '',
      notice: {},
      loading: true
    }
  },
  onLoad(options) {
    this.noticeId = options.id
    if (this.noticeId) {
      this.loadNoticeDetail()
    } else {
      uni.showToast({ title: '公告ID不能为空', icon: 'none' })
      uni.navigateBack()
    }
  },
  methods: {
    async loadNoticeDetail() {
      this.loading = true
      try {
        const res = await getNoticeDetail(this.noticeId)
        if (res.code === 200 && res.data) {
          this.notice = res.data
        } else {
          uni.showToast({ title: '获取公告详情失败', icon: 'none' })
        }
      } catch (e) {
        console.error('获取公告详情失败', e)
        uni.showToast({ title: '获取公告详情失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss">
.notice-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;

  .loading {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 400rpx;
    color: #999;
    font-size: 28rpx;
  }

  .content {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;

    .notice-header {
      border-bottom: 1rpx solid #f0f0f0;
      padding-bottom: 20rpx;
      margin-bottom: 20rpx;

      .notice-title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
        line-height: 1.5;
        display: block;
        margin-bottom: 16rpx;
      }

      .notice-meta {
        display: flex;
        align-items: center;

        .notice-type {
          padding: 4rpx 16rpx;
          border-radius: 8rpx;
          font-size: 22rpx;
          margin-right: 20rpx;

          &.notice {
            background: #E3F2FD;
            color: #1976D2;
          }

          &.warning {
            background: #FFF3E0;
            color: #F57C00;
          }
        }

        .notice-time {
          font-size: 24rpx;
          color: #999;
        }
      }
    }

    .notice-body {
      font-size: 30rpx;
      color: #333;
      line-height: 1.8;

      rich-text {
        width: 100%;
      }
    }
  }
}
</style>




