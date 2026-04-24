<template>
  <view class="about-container">
    <!-- Logo区域 -->
    <view class="logo-section">
      <view class="app-logo">🏢</view>
      <text class="app-name">智慧社区物业端</text>
      <text class="app-version">版本 {{ version }}</text>
    </view>

    <!-- 应用介绍 -->
    <view class="intro-section">
      <view class="section-title">应用介绍</view>
      <text class="intro-text">
        智慧社区物业端是一款专为物业管理打造的高效工作平台，集成预警处置、设备监控、故障维修、服务订单、访客审核等功能，帮助物业人员实现数字化、智能化管理，提升服务效率和业主满意度。
      </text>
    </view>

    <!-- 核心功能 -->
    <view class="features-section">
      <view class="section-title">核心功能</view>
      <view class="feature-list">
        <view class="feature-item">
          <text class="feature-icon">🚨</text>
          <view class="feature-info">
            <text class="feature-name">预警处置</text>
            <text class="feature-desc">实时接收安全预警，快速响应处理</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">📹</text>
          <view class="feature-info">
            <text class="feature-name">设备监控</text>
            <text class="feature-desc">实时监控设备状态，故障及时预警</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🔧</text>
          <view class="feature-info">
            <text class="feature-name">故障维修</text>
            <text class="feature-desc">在线派单跟踪，维修进度实时掌握</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">📋</text>
          <view class="feature-info">
            <text class="feature-name">服务订单</text>
            <text class="feature-desc">居民报修服务，全流程闭环管理</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">📝</text>
          <view class="feature-info">
            <text class="feature-name">访客审核</text>
            <text class="feature-desc">访客预约审核，保障社区安全</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🤖</text>
          <view class="feature-info">
            <text class="feature-name">AI助手</text>
            <text class="feature-desc">智能问答咨询，应急方案查询</text>
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
          <text class="contact-value">www.smartcommunity.com</text>
        </view>
        <view class="contact-item">
          <text class="contact-label">电子邮箱</text>
          <text class="contact-value">support@smartcommunity.com</text>
        </view>
      </view>
    </view>

    <!-- 版权信息 -->
    <view class="footer">
      <text class="copyright">© 2024 智慧社区 版权所有</text>
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

<style>
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.app-logo {
  width: 160rpx;
  height: 160rpx;
  background: #fff;
  border-radius: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0,0,0,0.2);
}

.app-name {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 12rpx;
}

.app-version {
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
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
  border-left: 6rpx solid #667eea;
}

.intro-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.feature-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #f5f7fa;
  border-radius: 12rpx;
}

.feature-icon {
  font-size: 48rpx;
  margin-right: 24rpx;
}

.feature-info {
  display: flex;
  flex-direction: column;
}

.feature-name {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 8rpx;
}

.feature-desc {
  font-size: 26rpx;
  color: #999;
}

.contact-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.contact-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background: #f5f7fa;
  border-radius: 12rpx;
}

.contact-label {
  font-size: 28rpx;
  color: #666;
}

.contact-value {
  font-size: 28rpx;
  color: #667eea;
}

.footer {
  text-align: center;
  padding: 40rpx;
}

.copyright {
  font-size: 24rpx;
  color: #999;
}
</style>
