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
            <image class="avatar-icon" src="/static/emojis/emoji_01_robot.png" />
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
            <image class="avatar-icon" src="/static/emojis/emoji_02_person.png" />
          </view>
        </view>


      <!-- 加载中 -->
        <view class="message-row ai-row" v-if="loading">
          <view class="avatar ai-avatar">
            <image class="avatar-icon" src="/static/emojis/emoji_01_robot.png" />
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
          <image class="question-icon" src="/static/emojis/emoji_25_bulb.png" />
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
          <image class="send-icon" src="/static/emojis/emoji_10_send.png" />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import careModeMixin from '@/mixins/careMode.js'
import { uniAiConfig } from '@/config/ai-config.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      messages: [
        {
          role: 'assistant',
          content: uniAiConfig.welcomeMessage,
          time: this.getCurrentTime()
        }
      ],
      inputMessage: '',
      loading: false,
      scrollTop: 0,
      quickQuestions: uniAiConfig.quickQuestions,
      conversationId: ''
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

    // 发送消息方法
    async sendMessage() {
      const message = this.inputMessage.trim()
      if (!message || this.loading) return

      this.messages.push({
        role: 'user',
        content: message,
        time: this.getCurrentTime()
      })

      this.inputMessage = ''
      this.scrollToBottom()
      this.loading = true

      try {
        await this.callAIStream(message)
      } catch (error) {
        console.error('AI请求失败', error)
        setTimeout(() => {
          this.messages.push({
            role: 'assistant',
            content: '抱歉，AI服务暂时不可用，请稍后再试。',
            time: this.getCurrentTime()
          })
          this.loading = false
          this.scrollToBottom()
        }, 500)
      }
    },

    // 发送快捷问题
    sendQuickQuestion(question) {
      // 将快捷问题设置为输入内容
      this.inputMessage = question
      // 调用发送消息方法
      this.sendMessage()
      // sendQuickQuestion方法结束
    },

    // 滚动到底部
    scrollToBottom() {
      // 延迟100ms执行滚动
      setTimeout(() => {
        // 设置滚动位置到底部
        this.scrollTop = this.messages.length * 1000
      }, 100)
      // scrollToBottom方法结束
    },

    // 格式化AI回复，去除 think 标签及其内容
    formatAIResponse(response) {
      // 如果响应为空则返回空字符串
      if (!response) return ''
      // 删除think标签及其内部内容
      let formatted = response.replace(/<think>[\s\S]*?<\/think>/gi, '')
      // 删除开头的br标签和换行
      formatted = formatted.replace(/^(<br\s*\/?>\s*)+|^\n+/, '')
      // 删除多余的空行
      formatted = formatted.replace(/\n{3,}/g, '\n\n')
      // 去除首尾空白
      formatted = formatted.trim()
      // 返回格式化后的内容
      return formatted
      // formatAIResponse方法结束
    },

    // 调用Dify AI接口（流式输出）
    async callAIStream(message) {
      const requestData = {
        inputs: {},
        query: message,
        user: uniAiConfig.userId,
        response_mode: 'streaming'
      }

      if (this.conversationId) {
        requestData.conversation_id = this.conversationId
      }

      try {
        const res = await uni.request({
          url: uniAiConfig.apiUrl,
          method: 'POST',
          header: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${uniAiConfig.apiKey}`
          },
          data: requestData,
          timeout: 60000
        })

        if (res.statusCode === 200 && res.data) {
          if (res.data.answer) {
            this.messages.push({
              role: 'assistant',
              content: this.formatAIResponse(res.data.answer),
              time: this.getCurrentTime()
            })
          }
          if (res.data.conversation_id) {
            this.conversationId = res.data.conversation_id
          }
          this.loading = false
          this.scrollToBottom()
          return res.data
        } else {
          this.messages.push({
            role: 'assistant',
            content: 'AI请求失败，请稍后重试',
            time: this.getCurrentTime()
          })
          this.loading = false
          this.scrollToBottom()
          throw new Error(res.data?.message || 'AI请求失败')
        }
      } catch (err) {
        this.messages.push({
          role: 'assistant',
          content: '网络请求失败，请检查网络连接',
          time: this.getCurrentTime()
        })
        this.loading = false
        this.scrollToBottom()
        throw err
      }
    },

    // 调用Dify AI接口（阻塞模式，备用）
    async callAI(message) {
      const requestData = {
        inputs: {},
        query: message,
        user: uniAiConfig.userId,
        response_mode: 'blocking'
      }

      if (this.conversationId) {
        requestData.conversation_id = this.conversationId
      }

      const res = await uni.request({
        url: uniAiConfig.apiUrl,
        method: 'POST',
        header: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${uniAiConfig.apiKey}`
        },
        data: requestData
      })

      if (res.statusCode === 200 && res.data.answer) {
        if (res.data.conversation_id) {
          this.conversationId = res.data.conversationId
        }
        return res.data.answer
      } else {
        throw new Error(res.data.message || 'AI请求失败')
      }
    }
  }
}
</script>

