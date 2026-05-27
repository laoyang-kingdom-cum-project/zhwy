<template>
  <view class="device-page">
    <!-- #ifdef APP-HARMONY -->
    <web-view :src="haUrl" class="web-view"></web-view>
    <!-- #endif -->
  </view>
</template>

<script>
import env from '@/config/env.js'

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
  }
}
</script>

<style>
.device-page { width: 100%; height: 100vh; background: #fff; display: flex; flex-direction: column; }
.web-view {
  flex: 1;
}
</style>



