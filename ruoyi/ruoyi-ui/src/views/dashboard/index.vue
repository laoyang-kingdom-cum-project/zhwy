<template>
  <div class="screen-container">
    <div class="screen-bg">
      <div class="bg-grid"></div>
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="screen-content">
      <!-- 顶部 -->
      <div class="screen-header">
        <div class="header-left">
          <div class="header-decoration"></div>
          <span class="header-label">SMART COMMUNITY</span>
        </div>
        <div class="header-center">
          <h1 class="header-title">智慧社区数据大屏</h1>
          <div class="header-line"></div>
        </div>
        <div class="header-right">
          <span class="header-date">{{ currentDate }}</span>
          <span class="header-time">{{ currentTime }}</span>
        </div>
      </div>

      <!-- 主体 -->
      <div class="screen-body">
        <!-- 左侧 -->
        <div class="body-left">
          <!-- 基本信息饼图 -->
          <div class="panel">
            <div class="panel-header">
              <div class="panel-title">
                <span class="title-icon"></span>
                <span>基本信息</span>
              </div>
            </div>
            <div class="panel-body">
              <div ref="familyPieChart" class="chart-box"></div>
              <div class="family-stats">
                <div class="family-stat-item">
                  <div class="stat-num cyan">{{ familyStats.totalFamilies }}</div>
                  <div class="stat-desc">接入户数</div>
                </div>
                <div class="family-stat-item">
                  <div class="stat-num green">{{ familyStats.onlineFamilies }}</div>
                  <div class="stat-desc">在线户数</div>
                </div>
                <div class="family-stat-item">
                  <div class="stat-num red">{{ familyStats.offlineFamilies }}</div>
                  <div class="stat-desc">离线户数</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 传感器折线图 -->
          <div class="panel">
            <div class="panel-header">
              <div class="panel-title">
                <span class="title-icon"></span>
                <span>传感器状态</span>
              </div>
            </div>
            <div class="panel-body">
              <div ref="sensorTrendChart" class="chart-box"></div>
            </div>
          </div>
        </div>

        <!-- 中间 -->
        <div class="body-center">
          <!-- 顶部统计 -->
          <div class="center-stats">
            <div class="center-stat-card" v-for="(item, idx) in centerStats" :key="idx">
              <div class="center-stat-icon" :style="{background: item.bg}">
                <span>{{ item.icon }}</span>
              </div>
              <div class="center-stat-info">
                <div class="center-stat-num" :style="{color: item.color}">{{ item.value }}</div>
                <div class="center-stat-label">{{ item.label }}</div>
              </div>
            </div>
          </div>

          <!-- 告警趋势 -->
          <div class="panel center-chart-panel">
            <div class="panel-header">
              <div class="panel-title">
                <span class="title-icon"></span>
                <span>告警趋势</span>
              </div>
            </div>
            <div class="panel-body">
              <div ref="alarmTrendChart" class="chart-box-lg"></div>
            </div>
          </div>

          <!-- 需关注事件 -->
          <div class="panel">
            <div class="panel-header">
              <div class="panel-title">
                <span class="title-icon"></span>
                <span>需关注事件</span>
              </div>
            </div>
            <div class="panel-body events-body">
              <div class="event-scroll">
                <div 
                  class="event-row" 
                  :class="'event-level-' + event.level"
                  v-for="(event, idx) in attentionEvents" 
                  :key="idx"
                  @click="showEventDetail(event)"
                >
                  <div class="event-level-dot"></div>
                  <div class="event-main">
                    <div class="event-top">
                      <span class="event-type">{{ event.eventType }}</span>
                      <span class="event-badge" :class="'badge-' + event.level">{{ event.levelDesc }}</span>
                    </div>
                    <div class="event-bottom">
                      <span class="event-room">{{ event.roomNo }}</span>
                      <span class="event-time">{{ event.time }}</span>
                    </div>
                  </div>
                  <div class="event-go">›</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧 -->
        <div class="body-right">
          <!-- 实时日志流 -->
          <div class="panel">
            <div class="panel-header">
              <div class="panel-title">
                <span class="title-icon"></span>
                <span>实时日志流</span>
              </div>
              <div class="panel-action" @click="refreshLogs">↻ 刷新</div>
            </div>
            <div class="panel-body logs-body">
              <div class="log-scroll">
                <div 
                  class="log-row" 
                  :class="'log-level-' + log.level"
                  v-for="(log, idx) in realTimeLogs" 
                  :key="idx"
                  @click="showEventDetail(log)"
                >
                  <div class="log-pulse"></div>
                  <div class="log-main">
                    <div class="log-top">
                      <span class="log-type">{{ log.eventType }}</span>
                      <span class="log-badge" :class="'badge-' + log.level">{{ log.levelDesc }}</span>
                    </div>
                    <div class="log-msg">{{ log.message }}</div>
                    <div class="log-meta">
                      <span>{{ log.roomNo }}</span>
                      <span>{{ log.time }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 事件详情弹窗 -->
    <div class="modal-mask" v-if="showDetailModal" @click.self="showDetailModal = false">
      <div class="modal-box">
        <div class="modal-header">
          <span class="modal-title">事件详情</span>
          <span class="modal-close" @click="showDetailModal = false">×</span>
        </div>
        <div class="modal-body" v-if="eventDetail">
          <div class="modal-row">
            <div class="modal-label">事件时间</div>
            <div class="modal-value">{{ eventDetail.eventTime || eventDetail.time }}</div>
          </div>
          <div class="modal-row">
            <div class="modal-label">事件类型</div>
            <div class="modal-value">{{ eventDetail.eventType }}</div>
          </div>
          <div class="modal-row">
            <div class="modal-label">关联门牌号</div>
            <div class="modal-value highlight">{{ eventDetail.roomNo }}</div>
          </div>
          <div class="modal-row">
            <div class="modal-label">事件消息</div>
            <div class="modal-value msg">{{ eventDetail.message }}</div>
          </div>
          <div class="modal-row">
            <div class="modal-label">事件状态</div>
            <div class="modal-value">
              <span class="status-tag" :class="'status-' + eventDetail.status">
                {{ eventDetail.statusDesc || (eventDetail.status === '1' ? '处理中' : eventDetail.status === '2' ? '已解决' : '待处理') }}
              </span>
            </div>
          </div>
          <div class="modal-row" v-if="eventDetail.level">
            <div class="modal-label">事件等级</div>
            <div class="modal-value">
              <span class="log-badge" :class="'badge-' + eventDetail.level">{{ eventDetail.levelDesc }}</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <div class="modal-btn" @click="showDetailModal = false">关闭</div>
          <div class="modal-btn primary" @click="handleEvent">处理事件</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { 
  getDashboardStats, 
  getFamilyStats, 
  getRealTimeLogs, 
  getAlarmTrend, 
  getSensorTrend, 
  getAttentionEvents,
  getEventDetail 
} from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: '',
      currentDate: '',
      stats: {
        totalDevices: 0, onlineDevices: 0, offlineDevices: 0, deviceOnlineRate: 0,
        totalWarnings: 0, pendingWarnings: 0,
        totalOrders: 0, pendingOrders: 0,
        totalMembers: 0
      },
      familyStats: { totalFamilies: 0, onlineFamilies: 0, offlineFamilies: 0 },
      realTimeLogs: [],
      alarmTrend: [],
      sensorTrend: [],
      attentionEvents: [],
      eventDetail: null,
      showDetailModal: false,
      refreshTimer: null,
      charts: {}
    }
  },
  computed: {
    centerStats() {
      return [
        { icon: '📡', label: '设备总数', value: this.stats.totalDevices, color: '#00d4ff', bg: 'rgba(0,212,255,0.15)' },
        { icon: '⚠️', label: '预警总数', value: this.stats.totalWarnings, color: '#ff6b6b', bg: 'rgba(255,107,107,0.15)' },
        { icon: '📋', label: '服务订单', value: this.stats.totalOrders, color: '#ffd93d', bg: 'rgba(255,217,61,0.15)' },
        { icon: '🏠', label: '接入户数', value: this.familyStats.totalFamilies, color: '#6bcb77', bg: 'rgba(107,203,119,0.15)' }
      ]
    }
  },
  mounted() {
    this.updateTime()
    this.loadAllData()
    this.refreshTimer = setInterval(() => this.loadAllData(), 30000)
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.refreshTimer) clearInterval(this.refreshTimer)
    window.removeEventListener('resize', this.handleResize)
    Object.values(this.charts).forEach(c => { if (c) c.dispose() })
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
      this.currentDate = now.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', weekday: 'long' })
      setTimeout(() => this.updateTime(), 1000)
    },
    async loadAllData() {
      await Promise.all([
        this.loadStats(),
        this.loadFamilyStats(),
        this.loadRealTimeLogs(),
        this.loadAlarmTrend(),
        this.loadSensorTrend(),
        this.loadAttentionEvents()
      ])
    },
    async loadStats() {
      try {
        const res = await getDashboardStats()
        if (res.code === 200 && res.data) this.stats = { ...this.stats, ...res.data }
      } catch (e) { console.error(e) }
    },
    async loadFamilyStats() {
      try {
        const res = await getFamilyStats()
        if (res.code === 200 && res.data) {
          this.familyStats = res.data
          this.$nextTick(() => this.initFamilyPieChart())
        }
      } catch (e) { console.error(e) }
    },
    async loadRealTimeLogs() {
      try {
        const res = await getRealTimeLogs()
        if (res.code === 200 && res.data) this.realTimeLogs = res.data
      } catch (e) { console.error(e) }
    },
    async loadAlarmTrend() {
      try {
        const res = await getAlarmTrend()
        if (res.code === 200 && res.data) {
          this.alarmTrend = res.data
          this.$nextTick(() => this.initAlarmTrendChart())
        }
      } catch (e) { console.error(e) }
    },
    async loadSensorTrend() {
      try {
        const res = await getSensorTrend()
        if (res.code === 200 && res.data) {
          this.sensorTrend = res.data
          this.$nextTick(() => this.initSensorTrendChart())
        }
      } catch (e) { console.error(e) }
    },
    async loadAttentionEvents() {
      try {
        const res = await getAttentionEvents()
        if (res.code === 200 && res.data) this.attentionEvents = res.data
      } catch (e) { console.error(e) }
    },
    async showEventDetail(event) {
      try {
        const res = await getEventDetail(event.id)
        if (res.code === 200 && res.data) this.eventDetail = res.data
        else this.eventDetail = event
      } catch (e) {
        this.eventDetail = event
      }
      this.showDetailModal = true
    },
    handleEvent() {
      this.$message.success('事件已处理')
      this.showDetailModal = false
    },
    refreshLogs() { this.loadRealTimeLogs() },
    handleResize() {
      Object.values(this.charts).forEach(c => { if (c) c.resize() })
    },
    initFamilyPieChart() {
      const dom = this.$refs.familyPieChart
      if (!dom) return
      if (this.charts.familyPie) this.charts.familyPie.dispose()
      this.charts.familyPie = echarts.init(dom)
      this.charts.familyPie.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}户 ({d}%)', backgroundColor: 'rgba(0,0,0,0.7)', borderColor: 'transparent', textStyle: { color: '#fff' } },
        series: [{
          type: 'pie', radius: ['55%', '80%'], center: ['50%', '50%'],
          label: { show: true, color: '#8892b0', fontSize: 11, formatter: '{b}\n{d}%' },
          labelLine: { lineStyle: { color: 'rgba(136,146,176,0.3)' } },
          itemStyle: { borderColor: '#0a0e27', borderWidth: 3 },
          emphasis: { scaleSize: 8, itemStyle: { shadowBlur: 20, shadowColor: 'rgba(0,0,0,0.5)' } },
          data: [
            { value: this.familyStats.onlineFamilies, name: '在线', itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 1, y2: 1, colorStops: [{ offset: 0, color: '#00d4ff' }, { offset: 1, color: '#6bcb77' }] } } },
            { value: this.familyStats.offlineFamilies, name: '离线', itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 1, y2: 1, colorStops: [{ offset: 0, color: '#ff6b6b' }, { offset: 1, color: '#ee5a24' }] } } }
          ]
        }]
      })
    },
    initAlarmTrendChart() {
      const dom = this.$refs.alarmTrendChart
      if (!dom) return
      if (this.charts.alarmTrend) this.charts.alarmTrend.dispose()
      this.charts.alarmTrend = echarts.init(dom)
      this.charts.alarmTrend.setOption({
        tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,14,39,0.9)', borderColor: 'rgba(0,212,255,0.3)', textStyle: { color: '#ccd6f6' } },
        legend: { data: ['正在进行', '已解决', '需关注'], textStyle: { color: '#8892b0', fontSize: 11 }, top: 0, right: 0, itemWidth: 12, itemHeight: 8 },
        grid: { left: '3%', right: '3%', bottom: '3%', top: 40, containLabel: true },
        xAxis: { type: 'category', data: this.alarmTrend.map(i => i.day), axisLine: { lineStyle: { color: 'rgba(136,146,176,0.2)' } }, axisLabel: { color: '#8892b0' }, axisTick: { show: false } },
        yAxis: { type: 'value', axisLine: { show: false }, axisTick: { show: false }, splitLine: { lineStyle: { color: 'rgba(136,146,176,0.08)' } }, axisLabel: { color: '#8892b0' } },
        series: [
          { name: '正在进行', type: 'line', smooth: true, symbol: 'circle', symbolSize: 6, data: this.alarmTrend.map(i => i.ongoing), lineStyle: { color: '#ff6b6b', width: 2 }, itemStyle: { color: '#ff6b6b', borderColor: '#0a0e27', borderWidth: 2 }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(255,107,107,0.25)' }, { offset: 1, color: 'rgba(255,107,107,0)' }] } } },
          { name: '已解决', type: 'line', smooth: true, symbol: 'circle', symbolSize: 6, data: this.alarmTrend.map(i => i.resolved), lineStyle: { color: '#6bcb77', width: 2 }, itemStyle: { color: '#6bcb77', borderColor: '#0a0e27', borderWidth: 2 }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(107,203,119,0.25)' }, { offset: 1, color: 'rgba(107,203,119,0)' }] } } },
          { name: '需关注', type: 'line', smooth: true, symbol: 'circle', symbolSize: 6, data: this.alarmTrend.map(i => i.attention), lineStyle: { color: '#ffd93d', width: 2 }, itemStyle: { color: '#ffd93d', borderColor: '#0a0e27', borderWidth: 2 }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(255,217,61,0.25)' }, { offset: 1, color: 'rgba(255,217,61,0)' }] } } }
        ]
      })
    },
    initSensorTrendChart() {
      const dom = this.$refs.sensorTrendChart
      if (!dom) return
      if (this.charts.sensorTrend) this.charts.sensorTrend.dispose()
      this.charts.sensorTrend = echarts.init(dom)
      this.charts.sensorTrend.setOption({
        tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,14,39,0.9)', borderColor: 'rgba(0,212,255,0.3)', textStyle: { color: '#ccd6f6' } },
        legend: { data: ['在线', '离线', '异常'], textStyle: { color: '#8892b0', fontSize: 11 }, top: 0, right: 0, itemWidth: 12, itemHeight: 8 },
        grid: { left: '3%', right: '3%', bottom: '3%', top: 40, containLabel: true },
        xAxis: { type: 'category', data: this.sensorTrend.map(i => i.hour), axisLine: { lineStyle: { color: 'rgba(136,146,176,0.2)' } }, axisLabel: { color: '#8892b0' }, axisTick: { show: false } },
        yAxis: { type: 'value', axisLine: { show: false }, axisTick: { show: false }, splitLine: { lineStyle: { color: 'rgba(136,146,176,0.08)' } }, axisLabel: { color: '#8892b0' } },
        series: [
          { name: '在线', type: 'line', smooth: true, symbol: 'circle', symbolSize: 5, data: this.sensorTrend.map(i => i.online), lineStyle: { color: '#00d4ff', width: 2 }, itemStyle: { color: '#00d4ff', borderColor: '#0a0e27', borderWidth: 2 } },
          { name: '离线', type: 'line', smooth: true, symbol: 'circle', symbolSize: 5, data: this.sensorTrend.map(i => i.offline), lineStyle: { color: '#8892b0', width: 1.5, type: 'dashed' }, itemStyle: { color: '#8892b0' } },
          { name: '异常', type: 'line', smooth: true, symbol: 'circle', symbolSize: 5, data: this.sensorTrend.map(i => i.abnormal), lineStyle: { color: '#ff6b6b', width: 2 }, itemStyle: { color: '#ff6b6b', borderColor: '#0a0e27', borderWidth: 2 } }
        ]
      })
    }
  }
}
</script>

