<template>
  <view class="device-page"></view>
</template>

<script>
import env from '@/config/env.js'

export default {
  onShow() {
    uni.showTabBar()
  },
  onReady() {
    // #ifdef APP-PLUS
    const info = uni.getSystemInfoSync()
    const haUrl = `http://${env.haHost}:${env.haPort}`

    const wv = plus.webview.create(haUrl, 'ha-webview', {
      top: (info.statusBarHeight + 44) + 'px',
      bottom: '50uni2/utils uni/utils/ha-fall-alert.js uni/utils/ha-websocket.jspx',
      'uni-app': 'none'
    })

    const currentWebview = this.$scope.$getAppWebview()
    currentWebview.append(wv)
    // #endif
  }
}
</script>

<style>
.device-page { width: 100%; height: 100vh; background: #fff; }
</style>