<template>
  <view class="care-mode-container" :class="{ 'care-mode': true }">
    <!-- 大字体标题 -->
    <view class="care-header">
      <text class="care-title">👴 关怀模式</text>
      <text class="care-subtitle">简单操作，大字显示</text>
    </view>

    <!-- 常用功能大按钮 -->
    <view class="care-grid">
      <view class="care-btn-large" @click="navigateTo('/pages/care/index')">
        <text class="btn-icon">❤️</text>
        <text class="btn-text">家人健康</text>
      </view>
      <view class="care-btn-large" @click="navigateTo('/pages/user/payment')">
        <text class="btn-icon">💰</text>
        <text class="btn-text">缴费记录</text>
      </view>
      <view class="care-btn-large" @click="navigateTo('/pages/service/index')">
        <text class="btn-icon">🔧</text>
        <text class="btn-text">物业服务</text>
      </view>
      <view class="care-btn-large" @click="makePhoneCall">
        <text class="btn-icon">📞</text>
        <text class="btn-text">紧急呼叫</text>
      </view>
    </view>

    <!-- 常用电话 -->
    <view class="care-section">
      <text class="section-title">常用电话</text>
      <view class="phone-list">
        <view class="phone-item" @click="makePhoneCall('400-888-8888')">
          <text class="phone-name">物业服务中心</text>
          <text class="phone-number">400-888-8888</text>
        </view>
        <view class="phone-item" @click="makePhoneCall('400-999-9999')">
          <text class="phone-name">24小时报修</text>
          <text class="phone-number">400-999-9999</text>
        </view>
        <view class="phone-item" @click="makePhoneCall('120')">
          <text class="phone-name">急救电话</text>
          <text class="phone-number">120</text>
        </view>
      </view>
    </view>

    <!-- 退出关怀模式 -->
    <view class="exit-btn" @click="exitCareMode">
      <text>退出关怀模式</text>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    navigateTo(path) {
      uni.navigateTo({ url: path })
    },
    makePhoneCall(phone) {
      if (!phone) {
        phone = '400-888-8888'
      }
      uni.makePhoneCall({ phoneNumber: phone })
    },
    exitCareMode() {
      uni.setStorageSync('careMode', false)
      uni.showToast({ title: '已退出关怀模式', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/user/index' })
      }, 1500)
    }
  }
}
</script>

<style lang="scss">
.care-mode-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40rpx;

  .care-header {
    text-align: center;
    padding: 60rpx 0;

    .care-title {
      font-size: 56rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 20rpx;
    }

    .care-subtitle {
      font-size: 36rpx;
      color: #666;
    }
  }

  .care-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 30rpx;
    margin-bottom: 40rpx;

    .care-btn-large {
      width: calc(50% - 15rpx);
      background: #fff;
      border-radius: 24rpx;
      padding: 60rpx 40rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.1);

      &:active {
        opacity: 0.8;
      }

      .btn-icon {
        font-size: 80rpx;
        margin-bottom: 20rpx;
      }

      .btn-text {
        font-size: 40rpx;
        font-weight: bold;
        color: #333;
      }
    }
  }

  .care-section {
    background: #fff;
    border-radius: 24rpx;
    padding: 40rpx;
    margin-bottom: 40rpx;

    .section-title {
      font-size: 44rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 30rpx;
    }

    .phone-list {
      .phone-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 30rpx 0;
        border-bottom: 2rpx solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        &:active {
          background: #f5f5f5;
        }

        .phone-name {
          font-size: 40rpx;
          color: #333;
        }

        .phone-number {
          font-size: 40rpx;
          color: #007AFF;
          font-weight: bold;
        }
      }
    }
  }

  .exit-btn {
    background: #FF6B6B;
    color: #fff;
    font-size: 40rpx;
    padding: 40rpx;
    border-radius: 24rpx;
    text-align: center;
    font-weight: bold;

    &:active {
      opacity: 0.8;
    }
  }
}
</style>
