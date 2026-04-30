<template>
  <web-view src="http://192.168.0.71:8123" :update-title="false"></web-view>
</template>

<script>
export default {
  onShow() {
    uni.showTabBar()
  }
}
</script>

<style>
/* 系统导航栏和系统 TabBar 均为原生层，层级高于 web-view */
</style>
