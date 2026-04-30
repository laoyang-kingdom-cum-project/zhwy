<template>
  <web-view
    src="http://192.168.0.71:8123"
    :update-title="false"
  ></web-view>
</template>

<script>
export default {
  onShow() {
    // 恢复系统 TabBar（其他页面的 custom-tabbar 会隐藏它）
    uni.showTabBar()
  }
}
</script>

<style>
/* web-view 自动占满，系统导航栏和tabBar为原生层，层级高于web-view */
</style>
