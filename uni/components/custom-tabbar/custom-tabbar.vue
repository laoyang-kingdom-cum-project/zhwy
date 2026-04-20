<template>
  <view class="custom-tabbar">
    <view
      v-for="(item, index) in tabList"
      :key="index"
      class="tab-item"
      :class="{ active: currentIndex === index }"
      @click="switchTab(item, index)"
    >
      <text class="tab-icon">{{ currentIndex === index ? item.selectedIcon : item.icon }}</text>
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
      tabList: [
        {
          pagePath: '/pages/index/index',
          text: '首页',
          icon: '🏠',
          selectedIcon: '🏡'
        },
        {
          pagePath: '/pages/device/index',
          text: '设备',
          icon: '🔌',
          selectedIcon: '💡'
        },
        {
          pagePath: '/pages/warning/index',
          text: '预警',
          icon: '🔔',
          selectedIcon: '🚨'
        },
        {
          pagePath: '/pages/care/index',
          text: '健康',
          icon: '❤️',
          selectedIcon: '💚'
        },
        {
          pagePath: '/pages/user/index',
          text: '我的',
          icon: '👤',
          selectedIcon: '😊'
        }
      ]
    }
  },
  mounted() {
    // 隐藏原生 TabBar
    uni.hideTabBar()
  },
  methods: {
    switchTab(item, index) {
      if (this.currentIndex === index) return
      uni.switchTab({
        url: item.pagePath
      })
    }
  }
}
</script>

<style scoped>
.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #ffffff;
  border-top: 1rpx solid #e5e5e5;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding-bottom: env(safe-area-inset-bottom);
  z-index: 999;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
}

.tab-icon {
  font-size: 44rpx;
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
