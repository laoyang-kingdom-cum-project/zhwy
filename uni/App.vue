<script>
	// #ifdef APP-PLUS
	import { initFallAlert } from '@/utils/ha-fall-alert.js'
	import env from '@/config/env.js'
	// #endif

	export default {
		onLaunch: function() {
			console.log('App Launch')

			// #ifdef APP-PLUS
			const haToken = uni.getStorageSync('ha_access_token') || env.haAccessToken
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
			checkLoginAndRedirect() {
				const token = uni.getStorageSync('token')
				const pages = getCurrentPages()
				const currentPage = pages[0] ? pages[0].route : ''
				
				if (token) {
					// 有token，如果在登录页则跳转到首�?
					if (currentPage === 'pages/login/index') {
						uni.reLaunch({
							url: '/pages/index/index'
						})
					}
				} else {
					// 没有token，如果不在登录页则跳转到登录�?
					if (currentPage !== 'pages/login/index') {
						uni.reLaunch({
							url: '/pages/login/index'
						})
					}
				}
			},
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

