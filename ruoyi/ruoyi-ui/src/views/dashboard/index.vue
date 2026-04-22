<template>
  <div class="dashboard-container">
    <!-- 顶部标题 -->
    <div class="dashboard-header">
      <h1 class="dashboard-title">智慧社区数据大屏</h1>
      <div class="dashboard-time">{{ currentTime }}</div>
    </div>

    <!-- 数据卡片区域 -->
    <div class="stats-row">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stat-card device-card">
            <div class="stat-icon">
              <svg-icon icon-class="device" />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalDevices }}</div>
              <div class="stat-label">设备总数</div>
            </div>
            <div class="stat-detail">
              <span class="online">{{ stats.onlineDevices }} 在线</span>
              <span class="offline">{{ stats.offlineDevices }} 离线</span>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stat-card warning-card">
            <div class="stat-icon">
              <svg-icon icon-class="warning" />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalWarnings }}</div>
              <div class="stat-label">预警总数</div>
            </div>
            <div class="stat-detail">
              <span class="pending">{{ stats.pendingWarnings }} 待处理</span>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stat-card order-card">
            <div class="stat-icon">
              <svg-icon icon-class="list" />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalOrders }}</div>
              <div class="stat-label">服务订单</div>
            </div>
            <div class="stat-detail">
              <span class="pending">{{ stats.pendingOrders }} 待处理</span>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stat-card member-card">
            <div class="stat-icon">
              <svg-icon icon-class="peoples" />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalMembers }}</div>
              <div class="stat-label">社区居民</div>
            </div>
            <div class="stat-detail">
              <span class="elderly">{{ stats.elderlyCount }} 老人</span>
              <span class="child">{{ stats.childCount }} 儿童</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-row">
      <el-row :gutter="20">
        <!-- 设备在线率 -->
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <div class="chart-card">
            <div class="chart-title">设备在线状态</div>
            <div class="device-status-chart">
              <div class="progress-ring">
                <div class="ring-bg"></div>
                <div class="ring-progress" :style="ringProgressStyle"></div>
                <div class="ring-inner">
                  <div class="ring-value">{{ stats.deviceOnlineRate }}%</div>
                  <div class="ring-label">在线率</div>
                </div>
              </div>
              <div class="device-legend">
                <div class="legend-item">
                  <span class="legend-dot online"></span>
                  <span class="legend-text">在线 {{ stats.onlineDevices }}</span>
                </div>
                <div class="legend-item">
                  <span class="legend-dot offline"></span>
                  <span class="legend-text">离线 {{ stats.offlineDevices }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 设备类型分布 -->
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <div class="chart-card">
            <div class="chart-title">设备类型分布</div>
            <div ref="deviceTypeChart" class="chart-content"></div>
          </div>
        </el-col>

        <!-- 预警趋势 -->
        <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
          <div class="chart-card">
            <div class="chart-title">近7天预警趋势</div>
            <div ref="warningTrendChart" class="chart-content"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 底部区域 -->
    <div class="bottom-row">
      <el-row :gutter="20">
        <!-- 实时预警 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <div class="chart-card warning-list-card">
            <div class="chart-title">
              实时预警
              <el-button type="text" size="small" @click="refreshData">
                <svg-icon icon-class="refresh" /> 刷新
              </el-button>
            </div>
            <div class="warning-list">
              <div 
                v-for="(item, index) in recentWarnings" 
                :key="index" 
                class="warning-item"
                :class="'level-' + (item.level || 1)"
              >
                <div class="warning-icon">⚠️</div>
                <div class="warning-info">
                  <div class="warning-title">{{ item.title }}</div>
                  <div class="warning-location">📍 {{ item.location || '未知位置' }}</div>
                </div>
                <div class="warning-time">{{ formatTime(item.time) }}</div>
              </div>
              <div v-if="recentWarnings.length === 0" class="empty-warning">
                暂无预警信息
              </div>
            </div>
          </div>
        </el-col>

        <!-- 房间设备分布 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <div class="chart-card">
            <div class="chart-title">房间设备分布</div>
            <div ref="roomDeviceChart" class="chart-content"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats, getDeviceTypeStats, getWarningTrend, getRecentWarnings, getRoomDeviceStats } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: '',
      stats: {
        totalDevices: 0,
        onlineDevices: 0,
        offlineDevices: 0,
        deviceOnlineRate: 0,
        totalWarnings: 0,
        pendingWarnings: 0,
        totalOrders: 0,
        pendingOrders: 0,
        totalMembers: 0,
        elderlyCount: 0,
        childCount: 0
      },
      deviceTypeStats: [],
      warningTrend: [],
      recentWarnings: [],
      roomDeviceStats: [],
      refreshTimer: null,
      charts: {}
    }
  },
  computed: {
    ringProgressStyle() {
      const degree = this.stats.deviceOnlineRate * 3.6
      return {
        background: `conic-gradient(#52c41a ${degree}deg, #f0f0f0 0deg)`
      }
    }
  },
  mounted() {
    this.updateTime()
    this.loadAllData()
    // 每30秒自动刷新
    this.refreshTimer = setInterval(() => {
      this.loadAllData()
    }, 30000)
    // 窗口大小改变时重绘图表
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
    window.removeEventListener('resize', this.handleResize)
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
      setTimeout(() => this.updateTime(), 1000)
    },
    async loadAllData() {
      await Promise.all([
        this.loadStats(),
        this.loadDeviceTypeStats(),
        this.loadWarningTrend(),
        this.loadRecentWarnings(),
        this.loadRoomDeviceStats()
      ])
    },
    async loadStats() {
      try {
        const res = await getDashboardStats()
        if (res.code === 200 && res.data) {
          this.stats = { ...this.stats, ...res.data }
        }
      } catch (e) {
        console.error('获取统计数据失败', e)
      }
    },
    async loadDeviceTypeStats() {
      try {
        const res = await getDeviceTypeStats()
        if (res.code === 200 && res.data) {
          this.deviceTypeStats = res.data
          this.initDeviceTypeChart()
        }
      } catch (e) {
        console.error('获取设备类型统计失败', e)
      }
    },
    async loadWarningTrend() {
      try {
        const res = await getWarningTrend()
        if (res.code === 200 && res.data) {
          this.warningTrend = res.data
          this.initWarningTrendChart()
        }
      } catch (e) {
        console.error('获取预警趋势失败', e)
      }
    },
    async loadRecentWarnings() {
      try {
        const res = await getRecentWarnings()
        if (res.code === 200 && res.data) {
          this.recentWarnings = res.data
        }
      } catch (e) {
        console.error('获取最近预警失败', e)
      }
    },
    async loadRoomDeviceStats() {
      try {
        const res = await getRoomDeviceStats()
        if (res.code === 200 && res.data) {
          this.roomDeviceStats = res.data
          this.initRoomDeviceChart()
        }
      } catch (e) {
        console.error('获取房间设备统计失败', e)
      }
    },
    refreshData() {
      this.$message.success('刷新中...')
      this.loadAllData()
    },
    initDeviceTypeChart() {
      const chartDom = this.$refs.deviceTypeChart
      if (!chartDom) return
      
      if (this.charts.deviceType) {
        this.charts.deviceType.dispose()
      }
      
      this.charts.deviceType = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: { fontSize: 12 }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['35%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: { show: false },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          data: this.deviceTypeStats
        }]
      }
      this.charts.deviceType.setOption(option)
    },
    initWarningTrendChart() {
      const chartDom = this.$refs.warningTrendChart
      if (!chartDom) return
      
      if (this.charts.warningTrend) {
        this.charts.warningTrend.dispose()
      }
      
      this.charts.warningTrend = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.warningTrend.map(item => item.day),
          axisLine: { lineStyle: { color: '#999' } }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: '#eee' } }
        },
        series: [{
          data: this.warningTrend.map(item => item.count),
          type: 'bar',
          barWidth: '50%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      }
      this.charts.warningTrend.setOption(option)
    },
    initRoomDeviceChart() {
      const chartDom = this.$refs.roomDeviceChart
      if (!chartDom) return
      
      if (this.charts.roomDevice) {
        this.charts.roomDevice.dispose()
      }
      
      this.charts.roomDevice = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: '#eee' } }
        },
        yAxis: {
          type: 'category',
          data: this.roomDeviceStats.map(item => item.room).reverse(),
          axisLine: { lineStyle: { color: '#999' } }
        },
        series: [{
          type: 'bar',
          data: this.roomDeviceStats.map(item => item.count).reverse(),
          barWidth: '60%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
              { offset: 0, color: '#faad14' },
              { offset: 1, color: '#ff7a45' }
            ]),
            borderRadius: [0, 10, 10, 0]
          }
        }]
      }
      this.charts.roomDevice.setOption(option)
    },
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) chart.resize()
      })
    },
    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1f3c 0%, #0d1225 100%);
  padding: 20px;
  color: #fff;
}

