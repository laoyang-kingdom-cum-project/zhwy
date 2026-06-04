<template>
  <view class="device-page">
    <!-- #ifdef APP-HARMONY -->
    <web-view :src="haUrl" class="web-view"></web-view>
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
  onNavigationBarButtonTap(e) {
    // 监听原生导航栏按钮点击 (在 pages.json 中配置)
    if (e.index === 0) {
      this.openAiLife()
    }
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
</style>