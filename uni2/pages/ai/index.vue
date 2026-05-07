<template>
  <view class="ai-container">
    <!-- 聊天记录区域 -->
    <scroll-view 
      class="chat-list" 
      scroll-y 
      :scroll-top="scrollTop"
      :scroll-with-animation="true"
    >
      <view class="chat-content">
        <!-- 消息列表 -->
        <view 
          class="message-row" 
          :class="item.role === 'assistant' ? 'ai-row' : 'user-row'" 
          v-for="(item, index) in messages" 
          :key="index"
        >
          <!-- AI头像 -->
          <view class="avatar ai-avatar" v-if="item.role === 'assistant'">
            <text class="avatar-icon">🤖</text>
          </view>
          
          <!-- 消息内容 -->
          <view class="message-body" :class="item.role === 'user' ? 'user-body' : ''">
            <view class="message-bubble" :class="item.role === 'assistant' ? 'ai-bubble' : 'user-bubble'">
              <text class="message-text">{{ item.content }}</text>
            </view>
            <text class="message-time" :class="item.role === 'user' ? 'user-time' : ''">{{ item.time }}</text>
          </view>

          <!-- 用户头像 -->
          <view class="avatar user-avatar" v-if="item.role === 'user'">
            <text class="avatar-icon">👤</text>
          </view>
        </view>

        <!-- 加载中 -->
        <view class="message-row ai-row" v-if="loading">
          <view class="avatar ai-avatar">
            <text class="avatar-icon">🤖</text>
          </view>
          <view class="loading-bubble">
            <view class="loading-dot"></view>
            <view class="loading-dot"></view>
            <view class="loading-dot"></view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 快捷问题（绝对定位在输入框上方） -->
    <view class="quick-questions" v-if="messages.length <= 2">
      <text class="quick-title">你可以问我</text>
      <view class="question-list">
        <view 
          class="question-item" 
          v-for="(q, i) in quickQuestions" 
          :key="i"
          @click="sendQuickQuestion(q)"
        >
          <text class="question-icon">💡</text>
          <text class="question-text">{{ q }}</text>
        </view>
      </view>
    </view>

    <!-- 输入区域（固定在底部） -->
    <view class="input-area">
      <view class="input-box">
        <input 
          class="chat-input" 
          v-model="inputMessage" 
          placeholder="输入消息..."
          confirm-type="send"
          @confirm="sendMessage"
          :disabled="loading"
        />
        <view 
          class="send-btn" 
          :class="{ active: inputMessage.trim() && !loading }" 
          @click="sendMessage"
        >
          <text class="send-icon">➤</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { propertyAiConfig } from '@/config/ai-config.js'

export default {
  data() {
    return {
      messages: [
        {
          role: 'assistant',
          content: propertyAiConfig.welcomeMessage,
          time: this.getCurrentTime()
        }
      ],
      inputMessage: '',
      loading: false,
      scrollTop: 0,
      quickQuestions: propertyAiConfig.quickQuestions
    }
  },
  methods: {
    // 获取当前时间
    getCurrentTime() {
      const now = new Date()
      const hours = now.getHours().toString().padStart(2, '0')
      const minutes = now.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },

    // 发送消息
    async sendMessage() {
      const message = this.inputMessage.trim()
      if (!message || this.loading) return

      // 添加用户消息
      this.messages.push({
        role: 'user',
        content: message,
        time: this.getCurrentTime()
      })

      this.inputMessage = ''
      this.scrollToBottom()
      this.loading = true

      // 调用AI接口
      try {
        let aiResponse = await this.callAI(message)
        // 删除开头的 <br> 标签和换行
        aiResponse = aiResponse.replace(/^(<br\s*\/?>\s*)+|^\n+/, '')
        this.messages.push({
          role: 'assistant',
          content: aiResponse,
          time: this.getCurrentTime()
        })
      } catch (error) {
        console.error('AI请求失败', error)
        // 使用模拟回复
        setTimeout(() => {
          this.messages.push({
            role: 'assistant',
            content: this.getMockReply(message),
            time: this.getCurrentTime()
          })
        }, 500)
      } finally {
        this.loading = false
        this.scrollToBottom()
      }
    },

    // 发送快捷问题
    sendQuickQuestion(question) {
      this.inputMessage = question
      this.sendMessage()
    },

    // 滚动到底部
    scrollToBottom() {
      setTimeout(() => {
        this.scrollTop = this.messages.length * 1000
      }, 100)
    },

    // 调用SiliconFlow AI接口
    async callAI(message) {
      // 构建消息历史（只取最近10条避免超出上下文）
      const historyMessages = this.messages.slice(-10).map(m => ({
        role: m.role,
        content: m.content
      }))

      const res = await uni.request({
        url: propertyAiConfig.apiUrl,
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          model: propertyAiConfig.model,
          messages: [
            {
              role: 'system',
              content: propertyAiConfig.systemPrompt
            },
            ...historyMessages,
            { role: 'user', content: message }
          ],
          temperature: propertyAiConfig.temperature,
          max_tokens: propertyAiConfig.maxTokens,
          stream: false
        }
      })

      if (res.statusCode === 200 && res.data.choices && res.data.choices[0]) {
        return res.data.choices[0].message.content
      } else {
        throw new Error(res.data.error?.message || 'AI请求失败')
      }
    },

    // 模拟回复（当AI接口不可用时使用）
    getMockReply(msg) {
      if (msg.includes('火')) {
        return '【火灾应急处理方案】\n\n1. 立即拨打119报警\n2. 组织人员疏散，确保居民安全\n3. 切断电源和燃气\n4. 使用灭火器进行初期灭火\n5. 引导消防车辆进入\n6. 清点人数，确保无人滞留\n\n⚠️ 注意：切勿乘坐电梯！'
      } else if (msg.includes('燃气') || msg.includes('煤气')) {
        return '【燃气泄漏处理方案】\n\n1. 立即关闭燃气总阀\n2. 打开门窗通风\n3. 禁止开关电器、使用明火\n4. 疏散周边人员\n5. 拨打燃气公司电话：95598\n6. 在安全区域等待专业人员处理\n\n⚠️ 注意：切勿在现场拨打电话！'
      } else if (msg.includes('电梯')) {
        return '【电梯困人救援流程】\n\n1. 安抚被困人员情绪\n2. 立即联系电梯维保单位\n3. 设置警示标志，防止他人使用\n4. 持续与被困人员保持沟通\n5. 必要时拨打119请求救援\n6. 记录事件经过，上报相关部门\n\n⚠️ 注意：切勿自行撬门救援！'
      } else if (msg.includes('设备') || msg.includes('报修')) {
        return '【设备报修流程】\n\n1. 接收居民报修申请\n2. 核实故障情况并分类\n3. 分配维修人员处理\n4. 跟踪维修进度\n5. 完成后通知居民验收\n6. 归档维修记录\n\n您可以在"服务订单"页面查看所有报修记录。'
      }
      return '收到您的问题，我会尽快为您解答。如需更详细的帮助，请联系物业管理中心。'
    }
  }
}
</script>

