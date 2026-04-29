<template>
  <view class="care-detail-container">
    <!-- API密钥输入弹窗 -->
    <view class="api-key-modal" v-if="showApiKeyModal">
      <view class="modal-mask" @click="closeModal"></view>
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">请输入API密钥</text>
          <text class="modal-close" @click="closeModal">×</text>
        </view>
        <view class="modal-body">
          <text class="modal-desc">首次使用AI健康助手需要输入SiliconFlow API密钥</text>
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

    <!-- 用户信息卡片 -->
    <view class="user-card">
      <image class="user-avatar" src="/static/logo.png" mode="aspectFill"></image>
      <view class="user-info">
        <text class="user-name">{{ careInfo.name }}</text>
        <text class="user-meta">{{ careInfo.age }}岁 | {{ careInfo.room }}</text>
        <view class="health-status" :class="careInfo.healthStatus === '0' ? 'normal' : 'warning'">
          {{ careInfo.healthStatus === '0' ? '正常' : careInfo.healthStatus === '1' ? '关注' : '异常' }}
        </view>
      </view>
    </view>

    <!-- 健康数据 -->
    <view class="section">
      <view class="section-title">今日健康数据</view>
      <view class="health-grid">
        <view class="health-item">
          <text class="health-icon">👣</text>
          <text class="health-value">{{ careInfo.steps }}</text>
          <text class="health-label">步数</text>
        </view>
        <view class="health-item">
          <text class="health-icon">😴</text>
          <text class="health-value">{{ careInfo.sleep }}小时</text>
          <text class="health-label">睡眠</text>
        </view>
        <view class="health-item">
          <text class="health-icon">❤️</text>
          <text class="health-value">{{ careInfo.heartRate }}</text>
          <text class="health-label">心率</text>
        </view>
        <view class="health-item">
          <text class="health-icon">🩸</text>
          <text class="health-value">{{ careInfo.pressure || '120/80' }}</text>
          <text class="health-label">血压</text>
        </view>
      </view>
    </view>

    <!-- 紧急联系人 -->
    <view class="section">
      <view class="section-title">紧急联系人</view>
      <view class="contact-item">
        <text class="contact-name">{{ careInfo.emergencyContact }}</text>
        <text class="call-btn" @click="makePhoneCall">📞 拨打电话</text>
      </view>
    </view>

    <!-- 活动记录 -->
    <view class="section">
      <view class="section-title">最近活动</view>
      <view class="activity-list">
        <view class="activity-item">
          <view class="activity-time">{{ formatTime(careInfo.lastActive) }}</view>
          <view class="activity-content">最后活动时间</view>
        </view>
      </view>
    </view>

    <!-- AI健康分析 -->
    <view class="section ai-section">
      <view class="section-title">
        <text>🤖 AI健康分析</text>
        <view class="refresh-btn" @click="refreshAIAnalysis" v-if="apiKey">
          <text class="refresh-icon">🔄</text>
        </view>
      </view>
      
      <view class="ai-analysis-card" v-if="apiKey">
        <view class="ai-header">
          <text class="ai-title">智能健康评估</text>
          <text class="ai-time">{{ aiAnalysisTime }}</text>
        </view>
        
        <view class="ai-content" v-if="aiAnalysis">
          <text class="ai-text">{{ aiAnalysis }}</text>
        </view>
        
        <view class="ai-loading" v-else-if="aiLoading">
          <view class="loading-dots">
            <view class="loading-dot"></view>
            <view class="loading-dot"></view>
            <view class="loading-dot"></view>
          </view>
          <text class="loading-text">正在分析健康数据...</text>
        </view>
        
        <view class="ai-empty" v-else>
          <text class="empty-icon">📊</text>
          <text class="empty-text">点击刷新按钮获取AI健康分析</text>
        </view>
      </view>
      
      <!-- Token输入框 -->
      <view class="token-input-card" v-else>
        <view class="token-header">
          <text class="token-title">🔑 请输入API密钥</text>
          <text class="token-desc">首次使用AI健康分析需要输入SiliconFlow API密钥</text>
        </view>
        <view class="token-input-wrap">
          <input 
            class="token-input" 
            v-model="tempApiKey" 
            placeholder="sk-xxxxxxxxxxxxxxxx"
            type="text"
          />
          <text class="token-tip">密钥将保存在本地，仅用于AI分析</text>
        </view>
        <view class="token-actions">
          <view class="token-btn cancel" @click="tempApiKey = ''">取消</view>
          <view class="token-btn confirm" @click="saveApiKey">确定</view>
        </view>
      </view>
    </view>

    <!-- 关怀按钮 -->
    <view class="action-section">
      <button class="care-btn" @click="sendCare">发送关怀提醒</button>
    </view>
  </view>
