<template>
  <view class="home-container">
    <!-- 顶部状态栏 -->
    <view class="header">
      <view class="location">
        <text class="icon">📍</text>
        <text class="text">{{ address || '阳光花园小区' }}</text>
      </view>
      <view class="weather">
        <text class="icon">🌡️</text>
        <text class="text">{{ temperature }}°C</text>
      </view>
    </view>

    <!-- 轮播图 -->
    <swiper class="banner" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500">
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image :src="item.image" mode="aspectFill" class="banner-img"></image>
        <view class="banner-title">{{ item.title }}</view>
      </swiper-item>
    </swiper>

    <!-- 快捷入口 -->
    <view class="quick-actions">
      <view class="action-item" v-for="(item, index) in quickActions" :key="index" @click="navigateTo(item.path)">
        <view class="action-icon" :style="{ background: item.bgColor }">
          <text class="icon-text">{{ item.icon }}</text>
        </view>
        <text class="action-name">{{ item.name }}</text>
      </view>
    </view>

    <!-- 设备状态 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">设备状态</text>
        <text class="section-more" @click="navigateTo('/pages/device/index')">查看更多 ></text>
      </view>
      <view class="device-status">
        <view class="status-item">
          <text class="status-num">{{ deviceStatus.online }}</text>
          <text class="status-label">在线设备</text>
        </view>
        <view class="status-item">
          <text class="status-num">{{ deviceStatus.offline }}</text>
          <text class="status-label">离线设备</text>
        </view>
        <view class="status-item">
          <text class="status-num">{{ deviceStatus.total }}</text>
          <text class="status-label">设备总数</text>
        </view>
      </view>
    </view>

    <!-- 社区公告 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">社区公告</text>
        <text class="section-more">查看更多 ></text>
      </view>
      <view class="notice-list">
        <view class="notice-item" v-for="(item, index) in notices" :key="index" @click="goToNoticeDetail(item)">
          <view class="notice-tag" :class="item.noticeType === '1' ? 'notice' : 'warning'">{{ item.noticeType === '1' ? '通知' : '公告' }}</view>
          <text class="notice-title">{{ item.noticeTitle }}</text>
          <text class="notice-time">{{ item.createTime ? item.createTime.substring(0, 10) : '' }}</text>
        </view>
      </view>
    </view>

    <!-- 安全预警 -->
    <view class="section" v-if="warnings.length > 0">
      <view class="section-header">
        <text class="section-title">安全预警</text>
        <text class="section-more" @click="navigateTo('/pages/warning/index')">查看更多 ></text>
      </view>
      <view class="warning-list">
        <view class="warning-item" v-for="(item, index) in warnings" :key="index" @click="navigateTo('/pages/warning/detail')">
          <view class="warning-icon" :class="'level-' + item.level">⚠️</view>
          <view class="warning-info">
            <text class="warning-title">{{ item.title }}</text>
            <text class="warning-location">{{ item.location }}</text>
          </view>
          <text class="warning-time">{{ item.time }}</text>
        </view>
      </view>
    </view>
    <!-- 自定义 TabBar -->
    <custom-tabbar :current-index="0"></custom-tabbar>
  </view>
</template>

