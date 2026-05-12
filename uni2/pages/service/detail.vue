<template>
  <view class="container">
    <!-- 订单信息卡片 -->
    <view class="info-card">
      <view class="card-title">订单信息</view>
      <view class="info-item">
        <text class="label">订单类型</text>
        <text class="value">{{order.orderType}}</text>
      </view>
      <view class="info-item">
        <text class="label">问题描述</text>
        <text class="value">{{order.description}}</text>
      </view>
      <view class="info-item">
        <text class="label">报修�?/text>
        <text class="value">{{order.userName}}</text>
      </view>
      <view class="info-item">
        <text class="label">联系电话</text>
        <text class="value">{{order.phone}}</text>
      </view>
      <view class="info-item">
        <text class="label">报修地址</text>
        <text class="value">{{order.address}}</text>
      </view>
      <view class="info-item">
        <text class="label">提交时间</text>
        <text class="value">{{order.createTime}}</text>
      </view>
      <view class="info-item">
        <text class="label">当前状�?/text>
        <view class="status-tag" :class="'status-' + (order.status || '0')">
          {{getStatusText(order.status || '0')}}
        </view>
      </view>
      <view class="info-item" v-if="order.handler">
        <text class="label">处理�?/text>
        <text class="value">{{order.handler}}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="(order.status || '0') !== '2'">
      <button v-if="(order.status || '0') === '0'" class="action-btn btn-primary" @click="assignOrder">分配工单</button>
      <button v-if="(order.status || '0') === '1'" class="action-btn btn-success" @click="completeOrder">标记完成</button>
    </view>
  </view>
</template>

<script>
import { getOrderDetail, assignOrder, updateOrderStatus } from '@/api/service.js'

export default {
  data() {
    return {
      orderId: null,
      order: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.orderId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({
        title: '加载�?..'
      })

      try {
        const res = await getOrderDetail(this.orderId)
        if (res.code === 200 && res.data) {
          this.order = res.data
        }
      } catch (e) {
        console.error('加载订单详情失败', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },

    getStatusText(status) {
      const map = { '0': '待分�?, '1': '处理�?, '2': '已完�? }
      return map[status] || '待分�?
    },

    assignOrder() {
      uni.showActionSheet({
        itemList: ['分配给维修组A', '分配给维修组B', '分配给维修组C'],
        success: async (res) => {
          const handlers = ['维修组A', '维修组B', '维修组C']
          try {
            const updateData = {
              ...this.order,
              status: '1',
              handler: handlers[res.tapIndex]
            }
            const result = await assignOrder(updateData)
            if (result.code === 200) {
              uni.showToast({
                title: '分配成功',
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
      })
    },

    completeOrder() {
      uni.showModal({
        title: '确认',
        content: '确定要标记该订单为已完成吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...this.order,
                status: '2'
              }
              const result = await updateOrderStatus(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: '操作成功',
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
  background: #FF6B6B;
}

.status-1 {
  background: #FFA502;
}

.status-2 {
  background: #2ED573;
}

.action-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.action-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 30rpx;
  border-radius: 40rpx;
  border: none;
}

.btn-primary {
  background: #667eea;
  color: #fff;
}

.btn-success {
  background: #2ED573;
  color: #fff;
}
</style>

