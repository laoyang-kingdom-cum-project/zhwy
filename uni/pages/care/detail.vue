<template>
  <view class="care-detail-container">
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
          <image class="health-icon" src="/static/emojis/emoji_03_footprints.png" />
          <text class="health-value">{{ careInfo.steps }}</text>
          <text class="health-label">步数</text>
        </view>
        <view class="health-item">
          <image class="health-icon" src="/static/emojis/emoji_04_sleeping.png" />
          <text class="health-value">{{ careInfo.sleep }}小时</text>
          <text class="health-label">睡眠</text>
        </view>
        <view class="health-item">
          <image class="health-icon" src="/static/emojis/emoji_05_heart.png" />
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
        <text class="call-btn" @click="makePhoneCall"><image class="inline-icon" src="/static/emojis/emoji_18_phone.png" /> 拨打电话</text>
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
        <text><image class="inline-icon" src="/static/emojis/emoji_01_robot.png" /> AI健康分析</text>
        <view class="refresh-btn" @click="refreshAIAnalysis">
          <image class="refresh-icon" src="/static/emojis/emoji_17_refresh.png" />
        </view>
      </view>
      
      <view class="ai-analysis-card">
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
          <image class="empty-icon" src="/static/emojis/emoji_52_chart.png" />
          <text class="empty-text">点击刷新按钮获取AI健康分析</text>
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
import { healthAiConfig } from '@/config/ai-config.js'

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
      aiLoading: false
    }
  },
  onLoad(options) {
    this.careId = options.id
    if (this.careId) {
      this.loadCareDetail()
    }
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
    async refreshAIAnalysis() {
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
        url: healthAiConfig.apiUrl,
        method: 'POST',
        header: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${healthAiConfig.apiKey}`
        },
        data: {
          inputs: {
            health_data: healthData
          },
          query: '请分析这位家人的健康状况，并给出专业建议。',
          user: healthAiConfig.userId,
          response_mode: 'blocking'
        }
      })

      if (res.statusCode === 200 && res.data.answer) {
        // 处理AI回复：删除 <think> 标签及其内容
        return this.formatAIResponse(res.data.answer)
      } else {
        throw new Error(res.data.message || 'AI请求失败')
      }
    },

    // 格式化AI回复，去除 <think> 标签及其内容
    formatAIResponse(response) {
      if (!response) return ''
      // 删除 <think> 标签及其内部内容（包括多行内容）
      let formatted = response.replace(/<think>[\s\S]*?<\/think>/gi, '')
      // 删除开头的 <br> 标签和换行
      formatted = formatted.replace(/^(<br\s*\/?>\s*)+|^\n+/, '')
      // 删除多余的空行
      formatted = formatted.replace(/\n{3,}/g, '\n\n')
      // 去除首尾空白
      formatted = formatted.trim()
      return formatted
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

.inline-icon {
  width: 28rpx;
  height: 28rpx;
  vertical-align: middle;
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
      width: 48rpx;
      height: 48rpx;
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
        width: 32rpx;
        height: 32rpx;
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
      width: 80rpx;
      height: 80rpx;
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
