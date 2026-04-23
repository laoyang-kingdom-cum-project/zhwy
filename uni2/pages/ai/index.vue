<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="header">
      <text class="title">🤖 AI物业助手</text>
      <text class="subtitle">智能问答 · 应急方案 · 知识库</text>
    </view>
    
    <!-- 快捷功能 -->
    <view class="quick-section">
      <view class="section-title">快捷功能</view>
      <view class="quick-grid">
        <view class="quick-item" @click="askEmergencyPlan">
          <text class="quick-icon">📋</text>
          <text class="quick-text">应急方案</text>
        </view>
        <view class="quick-item" @click="askResources">
          <text class="quick-icon">🏥</text>
          <text class="quick-text">周边资源</text>
        </view>
        <view class="quick-item" @click="askProcedures">
          <text class="quick-icon">📖</text>
          <text class="quick-text">处理流程</text>
        </view>
        <view class="quick-item" @click="askRegulations">
          <text class="quick-icon">⚖️</text>
          <text class="quick-text">法规政策</text>
        </view>
      </view>
    </view>
    
    <!-- 对话区域 -->
    <scroll-view class="chat-area" scroll-y :scroll-top="scrollTop" scroll-with-animation>
      <view class="message-list">
        <view class="message-item" v-for="(msg, index) in messages" :key="index" :class="msg.type">
          <view class="avatar">
            <text>{{msg.type === 'user' ? '👤' : '🤖'}}</text>
          </view>
          <view class="content">
            <text class="text">{{msg.content}}</text>
            <view class="time">{{msg.time}}</view>
          </view>
        </view>
        <view class="loading-msg" v-if="loading">
          <view class="avatar">
            <text>🤖</text>
          </view>
          <view class="content">
            <view class="typing">
              <text></text>
              <text></text>
              <text></text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
    
    <!-- 输入区域 -->
    <view class="input-area">
      <view class="input-wrap">
        <input 
          class="input" 
          v-model="inputMsg" 
          placeholder="请输入您的问题..." 
          confirm-type="send"
          @confirm="sendMessage"
        />
        <view class="send-btn" :class="{active: inputMsg.trim()}" @click="sendMessage">
          <text>发送</text>
        </view>
      </view>
      <view class="quick-questions">
        <text class="quick-q" v-for="(q, i) in quickQuestions" :key="i" @click="quickAsk(q)">{{q}}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { sendMessage, getEmergencyPlan } from '@/api/ai.js'

