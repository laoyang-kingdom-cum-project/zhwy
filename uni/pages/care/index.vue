<template>
  <view class="care-container">
    <view class="care-toolbar">
      <view class="toolbar-btn" @click="navigateToFamily">
        <text class="toolbar-icon">👨‍👩‍👧‍👦</text>
        <text class="toolbar-text">家人管理</text>
        <text class="toolbar-arrow">›</text>
      </view>
    </view>
    <view class="care-list">
      <view 
        class="care-card" 
        v-for="(item, index) in careList" 
        :key="index"
        @click="navigateToDetail(item)"
      >
        <view class="care-header">
          <image class="care-avatar" src="/static/logo.png" mode="aspectFill"></image>
          <view class="care-info">
            <text class="care-name">{{ item.name }}</text>
            <text class="care-room">{{ item.room }}</text>
          </view>
          <view class="care-status" :class="item.healthStatus === '0' ? 'normal' : 'warning'">
            {{ item.healthStatus === '0' ? '正常' : item.healthStatus === '1' ? '关注' : '异常' }}
          </view>
        </view>
        <view class="care-body">
          <view class="care-stats">
            <view class="stat">
              <text class="stat-icon">👣</text>
              <text class="stat-value">{{ item.steps }}</text>
              <text class="stat-label">步数</text>
            </view>
            <view class="stat">
              <text class="stat-icon">😴</text>
              <text class="stat-value">{{ item.sleep }}小时</text>
              <text class="stat-label">睡眠</text>
            </view>
            <view class="stat">
              <text class="stat-icon">❤️</text>
              <text class="stat-value">{{ item.heartRate }}</text>
              <text class="stat-label">心率</text>
            </view>
          </view>
        </view>
        <view class="care-footer">
          <text class="last-active">最后活动: {{ formatTime(item.lastActive) }}</text>
          <text class="emergency-contact">紧急联系人: {{ item.emergencyContact }}</text>
        </view>
      </view>
    </view>
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="3"></custom-tabbar>
  </view>
</template>

<script>
import { getCareList, updateFamily } from '@/api/care.js'
import { addWarning } from '@/api/warning.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      careList: []
    }
  },
  onLoad() {
    this.loadCareList()
  },
  onShow() {
    this.loadCareList()
  },
  methods: {
    // 加载关怀对象列表
    async loadCareList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getCareList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.careList = res.rows
          await this.checkFamilyHealthStatus()
        }
      } catch (e) {
        console.error('获取关怀列表失败', e)
        uni.showToast({ title: '获取数据失败', icon: 'none' })
      }
    },
    async checkFamilyHealthStatus() {
      const now = new Date()
      const needAttention = []
      const needWarning = []

      for (const member of this.careList) {
        if (!member.lastActive) continue

        const lastActiveDate = new Date(member.lastActive)
        const hoursSince = (now - lastActiveDate) / (1000 * 60 * 60)

        if (hoursSince > 4 && member.healthStatus === '0') {
          try {
            await updateFamily({
              id: member.id,
              name: member.name,
              age: member.age,
              room: member.room,
              emergencyContact: member.emergencyContact,
              healthStatus: '1',
              steps: member.steps || '0',
              sleep: member.sleep || '0',
              heartRate: member.heartRate || '0',
              pressure: member.pressure || '',
              lastActive: member.lastActive,
              userid: member.userid
            })
            member.healthStatus = '1'
          } catch (e) {
            console.error('更新健康状态失败', e)
          }
        }

        if (hoursSince > 8) {
          needAttention.push(member)
        }

        if (hoursSince > 24) {
          needWarning.push(member)
        }
      }

      if (needAttention.length > 0) {
        const names = needAttention.map(m => m.name).join('、')
        uni.showModal({
          title: '健康提醒',
          content: `家人「${names}」已超过8小时无活动记录，请关注他们的健康状况。`,
          showCancel: false,
          confirmText: '我知道了'
        })
      }

      for (const member of needWarning) {
        try {
          const sentWarnings = uni.getStorageSync('sentWarnings') || {}
          const lastSentTime = sentWarnings[member.id]
          const now = Date.now()
          
          if (lastSentTime && (now - lastSentTime) < 24 * 60 * 60 * 1000) {
            continue
          }
          
          const userInfo = uni.getStorageSync('userInfo')
          await addWarning({
            title: `家人「${member.name}」超过24小时无活动`,
            location: member.room || '未知住址',
            time: this.formatDateTime(new Date()),
            state: '0',
            level: '1',
            userid: userInfo ? userInfo.userId : 1
          })
          
          sentWarnings[member.id] = now
          uni.setStorageSync('sentWarnings', sentWarnings)
        } catch (e) {
          console.error('发送安全预警失败', e)
        }
      }
    },
    // 格式化时间（相对时间）
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
    // 格式化日期时间（yyyy-MM-dd HH:mm:ss）
    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    navigateToDetail(item) {
      uni.navigateTo({
        url: `/pages/care/detail?id=${item.id}`
      })
    },
    navigateToFamily() {
      uni.navigateTo({
        url: '/pages/care/family'
      })
    }
  }
}
</script>

<style lang="scss">
.care-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 130rpx;
}

.care-toolbar {
  margin-bottom: 24rpx;

  .toolbar-btn {
    display: flex;
    align-items: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16rpx;
    padding: 24rpx 32rpx;

    &:active {
      opacity: 0.85;
    }

    .toolbar-icon {
      font-size: 40rpx;
      margin-right: 16rpx;
    }

    .toolbar-text {
      flex: 1;
      font-size: 30rpx;
      color: #fff;
      font-weight: 500;
    }

    .toolbar-arrow {
      font-size: 36rpx;
      color: rgba(255, 255, 255, 0.7);
      font-weight: 300;
    }
  }
}

.care-list {
  .care-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .care-header {
      display: flex;
      align-items: center;
      margin-bottom: 24rpx;

      .care-avatar {
        width: 100rpx;
        height: 100rpx;
        border-radius: 50%;
        margin-right: 20rpx;
      }

      .care-info {
        flex: 1;

        .care-name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          display: block;
          margin-bottom: 8rpx;
        }

        .care-room {
          font-size: 26rpx;
          color: #999;
        }
      }

      .care-status {
        padding: 8rpx 20rpx;
        border-radius: 24rpx;
        font-size: 24rpx;

        &.normal {
          background: #E8F5E9;
          color: #2ED573;
        }

        &.warning {
          background: #FFF3E0;
          color: #FF9800;
        }
      }
    }

    .care-body {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;

      .care-stats {
        display: flex;
        justify-content: space-around;

        .stat {
          display: flex;
          flex-direction: column;
          align-items: center;

          .stat-icon {
            font-size: 40rpx;
            margin-bottom: 8rpx;
          }

          .stat-value {
            font-size: 32rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 4rpx;
          }

          .stat-label {
            font-size: 24rpx;
            color: #999;
          }
        }
      }
    }

    .care-footer {
      .last-active {
        font-size: 26rpx;
        color: #666;
        display: block;
        margin-bottom: 8rpx;
      }

      .emergency-contact {
        font-size: 26rpx;
        color: #007AFF;
      }
    }
  }
}
</style>