</template>

<script>
import { getCareDetail } from '@/api/care.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      careId: null,
      careInfo: {
        name: '',
        age: 0,
        room: '',
        emergencyContact: '',
        healthStatus: '0',
        steps: 0,
        sleep: 0,
        heartRate: 0,
        pressure: '',
        lastActive: ''
      },
      // AI相关
      aiAnalysis: '',
      aiAnalysisTime: '',
      aiLoading: false,
      apiKey: '',
      showApiKeyModal: false,
      tempApiKey: ''
    }
  },
  onLoad(options) {
    this.careId = options.id
    if (this.careId) {
      this.loadCareDetail()
    }
    this.checkApiKey()
  },
  methods: {
    // 加载关怀对象详情
    async loadCareDetail() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await getCareDetail(this.careId)
        uni.hideLoading()
        
        if (res.code === 200 && res.data) {
          this.careInfo = res.data
        } else {
          uni.showToast({ title: '获取数据失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('获取关怀详情失败', e)
        uni.showToast({ title: '获取数据失败', icon: 'none' })
      }
    },
    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      const now = new Date()
      const diff = now - date
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(diff / 3600000)
      const days = Math.floor(diff / 86400000)
      
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return `${minutes}分钟前`
      if (hours < 24) return `${hours}小时前`
      if (days < 30) return `${days}天前`
      return timeStr.substring(0, 10)
    },
    makePhoneCall() {
      // 从紧急联系人中提取手机号
      const phoneMatch = this.careInfo.emergencyContact.match(/(\d{11})/)
      if (phoneMatch) {
        uni.makePhoneCall({
          phoneNumber: phoneMatch[1]
        })
      } else {
        uni.showToast({ title: '未找到有效手机号', icon: 'none' })
      }
    },
    sendCare() {
      uni.showToast({
        title: '关怀提醒已发送',
        icon: 'success'
      })
    },
    // ========== AI相关方法 ==========
    checkApiKey() {
      const storedKey = uni.getStorageSync('ai_api_key')
      if (storedKey) {
        this.apiKey = storedKey
      }
    },
    closeModal() {
      this.showApiKeyModal = false
      this.tempApiKey = ''
    },
    saveApiKey() {
      const key = this.tempApiKey.trim()
      if (!key) {
        uni.showToast({ title: '请输入API密钥', icon: 'none' })
        return
      }
      if (!key.startsWith('sk-')) {
        uni.showToast({ title: '密钥格式不正确', icon: 'none' })
        return
      }
      this.apiKey = key
      uni.setStorageSync('ai_api_key', key)
      this.showApiKeyModal = false
      this.tempApiKey = ''
      uni.showToast({ title: '保存成功', icon: 'success' })
    },
    async refreshAIAnalysis() {
      if (!this.apiKey) {
        this.showApiKeyModal = true
        return
      }
      
      this.aiLoading = true
      
      try {
        const analysis = await this.getAIAnalysis()
        this.aiAnalysis = analysis
        this.aiAnalysisTime = this.getCurrentTime()
      } catch (error) {
        console.error('AI分析失败', error)
        uni.showToast({ title: 'AI分析失败，请稍后重试', icon: 'none' })
      } finally {
        this.aiLoading = false
      }
    },
    getCurrentTime() {
      const now = new Date()
      const hours = now.getHours().toString().padStart(2, '0')
      const minutes = now.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },
    async getAIAnalysis() {
      const API_URL = 'https://api.siliconflow.cn/v1/chat/completions'
      
      const healthData = `家人健康数据：
姓名：${this.careInfo.name}
年龄：${this.careInfo.age}岁
步数：${this.careInfo.steps}步
睡眠：${this.careInfo.sleep}小时
心率：${this.careInfo.heartRate}次/分钟
血压：${this.careInfo.pressure || '未记录'}
健康状态：${this.careInfo.healthStatus === '0' ? '正常' : this.careInfo.healthStatus === '1' ? '关注' : '异常'}
最后活动：${this.formatTime(this.careInfo.lastActive)}`
      
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
              content: `你是AI健康助手，擅长分析老年人健康数据并提供专业建议。

${healthData}

请根据以上健康数据，进行全面的健康分析，包括：
1. 整体健康状况评估
2. 各项指标分析（步数、睡眠、心率、血压等）
3. 健康建议和注意事项
4. 需要关注的风险点

回答要专业、简洁、友好，用清晰的段落结构呈现。`
            },
            {
              role: 'user',
              content: '请分析这位家人的健康状况，并给出专业建议。'
            }
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
.care-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

// 用户信息卡片
.user-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx;

  .user-avatar {
    width: 140rpx;
    height: 140rpx;
    border-radius: 50%;
    border: 4rpx solid rgba(255,255,255,0.3);
    margin-right: 30rpx;
  }

  .user-info {
    display: flex;
    flex-direction: column;

    .user-name {
      font-size: 40rpx;
      font-weight: bold;
      color: #fff;
      margin-bottom: 12rpx;
    }

    .user-meta {
      font-size: 28rpx;
      color: rgba(255,255,255,0.8);
      margin-bottom: 16rpx;
    }

    .health-status {
      display: inline-block;
      padding: 8rpx 20rpx;
      border-radius: 8rpx;
      font-size: 24rpx;

      &.normal {
        background: #E8F5E9;
        color: #388E3C;
      }

      &.warning {
        background: #FFF3E0;
        color: #F57C00;
      }
    }
  }
}

