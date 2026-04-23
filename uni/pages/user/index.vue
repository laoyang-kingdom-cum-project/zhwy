<template>
  <view class="user-container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <image class="user-avatar" :src="userInfo.avatar || '/static/logo.png'" mode="aspectFill"></image>
        <view class="user-detail">
          <text class="user-name">{{ userInfo.nickName || userInfo.userName }}</text>
          <text class="user-phone">{{ userInfo.phonenumber }}</text>
        </view>
      </view>
      <view class="user-stats">
        <view class="stat-item">
          <text class="stat-num">{{ userInfo.sex === '0' ? '男' : userInfo.sex === '1' ? '女' : '未知' }}</text>
          <text class="stat-label">性别</text>
        </view>
        <view class="stat-item" :key="refreshKey">
          <text class="stat-num address-num">{{ formattedAddress }}</text>
          <text class="stat-label">住址</text>
        </view>
        <view class="stat-item">
          <text class="stat-num">{{ liveDays }}天</text>
          <text class="stat-label">居住天数</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="navigateTo('/pages/user/profile')">
          <view class="menu-left">
            <text class="menu-icon">👤</text>
            <text class="menu-name">个人信息</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/user/password')">
          <view class="menu-left">
            <text class="menu-icon">🔒</text>
            <text class="menu-name">修改密码</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item">
          <view class="menu-left">
            <text class="menu-icon">👴</text>
            <text class="menu-name">关怀模式</text>
          </view>
          <switch :checked="careMode" @change="onCareModeChange" color="#007AFF" />
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="navigateTo('/pages/service/list')">
          <view class="menu-left">
            <text class="menu-icon">📋</text>
            <text class="menu-name">我的订单</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/user/payment')">
          <view class="menu-left">
            <text class="menu-icon">💰</text>
            <text class="menu-name">缴费记录</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="navigateTo('/pages/user/settings')">
          <view class="menu-left">
            <text class="menu-icon">⚙️</text>
            <text class="menu-name">系统设置</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/user/help')">
          <view class="menu-left">
            <text class="menu-icon">❓</text>
            <text class="menu-name">帮助中心</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="navigateTo('/pages/user/about')">
          <view class="menu-left">
            <text class="menu-icon">📄</text>
            <text class="menu-name">关于我们</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-btn" @click="logout">
      <text>退出登录</text>
    </view>
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="4"></custom-tabbar>
  </view>
</template>