<script>
import { getCarouselList, getNoticeList } from '@/api/home.js'
import { getDeviceList } from '@/api/device.js'
import { getWarningList } from '@/api/warning.js'
import config from '@/config/index.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      banners: [],
      quickActions: [
        { name: '物业报修', icon: '🔧', bgColor: '#FF6B6B', path: '/pages/service/repair' },
        { name: '访客预约', icon: '👥', bgColor: '#9B59B6', path: '/pages/visitor/index' },
        { name: 'AI对话', icon: '🤖', bgColor: '#2ED573', path: '/pages/ai/index' },
        { name: '物业服务', icon: '🏠', bgColor: '#FF6348', path: '/pages/service/index' },
        { name: '活动报名', icon: '🎉', bgColor: '#4ECDC4', path: '/pages/activity/index' },
        { name: '缴费服务', icon: '💰', bgColor: '#FFEAA7', path: '/pages/user/payment' },
        { name: '我的订单', icon: '📋', bgColor: '#45B7D1', path: '/pages/service/list' }
      ],
      deviceStatus: {
        online: 0,
        offline: 0,
        total: 0
      },
      notices: [],
      warnings: [],
      address: '',
      temperature: 25
    }
  },
  onLoad() {
    this.loadCarouselList()
    this.loadNoticeList()
    this.loadDeviceStatus()
    this.loadWarningList()
    this.loadAddress()
    this.generateTemperature()
  },
  onShow() {
    this.loadAddress()
    this.generateTemperature()
  },
  methods: {
    // 加载轮播图
    async loadCarouselList() {
      try {
        const res = await getCarouselList({ pageNum: 1, pageSize: 10 })
        if (res.code === 200 && res.rows) {
          console.log(config.baseUrl)
          this.banners = res.rows.map(item => (()=>{
            return {
              image: config.baseUrl + '/por-api' + item.img,
              title: item.title
            }
          })())
        }
      } catch (e) {
        console.error('获取轮播图失败', e)
      }
    },

    // 加载公告列表
    async loadNoticeList() {
      try {
        const res = await getNoticeList({ pageNum: 1, pageSize: 5 })
        if (res.code === 200 && res.rows) {
          this.notices = res.rows
        }
      } catch (e) {
        console.error('获取公告列表失败', e)
      }
    },

    // 跳转到公告详情
    goToNoticeDetail(item) {
      uni.navigateTo({
        url: `/pages/notice/detail?id=${item.noticeId}`
      })
    },

    // 加载设备状态统计
    async loadDeviceStatus() {
      try {
        // 从本地存储获取用户信息
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getDeviceList({ 
          pageNum: 1, 
          pageSize: 100,
          userid: userId 
        })
        if (res.code === 200 && res.rows) {
          const devices = res.rows
          const online = devices.filter(d => d.online === '1' || d.online === 1).length
          const total = devices.length
          this.deviceStatus = {
            online: online,
            offline: total - online,
            total: total
          }
        }
      } catch (e) {
        console.error('获取设备状态失败', e)
      }
    },

    // 加载预警列表
    async loadWarningList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getWarningList({
          pageNum: 1,
          pageSize: 5,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.warnings = res.rows.map(item => ({
            warningId: item.id,
            title: item.title,
            location: item.location,
            time: this.formatTime(item.time),
            level: parseInt(item.level) || 1
          }))
        }
      } catch (e) {
        console.error('获取预警列表失败', e)
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

    // 加载地址（从我的页面同步）
    loadAddress() {
      const address = uni.getStorageSync('address')
      if (address) {
        this.address = address
      }
    },

    // 生成温度（与设备页面保持一致）
    generateTemperature() {
      const timestamp = Date.now()
      const seed = timestamp % 1000
      // 温度: 20-30度之间波动
      this.temperature = (20 + (seed % 100) / 10).toFixed(1)
    },

    navigateTo(path) {
      // tabBar页面使用switchTab跳转
      const tabBarPages = ['/pages/index/index', '/pages/device/index', '/pages/warning/index', '/pages/care/index', '/pages/user/index']
      if (tabBarPages.includes(path)) {
        uni.switchTab({ url: path })
      } else {
        uni.navigateTo({ url: path })
      }
    }
  }
}
</script>

<style lang="scss">
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 130rpx;
}

// 顶部状态栏
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #fff;

  .location, .weather {
    display: flex;
    align-items: center;

    .icon {
      font-size: 32rpx;
      margin-right: 8rpx;
    }

    .text {
      font-size: 28rpx;
      color: #333;
    }
  }
}

// 轮播图
.banner {
  height: 300rpx;
  margin: 20rpx 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
  position: relative;

  .banner-img {
    width: 100%;
    height: 100%;
  }

  .banner-title {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(to top, rgba(0,0,0,0.6), transparent);
    color: #fff;
    font-size: 28rpx;
    padding: 40rpx 20rpx 20rpx;
    text-align: center;
  }
}

// 快捷入口
.quick-actions {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx;
  background: #fff;
  margin: 0 30rpx 20rpx;
  border-radius: 16rpx;

  .action-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;

    .action-icon {
      width: 90rpx;
      height: 90rpx;
      border-radius: 20rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 12rpx;

      .icon-text {
        font-size: 40rpx;
      }
    }

    .action-name {
      font-size: 24rpx;
      color: #666;
    }
  }
}

// 通用区块
.section {
  background: #fff;
  margin: 0 30rpx 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 24rpx;
      color: #999;
    }
  }
}

// 设备状态
.device-status {
  display: flex;
  justify-content: space-around;

  .status-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    .status-num {
      font-size: 48rpx;
      font-weight: bold;
      color: #007AFF;
      margin-bottom: 8rpx;
    }

    .status-label {
      font-size: 24rpx;
      color: #999;
    }
  }
}

// 公告列表
.notice-list {
  .notice-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .notice-tag {
      padding: 4rpx 12rpx;
      border-radius: 8rpx;
      font-size: 20rpx;
      margin-right: 16rpx;

      &.notice {
        background: #E3F2FD;
        color: #1976D2;
      }

      &.warning {
        background: #FFF3E0;
        color: #F57C00;
      }
    }

    .notice-title {
      flex: 1;
      font-size: 28rpx;
      color: #333;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .notice-time {
      font-size: 24rpx;
      color: #999;
      margin-left: 16rpx;
    }
  }
}

// 预警列表
.warning-list {
  .warning-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .warning-icon {
      width: 70rpx;
      height: 70rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36rpx;
      margin-right: 20rpx;

      &.level-1 {
        background: #FFEBEE;
      }

      &.level-2 {
        background: #FFF3E0;
      }
    }

    .warning-info {
      flex: 1;
      display: flex;
      flex-direction: column;

      .warning-title {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 8rpx;
      }

      .warning-location {
        font-size: 24rpx;
        color: #999;
      }
    }

    .warning-time {
      font-size: 24rpx;
      color: #999;
    }
  }
}
</style>
