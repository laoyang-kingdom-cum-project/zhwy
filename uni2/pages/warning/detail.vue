<template>
  <view class="container">
    <!-- 预警信息卡片 -->
    <view class="info-card">
      <view class="card-title">预警信息</view>
      <view class="info-item">
        <text class="label">预警标题</text>
        <text class="value">{{warning.title}}</text>
      </view>
      <view class="info-item">
        <text class="label">预警等级</text>
        <view class="level-tag" :class="'level-' + warning.level">{{warning.levelText}}</view>
      </view>
      <view class="info-item">
        <text class="label">预警位置</text>
        <text class="value">{{warning.location}}</text>
      </view>
      <view class="info-item">
        <text class="label">预警时间</text>
        <text class="value">{{warning.time}}</text>
      </view>
      <view class="info-item">
        <text class="label">当前状态</text>
        <view class="status-tag" :class="'status-' + (warning.state || '0')">{{warning.statusText}}</view>
      </view>
      <view class="info-item" v-if="warning.remark">
        <text class="label">备注说明</text>
        <text class="value">{{warning.remark}}</text>
      </view>
    </view>
    
    <!-- AI应急方案 -->
    <view class="ai-card">
      <view class="card-title">
        <image class="ai-icon" src="/static/emojis/emoji_01_robot.png" />
        <text>AI应急方案</text>
        <view class="ai-loading" v-if="aiLoading">
          <view class="loading-dot"></view>
          <view class="loading-dot"></view>
          <view class="loading-dot"></view>
        </view>
      </view>
      <view class="plan-content" v-if="emergencyPlan">
        <text class="plan-text">{{emergencyPlan}}</text>
      </view>
      <view class="plan-placeholder" v-else-if="!aiLoading">
        <text class="placeholder-text">点击获取AI应急方案</text>
        <button class="plan-btn" @click="loadAIPlan(warning.title, warning.location)">获取方案</button>
      </view>
    </view>
    
    <!-- 周边资源 -->
    <view class="resource-card" v-if="resources.length > 0">
      <view class="card-title">周边应急资源</view>
      <view class="resource-list">
        <view class="resource-item" v-for="(item, index) in resources" :key="index" @click="callPhone(item.phone)">
          <view class="resource-info">
            <text class="resource-name">{{item.name}}</text>
            <text class="resource-address">{{item.address}}</text>
          </view>
          <view class="resource-phone">
            <image class="phone-icon" src="/static/emojis/emoji_18_phone.png" />
            <text class="phone-num">{{item.phone}}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 处理记录 -->
    <view class="record-card" v-if="warning.handleTime">
      <view class="card-title">处理记录</view>
      <view class="record-item">
        <text class="record-label">处理时间</text>
        <text class="record-value">{{warning.handleTime}}</text>
      </view>
      <view class="record-item" v-if="warning.handleBy">
        <text class="record-label">处理人</text>
        <text class="record-value">{{warning.handleBy}}</text>
      </view>
      <view class="record-item" v-if="warning.handleResult">
        <text class="record-label">处理结果</text>
        <text class="record-value">{{warning.handleResult}}</text>
      </view>
    </view>
    
    <!-- 底部操作按钮 -->
    <view class="footer-actions" v-if="(warning.state || '0') !== '2'">
      <button v-if="(warning.state || '0') === '0'" class="action-btn btn-primary" @click="handleAction('1')">接单处理</button>
      <button v-if="(warning.state || '0') === '1'" class="action-btn btn-success" @click="handleAction('2')">标记完成</button>
      <button class="action-btn btn-default" @click="contactSupport">联系支援</button>
    </view>

  </view>
</template>

<script>
import { getWarningDetail, handleWarning } from '@/api/warning.js'
import { getEmergencyResources } from '@/api/ai.js'
import { emergencyAiConfig } from '@/config/ai-config.js'

