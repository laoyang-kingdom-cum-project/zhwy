<template>
  <view class="appointment-container">
    <!-- 页面标题 -->
    <view class="page-header">
      <text class="page-title">新建访客预约</text>
    </view>

    <!-- 表单区域 -->
    <view class="form-section">
      <!-- 访客信息 -->
      <view class="form-group">
        <view class="group-title">访客信息</view>
        
        <view class="form-item">
          <text class="item-label">访客姓名</text>
          <input 
            class="item-input" 
            v-model="form.visitorName" 
            placeholder="请输入访客姓�?
            maxlength="20"
          />
        </view>
        
        <view class="form-item">
          <text class="item-label">访客手机�?/text>
          <input 
            class="item-input" 
            v-model="form.visitorPhone" 
            placeholder="请输入访客手机号"
            type="number"
            maxlength="11"
          />
        </view>
        
        <view class="form-item">
          <text class="item-label">访客人数</text>
          <view class="number-picker">
            <text class="picker-btn" @click="changeCount(-1)">-</text>
            <text class="picker-value">{{ form.visitorCount }}</text>
            <text class="picker-btn" @click="changeCount(1)">+</text>
          </view>
        </view>
      </view>

      <!-- 访问时间 -->
      <view class="form-group">
        <view class="group-title">访问时间</view>
        
        <view class="form-item">
          <text class="item-label">访问日期</text>
          <picker mode="date" :value="form.visitDate" :start="startDate" @change="onDateChange">
            <view class="picker-view">
              <text :class="{ placeholder: !form.visitDate }">{{ form.visitDate || '请选择访问日期' }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item">
          <text class="item-label">开始时�?/text>
          <picker mode="time" :value="form.visitTimeStart" @change="onStartTimeChange">
            <view class="picker-view">
              <text :class="{ placeholder: !form.visitTimeStart }">{{ form.visitTimeStart || '请选择开始时�? }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item">
          <text class="item-label">结束时间</text>
          <picker mode="time" :value="form.visitTimeEnd" @change="onEndTimeChange">
            <view class="picker-view">
              <text :class="{ placeholder: !form.visitTimeEnd }">{{ form.visitTimeEnd || '请选择结束时间' }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>
      </view>

      <!-- 访问事由 -->
      <view class="form-group">
        <view class="group-title">访问信息</view>
        
        <view class="form-item">
          <text class="item-label">访问事由</text>
          <input 
            class="item-input" 
            v-model="form.visitReason" 
            placeholder="请输入访问事由（选填�?
            maxlength="100"
          />
        </view>
        
        <view class="form-item textarea-item">
          <text class="item-label">备注</text>
          <textarea 
            class="item-textarea" 
            v-model="form.remark" 
            placeholder="请输入备注信息（选填�?
            maxlength="200"
          />
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section">
      <view class="submit-btn" @click="submitAppointment">
        <text>提交预约</text>
      </view>
    </view>
  </view>
</template>

<script>
import { createVisitor } from '@/api/visitor.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      form: {
        visitorName: '',
        visitorPhone: '',
        visitorCount: 1,
        visitDate: '',
        visitTimeStart: '',
        visitTimeEnd: '',
        visitReason: '',
        remark: ''
      }
    }
  },
  computed: {
    // 最小日期为今天
    startDate() {
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  },
  methods: {
    // 修改人数
    changeCount(delta) {
      const newCount = this.form.visitorCount + delta
      if (newCount >= 1 && newCount <= 10) {
        this.form.visitorCount = newCount
      }
    },
    
    // 日期选择
    onDateChange(e) {
      this.form.visitDate = e.detail.value
    },
    
    // 开始时间选择
    onStartTimeChange(e) {
      this.form.visitTimeStart = e.detail.value
    },
    
    // 结束时间选择
    onEndTimeChange(e) {
      this.form.visitTimeEnd = e.detail.value
    },
    
    // 验证表单
    validateForm() {
      if (!this.form.visitorName.trim()) {
        uni.showToast({ title: '请输入访客姓�?, icon: 'none' })
        return false
      }
      
      if (!this.form.visitorPhone.trim()) {
        uni.showToast({ title: '请输入访客手机号', icon: 'none' })
        return false
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.form.visitorPhone)) {
        uni.showToast({ title: '请输入正确的手机�?, icon: 'none' })
        return false
      }
      
      if (!this.form.visitDate) {
        uni.showToast({ title: '请选择访问日期', icon: 'none' })
        return false
      }
      
      if (!this.form.visitTimeStart) {
        uni.showToast({ title: '请选择开始时�?, icon: 'none' })
        return false
      }
      
      if (!this.form.visitTimeEnd) {
        uni.showToast({ title: '请选择结束时间', icon: 'none' })
        return false
      }
      
      if (this.form.visitTimeStart >= this.form.visitTimeEnd) {
        uni.showToast({ title: '结束时间必须晚于开始时�?, icon: 'none' })
        return false
      }
      
      return true
    },
    
    // 提交预约
    async submitAppointment() {
      if (!this.validateForm()) return
      
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        // 构建提交数据，后端期望日期时间格式为 yyyy-MM-dd HH:mm:ss
        const data = {
          visitorName: this.form.visitorName,
          visitorPhone: this.form.visitorPhone,
          visitorCount: parseInt(this.form.visitorCount),
          visitDate: this.form.visitDate + ' 00:00:00',
          visitTimeStart: this.form.visitDate + ' ' + this.form.visitTimeStart + ':00',
          visitTimeEnd: this.form.visitDate + ' ' + this.form.visitTimeEnd + ':00',
          visitReason: this.form.visitReason,
          remark: this.form.remark,
          userId: userId,
          status: '0' // 待审�?        }
        
        console.log('提交数据:', data)
        const res = await createVisitor(data)
        if (res.code === 200) {
          uni.showToast({ 
            title: '预约提交成功', 
            icon: 'success',
            duration: 2000
          })
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({ title: res.msg || '提交失败', icon: 'none' })
        }
      } catch (e) {
        console.error('提交预约失败', e)
        uni.showToast({ title: '提交失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss">
.appointment-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

// 页面标题
.page-header {
  background: #fff;
  padding: 30rpx;
  text-align: center;
  border-bottom: 1rpx solid #f0f0f0;
  
  .page-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

// 表单区域
.form-section {
  padding: 20rpx;
}

.form-group {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  
  .group-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #333;
    padding: 30rpx;
    border-bottom: 1rpx solid #f5f5f5;
  }
}

.form-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
  
  &:last-child {
    border-bottom: none;
  }
  
  &.textarea-item {
    flex-direction: column;
    align-items: flex-start;
    
    .item-label {
      margin-bottom: 20rpx;
    }
  }
  
  .item-label {
    width: 160rpx;
    font-size: 30rpx;
    color: #333;
  }
  
  .item-input {
    flex: 1;
    font-size: 30rpx;
    color: #333;
  }
  
  .item-textarea {
    width: 100%;
    height: 160rpx;
    font-size: 30rpx;
    color: #333;
    line-height: 1.6;
  }
  
  .picker-view {
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 30rpx;
    color: #333;
    
    .placeholder {
      color: #999;
    }
    
    .arrow {
      color: #999;
      font-size: 28rpx;
    }
  }
  
  // 人数选择�?  .number-picker {
    display: flex;
    align-items: center;
    
    .picker-btn {
      width: 60rpx;
      height: 60rpx;
      background: #f5f5f5;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36rpx;
      color: #333;
    }
    
    .picker-value {
      width: 80rpx;
      text-align: center;
      font-size: 32rpx;
      color: #333;
    }
  }
}

// 提交按钮
.submit-section {
  padding: 40rpx 30rpx;
  
  .submit-btn {
    background: #007AFF;
    color: #fff;
    text-align: center;
    padding: 30rpx 0;
    border-radius: 50rpx;
    font-size: 32rpx;
    font-weight: 500;
    
    &:active {
      opacity: 0.9;
    }
  }
}
</style>




