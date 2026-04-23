<template>
  <view class="detail-container">
    <!-- 状态卡片 -->
    <view class="status-card">
      <view class="status-icon" :class="getStatusClass(detail.status)">
        {{ getStatusIcon(detail.status) }}
      </view>
      <text class="status-text">{{ getStatusText(detail.status) }}</text>
    </view>

    <!-- 访客信息 -->
    <view class="info-section">
      <view class="section-title">访客信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">访客姓名</text>
          <text class="value">{{ detail.visitorName }}</text>
        </view>
        <view class="info-item">
          <text class="label">联系电话</text>
          <text class="value">{{ detail.visitorPhone }}</text>
        </view>
        <view class="info-item">
          <text class="label">访客人数</text>
          <text class="value">{{ detail.visitorCount }}人</text>
        </view>
      </view>
    </view>

    <!-- 访问信息 -->
    <view class="info-section">
      <view class="section-title">访问信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">访问日期</text>
          <text class="value">{{ detail.visitDate }}</text>
        </view>
        <view class="info-item">
          <text class="label">访问时间</text>
          <text class="value">{{ formatTime(detail.visitTimeStart) }} - {{ formatTime(detail.visitTimeEnd) }}</text>
        </view>
        <view class="info-item">
          <text class="label">访问事由</text>
          <text class="value">{{ detail.visitReason || '无' }}</text>
        </view>
        <view class="info-item" v-if="detail.remark">
          <text class="label">备注</text>
          <text class="value">{{ detail.remark }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="detail.status === '0'">
      <view class="cancel-btn" @click="cancelAppointment">
        <text>取消预约</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getVisitorDetail, updateVisitor } from '@/api/visitor.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      detail: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.loadDetail(options.id)
    }
  },
  methods: {
    // 加载详情
    async loadDetail(id) {
      try {
        const res = await getVisitorDetail(id)
        if (res.code === 200 && res.data) {
          this.detail = res.data
        }
      } catch (e) {
        console.error('获取详情失败', e)
        uni.showToast({ title: '获取详情失败', icon: 'none' })
      }
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

    // 获取状态图标
    getStatusIcon(status) {
      const iconMap = {
        '0': '⏳',
        '1': '✅',
        '2': '❌',
        '3': '⌛',
        '4': '🚫'
      }
      return iconMap[status] || '❓'
    },

    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return ''
      if (timeStr.includes(' ')) {
        return timeStr.split(' ')[1].substring(0, 5)
      }
      if (timeStr.includes(':')) {
        return timeStr.substring(0, 5)
      }
      return timeStr
    },

    // 取消预约
    cancelAppointment() {
      uni.showModal({
        title: '提示',
        content: '确定要取消该预约吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const result = await updateVisitor({
                id: this.detail.id,
                status: '4'
              })
              if (result.code === 200) {
                uni.showToast({ title: '取消成功', icon: 'success' })
                this.detail.status = '4'
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
.detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

// 状态卡片
.status-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 60rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20rpx;

  .status-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
  }

  .status-text {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }
}

// 信息区块
.info-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  .info-list {
    .info-item {
      display: flex;
      justify-content: space-between;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .label {
        font-size: 28rpx;
        color: #999;
      }

      .value {
        font-size: 28rpx;
        color: #333;
      }
    }
  }
}

// 操作区
.action-section {
  padding: 30rpx;

  .cancel-btn {
    background: #FF6B6B;
    color: #fff;
    text-align: center;
    padding: 30rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
  }
}
</style>