// 通用区块
.section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
}

// 健康数据网格
.health-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .health-item {
    width: calc(50% - 10rpx);
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;
    margin-bottom: 20rpx;
    background: #f8f9fa;
    border-radius: 12rpx;
    box-sizing: border-box;

    .health-icon {
      font-size: 48rpx;
      margin-bottom: 12rpx;
    }

    .health-value {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 8rpx;
    }

    .health-label {
      font-size: 26rpx;
      color: #999;
    }
  }
}

// 紧急联系人
.contact-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;

  .contact-name {
    font-size: 30rpx;
    color: #333;
  }

  .call-btn {
    padding: 12rpx 24rpx;
    background: #E8F5E9;
    color: #388E3C;
    border-radius: 8rpx;
    font-size: 26rpx;
  }
}

// 活动记录
.activity-list {
  .activity-item {
    display: flex;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .activity-time {
      width: 160rpx;
      font-size: 26rpx;
      color: #999;
    }

    .activity-content {
      flex: 1;
      font-size: 28rpx;
      color: #333;
    }
  }
}

// 关怀按钮
.action-section {
  padding: 40rpx 30rpx;

  .care-btn {
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

// ========== AI相关样式 ==========
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
    background: #fff;
    border-radius: 24rpx;
    overflow: hidden;

    .modal-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 32rpx;
      border-bottom: 1rpx solid #f0f0f0;

      .modal-title {
        font-size: 34rpx;
        font-weight: bold;
        color: #333;
      }

      .modal-close {
        font-size: 48rpx;
        color: #999;
        line-height: 1;
      }
    }

    .modal-body {
      padding: 40rpx 32rpx;

      .modal-desc {
        display: block;
        font-size: 28rpx;
        color: #666;
        margin-bottom: 32rpx;
      }

      .api-key-input {
        width: 100%;
        height: 88rpx;
        padding: 0 24rpx;
        background: #f7f8fa;
        border-radius: 12rpx;
        font-size: 28rpx;
        box-sizing: border-box;
      }

      .modal-tip {
        display: block;
        font-size: 24rpx;
        color: #999;
        margin-top: 20rpx;
        text-align: center;
      }
    }

    .modal-footer {
      display: flex;
      border-top: 1rpx solid #f0f0f0;

      .modal-btn {
        flex: 1;
        height: 88rpx;
        line-height: 88rpx;
        text-align: center;
        font-size: 30rpx;

        &.cancel {
          color: #666;
          border-right: 1rpx solid #f0f0f0;
        }

        &.confirm {
          color: #007AFF;
        }
      }
    }
  }
}

