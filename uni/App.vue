<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
			this.applyCareMode()
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			// 应用关怀模式
			applyCareMode() {
				// #ifdef H5
				const careMode = uni.getStorageSync('careMode')
				const pages = getCurrentPages()
				const currentPage = pages[pages.length - 1]
				if (currentPage) {
					const pageElement = currentPage.$el || currentPage.$page
					if (pageElement && pageElement.classList) {
						if (careMode) {
							pageElement.classList.add('care-mode-active')
						} else {
							pageElement.classList.remove('care-mode-active')
						}
					}
				}
				// #endif
			}
		}
	}
</script>

<style>
	/* 全局基础样式 */
	page {
		font-size: 28rpx;
	}
	
	/* 关怀模式全局样式 - 字体放大 */
	.care-mode-active,
	.care-mode-active text,
	.care-mode-active view,
	.care-mode-active button,
	.care-mode-active input,
	.care-mode-active textarea,
	.care-mode-active label,
	.care-mode-active navigator {
		font-size: 1.15em !important;
	}
	
	/* 标题字体更大 */
	.care-mode-active .title,
	.care-mode-active .page-title,
	.care-mode-active .section-title,
	.care-mode-active .device-name,
	.care-mode-active .user-name,
	.care-mode-active .menu-name {
		font-size: 1.25em !important;
	}
	
	/* 按钮字体 */
	.care-mode-active button,
	.care-mode-active .btn,
	.care-mode-active .menu-item {
		font-size: 1.1em !important;
	}
</style>