<script>
import { getUserInfo } from '@/api/login.js'
import config from '@/config/index.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      userInfo: {
        nickName: '',
        avatar: '',
        phonenumber: '',
        email: '',
        createTime: '',
        userId: ''
      },
      refreshKey: 0,
      careMode: false,
      _androidTts: null,
      _androidTtsReady: false,
      _androidTtsInitializing: false,
      _pendingTtsText: ''
    }
  },
  computed: {
    // 获取本地保存的住址，显示前4个字+...
    formattedAddress() {
      const address = uni.getStorageSync('userAddress')
      if (!address) return '未设置'
      if (address.length <= 4) return address
      return address.substring(0, 4) + '...'
    },
    // 计算居住天数
    liveDays() {
      if (!this.userInfo.createTime) return 1
      const createDate = new Date(this.userInfo.createTime)
      const now = new Date()
      const diff = now - createDate
      return Math.floor(diff / (1000 * 60 * 60 * 24)) + 1
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadCareMode()
  },
  onShow() {
    this.loadUserInfo()
    this.loadCareMode()
    // 应用关怀模式样式
    this.applyCareModeToPage()
    // 强制刷新计算属性
    this.refreshKey++
  },
  onUnload() {
    this.releaseSystemTTS()
  },
  methods: {
    // 加载用户信息
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200 && res.user) {
          this.userInfo = {
            ...res.user,
            avatar: res.user.avatar ? config.baseUrl + '/por-api' + res.user.avatar : '/static/logo.png'
          }
          // 更新本地存储
          uni.setStorageSync('userInfo', res.user)
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },

    navigateTo(path) {
      if (!path) {
        uni.showToast({ title: '功能开发中', icon: 'none' })
        return
      }
      uni.navigateTo({ url: path })
    },

    // 加载关怀模式设置
    loadCareMode() {
      this.careMode = uni.getStorageSync('careMode') === true
    },
    // switch组件切换事件
    onCareModeChange(e) {
      this.careMode = e.detail.value
      uni.setStorageSync('careMode', this.careMode)
      this.applyCareModeToPage()
      if (this.careMode) {
        uni.showToast({ title: '关怀模式已开启', icon: 'success' })
        this.speakBySystemTTS('关怀模式已开启，语音朗读已启用。')
      } else {
        this.stopSystemTTS()
        uni.showToast({ title: '关怀模式已关闭', icon: 'none' })
      }
    },
    // 调用系统TTS播报文本
    speakBySystemTTS(text) {
      if (!text) {
        return
      }
      // #ifdef APP-PLUS
      if (plus.os.name === 'Android') {
        this.speakByAndroidSystemTTS(text)
        return
      }
      // #endif
      this.speakByWebSpeech(text)
    },
    // Android: 使用系统自带TextToSpeech播报
    speakByAndroidSystemTTS(text) {
      // #ifdef APP-PLUS
      if (!text) {
        return
      }
      this._pendingTtsText = text

      if (this._androidTts && this._androidTtsReady) {
        this.speakByAndroidTtsInstance(this._androidTts, text)
        return
      }

      if (this._androidTtsInitializing) {
        return
      }

      this._androidTtsInitializing = true
      try {
        const main = plus.android.runtimeMainActivity()
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        const listener = plus.android.implements('android.speech.tts.TextToSpeech.OnInitListener', {
          onInit: (status) => {
            this._androidTtsInitializing = false
            if (status !== TextToSpeech.SUCCESS) {
              this._androidTtsReady = false
              uni.showToast({ title: '系统TTS初始化失败', icon: 'none' })
              return
            }

            this._androidTtsReady = true
            this.flushPendingAndroidTTS()
          }
        })

        this._androidTts = new TextToSpeech(main, listener)
      } catch (err) {
        this._androidTtsInitializing = false
        this._androidTtsReady = false
        console.error('系统TTS调用失败', err)
        this.speakByWebSpeech(text)
      }
      // #endif
    },
    flushPendingAndroidTTS(retryCount = 0) {
      // #ifdef APP-PLUS
      if (!this._pendingTtsText) {
        return
      }
      if (!this._androidTts || !this._androidTtsReady) {
        if (retryCount < 10) {
          setTimeout(() => {
            this.flushPendingAndroidTTS(retryCount + 1)
          }, 60)
        }
        return
      }

      const text = this._pendingTtsText
      this._pendingTtsText = ''
      this.speakByAndroidTtsInstance(this._androidTts, text)
      // #endif
    },
    speakByAndroidTtsInstance(tts, text) {
      // #ifdef APP-PLUS
      if (!tts || !text) {
        return
      }
      try {
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        const Locale = plus.android.importClass('java.util.Locale')

        let langResult = plus.android.invoke(tts, 'setLanguage', Locale.SIMPLIFIED_CHINESE)
        if (langResult === TextToSpeech.LANG_MISSING_DATA || langResult === TextToSpeech.LANG_NOT_SUPPORTED) {
          langResult = plus.android.invoke(tts, 'setLanguage', Locale.CHINESE)
        }
        if (langResult === TextToSpeech.LANG_MISSING_DATA || langResult === TextToSpeech.LANG_NOT_SUPPORTED) {
          uni.showModal({
            title: '语音播报不可用',
            content: '系统缺少中文语音包，是否前往安装？',
            confirmText: '去安装',
            cancelText: '取消',
            success: (res) => {
              if (res.confirm) {
                this.openAndroidTTSSettings()
              }
            }
          })
          return
        }

        plus.android.invoke(tts, 'setPitch', 1.0)
        plus.android.invoke(tts, 'setSpeechRate', 1.0)

        let speakResult = TextToSpeech.ERROR
        try {
          speakResult = plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null, 'care_mode_tts')
        } catch (newApiErr) {
          speakResult = plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null)
        }

        if (speakResult === TextToSpeech.ERROR) {
          uni.showToast({ title: '系统TTS播报失败', icon: 'none' })
        }
      } catch (err) {
        console.error('系统TTS朗读失败', err)
        this.speakByWebSpeech(text)
      }
      // #endif
    },
    openAndroidTTSSettings() {
      // #ifdef APP-PLUS
      if (plus.os.name !== 'Android') {
        return
      }
      try {
        const main = plus.android.runtimeMainActivity()
        const Intent = plus.android.importClass('android.content.Intent')
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        const intent = new Intent(TextToSpeech.Engine.ACTION_TTS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        main.startActivity(intent)
      } catch (err) {
        console.error('打开TTS设置失败', err)
        uni.showToast({ title: '无法打开TTS设置', icon: 'none' })
      }
      // #endif
    },
    // H5或降级：使用浏览器语音播报
    speakByWebSpeech(text) {
      // #ifdef H5
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        try {
          window.speechSynthesis.cancel()
          const utterance = new SpeechSynthesisUtterance(text)
          utterance.lang = 'zh-CN'
          utterance.rate = 1
          utterance.pitch = 1
          window.speechSynthesis.speak(utterance)
          return
        } catch (err) {
          console.error('浏览器TTS调用失败', err)
        }
      }
      // #endif
      uni.showToast({ title: '当前设备不支持语音朗读', icon: 'none' })
    },
    stopSystemTTS() {
      // #ifdef APP-PLUS
      this._pendingTtsText = ''
      if (this._androidTts) {
        try {
          plus.android.invoke(this._androidTts, 'stop')
        } catch (err) {
          console.error('停止系统TTS失败', err)
        }
      }
      // #endif
      // #ifdef H5
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        window.speechSynthesis.cancel()
      }
      // #endif
    },
    releaseSystemTTS() {
      this.stopSystemTTS()
      // #ifdef APP-PLUS
      this._androidTtsReady = false
      this._androidTtsInitializing = false
      if (this._androidTts) {
        const tts = this._androidTts
        this._androidTts = null
        try {
          plus.android.invoke(tts, 'shutdown')
        } catch (err) {
          console.error('释放系统TTS失败', err)
        }
        try {
          plus.android.deleteObject(tts)
        } catch (deleteErr) {
          console.error('清理系统TTS对象失败', deleteErr)
        }
      }
      // #endif
    },
    // 应用关怀模式样式
    applyCareModeToPage() {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1]
      if (currentPage && currentPage.$el) {
        if (this.careMode) {
          currentPage.$el.classList.add('care-mode-active')
        } else {
          currentPage.$el.classList.remove('care-mode-active')
        }
      }
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.reLaunch({ url: '/pages/login/index' })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.user-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