export default {
  data() {
    return {
      messages: [
        {
          type: 'ai',
          content: '您好！我是物业AI助手，可以帮您：\n1. 查询应急处理方案\n2. 获取周边应急资源\n3. 解答物业管理问题\n4. 提供政策法规咨询\n\n请问有什么可以帮您？',
          time: this.formatTime()
        }
      ],
      inputMsg: '',
      loading: false,
      scrollTop: 0,
      quickQuestions: [
        '火灾应急方案',
        '燃气泄漏处理',
        '电梯困人救援',
        '设备报修流程'
      ]
    }
  },
  watch: {
    messages() {
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    }
  },
  methods: {
    formatTime() {
      const now = new Date()
      return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
    },
    
    scrollToBottom() {
      this.scrollTop = this.messages.length * 200
    },
    
    async sendMessage() {
      const msg = this.inputMsg.trim()
      if (!msg) return
      
      // 添加用户消息
      this.messages.push({
        type: 'user',
        content: msg,
        time: this.formatTime()
      })
      
      this.inputMsg = ''
      this.loading = true
      
      try {
        const res = await sendMessage({ message: msg })
        if (res.code === 200 && res.data) {
          this.messages.push({
            type: 'ai',
            content: res.data.reply,
            time: this.formatTime()
          })
        } else {
          this.messages.push({
            type: 'ai',
            content: '抱歉，我暂时无法回答您的问题，请稍后再试。',
            time: this.formatTime()
          })
        }
      } catch (e) {
        console.error('发送消息失败', e)
        // 模拟回复
        setTimeout(() => {
          this.messages.push({
            type: 'ai',
            content: this.getMockReply(msg),
            time: this.formatTime()
          })
        }, 1000)
      } finally {
        this.loading = false
      }
    },
    
    getMockReply(msg) {
      if (msg.includes('火')) {
        return '【火灾应急处理方案】\n\n1. 立即拨打119报警\n2. 组织人员疏散，确保居民安全\n3. 切断电源和燃气\n4. 使用灭火器进行初期灭火\n5. 引导消防车辆进入\n6. 清点人数，确保无人滞留\n\n⚠️ 注意：切勿乘坐电梯！'
      } else if (msg.includes('燃气') || msg.includes('煤气')) {
        return '【燃气泄漏处理方案】\n\n1. 立即关闭燃气总阀\n2. 打开门窗通风\n3. 禁止开关电器、使用明火\n4. 疏散周边人员\n5. 拨打燃气公司电话：95598\n6. 在安全区域等待专业人员处理\n\n⚠️ 注意：切勿在现场拨打电话！'
      } else if (msg.includes('电梯')) {
        return '【电梯困人救援流程】\n\n1. 安抚被困人员情绪\n2. 立即联系电梯维保单位\n3. 设置警示标志，防止他人使用\n4. 持续与被困人员保持沟通\n5. 必要时拨打119请求救援\n6. 记录事件经过，上报相关部门\n\n⚠️ 注意：切勿自行撬门救援！'
      }
      return '收到您的问题，我会尽快为您解答。如需更详细的帮助，请联系物业管理中心。'
    },
    
    quickAsk(q) {
      this.inputMsg = q
      this.sendMessage()
    },
    
    askEmergencyPlan() {
      this.inputMsg = '查询应急方案'
      this.sendMessage()
    },
    
    askResources() {
      this.inputMsg = '周边应急资源'
      this.sendMessage()
    },
    
    askProcedures() {
      this.inputMsg = '处理流程'
      this.sendMessage()
    },
    
    askRegulations() {
      this.inputMsg = '法规政策'
      this.sendMessage()
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx;
  text-align: center;
}

.title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.subtitle {
  display: block;
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 10rpx;
}

.quick-section {
  background: #fff;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
}

.quick-grid {
  display: flex;
  flex-wrap: wrap;
}

.quick-item {
  width: 25%;
  text-align: center;
  padding: 20rpx 0;
}

.quick-icon {
  display: block;
  font-size: 48rpx;
  margin-bottom: 10rpx;
}

.quick-text {
  display: block;
  font-size: 24rpx;
  color: #666;
}

.chat-area {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;
}

.message-list {
  padding-bottom: 20rpx;
}

.message-item {
  display: flex;
  margin-bottom: 30rpx;
}

.message-item.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 20rpx;
}

.avatar text {
  font-size: 40rpx;
}

.content {
  max-width: 70%;
  padding: 20rpx;
  border-radius: 16rpx;
  background: #fff;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}

.message-item.user .content {
  background: #667eea;
}

.text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  white-space: pre-wrap;
}

.message-item.user .text {
  color: #fff;
}

.time {
  font-size: 22rpx;
  color: #999;
  margin-top: 10rpx;
  text-align: right;
}

.message-item.user .time {
  color: rgba(255,255,255,0.7);
}

.loading-msg {
  display: flex;
  margin-bottom: 30rpx;
}

.typing {
  display: flex;
  align-items: center;
  padding: 20rpx;
}

.typing text {
  width: 16rpx;
  height: 16rpx;
  background: #ccc;
  border-radius: 50%;
  margin: 0 6rpx;
  animation: typing 1.4s infinite;
}

.typing text:nth-child(2) {
  animation-delay: 0.2s;
}

.typing text:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10rpx);
  }
}

.input-area {
  background: #fff;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid #f0f0f0;
}

.input-wrap {
  display: flex;
  align-items: center;
  background: #f5f7fa;
  border-radius: 40rpx;
  padding: 10rpx 10rpx 10rpx 30rpx;
}

.input {
  flex: 1;
  height: 60rpx;
  font-size: 28rpx;
  color: #333;
}

.send-btn {
  padding: 16rpx 40rpx;
  background: #ccc;
  border-radius: 30rpx;
  margin-left: 10rpx;
}

.send-btn text {
  font-size: 28rpx;
  color: #fff;
}

.send-btn.active {
  background: #667eea;
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  margin-top: 20rpx;
}

.quick-q {
  padding: 10rpx 24rpx;
  background: #f5f7fa;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #667eea;
  margin-right: 16rpx;
  margin-bottom: 10rpx;
}
</style>
