<template>
  <web-view
    src="http://192.168.0.71:8123/?external_auth=1"
    :update-title="false"
  ></web-view>
</template>

<script>
export default {
  onShow() {
    uni.showTabBar()
  },
  onReady() {
    // #ifdef APP-PLUS
    const token = uni.getStorageSync('ha_access_token') ||
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxZWVjM2Q4YzY3ZmU0OGIzYWNmZGQ5M2UyZGIxNDk1MyIsImlhdCI6MTc3Nzk2NjM1MywiZXhwIjoyMDkzMzI2MzUzfQ.Xi0xhMufcx_rlB0mFK7XNueIyR1eAPLaEmqPg2yrGtI'

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

    // 多次注入确保 HA 页面加载前/后都能拿到
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