.dashboard-header {
  text-align: center;
  margin-bottom: 30px;

  .dashboard-title {
    font-size: 36px;
    font-weight: bold;
    background: linear-gradient(90deg, #00d4ff, #00ff88);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    margin: 0 0 10px 0;
  }

  .dashboard-time {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.6);
  }
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
  }

  &.device-card::before { background: linear-gradient(90deg, #00d4ff, #00ff88); }
  &.warning-card::before { background: linear-gradient(90deg, #ff4d4f, #ff7875); }
  &.order-card::before { background: linear-gradient(90deg, #faad14, #ffc53d); }
  &.member-card::before { background: linear-gradient(90deg, #52c41a, #95de64); }

  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 30px;
    margin-right: 16px;
    background: rgba(255, 255, 255, 0.1);

    .svg-icon {
      width: 32px;
      height: 32px;
    }
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 32px;
      font-weight: bold;
      color: #fff;
      line-height: 1.2;
    }

    .stat-label {
      font-size: 14px;
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .stat-detail {
    position: absolute;
    bottom: 16px;
    right: 24px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.5);

    span {
      margin-left: 10px;
      padding: 2px 8px;
      border-radius: 10px;
      background: rgba(255, 255, 255, 0.1);

      &.online { color: #52c41a; }
      &.offline { color: #ff4d4f; }
      &.pending { color: #faad14; }
      &.elderly { color: #ff4d4f; }
      &.child { color: #1890ff; }
    }
  }
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  height: 320px;

  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #fff;
    margin-bottom: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .chart-content {
    height: calc(100% - 40px);
  }
}

.device-status-chart {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: calc(100% - 40px);

  .progress-ring {
    width: 160px;
    height: 160px;
    position: relative;

    .ring-bg,
    .ring-progress {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }

    .ring-bg {
      background: rgba(255, 255, 255, 0.1);
    }

    .ring-inner {
      position: absolute;
      top: 15px;
      left: 15px;
      right: 15px;
      bottom: 15px;
      background: #1a1f3c;
      border-radius: 50%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .ring-value {
        font-size: 36px;
        font-weight: bold;
        color: #52c41a;
      }

      .ring-label {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }

  .device-legend {
    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 16px;

      .legend-dot {
        width: 12px;
        height: 12px;
        border-radius: 50%;
        margin-right: 8px;

        &.online { background: #52c41a; }
        &.offline { background: #ff4d4f; }
      }

      .legend-text {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.warning-list-card {
  height: 320px;

  .warning-list {
    height: calc(100% - 40px);
    overflow-y: auto;

    .warning-item {
      display: flex;
      align-items: center;
      padding: 12px;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 8px;
      margin-bottom: 10px;
      border-left: 4px solid #ff4d4f;

      &.level-1 { border-left-color: #ff4d4f; }
      &.level-2 { border-left-color: #faad14; }
      &.level-3 { border-left-color: #52c41a; }

      .warning-icon {
        font-size: 24px;
        margin-right: 12px;
      }

      .warning-info {
        flex: 1;

        .warning-title {
          font-size: 14px;
          color: #fff;
          margin-bottom: 4px;
        }

        .warning-location {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.5);
        }
      }

      .warning-time {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.4);
      }
    }

    .empty-warning {
      text-align: center;
      padding: 60px 0;
      color: rgba(255, 255, 255, 0.4);
    }
  }
}
</style>
