<template>
  <view class="help-container">
    <!-- 常见问题分类 -->
    <view class="category-list">
      <view 
        class="category-item" 
        v-for="(item, index) in categories" 
        :key="index"
        @click="selectCategory(index)"
        :class="{ active: currentCategory === index }"
      >
        <text class="category-icon">{{ item.icon }}</text>
        <text class="category-name">{{ item.name }}</text>
      </view>
    </view>

    <!-- 问题列表 -->
    <view class="faq-section">
      <view class="section-title">{{ categories[currentCategory].name }}</view>
      <view class="faq-list">
        <view 
          class="faq-item" 
          v-for="(item, index) in currentFaqs" 
          :key="index"
          @click="toggleFaq(index)"
        >
          <view class="faq-question">
            <text class="q-mark">Q</text>
            <text class="question-text">{{ item.question }}</text>
            <text class="expand-icon" :class="{ expanded: item.expanded }">▼</text>
          </view>
          <view class="faq-answer" v-if="item.expanded">
            <text class="a-mark">A</text>
            <text class="answer-text">{{ item.answer }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 联系客服 -->
    <view class="contact-section">
      <view class="section-title">联系客服</view>
      <view class="contact-list">
        <view class="contact-item" @click="makeCall">
          <text class="contact-icon">📞</text>
          <view class="contact-info">
            <text class="contact-name">客服热线</text>
            <text class="contact-value">400-888-8888</text>
          </view>
        </view>
        <view class="contact-item" @click="openChat">
          <text class="contact-icon">💬</text>
          <view class="contact-info">
            <text class="contact-name">在线客服</text>
            <text class="contact-value">9:00-18:00</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 意见反馈表单 -->
    <view class="feedback-section" v-if="showFeedback">
      <view class="section-title">意见反馈</view>
      <view class="feedback-form">
        <textarea 
          class="feedback-input" 
          v-model="feedbackContent"
          placeholder="请输入您的意见或建议..."
          maxlength="500"
        />
        <view class="feedback-count">{{feedbackContent.length}}/500</view>
        <view class="feedback-btns">
          <view class="btn btn-cancel" @click="cancelFeedback">取消</view>
          <view class="btn btn-submit" @click="submitFeedback">提交</view>
        </view>
      </view>
    </view>

    <!-- 反馈入口 -->
    <view class="feedback-btn" @click="showFeedbackForm" v-if="!showFeedback">
      <text>意见反馈</text>
    </view>
  </view>
</template>

<script>
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      currentCategory: 0,
      showFeedback: false,
      feedbackContent: '',
      categories: [
        { name: '常见问题', icon: '❓' },
        { name: '账号相关', icon: '👤' },
        { name: '缴费相关', icon: '💰' },
        { name: '设备相关', icon: '🔌' }
      ],
      faqs: {
        0: [
          { question: '如何绑定房屋？', answer: '进入"我的"页面，点击"个人信息"，选择"房屋绑定"，输入您的房屋信息即可完成绑定。', expanded: false },
          { question: '忘记密码怎么办？', answer: '在登录页面点击"忘记密码"，通过手机验证码验证后可以重置密码。', expanded: false },
          { question: '如何缴纳物业费？', answer: '进入"我的"页面，点击"缴费记录"，选择待缴费账单进行支付。', expanded: false },
          { question: '设备离线怎么办？', answer: '请检查设备电源是否正常，网络连接是否稳定，如仍有问题请联系客服。', expanded: false }
        ],
        1: [
          { question: '如何修改个人信息？', answer: '进入"我的"页面，点击"个人信息"，可以修改昵称、头像等信息。', expanded: false },
          { question: '如何更换绑定手机？', answer: '进入"系统设置"，选择"绑定手机"，验证原手机后可以更换。', expanded: false },
          { question: '一个账号可以绑定多个房屋吗？', answer: '目前一个账号只能绑定一个房屋，如需更换请联系物业。', expanded: false }
        ],
        2: [
          { question: '支持哪些支付方式？', answer: '目前支持微信支付、支付宝支付。', expanded: false },
          { question: '缴费后多久到账？', answer: '缴费成功后一般实时到账，如有延迟请耐心等待5分钟。', expanded: false },
          { question: '如何开具发票？', answer: '缴费成功后，在缴费记录中选择对应账单，点击"申请发票"。', expanded: false }
        ],
        3: [
          { question: '如何添加智能设备？', answer: '进入"设备"页面，点击右上角"+"号，按照提示进行设备配网。', expanded: false },
          { question: '设备控制失败怎么办？', answer: '请检查设备是否在线，网络是否正常，如仍有问题请重启设备。', expanded: false },
          { question: '如何设置定时任务？', answer: '进入设备控制页面，点击"定时"，添加您需要的定时任务。', expanded: false }
        ]
      }
    }
  },
  computed: {
    currentFaqs() {
      return this.faqs[this.currentCategory]
    }
  },
  methods: {
    selectCategory(index) {
      this.currentCategory = index
    },
    toggleFaq(index) {
      this.faqs[this.currentCategory][index].expanded = !this.faqs[this.currentCategory][index].expanded
    },
    makeCall() {
      uni.makePhoneCall({
        phoneNumber: '400-888-8888'
      })
    },
    openChat() {
      
    },
    showFeedbackForm() {
      this.showFeedback = true
      this.feedbackContent = ''
    },
    cancelFeedback() {
      this.showFeedback = false
      this.feedbackContent = ''
    },
    submitFeedback() {
      if (!this.feedbackContent.trim()) {
        uni.showToast({ title: '请输入反馈内容', icon: 'none' })
        return
      }
      // 这里可以调用接口提交反馈
      uni.showToast({ title: '提交成功', icon: 'success' })
      this.showFeedback = false
      this.feedbackContent = ''
    }
  }
}
</script>

