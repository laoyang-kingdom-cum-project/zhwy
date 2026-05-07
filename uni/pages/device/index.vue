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
    const haUrlBase = `http://${env.haHost}:${env.haPort}`
    
    // ⚔️ 步骤一：特洛伊木马潜入！先加载 HA 官方必定存在的静态轻量文件
    const wv = plus.webview.create(haUrlBase + '/manifest.json', 'ha-webview', {
      top: (info.statusBarHeight + 44) + 'px',
      bottom: '50px',
      'uni-app': 'none'
    })

    const currentWebview = this.$scope.$getAppWebview()
    currentWebview.append(wv)

    // ⚔️ 步骤二：潜入成功后，实施底层 Token 注入
    wv.addEventListener('loaded', function() {
      // 只有在加载 manifest.json 时才执行伪造逻辑，防止死循环
      wv.evalJS(`
        if (location.href.indexOf('manifest.json') !== -1) {
          // 完美伪造 HA 前端的 AuthData 内部核心数据结构
          var fakeTokens = {
            access_token: "${token}",
            expires_in: 3153600000, 
            expires: Date.now() + 3153600000000, // 设定绝对过期时间为 100 年后
            refresh_token: "${token}", // 用长期令牌占位，防止 HA 后台触发刷新逻辑
            clientId: "uni-app-client",
            hassUrl: "${haUrlBase}"
          };
          
          // 将伪造的身份芯片强行植入浏览器本地存储
          window.localStorage.setItem("hassTokens", JSON.stringify(fakeTokens));
          
          // 摘下面具，以合法身份直接加载主页！（去掉了讨厌的 external_auth）
          window.location.replace("${haUrlBase}/");
        }
      `)
    })
    // #endif
  }
}
</script>

<style>
.device-page { width: 100%; height: 100vh; background: #fff; }
</style>