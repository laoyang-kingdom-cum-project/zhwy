<template>
  <view class="container">
    <!-- 故障信息卡片 -->
    <view class="info-card">
      <view class="card-title">故障信息</view>
      <view class="info-item">
        <text class="label">故障设备</text>
        <text class="value">{{fault.deviceName}}</text>
      </view>
      <view class="info-item">
        <text class="label">故障类型</text>
        <text class="value">{{fault.faultType}}</text>
      </view>
      <view class="info-item">
        <text class="label">故障描述</text>
        <text class="value">{{fault.faultDesc}}</text>
      </view>
      <view class="info-item">
        <text class="label">上报时间</text>
        <text class="value">{{fault.reportTime}}</text>
      </view>
      <view class="info-item">
        <text class="label">处理状�?/text>
        <view class="status-tag" :class="'status-' + (fault.handleStatus || '0')">
          {{getStatusText(fault.handleStatus || '0')}}
        </view>
      </view>
      <view class="info-item" v-if="fault.handleTime">
        <text class="label">处理时间</text>
        <text class="value">{{fault.handleTime}}</text>
      </view>
      <view class="info-item" v-if="fault.handleResult">
        <text class="label">处理结果</text>
        <text class="value">{{fault.handleResult}}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section" v-if="(fault.handleStatus || '0') !== '2'">
      <button v-if="(fault.handleStatus || '0') === '0'" class="action-btn btn-primary" @click="handleFault('1')">开始处�?/button>
      <button v-if="(fault.handleStatus || '0') === '1'" class="action-btn btn-success" @click="completeFault">完成处理</button>
    </view>
  </view>
</template>

<script>
import { getFaultDetail, handleFault } from '@/api/fault.js'

export default {
  data() {
    return {
      faultId: null,
      fault: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.faultId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({
        title: '加载�?..'
      })

      try {
        const res = await getFaultDetail(this.faultId)
        if (res.code === 200 && res.data) {
          this.fault = res.data
        }
      } catch (e) {
        console.error('加载故障详情失败', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },

    getStatusText(status) {
      const map = { '0': '待处�?, '1': '处理�?, '2': '已处�? }
      return map[status] || '待处�?
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

    handleFault(status) {
      uni.showModal({
        title: '确认',
        content: '确定要开始处理该故障吗？',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '处理�?..' })
            try {
              const updateData = {
                id: this.fault.id,
                handleStatus: status,
                handleTime: this.formatDateTime(new Date())
              }
              console.log('更新故障数据:', updateData)
              const result = await handleFault(updateData)
              console.log('更新故障结果:', result)
              uni.hideLoading()
              if (result.code === 200) {
                uni.showToast({ title: '操作成功', icon: 'success' })
                this.loadDetail()
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

    completeFault() {
      uni.showModal({
        title: '处理结果',
        editable: true,
        placeholderText: '请输入处理结�?..',
        success: async (res) => {
          if (res.confirm && res.content) {
            uni.showLoading({ title: '提交�?..' })
            try {
              const updateData = {
                id: this.fault.id,
                handleStatus: '2',
                handleResult: res.content
              }
              console.log('完成故障数据:', updateData)
              const result = await handleFault(updateData)
              console.log('完成故障结果:', result)
              uni.hideLoading()
              if (result.code === 200) {
                uni.showToast({ title: '处理完成', icon: 'success' })
                this.loadDetail()
              } else {
                uni.showToast({ title: result.msg || '操作失败', icon: 'none' })
              }
            } catch (e) {
              uni.hideLoading()
              console.error('完成故障失败:', e)
              uni.showToast({ title: e.message || '操作失败', icon: 'none' })
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