<style scoped>
.screen-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
  background: #0a0e27;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.screen-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(rgba(0,212,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0,212,255,0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
}

.glow-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  left: -100px;
  background: rgba(0,212,255,0.08);
}

.glow-2 {
  width: 500px;
  height: 500px;
  bottom: -150px;
  right: -100px;
  background: rgba(107,203,119,0.06);
}

.screen-content {
  position: relative;
  z-index: 1;
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 16px 24px;
}

/* ===== 头部 ===== */
.screen-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 12px;
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-decoration {
  width: 4px;
  height: 28px;
  background: linear-gradient(180deg, #00d4ff, #6bcb77);
  border-radius: 2px;
}

.header-label {
  font-size: 12px;
  color: #8892b0;
  letter-spacing: 4px;
}

.header-center {
  text-align: center;
}

.header-title {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(90deg, #00d4ff 0%, #6bcb77 50%, #ffd93d 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 6px;
  margin: 0;
}

.header-line {
  width: 400px;
  height: 2px;
  margin: 8px auto 0;
  background: linear-gradient(90deg, transparent, rgba(0,212,255,0.4), transparent);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-date {
  font-size: 13px;
  color: #8892b0;
}

.header-time {
  font-size: 20px;
  font-weight: 700;
  color: #00d4ff;
  font-family: 'Courier New', monospace;
  letter-spacing: 2px;
}

/* ===== 主体 ===== */
.screen-body {
  flex: 1;
  display: flex;
  gap: 16px;
  min-height: 0;
  margin-top: 12px;
}

.body-left, .body-right {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.body-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
  min-width: 0;
}

/* ===== 面板 ===== */
.panel {
  background: rgba(13, 20, 52, 0.8);
  border: 1px solid rgba(0,212,255,0.12);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  flex: 1;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid rgba(0,212,255,0.08);
  flex-shrink: 0;
}

.panel-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #ccd6f6;
}

.title-icon {
  width: 3px;
  height: 14px;
  background: linear-gradient(180deg, #00d4ff, #6bcb77);
  border-radius: 2px;
}

.panel-action {
  font-size: 12px;
  color: #00d4ff;
  cursor: pointer;
  transition: opacity 0.2s;
}

.panel-action:hover {
  opacity: 0.7;
}

.panel-body {
  flex: 1;
  padding: 12px;
  min-height: 0;
}

.chart-box {
  height: 180px;
}

.chart-box-lg {
  height: 200px;
}

/* ===== 家庭统计 ===== */
.family-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 8px;
}

.family-stat-item {
  text-align: center;
}

.stat-num {
  font-size: 24px;
  font-weight: 700;
  font-family: 'Courier New', monospace;
}

.stat-num.cyan { color: #00d4ff; }
.stat-num.green { color: #6bcb77; }
.stat-num.red { color: #ff6b6b; }

.stat-desc {
  font-size: 11px;
  color: #8892b0;
  margin-top: 4px;
}

/* ===== 中间统计 ===== */
.center-stats {
  display: flex;
  gap: 16px;
  flex-shrink: 0;
}

.center-stat-card {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: rgba(13, 20, 52, 0.8);
  border: 1px solid rgba(0,212,255,0.12);
  border-radius: 8px;
  transition: border-color 0.3s;
}

.center-stat-card:hover {
  border-color: rgba(0,212,255,0.3);
}

.center-stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}

.center-stat-info {
  flex: 1;
}

.center-stat-num {
  font-size: 26px;
  font-weight: 700;
  font-family: 'Courier New', monospace;
  line-height: 1.2;
}

.center-stat-label {
  font-size: 12px;
  color: #8892b0;
  margin-top: 2px;
}

.center-chart-panel {
  flex: 1.2;
}

/* ===== 事件列表 ===== */
.events-body, .logs-body {
  overflow: hidden;
}

.event-scroll, .log-scroll {
  height: 100%;
  overflow-y: auto;
}

.event-scroll::-webkit-scrollbar, .log-scroll::-webkit-scrollbar {
  width: 3px;
}

.event-scroll::-webkit-scrollbar-thumb, .log-scroll::-webkit-scrollbar-thumb {
  background: rgba(0,212,255,0.2);
  border-radius: 3px;
}

.event-row {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  margin-bottom: 6px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
  border-left: 3px solid transparent;
}

.event-row:hover {
  background: rgba(0,212,255,0.05);
}

.event-row.event-level-1 { border-left-color: #ff6b6b; }
.event-row.event-level-2 { border-left-color: #ffd93d; }

.event-level-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 10px;
  flex-shrink: 0;
}

.event-level-1 .event-level-dot { background: #ff6b6b; box-shadow: 0 0 8px rgba(255,107,107,0.5); }
.event-level-2 .event-level-dot { background: #ffd93d; box-shadow: 0 0 8px rgba(255,217,61,0.5); }

.event-main {
  flex: 1;
  min-width: 0;
}

.event-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 4px;
}

.event-type {
  font-size: 13px;
  font-weight: 600;
  color: #ccd6f6;
}

.event-badge, .log-badge {
  font-size: 10px;
  padding: 1px 8px;
  border-radius: 8px;
  font-weight: 600;
}

.badge-1 { background: rgba(255,107,107,0.15); color: #ff6b6b; }
.badge-2 { background: rgba(255,217,61,0.15); color: #ffd93d; }
.badge-3 { background: rgba(107,203,119,0.15); color: #6bcb77; }

.event-bottom {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #5a6380;
}

.event-go {
  font-size: 18px;
  color: #5a6380;
  margin-left: 8px;
}

/* ===== 日志流 ===== */
.log-row {
  display: flex;
  padding: 10px 12px;
  margin-bottom: 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
  background: rgba(0,212,255,0.02);
  border: 1px solid rgba(0,212,255,0.06);
}

.log-row:hover {
  background: rgba(0,212,255,0.06);
}

.log-pulse {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 10px;
  margin-top: 4px;
  flex-shrink: 0;
}

.log-level-1 .log-pulse { background: #ff6b6b; animation: pulse 1.5s infinite; }
.log-level-2 .log-pulse { background: #ffd93d; animation: pulse 2s infinite; }
.log-level-3 .log-pulse { background: #6bcb77; }

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba(255,107,107,0.4); }
  50% { box-shadow: 0 0 0 6px rgba(255,107,107,0); }
}

.log-main {
  flex: 1;
  min-width: 0;
}

.log-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 4px;
}

.log-type {
  font-size: 13px;
  font-weight: 600;
  color: #ccd6f6;
}

.log-msg {
  font-size: 12px;
  color: #8892b0;
  line-height: 1.4;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.log-meta {
  display: flex;
  justify-content: space-between;
  font-size: 10px;
  color: #5a6380;
}

/* ===== 弹窗 ===== */
.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(5,8,22,0.8);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-box {
  width: 480px;
  background: linear-gradient(135deg, #0d1434 0%, #111b3c 100%);
  border: 1px solid rgba(0,212,255,0.2);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0,0,0,0.5), 0 0 40px rgba(0,212,255,0.1);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(0,212,255,0.1);
}

.modal-title {
  font-size: 16px;
  font-weight: 600;
  color: #ccd6f6;
}

.modal-close {
  font-size: 24px;
  color: #5a6380;
  cursor: pointer;
  line-height: 1;
  transition: color 0.2s;
}

.modal-close:hover {
  color: #ff6b6b;
}

.modal-body {
  padding: 20px;
}

.modal-row {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid rgba(0,212,255,0.05);
}

.modal-row:last-child {
  border-bottom: none;
}

.modal-label {
  width: 90px;
  font-size: 13px;
  color: #5a6380;
  flex-shrink: 0;
}

.modal-value {
  flex: 1;
  font-size: 13px;
  color: #ccd6f6;
}

.modal-value.highlight {
  color: #00d4ff;
  font-weight: 600;
}

.modal-value.msg {
  line-height: 1.6;
}

.status-tag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 8px;
  font-size: 12px;
}

.status-tag.status-0 { background: rgba(136,146,176,0.15); color: #8892b0; }
.status-tag.status-1 { background: rgba(255,217,61,0.15); color: #ffd93d; }
.status-tag.status-2 { background: rgba(107,203,119,0.15); color: #6bcb77; }

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  border-top: 1px solid rgba(0,212,255,0.1);
}

.modal-btn {
  flex: 1;
  height: 38px;
  line-height: 38px;
  text-align: center;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
  background: rgba(136,146,176,0.1);
  color: #8892b0;
  border: 1px solid rgba(136,146,176,0.2);
}

.modal-btn:hover {
  background: rgba(136,146,176,0.2);
}

.modal-btn.primary {
  background: linear-gradient(135deg, #00d4ff, #6bcb77);
  color: #0a0e27;
  font-weight: 600;
  border: none;
}

.modal-btn.primary:hover {
  opacity: 0.9;
}
</style>