<style lang="scss">
.ai-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

// 聊天列表
.chat-list {
  flex: 1;
  overflow-y: auto;
}

.chat-content {
  padding: 30rpx 30rpx 160rpx;
  box-sizing: border-box;
}

// 消息行
.message-row {
  display: flex;
  margin-bottom: 40rpx;
  align-items: flex-start;

  &.ai-row {
    justify-content: flex-start;
  }

  &.user-row {
    justify-content: flex-end;
  }
}

// 头像
.avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  &.ai-avatar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    margin-right: 20rpx;
  }

  &.user-avatar {
    background: #007AFF;
    margin-left: 20rpx;
  }

  .avatar-icon {
    width: 40rpx;
    height: 40rpx;
  }
}

// 消息体
.message-body {
  display: flex;
  flex-direction: column;
  max-width: calc(100% - 100rpx);

  &.user-body {
    align-items: flex-end;
  }
}

// 消息气泡
.message-bubble {
  padding: 24rpx 28rpx;
  word-break: break-all;
  border-radius: 24rpx;

  &.ai-bubble {
    background: #fff;
    box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);

    .message-text {
      color: #333;
    }
  }

  &.user-bubble {
    background: #007AFF;

    .message-text {
      color: #fff;
    }
  }

  .message-text {
    font-size: 30rpx;
    line-height: 1.6;
  }
}

// 消息时间
.message-time {
  font-size: 22rpx;
  color: #999;
  margin-top: 12rpx;

  &.user-time {
    text-align: right;
  }
}

// 加载动画
.loading-bubble {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 24rpx 32rpx;
  background: #fff;
  border-radius: 24rpx;
  border-top-left-radius: 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);

  .loading-dot {
    width: 16rpx;
    height: 16rpx;
    background: #ccc;
    border-radius: 50%;
    animation: bounce 1.4s infinite ease-in-out both;

    &:nth-child(1) {
      animation-delay: -0.32s;
    }

    &:nth-child(2) {
      animation-delay: -0.16s;
    }
  }
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

// 快捷问题（绝对定位在输入框上方）
.quick-questions {
  position: fixed;
  bottom: 140rpx;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #e8e8e8;
  z-index: 99;

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

    .question-item {
      display: flex;
      align-items: center;
      padding: 16rpx 24rpx;
      background: #f0f5ff;
      border: 1rpx solid #d6e4ff;
      border-radius: 32rpx;

      &:active {
        background: #e0eaff;
      }

      .question-icon {
        width: 28rpx;
        height: 28rpx;
        margin-right: 8rpx;
      }

      .question-text {
        font-size: 26rpx;
        color: #2f54eb;
      }
    }
  }
}

// 输入区域（固定在底部）
.input-area {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx 40rpx;
  border-top: 1rpx solid #e8e8e8;
  z-index: 100;

  .input-box {
    display: flex;
    align-items: center;
    background: #f5f5f5;
    border-radius: 40rpx;
    padding: 12rpx 12rpx 12rpx 30rpx;

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

      &.active {
        background: #007AFF;
      }

      .send-icon {
        width: 32rpx;
        height: 32rpx;
      }
    }
  }
}
</style>
