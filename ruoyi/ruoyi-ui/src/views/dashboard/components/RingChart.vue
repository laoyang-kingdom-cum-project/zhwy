<template>
  <div class="flex flex-col items-center w-full min-w-0">
    <div class="relative w-20 h-20 flex-shrink-0">
      <svg class="w-full h-full transform -rotate-90" viewBox="0 0 36 36">
        <path
          class="text-gray-700"
          d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
          fill="none"
          stroke="currentColor"
          stroke-width="3"
        />
        <path
          :class="colorClass"
          :stroke-dasharray="dashArray"
          d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
          fill="none"
          stroke="currentColor"
          stroke-width="3"
        />
      </svg>
      <div class="absolute inset-0 flex items-center justify-center text-lg font-bold">
        {{ value }}%
      </div>
    </div>
    <div class="mt-2 text-sm text-gray-300 w-full text-center truncate px-1">{{ label }}</div>
    <div class="text-xs text-cyan-400 w-full text-center truncate px-1">{{ sub }}</div>
  </div>
</template>

<script>
export default {
  name: 'RingChart',
  props: {
    value: { type: Number, required: true },
    color: { type: String, default: '#22d3ee' },
    label: { type: String, required: true },
    sub: { type: String, default: '' }
  },
  computed: {
    dashArray() {
      return `${this.value}, 100`
    },
    colorClass() {
      return this.color === '#eab308' ? 'text-yellow-500' : 'text-cyan-400'
    }
  }
}
</script>
