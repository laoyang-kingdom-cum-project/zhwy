<template>
  <web-view
    src="http://192.168.0.71:8123"
    :update-title="false"
  ></web-view>
</template>

<script>
export default {
  onShow() {
    // #ifdef APP-PLUS
    uni.showTabBar()
    // #endif
  }
}
</script>

<style>
/* web-view 自动占满，系统导航栏和tabBar为原生层覆盖在上 */
</style>
