<template>
  <view class="device-page">
    <!-- #ifdef APP-HARMONY -->
    <web-view :src="haUrl" class="web-view"></web-view>
    <view class="fab-btn" @click="openAiLife">
      <text class="fab-text">智慧</text>
      <text class="fab-text">生活</text>
    </view>
    <!-- #endif -->
  </view>
</template>

<script>
import env from '@/config/env.js'
import { openAiLifeCommand } from '@/api/device.js'

export default {
  data() {
    return {
      haUrl: `http://${env.haHost}:${env.haPort}`
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
.web-view {
  flex: 1;
}

/* #ifdef APP-HARMONY */
.fab-btn {
  position: absolute;
  right: 20px;
  bottom: 80px;
  z-index: 99999;
  width: 56px;
  height: 56px;
  background-color: #007aff;
  border-radius: 28px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 10px rgba(0, 122, 255, 0.4);
}

.fab-text {
  color: #ffffff;
  font-size: 12px;
  line-height: 1.2;
}
/* #endif */
</style>