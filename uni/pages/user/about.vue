<template>
  <view class="about-container">
    <!-- Logo区域 -->
    <view class="logo-section">
      <image class="app-logo" src="/static/logo.png" mode="aspectFit"></image>
      <text class="app-name">智慧物业</text>
      <text class="app-version">版本 {{ version }}</text>
    </view>

    <!-- 应用介绍 -->
    <view class="intro-section">
      <view class="section-title">应用介绍</view>
      <text class="intro-text">
        智慧物业是一款为现代社区打造的智能物业管理平台，集成了智能家居控制、物业服务、安全预警、家人健康关怀等功能，致力于为业主提供便捷、安全、舒适的居住体验。
      </text>
    </view>

    <!-- 核心功能 -->
    <view class="features-section">
      <view class="section-title">核心功能</view>
      <view class="feature-list">
        <view class="feature-item">
          <text class="feature-icon">🏠</text>
          <view class="feature-info">
            <text class="feature-name">智能家居</text>
            <text class="feature-desc">远程控制家中设备，智能场景联动</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🛡️</text>
          <view class="feature-info">
            <text class="feature-name">安全预警</text>
            <text class="feature-desc">实时安全监控，异常情况及时提醒</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">👴</text>
          <view class="feature-info">
            <text class="feature-name">家人健康</text>
            <text class="feature-desc">关注家人健康状态，异常及时预警</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🔧</text>
          <view class="feature-info">
            <text class="feature-name">物业服务</text>
            <text class="feature-desc">在线报修、缴费，服务进度实时跟踪</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 联系我们 -->
    <view class="contact-section">
      <view class="section-title">联系我们</view>
      <view class="contact-list">
        <view class="contact-item" @click="makeCall">
          <text class="contact-label">客服电话</text>
          <text class="contact-value">400-888-8888</text>
        </view>
        <view class="contact-item">
          <text class="contact-label">官方网站</text>
          <text class="contact-value">www.smartproperty.com</text>
        </view>
        <view class="contact-item">
          <text class="contact-label">电子邮箱</text>
          <text class="contact-value">support@smartproperty.com</text>
        </view>
      </view>
    </view>

    <!-- 版权信息 -->
    <view class="footer">
      <text class="copyright">© 2024 智慧物业 版权所有</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      version: '1.0.0'
    }
  },
  methods: {
    makeCall() {
      uni.makePhoneCall({
        phoneNumber: '400-888-8888'
      })
    }
  }
}
</script>

<style lang="scss">
.about-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
  background: #fff;

  .app-logo {
    width: 160rpx;
    height: 160rpx;
    margin-bottom: 20rpx;
  }

  .app-name {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 12rpx;
  }

  .app-version {
    font-size: 26rpx;
    color: #999;
  }
}

.intro-section, .features-section, .contact-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  padding-left: 20rpx;
  border-left: 6rpx solid #007AFF;
}

.intro-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.feature-list {
  .feature-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .feature-icon {
      font-size: 48rpx;
      margin-right: 24rpx;
    }

    .feature-info {
      flex: 1;

      .feature-name {
        font-size: 30rpx;
        color: #333;
        font-weight: 500;
        display: block;
        margin-bottom: 8rpx;
      }

      .feature-desc {
        font-size: 26rpx;
        color: #999;
      }
    }
  }
}

.contact-list {
  .contact-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .contact-label {
      font-size: 28rpx;
      color: #666;
    }

    .contact-value {
      font-size: 28rpx;
      color: #007AFF;
    }
  }
}

.footer {
  text-align: center;
  padding: 40rpx 0;

  .copyright {
    font-size: 24rpx;
    color: #999;
  }
}
</style>