// AI区块
.ai-section {
  padding: 30rpx;

  .section-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;

    .refresh-btn {
      width: 64rpx;
      height: 64rpx;
      border-radius: 50%;
      background: #f7f8fa;
      display: flex;
      align-items: center;
      justify-content: center;

      &:active {
        background: #e8e9eb;
      }

      .refresh-icon {
        font-size: 32rpx;
      }
    }
  }
}

// Token输入卡片
.token-input-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

  .token-header {
    text-align: center;
    margin-bottom: 32rpx;

    .token-title {
      display: block;
      font-size: 34rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 12rpx;
    }

    .token-desc {
      font-size: 26rpx;
      color: #999;
    }
  }

  .token-input-wrap {
    margin-bottom: 32rpx;

    .token-input {
      width: 100%;
      height: 88rpx;
      padding: 0 24rpx;
      background: #f7f8fa;
      border-radius: 12rpx;
      font-size: 28rpx;
      box-sizing: border-box;
    }

    .token-tip {
      display: block;
      font-size: 24rpx;
      color: #999;
      margin-top: 16rpx;
      text-align: center;
    }
  }

  .token-actions {
    display: flex;
    gap: 24rpx;

    .token-btn {
      flex: 1;
      height: 88rpx;
      line-height: 88rpx;
      text-align: center;
      border-radius: 12rpx;
      font-size: 30rpx;

      &.cancel {
        background: #f7f8fa;
        color: #666;
      }

      &.confirm {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
      }
    }
  }
}

// AI分析卡片
.ai-analysis-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  padding: 32rpx;
  color: #fff;

  .ai-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;

    .ai-title {
      font-size: 32rpx;
      font-weight: bold;
    }

    .ai-time {
      font-size: 24rpx;
      opacity: 0.8;
    }
  }

  .ai-content {
    .ai-text {
      font-size: 28rpx;
      line-height: 1.8;
      white-space: pre-wrap;
    }
  }

  .ai-loading {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 60rpx 0;

    .loading-dots {
      display: flex;
      margin-bottom: 24rpx;

      .loading-dot {
        width: 20rpx;
        height: 20rpx;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.5);
        margin: 0 8rpx;
        animation: loading 1.4s infinite ease-in-out both;

        &:nth-child(1) { animation-delay: -0.32s; }
        &:nth-child(2) { animation-delay: -0.16s; }
      }
    }

    .loading-text {
      font-size: 28rpx;
      opacity: 0.8;
    }
  }

  .ai-empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 60rpx 0;

    .empty-icon {
      font-size: 80rpx;
      margin-bottom: 20rpx;
      opacity: 0.6;
    }

    .empty-text {
      font-size: 28rpx;
      opacity: 0.8;
      text-align: center;
    }
  }
}

@keyframes loading {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}
</style>