export default {
  data() {
    return {
      warningId: null,
      warning: {},
      emergencyPlan: '',
      resources: [],
      aiLoading: false
    }
  },
  onLoad(options) {
    if (options.id) {
      this.warningId = options.id
      this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      uni.showLoading({ title: '加载中...' })
      
      try {
        // 获取预警详情
        const res = await getWarningDetail(this.warningId)
        if (res.code === 200 && res.data) {
          this.warning = {
            ...res.data,
            levelText: this.getLevelText(res.data.level),
            statusText: this.getStatusText(res.data.state || '0')
          }
          
          // 获取周边资源
          this.loadResources(res.data.location)
        }
      } catch (e) {
        console.error('加载预警详情失败', e)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },

    // 使用AI接口获取应急方案（流式输出）
    async loadAIPlan(title, location) {
      this.aiLoading = true
      this.emergencyPlan = ''

      try {
        const message = `请为以下物业预警提供应急处理方案：\n预警类型：${title}\n发生位置：${location}\n\n请提供详细的应急处理步骤和注意事项。`

        await this.callAIStream(message)
      } catch (error) {
        console.error('获取AI应急方案失败', error)
        this.emergencyPlan = '获取AI方案失败，请根据现场情况采取相应措施，确保人员安全。'
      } finally {
        this.aiLoading = false
      }
    },

    // 调用 Dify AI 接口（流式输出）
    async callAIStream(message) {
      const requestData = {
        inputs: {},
        query: message,
        user: emergencyAiConfig.userId,
        response_mode: 'streaming'
      }

      let fullAnswer = ''

      return new Promise((resolve, reject) => {
        const requestTask = uni.request({
          url: emergencyAiConfig.apiUrl,
          method: 'POST',
          header: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${emergencyAiConfig.apiKey}`,
            'Accept': 'text/event-stream'
          },
          data: requestData,
          timeout: 120000,
          enableChunked: true,
          success: () => {
            resolve(fullAnswer)
          },
          fail: (err) => {
            reject(err)
          }
        })

        requestTask.onChunkReceived((res) => {
          const chunk = new Uint8Array(res.data)
          const text = new TextDecoder('utf-8').decode(chunk)
          this.parseStreamData(text, (answerText) => {
            if (answerText) {
              fullAnswer += answerText
              this.emergencyPlan = this.formatAIResponse(fullAnswer)
            }
          })
        })
      })
    },

    parseStreamData(chunkText, onAnswer) {
      const lines = chunkText.split('\n')
      for (const line of lines) {
        const trimmed = line.trim()
        if (trimmed.startsWith('data:')) {
          const jsonStr = trimmed.slice(5).trim()
          if (jsonStr === '[DONE]') continue
          try {
            const data = JSON.parse(jsonStr)
            if (data.event === 'message' && data.answer) {
              onAnswer(data.answer)
            } else if (data.event === 'agent_message' && data.answer) {
              onAnswer(data.answer)
            }
          } catch (e) {}
        }
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
    },

    async loadResources(location) {
      try {
        const res = await getEmergencyResources({ location })
        if (res.code === 200 && res.data) {
          this.resources = res.data.list || []
        }
      } catch (e) {
        console.error('获取应急资源失败', e)
      }
    },
    
    getLevelText(level) {
      const map = { '1': '紧急', '2': '重要', '3': '一般' }
      return map[level] || '一般'
    },
    
    getStatusText(status) {
      const map = { '0': '待处理', '1': '处理中', '2': '已处理' }
      return map[status] || '待处理'
    },
    
    handleAction(status) {
      const statusText = status === '1' ? '接单' : '完成'
      uni.showModal({
        title: '确认',
        content: `确定要${statusText}该预警吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const updateData = {
                ...this.warning,
                state: status
              }
              const result = await handleWarning(updateData)
              if (result.code === 200) {
                uni.showToast({
                  title: `${statusText}成功`,
                  icon: 'success'
                })
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
              }
            } catch (e) {
              uni.showToast({
                title: '操作失败',
                icon: 'none'
              })
            }
          }
        }
      })
    },
    
    contactSupport() {
      uni.makePhoneCall({
        phoneNumber: '400-888-8888'
      })
    },
    
    callPhone(phone) {
      uni.makePhoneCall({
        phoneNumber: phone
      })
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20rpx;
  padding-bottom: 140rpx;
}

.info-card, .ai-card, .resource-card, .record-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}

.card-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
}

.ai-icon {
  width: 32rpx;
  height: 32rpx;
  margin-right: 12rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f7fa;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 28rpx;
  color: #666;
}

.value {
  font-size: 28rpx;
  color: #333;
  max-width: 400rpx;
  text-align: right;
}

.level-tag, .status-tag {
  padding: 8rpx 20rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.level-1 { background: #FFE5E5; color: #FF6B6B; }
.level-2 { background: #FFF4E5; color: #FFA502; }
.level-3 { background: #E5F5E5; color: #2ED573; }

.status-0 { background: #FFE5E5; color: #FF6B6B; }
.status-1 { background: #FFF4E5; color: #FFA502; }
.status-2 { background: #E5F5E5; color: #2ED573; }

/* AI应急方案 */
.ai-loading {
  display: flex;
  gap: 8rpx;
  margin-left: 16rpx;
}

.loading-dot {
  width: 12rpx;
  height: 12rpx;
  background: #667eea;
  border-radius: 50%;
  animation: loading 1.4s infinite ease-in-out both;
}

.loading-dot:nth-child(1) { animation-delay: -0.32s; }
.loading-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes loading {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.plan-content {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 24rpx;
}

.plan-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.8;
  white-space: pre-wrap;
}

.plan-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 24rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  gap: 20rpx;
}

.placeholder-text {
  font-size: 28rpx;
  color: #999;
}

.plan-btn {
  font-size: 28rpx;
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16rpx 48rpx;
  border-radius: 32rpx;
  border: none;
  line-height: 1.5;
}

/* 周边资源 */
.resource-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f7fa;
}

.resource-item:last-child {
  border-bottom: none;
}

.resource-info {
  flex: 1;
}

.resource-name {
  display: block;
  font-size: 30rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.resource-address {
  display: block;
  font-size: 26rpx;
  color: #999;
}

.resource-phone {
  display: flex;
  align-items: center;
  padding: 12rpx 24rpx;
  background: #f0f4ff;
  border-radius: 30rpx;
}

.phone-icon {
  width: 28rpx;
  height: 28rpx;
  margin-right: 8rpx;
}

.phone-num {
  font-size: 28rpx;
  color: #667eea;
}

/* 处理记录 */
.record-item {
  display: flex;
  padding: 16rpx 0;
}

.record-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #666;
}

.record-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

/* 底部操作 */
.footer-actions {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  border-radius: 40rpx;
  font-size: 30rpx;
  border: none;
}

.btn-primary {
  background: #667eea;
  color: #fff;
}

.btn-success {
  background: #2ED573;
  color: #fff;
}

.btn-default {
  background: #f5f7fa;
  color: #666;
}
</style>
