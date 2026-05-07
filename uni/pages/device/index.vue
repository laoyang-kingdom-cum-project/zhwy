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

    // 修复了 V2 协议回调名字解析的致命错误，并且确保严丝合缝
    const authJs = `
    (function(){
      var TK = { access_token: "${token}", expires_in: 315360000 };
      
      // V2 协议（官方 HA Companion 标准）
      window.externalAppV2 = {
        postMessage: function(msgStr) {
          try {
            var msg = typeof msgStr === 'string' ? JSON.parse(msgStr) : msgStr;
            if (msg && msg.type === 'getExternalAuth' && msg.payload && msg.payload.callback) {
              // HA 发来的是回调函数的名字（字符串），我们需要去 window 里找这个函数执行它
              var cbName = msg.payload.callback;
              if (typeof window[cbName] === 'function') {
                window[cbName](true, TK);
              }
            }
          } catch(e) {}
        }
      };

      // V1 协议降级保护
      window.externalApp = {
        getExternalAuth: function(o) {
          if (typeof window.externalAuthSetToken === 'function') {
            window.externalAuthSetToken(true, TK);
          }
        },
        revokeExternalAuth: function(o) {
          if (typeof window.externalAuthRevokeToken === 'function') {
            window.externalAuthRevokeToken(false);
          }
        }
      };
    })();`;

    const fileName = 'ha-auth-' + Date.now() + '.js'
    const filePath = '_doc/' + fileName

    // 开始异步写文件...
    plus.io.requestFileSystem(plus.io.PRIVATE_DOC, function(fs) {
      fs.root.getFile(fileName, { create: true }, function(fileEntry) {
        fileEntry.createWriter(function(writer) {
          writer.onwriteend = function() {
            
            // 🌟 核心破局点 1：先创建一个【空白】的 Webview，绝对不要在这里传入 haUrl！
            const wv = plus.webview.create('', 'ha-webview', {
              top: (info.statusBarHeight + 44) + 'px',
              bottom: '50px',
              'uni-app': 'none'
            })

            // 🌟 核心破局点 2：趁着网页空白，将魔法代码深深植入
            wv.appendJsFile(filePath)

            const currentWebview = this.$scope.$getAppWebview()
            currentWebview.append(wv)

            // 🌟 核心破局点 3：子弹全部上膛完毕，正式命令加载 HA！
            // 这样能保证我们的 JS 绝对会在 HA 的任何环境检查之前执行完毕喵！
            wv.loadURL(haUrl)

          }.bind(this)
          writer.write(authJs)
        }.bind(this))
      }.bind(this))
    }.bind(this))
    // #endif
  }
}
</script>

<style>
.device-page { width: 100%; height: 100vh; background: #fff; }
</style>