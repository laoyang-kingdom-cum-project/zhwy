<template>
  <view class="password-container">
    <!-- 旧密�?-->
    <view class="form-item">
      <text class="label">旧密�?/text>
      <input 
        class="input" 
        v-model="form.oldPassword" 
        placeholder="请输入旧密码"
        type="password"
      />
    </view>

    <!-- 新密�?-->
    <view class="form-item">
      <text class="label">新密�?/text>
      <input 
        class="input" 
        v-model="form.newPassword" 
        placeholder="请输入新密码"
        type="password"
      />
    </view>

    <!-- 确认密码 -->
    <view class="form-item">
      <text class="label">确认密码</text>
      <input 
        class="input" 
        v-model="form.confirmPassword" 
        placeholder="请再次输入新密码"
        type="password"
      />
    </view>

    <!-- 提示 -->
    <view class="tips">
      <text>密码长度6-20位，建议包含字母和数�?/text>
    </view>

    <!-- 保存按钮 -->
    <view class="submit-section">
      <button class="submit-btn" @click="changePassword">确认修改</button>
    </view>
  </view>
</template>

<script>
import { updatePassword } from '@/api/login.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      form: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    async changePassword() {
      if (!this.form.oldPassword) {
        uni.showToast({ title: '请输入旧密码', icon: 'none' })
        return
      }
      if (!this.form.newPassword) {
        uni.showToast({ title: '请输入新密码', icon: 'none' })
        return
      }
      if (this.form.newPassword.length < 6) {
        uni.showToast({ title: '密码长度不能少于6�?, icon: 'none' })
        return
      }
      if (this.form.newPassword !== this.form.confirmPassword) {
        uni.showToast({ title: '两次输入的密码不一�?, icon: 'none' })
        return
      }
      
      uni.showLoading({ title: '修改�?..' })
      try {
        const res = await updatePassword({
          oldPassword: this.form.oldPassword,
          newPassword: this.form.newPassword
        })
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '修改成功', icon: 'success' })
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({ title: res.msg || '修改失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('修改密码失败', e)
        uni.showToast({ title: '修改失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss">
.password-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  border-bottom: 1rpx solid #f0f0f0;

  .label {
    width: 160rpx;
    font-size: 30rpx;
    color: #333;
  }

  .input {
    flex: 1;
    font-size: 30rpx;
    color: #333;
  }
}

.tips {
  padding: 30rpx;
  text {
    font-size: 26rpx;
    color: #999;
  }
}

.submit-section {
  padding: 60rpx 30rpx;

  .submit-btn {
    width: 100%;
    height: 90rpx;
    line-height: 90rpx;
    background: #007AFF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 12rpx;
    border: none;

    &:active {
      opacity: 0.8;
    }
  }
}
</style>




