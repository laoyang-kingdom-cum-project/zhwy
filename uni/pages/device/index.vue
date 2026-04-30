<template>
  <view class="device-page">
    <web-view class="webview" src="http://192.168.0.71:8123"></web-view>
    <cover-view class="nav-bar">
      <cover-view class="nav-title">智能家居</cover-view>
    </cover-view>
  </view>
</template>

<script>
export default {
  onShow() {
    uni.showTabBar()
  }
}
</script>

<style>
.device-page {
  width: 100%;
  height: 100vh;
  position: relative;
}

.webview {
  width: 100%;
  height: 100%;
}

.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 44px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #e5e5e5;
}

.nav-title {
  font-size: 17px;
  font-weight: 600;
  color: #333333;
}
</style>
