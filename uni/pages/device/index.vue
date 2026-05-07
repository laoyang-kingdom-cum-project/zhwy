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

    const authJs = `
window.externalApp={
  getExternalAuth:function(o){window.externalAuthSetToken(!0,{access_token:"${token}",expires_in:315360000})},
  revokeExternalAuth:function(){window.externalAuthRevokeToken(!1)}
};
window.externalAppV2={
  getExternalAuth:function(o){window.externalAuthSetToken(!0,{access_token:"${token}",expires_in:315360000})},
  revokeExternalAuth:function(){window.externalAuthRevokeToken(!1)}
};`

    plus.io.requestFileSystem(plus.io.PRIVATE_DOC, function(fs) {
      fs.root.getFile('ha-auth.js', { create: true }, function(fileEntry) {
        fileEntry.createWriter(function(writer) {
          writer.onwriteend = function() {
            const wv = plus.webview.create(haUrl, 'ha-webview', {
              top: (info.statusBarHeight + 44) + 'px',
              bottom: '50px',
              'uni-app': 'none'
            })

            wv.appendJsFile('_doc/ha-auth.js')

            const currentWebview = this.$scope.$getAppWebview()
            currentWebview.append(wv)
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
