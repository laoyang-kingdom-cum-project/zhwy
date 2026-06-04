<template>
  <view class="device-page">
    <!-- #ifdef APP-HARMONY -->
    <view class="harmony-container">
      <web-view :src="haUrl" :webview-styles="webviewStyles"></web-view>
      <view class="bottom-bar">
        <view class="harmony-btn" @click="openAiLife">
          <text class="btn-text">🚀 打开智慧生活</text>
        </view>
      </view>
    </view>
    <!-- #endif -->
  </view>
</template>

<script>
import env from '@/config/env.js'
import { openAiLifeCommand } from '@/api/device.js'

export default {
  data() {
    const sysInfo = uni.getSystemInfoSync()
    // 留出 80px 给底部的按钮栏
    const webviewHeight = sysInfo.windowHeight - 80
    
    return {
      haUrl: `http://${env.haHost}:${env.haPort}`,
      webviewStyles: {
        height: webviewHeight + 'px',
        width: '100%'
      }
    }
  },
  onShow() {
    uni.showTabBar()
  },
  onReady() {
    // #ifdef APP-PLUS
    const info = uni.getSystemInfoSync()
    const haUrl = `http://${env.haHost}:${env.haPort}` // 连后缀都不需要了

    const wv = plus.webview.create(haUrl, 'ha-webview', {
      top: (info.statusBarHeight + 44) + 'px',
      bottom: '50px',
      'uni-app': 'none'
    })

    const currentWebview = this.$scope.$getAppWebview()
    currentWebview.append(wv)
    // #endif
  },
  methods: {
    // #ifdef APP-HARMONY
    openAiLife() {
      uni.showLoading({ title: '正在发送指令...' })
      openAiLifeCommand().then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          uni.showToast({ title: '已发送打开指令', icon: 'success' })
        } else {
          uni.showToast({ title: res.msg || '执行失败', icon: 'none' })
        }
      }).catch(err => {
        uni.hideLoading()
        uni.showToast({ title: '网络请求失败', icon: 'none' })
        console.error('发送指令异常', err)
      })
    }
    // #endif
  }
}
</script>

<style>
.device-page { width: 100%; height: 100vh; background: #fff; display: flex; flex-direction: column; position: relative; }

/* #ifdef APP-HARMONY */
.harmony-container {
  flex: 1;
  width: 100%;
  position: relative;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  /* 如果有原生 tabbar，需要使用 var(--window-bottom) 确保在 tabbar 之上 */
  bottom: var(--window-bottom, 0);
  left: 0;
  width: 100%;
  height: 80px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  z-index: 999;
}

.harmony-btn {
  background-color: #007aff;
  padding: 12px 40px;
  border-radius: 25px;
  box-shadow: 0 4px 10px rgba(0, 122, 255, 0.3);
}

.btn-text {
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
}
/* #endif */
</style>