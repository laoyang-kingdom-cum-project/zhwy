<template>
  <div class="glass rounded-2xl p-4">
    <div class="flex items-center gap-2 mb-4 border-b border-cyan-500/30 pb-2">
      <div class="w-1 h-4 bg-cyan-400 rounded-full"></div>
      <h3 class="text-lg font-semibold text-cyan-50">{{ title }}</h3>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'DashboardCard',
  props: {
    title: {
      type: String,
      required: true
    }
  }
}
</script>

<style scoped>
.glass {
  background: rgba(15, 23, 42, 0.85);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(103, 232, 249, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.6);
}
</style>
