<template>
  <div class="glass rounded-xl p-3">
    <div class="text-gray-400 text-sm mb-1">{{ label }}</div>
    <div class="text-2xl font-bold" :class="colorClass">{{ formattedValue }}</div>
  </div>
</template>

<script>
export default {
  name: 'BottomMetric',
  props: {
    label: {
      type: String,
      required: true
    },
    value: {
      type: Number,
      required: true
    },
    color: {
      type: String,
      default: 'text-cyan-400'
    }
  },
  computed: {
    formattedValue() {
      return this.value.toLocaleString()
    },
    colorClass() {
      return this.color
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
