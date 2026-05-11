<template>
  <view class="repair-container">
    <!-- 报修类型 -->
    <view class="form-section">
      <view class="section-title">报修类型</view>
      <view class="type-grid">
        <view 
          class="type-item" 
          v-for="(item, index) in repairTypes" 
          :key="index"
          :class="{ active: form.type === item }"
          @click="selectType(item)"
        >
          <text>{{ item }}</text>
        </view>
      </view>
    </view>

    <!-- 报修内容 -->
    <view class="form-section">
      <view class="section-title">报修内容</view>
      <textarea 
        class="content-input" 
        v-model="form.content"
        placeholder="请详细描述您遇到的问�?.."
        maxlength="200"
      ></textarea>
      <text class="word-count">{{ form.content.length }}/200</text>
    </view>

    <!-- 报修地址 -->
    <view class="form-section">
      <view class="section-title">报修地址</view>
      <input 
        class="address-input" 
        v-model="form.address"
        placeholder="请输入详细地址"
      />
    </view>

    <!-- 联系电话 -->
    <view class="form-section">
      <view class="section-title">联系电话</view>
      <input 
        class="phone-input" 
        v-model="form.contact"
        placeholder="请输入联系电�?
        type="number"
      />
    </view>

    <!-- 联系人姓�?-->
    <view class="form-section">
      <view class="section-title">联系人姓�?/view>
      <input 
        class="name-input" 
        v-model="form.contactName"
        placeholder="请输入联系人姓名"
      />
    </view>

    <!-- 预约时间 -->
    <view class="form-section">
      <view class="section-title">预约时间（选填�?/view>
      <picker mode="date" :value="form.appointmentDate" @change="onDateChange">
        <view class="picker-view">
          <text class="picker-text">{{ form.appointmentDate || '请选择日期' }}</text>
          <text class="picker-arrow">></text>
        </view>
      </picker>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section">
      <button class="submit-btn" @click="submitRepair">提交报修</button>
    </view>
  </view>
</template>

<script>
import { createOrder } from '@/api/service.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      repairTypes: ['水电维修', '家电维修', '门窗维修', '墙面维修', '地板维修', '其他'],
      form: {
        type: '',
        content: '',
        address: '',
        contact: '',
        contactName: '',
        appointmentDate: ''
      }
    }
  },
  methods: {
    selectType(type) {
      this.form.type = type
    },
    onDateChange(e) {
      this.form.appointmentDate = e.detail.value
    },
    async submitRepair() {
      if (!this.form.type) {
        uni.showToast({ title: '请选择报修类型', icon: 'none' })
        return
      }
      if (!this.form.content) {
        uni.showToast({ title: '请填写报修内�?, icon: 'none' })
        return
      }
      if (!this.form.address) {
        uni.showToast({ title: '请填写报修地址', icon: 'none' })
        return
      }
      if (!this.form.contact) {
        uni.showToast({ title: '请填写联系电�?, icon: 'none' })
        return
      }
      if (!this.form.contactName) {
        uni.showToast({ title: '请填写联系人姓名', icon: 'none' })
        return
      }
      
      uni.showLoading({ title: '提交�?..' })
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await createOrder({
          type: this.form.type,
          content: this.form.content,
          address: this.form.address,
          contactPhone: this.form.contact,
          contactName: this.form.contactName,
          appointmentTime: this.form.appointmentDate ? this.form.appointmentDate + ' 00:00:00' : null,
          userId: userId,
          status: '0'
        })
        
        uni.hideLoading()
        if (res.code === 200) {
          uni.showToast({ title: '提交成功', icon: 'success' })
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({ title: res.msg || '提交失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('提交报修失败', e)
        uni.showToast({ title: '提交失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss">
.repair-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.form-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 20rpx;
  }

  .type-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;

    .type-item {
      padding: 20rpx 40rpx;
      background: #f5f5f5;
      border-radius: 12rpx;
      font-size: 28rpx;
      color: #666;

      &.active {
        background: #007AFF;
        color: #fff;
      }
    }
  }

  .content-input {
    width: 100%;
    height: 200rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }

  .word-count {
    display: block;
    text-align: right;
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
  }

  .address-input,
  .phone-input,
  .name-input {
    width: 100%;
    height: 80rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }

  .picker-view {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 80rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 0 20rpx;

    .picker-text {
      font-size: 28rpx;
      color: #333;
    }

    .picker-arrow {
      font-size: 28rpx;
      color: #999;
    }
  }
}

.submit-section {
  padding: 40rpx 30rpx;

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




