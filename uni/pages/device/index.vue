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

    // 注入代码：V1 + V2 双协议 + 主动出击打破 60 秒死锁
    const injectCode = `
(function(){
  if (window.__ha_auth_injected) return;
  window.__ha_auth_injected = true;

  var TK = { access_token: "${token}", expires_in: 315360000 };

  // V1 协议降级
  window.externalApp = {
    getExternalAuth: function(o) { window.externalAuthSetToken(true, TK); },
    revokeExternalAuth: function(o) { window.externalAuthRevokeToken(false); },
    postMessage: function(msg) {}
  };

  // V2 协议（官方 HA Companion 标准）
  window.externalAppV2 = {
    getExternalAuth: function(o) { window.externalAuthSetToken(true, TK); },
    revokeExternalAuth: function(o) { window.externalAuthRevokeToken(false); },
    connectionStatus: function(cb) { cb(true); },
    setThemeColor: function(c) {},
    postMessage: function(msg) {
      try {
        var data = typeof msg === 'string' ? JSON.parse(msg) : msg;
        if (data && data.payload && typeof data.payload.callback === 'function') {
          data.payload.callback(TK);
        }
      } catch(e) {}
    }
  };

  // 主动出击：如果 HA 已经把接收端准备好了，直接硬塞 Token
  if (typeof window.externalAuthSetToken === 'function') {
    window.externalAuthSetToken(true, TK);
  }
})();`

    // 创建 webview
    const wv = plus.webview.create(haUrl, 'ha-webview', {
      top: (info.statusBarHeight + 44) + 'px',
      bottom: '50px',
      'uni-app': 'none'
    })

    // 多重生命周期注入 — loading 抢跑 + loaded 补刀
    wv.addEventListener('loading', function() {
      wv.evalJS(injectCode)
    })

    wv.addEventListener('loaded', function() {
      wv.evalJS(injectCode)
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