// 用户信息卡片
.user-card {
  background: linear-gradient(135deg, #007AFF 0%, #00C6FF 100%);
  padding: 60rpx 40rpx 40rpx;
  border-radius: 0 0 40rpx 40rpx;

  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 40rpx;

    .user-avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      border: 4rpx solid rgba(255,255,255,0.3);
      margin-right: 30rpx;
    }

    .user-detail {
      display: flex;
      flex-direction: column;

      .user-name {
        font-size: 40rpx;
        color: #fff;
        font-weight: bold;
        margin-bottom: 12rpx;
      }

      .user-phone {
        font-size: 28rpx;
        color: rgba(255,255,255,0.9);
      }
    }
  }

  .user-stats {
    display: flex;
    justify-content: space-around;
    background: rgba(255,255,255,0.15);
    border-radius: 20rpx;
    padding: 30rpx 0;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      flex: 1;

      .stat-num {
        font-size: 32rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 8rpx;
        line-height: 1.2;
      }

      .stat-num.address-num {
        font-size: 28rpx;
        max-width: 140rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .stat-label {
        font-size: 24rpx;
        color: rgba(255,255,255,0.7);
      }
    }
  }
}

// 菜单区域
.menu-section {
  padding: 20rpx;

  .menu-group {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;

    .menu-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .menu-left {
        display: flex;
        align-items: center;

        .menu-icon {
          font-size: 36rpx;
          margin-right: 20rpx;
        }

        .menu-name {
          font-size: 30rpx;
          color: #333;
        }
      }

      .menu-arrow {
        font-size: 28rpx;
        color: #ccc;
      }
    }
  }
}

// 退出登录
.logout-btn {
  margin: 40rpx 20rpx;
  height: 90rpx;
  background: #fff;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  text {
    font-size: 32rpx;
    color: #FF6B6B;
    font-weight: 500;
  }
}
</style>
