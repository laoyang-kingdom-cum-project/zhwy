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
export default {
  data() {
    return {
      currentCategory: 0,
      showFeedback: false,
      feedbackContent: '',
      categories: [
        { name: '常见问题', icon: '❓' },
        { name: '账号相关', icon: '👤' },
        { name: '预警处理', icon: '🚨' },
        { name: '设备相关', icon: '🔌' }
      ],
      faqs: {
        0: [
          { question: '如何查看预警信息？', answer: '进入"预警"页面，可以查看所有预警信息，点击可查看详情并进行处理。', expanded: false },
          { question: '忘记密码怎么办？', answer: '在登录页面点击"忘记密码"，通过手机验证码验证后可以重置密码。', expanded: false },
          { question: '如何查看设备状态？', answer: '进入"设备"页面，可以查看所有设备的运行状态和故障信息。', expanded: false },
          { question: '如何审核访客预约？', answer: '进入"访客审核"页面，查看待审核的访客预约，点击可进行审核操作。', expanded: false }
        ],
        1: [
          { question: '如何修改个人信息？', answer: '进入"我的"页面，点击"个人信息"，可以修改昵称、邮箱等信息。', expanded: false },
          { question: '如何修改密码？', answer: '进入"我的"页面，点击"修改密码"，输入旧密码和新密码即可完成修改。', expanded: false },
          { question: '一个账号可以管理多个小区吗？', answer: '目前一个账号只能管理一个小区，如需更换请联系管理员。', expanded: false }
        ],
        2: [
          { question: '预警等级有哪些？', answer: '预警分为三个等级：紧急（红色）、重要（橙色）、一般（绿色）。', expanded: false },
          { question: '如何处理预警？', answer: '点击预警卡片进入详情，根据状态选择"接单"或"完成"操作。', expanded: false },
          { question: '预警处理流程是什么？', answer: '待处理→接单→处理中→完成，每个状态变更都会记录处理人信息。', expanded: false }
        ],
        3: [
          { question: '如何查看设备故障？', answer: '进入"故障维修"页面，可以查看所有设备故障记录和处理状态。', expanded: false },
          { question: '设备离线怎么办？', answer: '请检查设备电源和网络连接，如仍有问题请联系维修人员。', expanded: false },
          { question: '如何分配维修任务？', answer: '在故障详情页面，可以选择维修人员进行任务分配。', expanded: false }
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
      uni.showToast({ title: '功能开发中', icon: 'none' })
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

<style>
.help-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

/* 分类标签 */
.category-list {
  display: flex;
  background: #fff;
  padding: 20rpx;
  gap: 16rpx;
  overflow-x: auto;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #f5f7fa;
  border-radius: 16rpx;
  min-width: 140rpx;
}

.category-item.active {
  background: #667eea;
}

.category-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.category-name {
  font-size: 26rpx;
  color: #666;
}

.category-item.active .category-name {
  color: #fff;
}

/* FAQ区域 */
.faq-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  padding-left: 20rpx;
  border-left: 6rpx solid #667eea;
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.faq-item {
  background: #f5f7fa;
  border-radius: 12rpx;
  overflow: hidden;
}

.faq-question {
  display: flex;
  align-items: center;
  padding: 24rpx;
}

.q-mark {
  width: 40rpx;
  height: 40rpx;
  background: #667eea;
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
  border-radius: 8rpx;
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
}

.expand-icon.expanded {
  transform: rotate(180deg);
}

.faq-answer {
  display: flex;
  padding: 0 24rpx 24rpx;
  border-top: 1rpx solid #e8e8e8;
  margin-top: 0;
  padding-top: 24rpx;
}

.a-mark {
  width: 40rpx;
  height: 40rpx;
  background: #2ed573;
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
  border-radius: 8rpx;
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

/* 联系客服 */
.contact-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.contact-list {
  display: flex;
  gap: 20rpx;
}

.contact-item {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #f5f7fa;
  border-radius: 12rpx;
}

.contact-icon {
  font-size: 48rpx;
  margin-right: 16rpx;
}

.contact-info {
  display: flex;
  flex-direction: column;
}

.contact-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.contact-value {
  font-size: 24rpx;
  color: #999;
}

/* 反馈按钮 */
.feedback-btn {
  margin: 40rpx 30rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 16rpx;
  text-align: center;
}

.feedback-btn text {
  font-size: 30rpx;
  color: #667eea;
}

/* 意见反馈表单 */
.feedback-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.feedback-form {
  display: flex;
  flex-direction: column;
}

.feedback-input {
  width: 100%;
  height: 200rpx;
  background: #f5f7fa;
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
}

.feedback-btns .btn {
  flex: 1;
  padding: 24rpx 0;
  border-radius: 12rpx;
  text-align: center;
  font-size: 30rpx;
}

.btn-cancel {
  background: #f5f7fa;
  color: #666;
}

.btn-submit {
  background: #667eea;
  color: #fff;
}
</style>
