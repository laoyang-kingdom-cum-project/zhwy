<template>
  <view class="device-page">
    <web-view src="http://192.168.0.71:8123" :update-title="false"></web-view>

    <cover-view class="custom-tabbar">
      <cover-view class="tab-item" @click="switchTab('/pages/index/index')">
        <cover-view class="tab-icon">🏠</cover-view>
        <cover-view class="tab-text">首页</cover-view>
      </cover-view>
      <cover-view class="tab-item active">
        <cover-view class="tab-icon">💡</cover-view>
        <cover-view class="tab-text">设备</cover-view>
      </cover-view>
      <cover-view class="tab-item" @click="switchTab('/pages/warning/index')">
        <cover-view class="tab-icon">🚨</cover-view>
        <cover-view class="tab-text">预警</cover-view>
      </cover-view>
      <cover-view class="tab-item" @click="switchTab('/pages/care/index')">
        <cover-view class="tab-icon">💚</cover-view>
        <cover-view class="tab-text">健康</cover-view>
      </cover-view>
      <cover-view class="tab-item" @click="switchTab('/pages/user/index')">
        <cover-view class="tab-icon">😊</cover-view>
        <cover-view class="tab-text">我的</cover-view>
      </cover-view>
    </cover-view>
  </view>
</template>

<script>
export default {
  onShow() {
    uni.hideTabBar()
  },
  methods: {
    switchTab(url) {
      uni.switchTab({ url })
    }
  }
}
</script>

<style>
.device-page {
  width: 100%;
  height: 100vh;
}

.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #ffffff;
  border-top: 1px solid #e5e5e5;
  display: flex;
  z-index: 999;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tab-icon {
  font-size: 40rpx;
  margin-bottom: 4rpx;
}

.tab-text {
  font-size: 22rpx;
  color: #999999;
}

.tab-item.active .tab-text {
  color: #007AFF;
}
</style>
