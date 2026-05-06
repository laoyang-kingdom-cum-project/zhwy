<template>
  <web-view
    ref="webview"
    src="http://192.168.0.71:8123"
    :update-title="false"
    @load="onWebviewLoad"
    @message="handleMessage"
  ></web-view>
</template>

<script>
export default {
  data() {
    return {
      webview: null,
    }
  },
  onShow() {
    uni.showTabBar()
  },
  onHide() {

  },
  onUnload() {

  },
  methods: {
    onWebviewLoad() {
      // #ifdef APP-PLUS
      this.$refs.webview.evalJS("document.body.style.backgroundColor = '#f5f5f5';")
      // #endif
    },
    handleMessage(e) {
      console.log('收到 webview 消息:', e.detail.data)
    },
  }
}
</script>

<style>

</style>