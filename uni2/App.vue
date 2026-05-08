<script>
// #ifdef APP-PLUS
import { initFallAlert } from '@/utils/ha-fall-alert.js'
import env from '@/config/env.js'
// #endif

export default {
  onLaunch: function() {
    console.log('App Launch')
    // 检查登录状态
    const token = uni.getStorageSync('token')
    if (!token) {
      uni.reLaunch({
        url: '/pages/login/index'
      })
    }

    // #ifdef APP-PLUS
    const haToken = uni.getStorageSync('ha_access_token') || env.haAccessToken
    if (haToken) {
      initFallAlert(haToken)
    }
    // #endif
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style>
/* 全局样式 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 重置按钮样式 */
button::after {
  border: none;
}

button {
  margin: 0;
  padding: 0;
  background: none;
  line-height: inherit;
}

/* 隐藏滚动条 */
::-webkit-scrollbar {
  display: none;
  width: 0 !important;
  height: 0 !important;
  -webkit-appearance: none;
  background: transparent;
}
</style>
