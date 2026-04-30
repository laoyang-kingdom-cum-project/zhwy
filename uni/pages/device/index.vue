<template>
  <view class="device-page">
    <web-view
      src="http://192.168.0.71:8123"
      :update-title="false"
      :webview-styles="webviewStyles"
    ></web-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      webviewStyles: {
        progress: { color: '#007AFF' }
      }
    }
  },
  onReady() {
    // #ifdef APP-PLUS
    const info = uni.getSystemInfoSync()
    const top = info.statusBarHeight + 44 // 状态栏 + 导航栏
    const bottom = 50 // tabBar 高度
    const currentWebview = this.$scope.$getAppWebview()
    setTimeout(() => {
      const wv = currentWebview.children()[0]
      if (wv) {
        wv.setStyle({ top, bottom })
      }
    }, 300)
    // #endif

    // #ifdef H5
    setTimeout(() => {
      const iframe = document.querySelector('.device-page iframe')
      if (iframe) {
        iframe.style.top = '44px'
        iframe.style.height = 'calc(100vh - 44px - 50px)'
      }
    }, 300)
    // #endif
  },
  onShow() {
    uni.showTabBar()
  }
}
</script>

<style>
.device-page {
  width: 100%;
  height: 100vh;
  position: relative;
  background: #fff;
}
</style>
