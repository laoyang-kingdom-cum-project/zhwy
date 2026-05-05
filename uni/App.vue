<script>
	// #ifdef APP-PLUS
	import { initFallAlert } from '@/utils/ha-fall-alert.js'
	// #endif

	export default {
		onLaunch: function() {
			console.log('App Launch')

			// #ifdef APP-PLUS
			// 初始化 HA WebSocket 告警监听
			const haToken = uni.getStorageSync('ha_access_token') ||
				'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxZWVjM2Q4YzY3ZmU0OGIzYWNmZGQ5M2UyZGIxNDk1MyIsImlhdCI6MTc3Nzk2NjM1MywiZXhwIjoyMDkzMzI2MzUzfQ.Xi0xhMufcx_rlB0mFK7XNueIyR1eAPLaEmqPg2yrGtI'
			if (haToken) {
				initFallAlert(haToken)
			}
			// #endif
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
