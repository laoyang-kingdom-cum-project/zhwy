<template>
  <view class="container">
    <!-- 预警信息卡片 -->
    <view class="info-card">
      <view class="card-title">预警信息</view>
      <view class="info-item">
        <text class="label">预警标题</text>
        <text class="value">{{warning.title}}</text>
      </view>
      <view class="info-item">
        <text class="label">预警等级</text>
        <view class="level-tag" :class="'level-' + warning.level">{{warning.levelText}}</view>
      </view>
      <view class="info-item">
        <text class="label">预警位置</text>
        <text class="value">{{warning.location}}</text>
      </view>
      <view class="info-item">
        <text class="label">预警时间</text>
        <text class="value">{{warning.time}}</text>
      </view>
      <view class="info-item">
        <text class="label">当前状态</text>
        <view class="status-tag" :class="'status-' + (warning.status || '0')">{{warning.statusText}}</view>
      </view>
      <view class="info-item" v-if="warning.remark">
        <text class="label">备注说明</text>
        <text class="value">{{warning.remark}}</text>
      </view>
    </view>
    
    <!-- AI应急方案 -->
    <view class="ai-card" v-if="emergencyPlan">
      <view class="card-title">
        <text class="ai-icon">🤖</text>
        <text>AI应急方案</text>
      </view>
      <view class="plan-content">
        <text class="plan-text">{{emergencyPlan}}</text>
      </view>
    </view>
    
    <!-- 周边资源 -->
    <view class="resource-card" v-if="resources.length > 0">
      <view class="card-title">周边应急资源</view>
      <view class="resource-list">
        <view class="resource-item" v-for="(item, index) in resources" :key="index" @click="callPhone(item.phone)">
          <view class="resource-info">
            <text class="resource-name">{{item.name}}</text>
            <text class="resource-address">{{item.address}}</text>
          </view>
          <view class="resource-phone">
            <text class="phone-icon">📞</text>
            <text class="phone-num">{{item.phone}}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 处理记录 -->
    <view class="record-card" v-if="warning.handleTime">
      <view class="card-title">处理记录</view>
      <view class="record-item">
        <text class="record-label">处理时间</text>
        <text class="record-value">{{warning.handleTime}}</text>
      </view>
      <view class="record-item" v-if="warning.handleBy">
        <text class="record-label">处理人</text>
        <text class="record-value">{{warning.handleBy}}</text>
      </view>
      <view class="record-item" v-if="warning.handleResult">
        <text class="record-label">处理结果</text>
        <text class="record-value">{{warning.handleResult}}</text>
      </view>
    </view>
    
    <!-- 底部操作按钮 -->
    <view class="footer-actions" v-if="(warning.status || '0') !== '2'">
      <button v-if="(warning.status || '0') === '0'" class="action-btn btn-primary" @click="handleAction('1')">接单处理</button>
      <button v-if="(warning.status || '0') === '1'" class="action-btn btn-success" @click="handleAction('2')">标记完成</button>
      <button class="action-btn btn-default" @click="contactSupport">联系支援</button>
    </view>
  </view>
</template>

<script>
import { getWarningDetail, handleWarning } from '@/api/warning.js'
import { getEmergencyPlan, getEmergencyResources } from '@/api/ai.js'

export default {
  data() {
    return {
      warningId: null,
      warning: {},
      emergencyPlan: '',
      resources: []
    }
  },
  onLoad(options) {
    if (options.id) {
      this.warningId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({
        title: '加载中...'
      })
      
      try {
        // 获取预警详情
        const res = await getWarningDetail(this.warningId)
        if (res.code === 200 && res.data) {
          this.warning = {
            ...res.data,
            levelText: this.getLevelText(res.data.level),
            statusText: this.getStatusText(res.data.status || '0')
          }
          
          // 获取AI应急方案
          this.loadEmergencyPlan(res.data.level, res.data.title)
          
          // 获取周边资源
          this.loadResources(res.data.location)
        }
      } catch (e) {
        console.error('加载预警详情失败', e)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },
    
    async loadEmergencyPlan(level, title) {
      try {
        // 根据预警类型获取应急方案
        const type = title.includes('火') ? 'fire' : 
                     title.includes('燃气') ? 'gas' : 
                     title.includes('入侵') ? 'intrusion' : 'default'
        const res = await getEmergencyPlan(type)
        if (res.code === 200 && res.data) {
          this.emergencyPlan = res.data.plan || '请根据现场情况采取相应措施，确保人员安全。'
        }
      } catch (e) {
        console.error('获取应急方案失败', e)
        this.emergencyPlan = '请根据现场情况采取相应措施，确保人员安全。'
      }
    },
    
    async loadResources(location) {
      try {
        const res = await getEmergencyResources({ location })
        if (res.code === 200 && res.data) {
          this.resources = res.data.list || []
        }
      } catch (e) {
        console.error('获取应急资源失败', e)
      }
    },
    
    getLevelText(level) {
      const map = { '1': '紧急', '2': '重要', '3': '一般' }
      return map[level] || '一般'
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    handleAction(status) {
      const statusText = status === '1' ? '接单' : '完成'
      uni.showModal({
        title: '确认',
        content: `确定要${statusText}该预警吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...this.warning,
                status: status
              }
              const result = await handleWarning(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: `${statusText}成功`,
                  icon: 'success'
                })
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
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
    },
    
    callPhone(phone) {
      uni.makePhoneCall({
        phoneNumber: phone
      })
    },
    
    contactSupport() {
      uni.makePhoneCall({
        phoneNumber: '110'
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

.info-card, .ai-card, .resource-card, .record-card {
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
  display: flex;
  align-items: center;
}

.ai-icon {
  margin-right: 10rpx;
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

.level-tag, .status-tag {
  padding: 6rpx 20rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #fff;
}

.level-1 {
  background: #FF6B6B;
}

.level-2 {
  background: #FFA502;
}

.level-3 {
  background: #2ED573;
}

.status-0 {
  background: #FFE5E5;
  color: #FF6B6B;
}

.status-1 {
  background: #FFF4E5;
  color: #FFA502;
}

.status-2 {
  background: #E5F5E5;
  color: #2ED573;
}

.plan-content {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 24rpx;
  border-left: 6rpx solid #667eea;
}

.plan-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.resource-list {
  margin-top: 10rpx;
}

.resource-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
}

.resource-info {
  flex: 1;
}

.resource-name {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 8rpx;
}

.resource-address {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.resource-phone {
  display: flex;
  align-items: center;
  background: #667eea;
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
}

.phone-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.phone-num {
  font-size: 26rpx;
  color: #fff;
}

.record-item {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f5f7fa;
}

.record-item:last-child {
  border-bottom: none;
}

.record-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #666;
}

.record-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.footer-actions {
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
  margin: 0 10rpx;
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

.btn-default {
  background: #f5f7fa;
  color: #666;
}
</style>
