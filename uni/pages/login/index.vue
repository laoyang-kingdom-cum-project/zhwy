<template>
  <view class="login-container">
    <view class="login-box">
      <!-- Logo区域 -->
      <view class="logo">
        <image src="/static/logo.png" mode="aspectFit" class="logo-img"></image>
        <text class="title">智慧社区</text>
        <text class="subtitle">让生活更便捷</text>
      </view>

      <!-- 表单区域 -->
      <view class="form">
        <!-- 用户名 -->
        <view class="input-item">
          <text class="label">账号</text>
          <input
            type="text"
            v-model="loginForm.username"
            placeholder="请输入用户名"
            class="input"
          />
        </view>

        <!-- 密码 -->
        <view class="input-item">
          <text class="label">密码</text>
          <input
            type="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
            class="input"
          />
        </view>

        <!-- 验证码 -->
        <view class="input-item captcha-item">
          <text class="label">验证码</text>
          <input
            type="text"
            v-model="loginForm.code"
            placeholder="请输入验证码"
            class="input captcha-input"
          />
          <image
            :src="captchaUrl"
            mode="aspectFit"
            class="captcha-img"
            @click="refreshCaptcha"
          ></image>
        </view>

        <!-- 记住密码和自动登录 -->
        <view class="login-options">
          <view class="option-item" @click="toggleRememberPassword">
            <view class="checkbox" :class="{ 'checked': rememberPassword }">
              <text v-if="rememberPassword" class="check-icon">✓</text>
            </view>
            <text class="option-text">记住密码</text>
          </view>
          <view class="option-item" @click="toggleAutoLogin" style="display: none;">
            <view class="checkbox" :class="{ 'checked': autoLogin }">
              <text v-if="autoLogin" class="check-icon">✓</text>
            </view>
            <text class="option-text">自动登录</text>
          </view>
        </view>

        <!-- 登录按钮 -->
        <button
          class="login-btn"
          :loading="loading"
          @click="handleLogin"
        >
          登 录
        </button>
      </view>

      <!-- 隐私协议 -->
      <view class="agreement">
        <view class="checkbox" :class="{ 'checked': agreed }" @click="toggleAgreement">
          <text v-if="agreed" class="check-icon">✓</text>
        </view>
        <text class="agreement-text">
          登录即代表您同意
          <text class="link" @click="goToAgreement('user')">《用户协议》</text>
          和
          <text class="link" @click="goToAgreement('privacy')">《隐私政策》</text>
        </text>
      </view>
    </view>
  </view>
</template>

