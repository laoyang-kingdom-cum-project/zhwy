<template>
  <view class="device-page">
    <view class="nav-bar">
      <text class="nav-title">智能家居</text>
    </view>
    <web-view class="webview" src="http://192.168.0.71:8123"></web-view>
    <custom-tabbar :current-index="1"></custom-tabbar>
  </view>
</template>

<script>
export default {}
</script>

<style lang="scss">
.device-page {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.nav-bar {
  height: 88rpx;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1rpx solid #eee;
  flex-shrink: 0;

  .nav-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
  }
}

.webview {
  flex: 1;
  width: 100%;
}
</style>
