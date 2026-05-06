<template>
  <web-view
    :src="haUrl"
    :update-title="false"
  ></web-view>
</template>

<script>
import env from '@/config/env.js'

export default {
  data() {
    return {
      haUrl: `http://${env.haHost}:${env.haPort}/?external_auth=1`
    }
  },
  onShow() {
    uni.showTabBar()
  },
  onReady() {
    // #ifdef APP-PLUS
    const token = uni.getStorageSync('ha_access_token') || env.haAccessToken

    const injectCode = `
      window.externalApp = {
        getExternalAuth: function(options) {
          window.externalAuthSetToken(true, {
            access_token: '${token}',
            expires_in: 315360000
          })
        },
        revokeExternalAuth: function() {
          window.externalAuthRevokeToken(false)
        }
      }
      if (typeof window.externalAuthSetToken === 'function') {
        window.externalAuthSetToken(true, {
          access_token: '${token}',
          expires_in: 315360000
        })
      }
    `

    const doInject = () => {
      const currentWebview = this.$scope.$getAppWebview()
      const wv = currentWebview && currentWebview.children() && currentWebview.children()[0]
      if (wv) {
        wv.evalJS(injectCode)
        console.log('[HA-Auth] 外部认证代码已注入')
      }
    }

    setTimeout(doInject, 200)
    setTimeout(doInject, 800)
    setTimeout(doInject, 2000)
    // #endif
  }
}
</script>