<script>
import { login, getCaptcha, getUserInfo } from '@/api/login.js'
import config from '@/config/index.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        code: '',
        uuid: ''
      },
      captchaUrl: '',
      loading: false,
      agreed: false,
      rememberPassword: false,
      autoLogin: false
    }
  },
  onLoad() {
    this.refreshCaptcha()
    this.loadSavedLoginInfo()
    this.checkAutoLogin()
  },
  methods: {
    // 加载保存的登录信息
    loadSavedLoginInfo() {
      const savedUsername = uni.getStorageSync('savedUsername')
      const savedPassword = uni.getStorageSync('savedPassword')
      const rememberPassword = uni.getStorageSync('rememberPassword')
      const autoLogin = uni.getStorageSync('autoLogin')

      if (savedUsername) {
        this.loginForm.username = savedUsername
      }
      if (rememberPassword && savedPassword) {
        this.loginForm.password = savedPassword
        this.rememberPassword = true
      }
      this.autoLogin = autoLogin || false
    },

    // 检查自动登录
    async checkAutoLogin() {
      if (this.autoLogin && this.loginForm.username && this.loginForm.password) {
        // 自动登录需要验证码，这里只自动填充账号密码
        uni.showToast({ title: '请完成验证码登录', icon: 'none' })
      }
    },

    // 保存登录信息
    saveLoginInfo() {
      if (this.rememberPassword) {
        uni.setStorageSync('savedUsername', this.loginForm.username)
        uni.setStorageSync('savedPassword', this.loginForm.password)
        uni.setStorageSync('rememberPassword', true)
      } else {
        uni.removeStorageSync('savedPassword')
        uni.setStorageSync('rememberPassword', false)
        uni.setStorageSync('savedUsername', this.loginForm.username)
      }
      uni.setStorageSync('autoLogin', this.autoLogin)
    },

    // 刷新验证码
    async refreshCaptcha() {
      try {
        const res = await getCaptcha()
        if (res.code === 200) {
          this.captchaUrl = 'data:image/jpeg;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      } catch (e) {
        console.error('获取验证码失败', e)
      }
    },

    // 切换记住密码
    toggleRememberPassword() {
      this.rememberPassword = !this.rememberPassword
      if (!this.rememberPassword) {
        this.autoLogin = false
      }
    },

    // 切换自动登录
    toggleAutoLogin() {
      this.autoLogin = !this.autoLogin
      if (this.autoLogin) {
        this.rememberPassword = true
      }
    },

    // 切换协议勾选
    toggleAgreement() {
      this.agreed = !this.agreed
    },

    // 跳转到协议页面
    goToAgreement(type) {
      uni.navigateTo({
        url: `/pages/agreement/index?type=${type}`
      })
    },

    // 登录
    async handleLogin() {
      if (!this.loginForm.username) {
        uni.showToast({ title: '请输入用户名', icon: 'none' })
        return
      }
      if (!this.loginForm.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
      }
      if (!this.loginForm.code) {
        uni.showToast({ title: '请输入验证码', icon: 'none' })
        return
      }
      if (!this.agreed) {
        uni.showToast({ title: '请同意用户协议和隐私政策', icon: 'none' })
        return
      }

      this.loading = true
      try {
        const res = await login(this.loginForm)
        if (res.code === 200) {
          uni.setStorageSync('token', res.token)

          // 保存登录信息（记住密码、自动登录）
          this.saveLoginInfo()

          // 获取并存储用户信息
          try {
            const userRes = await getUserInfo()
            if (userRes.code === 200 && userRes.user) {
              uni.setStorageSync('userInfo', userRes.user)
            }
          } catch (e) {
            console.error('获取用户信息失败', e)
          }

          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/index/index'
            })
          }, 1500)
        }
      } catch (e) {
        this.refreshCaptcha()
        this.loginForm.code = ''
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss">
.login-container {
  min-height: 100vh;
  background: #ffffff;
  overflow-y: auto;
}

.login-box {
  padding: 120rpx 60rpx 60rpx;
}

// Logo区域
.logo {
  text-align: center;
  margin-bottom: 100rpx;

  .logo-img {
    width: 160rpx;
    height: 160rpx;
    margin-bottom: 30rpx;
  }

  .title {
    display: block;
    font-size: 56rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
    letter-spacing: 4rpx;
  }

  .subtitle {
    display: block;
    font-size: 28rpx;
    color: #999;
    letter-spacing: 8rpx;
  }
}

// 表单区域
.form {
  .input-item {
    display: flex;
    align-items: center;
    height: 100rpx;
    background: #f8f8f8;
    border-radius: 50rpx;
    padding: 0 40rpx;
    margin-bottom: 30rpx;

    .label {
      font-size: 28rpx;
      color: #333;
      font-weight: 500;
      margin-right: 24rpx;
      min-width: 80rpx;
    }

    .input {
      flex: 1;
      font-size: 28rpx;
      color: #333;
      border: none;
      background: transparent;
    }

    // 验证码特殊样式
    &.captcha-item {
      padding-right: 20rpx;

      .captcha-input {
        flex: 1;
      }

      .captcha-img {
        width: 160rpx;
        height: 70rpx;
        border-radius: 8rpx;
      }
    }
  }

  // 登录选项（记住密码、自动登录）
  .login-options {
    display: flex;
    justify-content: space-between;
    margin-top: 30rpx;
    padding: 0 20rpx;

    .option-item {
      display: flex;
      align-items: center;

      .checkbox {
        width: 32rpx;
        height: 32rpx;
        border: 2rpx solid #ccc;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12rpx;
        flex-shrink: 0;
        transition: all 0.2s;

        &.checked {
          background: #007AFF;
          border-color: #007AFF;
        }

        .check-icon {
          font-size: 20rpx;
          color: #fff;
          font-weight: bold;
          line-height: 1;
        }
      }

      .option-text {
        font-size: 26rpx;
        color: #666;
      }
    }
  }

  // 登录按钮
  .login-btn {
    width: 100%;
    height: 100rpx;
    line-height: 100rpx;
    background: #333;
    color: #fff;
    font-size: 32rpx;
    border-radius: 50rpx;
    margin-top: 40rpx;
    border: none;
    font-weight: 500;

    &:active {
      opacity: 0.8;
    }
  }
}

// 隐私协议
.agreement {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 60rpx;
  padding: 0 40rpx;

  .checkbox {
    width: 32rpx;
    height: 32rpx;
    border: 2rpx solid #ccc;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12rpx;
    flex-shrink: 0;
    transition: all 0.2s;

    &.checked {
      background: #007AFF;
      border-color: #007AFF;
    }

    .check-icon {
      font-size: 20rpx;
      color: #fff;
      font-weight: bold;
      line-height: 1;
    }
  }

  .agreement-text {
    font-size: 22rpx;
    color: #999;
    line-height: 1.6;

    .link {
      color: #007AFF;
      font-weight: 500;
    }
  }
}
</style>
