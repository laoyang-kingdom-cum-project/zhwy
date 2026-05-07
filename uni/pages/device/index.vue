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
    const token = uni.getStorageSync('ha_access_token') || env.haAccessToken
    const haUrl = `http://${env.haHost}:${env.haPort}/?external_auth=1`

    const injectCode = `
      window.externalApp={
        getExternalAuth:function(o){window.externalAuthSetToken(true,{access_token:"${token}",expires_in:315360000})},
        revokeExternalAuth:function(){window.externalAuthRevokeToken(false)}
      };
      window.externalAppV2={
        getExternalAuth:function(o){window.externalAuthSetToken(true,{access_token:"${token}",expires_in:315360000})},
        revokeExternalAuth:function(){window.externalAuthRevokeToken(false)}
      };
    `

    const wv = plus.webview.create('about:blank', 'ha-webview', {
      top: (info.statusBarHeight + 44) + 'px',
      bottom: '50px',
      'uni-app': 'none'
    })

    // 关键：在 HA 页面加载前注入，确保 externalApp 在 HA 脚本执行前就存在
    wv.addEventListener('loading', function() {
      wv.evalJS(injectCode)
    })

    wv.loadURL(haUrl)

    const currentWebview = this.$scope.$getAppWebview()
    currentWebview.append(wv)
    // #endif
  }
}
</script>

<style>
.device-page {
  width: 100%;
  height: 100vh;
  background: #fff;
}
</style>
