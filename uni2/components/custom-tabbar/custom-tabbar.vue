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
import { openLink } from '@/uni_modules/app-launcher'

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
      // 根据平台使用不同的包名
      // #ifdef APP-HARMONY
      const bundleName = 'com.huawei.hmos.ailife'  // 鸿蒙版包名
      // #endif
      // #ifndef APP-HARMONY
      const bundleName = 'com.huawei.smarthome'    // Android版包名
      // #endif

      const downloadUrl = 'https://appgallery.huawei.com/app/C100046879'

      console.log('尝试打开应用，包名:', bundleName)

      // 使用插件通过包名跳转
      const appLauncher = uni.requireUTSPlugin('uni_modules/app-launcher')
      if (appLauncher && appLauncher.launchApp) {
        const result = appLauncher.launchApp({
          bundleName: bundleName,
          abilityName: 'EntryAbility'
        })
        if (!result) {
          this.showDownloadTip(downloadUrl)
        }
      } else {
        console.error('原生插件未找到')
        this.showDownloadTip(downloadUrl)
      }
    },

    // 显示下载提示
    showDownloadTip(downloadUrl) {
      uni.showModal({
        title: '提示',
        content: '未检测到华为智慧生活App，是否前往下载？',
        success: (res) => {
          if (res.confirm) {
            // #ifdef APP-PLUS
            plus.runtime.openURL(downloadUrl)
            // #endif
            // #ifdef APP-HARMONY
            // 鸿蒙使用原生插件打开链接
            const appLauncher = uni.requireUTSPlugin('uni_modules/app-launcher')
            if (appLauncher && appLauncher.openLink) {
              appLauncher.openLink({ urlScheme: downloadUrl })
            }
            // #endif
          }
        }
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