<style>
.ai-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

/* 聊天列表 */
.chat-list {
  flex: 1;
  overflow-y: auto;
}

.chat-content {
  padding: 30rpx 30rpx 300rpx;
  box-sizing: border-box;
}

/* 消息行 */
.message-row {
  display: flex;
  margin-bottom: 40rpx;
  align-items: flex-start;
}

.message-row.ai-row {
  justify-content: flex-start;
}

.message-row.user-row {
  justify-content: flex-end;
}

/* 头像 */
.avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar.ai-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-right: 20rpx;
}

.avatar.user-avatar {
  background: #667eea;
  margin-left: 20rpx;
}

.avatar-icon {
  font-size: 36rpx;
}

/* 消息体 */
.message-body {
  display: flex;
  flex-direction: column;
  max-width: calc(100% - 100rpx);
}

.message-body.user-body {
  align-items: flex-end;
}

/* 消息气泡 */
.message-bubble {
  padding: 24rpx 28rpx;
  word-break: break-all;
  border-radius: 24rpx;
}

.message-bubble.ai-bubble {
  background: #fff;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}

.message-bubble.ai-bubble .message-text {
  color: #333;
}

.message-bubble.user-bubble {
  background: #667eea;
}

.message-bubble.user-bubble .message-text {
  color: #fff;
}

.message-text {
  font-size: 30rpx;
  line-height: 1.6;
}

/* 消息时间 */
.message-time {
  font-size: 22rpx;
  color: #999;
  margin-top: 12rpx;
}

.message-time.user-time {
  text-align: right;
}

/* 加载动画 */
.loading-bubble {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 24rpx 32rpx;
  background: #fff;
  border-radius: 24rpx;
  border-top-left-radius: 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}

.loading-dot {
  width: 16rpx;
  height: 16rpx;
  background: #ccc;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.loading-dot:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 快捷问题（绝对定位在输入框上方） */
.quick-questions {
  position: fixed;
  bottom: 140rpx;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #e8e8e8;
  z-index: 99;
}

.quick-title {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 16rpx;
  display: block;
}

.question-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.question-item {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  background: #f0f5ff;
  border: 1rpx solid #d6e4ff;
  border-radius: 32rpx;
}

.question-item:active {
  background: #e0eaff;
}

.question-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.question-text {
  font-size: 26rpx;
  color: #667eea;
}

/* 输入区域（固定在底部） */
.input-area {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx 40rpx;
  border-top: 1rpx solid #e8e8e8;
  z-index: 100;
}

.input-box {
  display: flex;
  align-items: center;
  background: #f5f5f5;
  border-radius: 40rpx;
  padding: 12rpx 12rpx 12rpx 30rpx;
}

.chat-input {
  flex: 1;
  height: 56rpx;
  font-size: 30rpx;
  color: #333;
}

.send-btn {
  width: 72rpx;
  height: 56rpx;
  background: #d9d9d9;
  border-radius: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 12rpx;
  transition: all 0.3s;
}

.send-btn.active {
  background: #667eea;
}

.send-icon {
  font-size: 28rpx;
  color: #fff;
  margin-left: 4rpx;
}
</style>