<style lang="scss">
.help-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.category-list {
  display: flex;
  background: #fff;
  padding: 20rpx;
  gap: 20rpx;
  overflow-x: auto;

  .category-item {
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 30rpx;
    border-radius: 16rpx;
    background: #f5f5f5;

    &.active {
      background: #007AFF;

      .category-icon, .category-name {
        color: #fff;
      }
    }

    .category-icon {
      font-size: 40rpx;
      margin-bottom: 8rpx;
    }

    .category-name {
      font-size: 26rpx;
      color: #666;
    }
  }
}

.faq-section, .contact-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    padding: 24rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }
}

.faq-list {
  .faq-item {
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child { border-bottom: none; }

    .faq-question {
      display: flex;
      align-items: center;
      padding: 24rpx 30rpx;

      .q-mark {
        width: 36rpx;
        height: 36rpx;
        background: #007AFF;
        color: #fff;
        border-radius: 8rpx;
        font-size: 24rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16rpx;
        flex-shrink: 0;
      }

      .question-text {
        flex: 1;
        font-size: 30rpx;
        color: #333;
      }

      .expand-icon {
        font-size: 24rpx;
        color: #999;
        transition: transform 0.3s;

        &.expanded {
          transform: rotate(180deg);
        }
      }
    }

    .faq-answer {
      display: flex;
      padding: 0 30rpx 24rpx;
      background: #fafafa;

      .a-mark {
        width: 36rpx;
        height: 36rpx;
        background: #2ED573;
        color: #fff;
        border-radius: 8rpx;
        font-size: 24rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16rpx;
        flex-shrink: 0;
      }

      .answer-text {
        flex: 1;
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
      }
    }
  }
}

.contact-list {
  .contact-item {
    display: flex;
    align-items: center;
    padding: 24rpx 30rpx;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child { border-bottom: none; }

    .contact-icon {
      font-size: 48rpx;
      margin-right: 20rpx;
    }

    .contact-info {
      display: flex;
      flex-direction: column;

      .contact-name {
        font-size: 30rpx;
        color: #333;
        margin-bottom: 8rpx;
      }

      .contact-value {
        font-size: 26rpx;
        color: #999;
      }
    }
  }
}

.feedback-btn {
  margin: 40rpx 30rpx;
  background: #007AFF;
  color: #fff;
  text-align: center;
  padding: 28rpx 0;
  border-radius: 12rpx;
  font-size: 32rpx;

  &:active {
    opacity: 0.9;
  }
}

// 意见反馈表单
.feedback-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    padding: 24rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  .feedback-form {
    padding: 30rpx;

    .feedback-input {
      width: 100%;
      height: 200rpx;
      background: #f5f5f5;
      border-radius: 12rpx;
      padding: 20rpx;
      font-size: 28rpx;
      color: #333;
      box-sizing: border-box;
    }

    .feedback-count {
      text-align: right;
      font-size: 24rpx;
      color: #999;
      margin-top: 12rpx;
    }

    .feedback-btns {
      display: flex;
      gap: 20rpx;
      margin-top: 30rpx;

      .btn {
        flex: 1;
        padding: 24rpx 0;
        border-radius: 12rpx;
        text-align: center;
        font-size: 30rpx;

        &.btn-cancel {
          background: #f5f5f5;
          color: #666;
        }

        &.btn-submit {
          background: #007AFF;
          color: #fff;
        }
      }
    }
  }
}
</style>
