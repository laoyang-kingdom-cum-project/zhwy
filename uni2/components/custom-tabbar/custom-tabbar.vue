<template>
  <view class="custom-tabbar">
    <view 
      class="tab-item" 
      v-for="(item, index) in tabs" 
      :key="index"
      :class="{ active: currentIndex === index }"
      @click="switchTab(index)"
    >
      <text class="tab-icon">{{ currentIndex === index ? item.activeIcon : item.icon }}</text>
      <text class="tab-text">{{ item.text }}</text>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    currentIndex: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      tabs: [
        { text: '首页', icon: '🏠', activeIcon: '🏠', path: '/pages/index/index' },
        { text: '预警', icon: '⚠️', activeIcon: '⚠️', path: '/pages/warning/index' },
        { text: '设备', icon: '📱', activeIcon: '📱', path: '/pages/device/index' },
        { text: '我的', icon: '👤', activeIcon: '👤', path: '/pages/user/index' }
      ]
    }
  },
  methods: {
    switchTab(index) {
      if (this.currentIndex === index) return
      uni.reLaunch({
        url: this.tabs[index].path
      })
    }
  }
}
</script>

<style>
.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1rpx solid #f0f0f0;
  padding-bottom: env(safe-area-inset-bottom);
  z-index: 999;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  flex: 1;
}

.tab-icon {
  font-size: 44rpx;
  margin-bottom: 4rpx;
}

.tab-text {
  font-size: 22rpx;
  color: #999;
}

.tab-item.active .tab-text {
  color: #667eea;
}
</style>
