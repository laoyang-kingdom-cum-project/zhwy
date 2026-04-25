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

      // 点击设备tab时，跳转华为智慧生活App
      if (index === 2) {
        this.openSmartLifeApp()
        return
      }

      uni.reLaunch({
        url: this.tabs[index].path
      })
    },

    // 打开华为智慧生活App
    openSmartLifeApp() {
      // 华为智慧生活包名和URL Scheme
      const packageName = 'com.huawei.smarthome'
      const urlScheme = 'huaweismarthome://'
      // 华为应用市场下载链接
      const downloadUrl = 'https://appgallery.huawei.com/app/C100046879'

      // 打开URL的通用方法
      const openURL = (url) => {
        if (typeof window !== 'undefined' && window.location) {
          window.location.href = url
          return true
        }
        return false
      }

      // 显示下载提示
      const showDownloadTip = () => {
        uni.showModal({
          title: '提示',
          content: '未检测到华为智慧生活App，是否前往下载？',
          success: (res) => {
            if (res.confirm) {
              openURL(downloadUrl)
            }
          }
        })
      }

      // #ifdef APP-PLUS
      // Android/iOS App - 通过包名启动
      plus.runtime.launchApplication({
        pname: packageName
      }, () => {
        // 包名失败，尝试URL Scheme
        plus.runtime.openURL(urlScheme, showDownloadTip)
      })
      // #endif

      // #ifndef APP-PLUS
      // 非App环境使用URL Scheme
      openURL(urlScheme)
      setTimeout(showDownloadTip, 2500)
      // #endif
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
