// 关怀模式 mixin - 用于所有页面
export default {
  onShow() {
    this.applyCareMode()
  },
  onLoad() {
    this.applyCareMode()
  },
  methods: {
    // 应用关怀模式样式
    applyCareMode() {
      const careMode = uni.getStorageSync('careMode')
      // 使用 nextTick 确保 DOM 已渲染
      this.$nextTick && this.$nextTick(() => {
        const pages = getCurrentPages()
        const currentPage = pages[pages.length - 1]
        if (currentPage) {
          // 尝试多种方式获取页面元素
          let pageElement = null
          if (currentPage.$el) {
            pageElement = currentPage.$el
          } else if (currentPage.$page) {
            pageElement = currentPage.$page
          }
          
          // H5 环境下通过 querySelector 获取
          if (!pageElement && typeof document !== 'undefined') {
            pageElement = document.querySelector('page') || document.body
          }
          
          if (pageElement) {
            if (careMode) {
              pageElement.classList.add('care-mode-active')
            } else {
              pageElement.classList.remove('care-mode-active')
            }
          }
        }
      })
    }
  }
}
