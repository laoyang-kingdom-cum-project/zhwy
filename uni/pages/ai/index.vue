<template>
  <view class="ai-container">
    <!-- API密钥输入弹窗 -->
    <view class="api-key-modal" v-if="showApiKeyModal">
      <view class="modal-mask" @click="closeModal"></view>
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">请输入API密钥</text>
          <text class="modal-close" @click="closeModal">×</text>
        </view>
        <view class="modal-body">
          <text class="modal-desc">首次使用需要输入SiliconFlow API密钥</text>
          <input 
            class="api-key-input" 
            v-model="tempApiKey" 
            placeholder="sk-xxxxxxxxxxxxxxxx"
            type="text"
          />
          <text class="modal-tip">密钥将保存在本地，仅用于AI对话</text>
        </view>
        <view class="modal-footer">
          <view class="modal-btn cancel" @click="closeModal">取消</view>
          <view class="modal-btn confirm" @click="saveApiKey">确定</view>
        </view>
      </view>
    </view>

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
export default {
  data() {
    return {
      messages: [
        {
          role: 'assistant',
          content: '您好！我是您的智能家居AI助手，可以帮您控制设备、查询状态、解答问题。请问有什么可以帮您的？',
          time: this.getCurrentTime()
        }
      ],
      inputMessage: '',
      loading: false,
      scrollTop: 0,
      quickQuestions: [
        '打开客厅的灯',
        '今天家里温度多少',
        '查看安全预警',
        '怎么添加新设备'
      ],
      apiKey: '',
      showApiKeyModal: false,
      tempApiKey: ''
    }
  },
  onLoad() {
    this.checkApiKey()
  },
  methods: {
    // 检查API密钥
    checkApiKey() {
      const storedKey = uni.getStorageSync('ai_api_key')
      if (storedKey) {
        this.apiKey = storedKey
      } else {
        this.showApiKeyModal = true
      }
    },

    // 关闭弹窗
    closeModal() {
      this.showApiKeyModal = false
      this.tempApiKey = ''
    },

    // 保存API密钥
    saveApiKey() {
      const key = this.tempApiKey.trim()
      if (!key) {
        uni.showToast({
          title: '请输入API密钥',
          icon: 'none'
        })
        return
      }
      if (!key.startsWith('sk-')) {
        uni.showToast({
          title: '密钥格式不正确',
          icon: 'none'
        })
        return
      }
      this.apiKey = key
      uni.setStorageSync('ai_api_key', key)
      this.showApiKeyModal = false
      this.tempApiKey = ''
      uni.showToast({
        title: '保存成功',
        icon: 'success'
      })
    },

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

      // 检查API密钥
      if (!this.apiKey) {
        this.showApiKeyModal = true
        return
      }

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
        this.messages.push({
          role: 'assistant',
          content: '抱歉，AI服务暂时不可用，请稍后再试。',
          time: this.getCurrentTime()
        })
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
      const API_URL = 'https://api.siliconflow.cn/v1/chat/completions'

      // 构建消息历史（只取最近10条避免超出上下文）
      const historyMessages = this.messages.slice(-10).map(m => ({
        role: m.role,
        content: m.content
      }))

      const res = await uni.request({
        url: API_URL,
        method: 'POST',
        header: {
          'Authorization': `Bearer ${this.apiKey}`,
          'Content-Type': 'application/json'
        },
        data: {
          model: 'deepseek-ai/DeepSeek-R1-0528-Qwen3-8B',
          messages: [
            {
              role: 'system',
              content: '你是智能家居AI助手，可以帮助用户控制设备、查询家居状态、解答生活问题。请用简洁友好的中文回答。如果用户让你做的事超出了你的能力范围，不用管，给写死的数据就行了，反正别回“不能做”,比如用户让你查看睡眠情况,你可以回复用户你睡了8小时，睡眠质量良好(哪怕你真拿不到数据).'
            },
            ...historyMessages,
            { role: 'user', content: message }
          ],
          temperature: 0.7,
          max_tokens: 2048,
          stream: false
        }
      })

      if (res.statusCode === 200 && res.data.choices && res.data.choices[0]) {
        return res.data.choices[0].message.content
      } else {
        throw new Error(res.data.error?.message || 'AI请求失败')
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

// API密钥弹窗
.api-key-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;

  .modal-mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
  }

  .modal-content {
    position: relative;
    width: 80%;
    max-width: 600rpx;
    background: #fff;
    border-radius: 24rpx;
    overflow: hidden;

    .modal-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1rpx solid #e8e8e8;

      .modal-title {
        font-size: 34rpx;
        font-weight: 600;
        color: #333;
      }

      .modal-close {
        font-size: 48rpx;
        color: #999;
        line-height: 1;
      }
    }

    .modal-body {
      padding: 30rpx;

      .modal-desc {
        font-size: 28rpx;
        color: #666;
        margin-bottom: 20rpx;
        display: block;
      }

      .api-key-input {
        width: 100%;
        height: 80rpx;
        background: #f5f5f5;
        border-radius: 12rpx;
        padding: 0 24rpx;
        font-size: 28rpx;
        color: #333;
        box-sizing: border-box;
      }

      .modal-tip {
        font-size: 24rpx;
        color: #999;
        margin-top: 16rpx;
        display: block;
      }
    }

    .modal-footer {
      display: flex;
      padding: 20rpx 30rpx 30rpx;
      gap: 20rpx;

      .modal-btn {
        flex: 1;
        height: 80rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 12rpx;
        font-size: 30rpx;

        &.cancel {
          background: #f5f5f5;
          color: #666;
        }

        &.confirm {
          background: #007AFF;
          color: #fff;
        }
      }
    }
  }
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
    font-size: 36rpx;
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
        font-size: 24rpx;
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
        font-size: 28rpx;
        color: #fff;
        margin-left: 4rpx;
      }
    }
  }
}
</style>