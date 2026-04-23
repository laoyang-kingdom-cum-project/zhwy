<template>
  <view class="container">
    <!-- 访客信息卡片 -->
    <view class="info-card">
      <view class="card-title">访客信息</view>
      <view class="info-item">
        <text class="label">访客姓名</text>
        <text class="value">{{visitor.visitorName}}</text>
      </view>
      <view class="info-item">
        <text class="label">联系电话</text>
        <text class="value">{{visitor.visitorPhone}}</text>
      </view>
      <view class="info-item">
        <text class="label">身份证号</text>
        <text class="value">{{visitor.idCard || '未填写'}}</text>
      </view>
    </view>

    <!-- 访问信息卡片 -->
    <view class="info-card">
      <view class="card-title">访问信息</view>
      <view class="info-item">
        <text class="label">被访人</text>
        <text class="value">{{visitor.hostName}}</text>
      </view>
      <view class="info-item">
        <text class="label">访问地址</text>
        <text class="value">{{visitor.address}}</text>
      </view>
      <view class="info-item">
        <text class="label">访问时间</text>
        <text class="value">{{visitor.visitTime}}</text>
      </view>
      <view class="info-item">
        <text class="label">来访事由</text>
        <text class="value">{{visitor.reason}}</text>
      </view>
      <view class="info-item" v-if="visitor.remark">
        <text class="label">备注</text>
        <text class="value">{{visitor.remark}}</text>
      </view>
      <view class="info-item">
        <text class="label">申请时间</text>
        <text class="value">{{visitor.createTime}}</text>
      </view>
      <view class="info-item">
        <text class="label">审核状态</text>
        <view class="status-tag" :class="'status-' + (visitor.status || '0')">
          {{getStatusText(visitor.status || '0')}}
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="(visitor.status || '0') === '0'">
      <button class="action-btn btn-danger" @click="auditVisitor('2')">拒绝</button>
      <button class="action-btn btn-success" @click="auditVisitor('1')">通过</button>
    </view>
  </view>
</template>

<script>
import { getVisitorDetail, auditVisitor } from '@/api/visitor.js'

export default {
  data() {
    return {
      visitorId: null,
      visitor: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.visitorId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({
        title: '加载中...'
      })

      try {
        const res = await getVisitorDetail(this.visitorId)
        if (res.code === 200 && res.data) {
          this.visitor = res.data
        }
      } catch (e) {
        console.error('加载访客详情失败', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },

    getStatusText(status) {
      const map = { '0': '待审核', '1': '已通过', '2': '已拒绝' }
      return map[status] || '待审核'
    },

    auditVisitor(status) {
      const statusText = status === '1' ? '通过' : '拒绝'
      uni.showModal({
        title: '确认',
        content: `确定要${statusText}该访客预约吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...this.visitor,
                status: status
              }
              const result = await auditVisitor(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: '审核成功',
                  icon: 'success'
                })
                this.loadDetail()
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
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20rpx;
  padding-bottom: 140rpx;
}

.info-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.card-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f7fa;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 28rpx;
  color: #666;
}

.value {
  font-size: 28rpx;
  color: #333;
  max-width: 400rpx;
  text-align: right;
}

.status-tag {
  padding: 6rpx 20rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #fff;
}

.status-0 {
  background: #FFA502;
}

.status-1 {
  background: #2ED573;
}

.status-2 {
  background: #FF6B6B;
}

.action-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.action-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 30rpx;
  border-radius: 40rpx;
  border: none;
  margin: 0 10rpx;
}

.btn-danger {
  background: #FF6B6B;
  color: #fff;
}

.btn-success {
  background: #2ED573;
  color: #fff;
}
</style>
