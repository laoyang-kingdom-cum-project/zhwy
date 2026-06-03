<template>
  <view class="device-page">
    <!-- #ifdef APP-HARMONY -->
    <web-view :src="haUrl" class="web-view"></web-view>
    <view class="fab-btn" @click="openAiLife">
      <text class="fab-text">智慧</text>
      <text class="fab-text">生活</text>
    </view>
    <!-- #endif -->
  </view>
</template>

<script>
import env from '@/config/env.js'

export default {
  data() {
    return {
      haUrl: `http://${env.haHost}:${env.haPort}`
    }
  },
  onShow() {
    uni.showTabBar()
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
      try {
        if (typeof plus !== 'undefined' && plus.runtime) {
          // 鸿蒙系统下推荐使用 openURL 和 Scheme 来唤起应用
          plus.runtime.openURL('ailife://com.huawei.hmos.ailife', function(err) {
            console.error('打开 ailife:// 失败', err);
            // 尝试备用/旧版协议
            plus.runtime.openURL('huaweiailife://', function(err2) {
              console.error('打开 huaweiailife:// 失败', err2);
              uni.showToast({
                title: '打开失败，请检查是否已安装智慧生活',
                icon: 'none'
              });
            });
          });
        } else if (uni.openLink) {
          uni.openLink({
            url: 'ailife://com.huawei.hmos.ailife',
            fail: () => {
              uni.showToast({
                title: '打开失败，请检查是否已安装智慧生活',
                icon: 'none'
              });
            }
          });
        } else {
          uni.showToast({
            title: '当前环境不支持跳转',
            icon: 'none'
          })
        }
      } catch (e) {
        console.error(e)
      }
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

/* #ifdef APP-HARMONY */
.fab-btn {
  position: absolute;
  right: 20px;
  bottom: 80px;
  z-index: 99999;
  width: 56px;
  height: 56px;
  background-color: #007aff;
  border-radius: 28px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 10px rgba(0, 122, 255, 0.4);
}

.fab-text {
  color: #ffffff;
  font-size: 12px;
  line-height: 1.2;
}
/* #endif */
</style>