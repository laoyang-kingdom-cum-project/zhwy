<template>
  <!-- 
    智慧养老社区监测平台 - 3D可视化主页面
    功能概述：
    1. 3D沙盘展示：使用Three.js渲染社区建筑群
    2. 数据面板：左右两侧展示实时数据、图表、日志和告警
    3. 交互功能：支持鼠标拖拽旋转、滚轮缩放
    4. 实时日志流：模拟实时告警日志推送
    5. 事件详情弹窗：展示告警事件详细信息
  -->
  <div class="data-screen">
    <!-- Three.js 3D画布容器 -->
    <canvas ref="canvas" class="canvas-container"></canvas>

    <!-- 顶部标题栏 -->
    <div class="screen-header">
      <div class="header-left">
        <span class="time">{{ currentTime }}</span>
      </div>
      <div class="header-center">
        <h1 class="title">智慧养老社区监测平台</h1>
      </div>
      <div class="header-right">
        <span class="weather">20~28℃ 晴转多云</span>
      </div>
    </div>

    <!-- 左侧数据面板 -->
    <div class="panel-left">
      <!-- 基本信息饼图 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-s-data"></i>
          <span>基本信息</span>
          <span class="sub-title">Basic Info</span>
        </div>
        <div ref="householdChart" class="chart-container"></div>
        <div class="household-stats">
          <div class="stat-item online">
            <span class="stat-value">856</span>
            <span class="stat-label">在线户数</span>
          </div>
          <div class="stat-item offline">
            <span class="stat-value">144</span>
            <span class="stat-label">离线户数</span>
          </div>
          <div class="stat-item total">
            <span class="stat-value">1000</span>
            <span class="stat-label">总户数</span>
          </div>
        </div>
      </div>

      <!-- 传感器状态 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-cpu"></i>
          <span>传感器状态</span>
          <span class="sub-title">Sensor Status</span>
        </div>
        <div ref="sensorChart" class="chart-container"></div>
      </div>

      <!-- 实时日志流 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-bell"></i>
          <span>实时日志流</span>
          <span class="sub-title">Real-time Logs</span>
        </div>
        <div class="log-list" ref="logList">
          <transition-group name="log-slide">
            <div v-for="(log, index) in displayLogs" :key="log.id" class="log-item" :class="[log.level, { 'is-new': log.isNew }]" @click="showDetail(log)">
              <div class="log-time">{{ log.time }}</div>
              <div class="log-type">{{ log.type }}</div>
              <div class="log-level" :class="log.level">{{ log.levelText }}</div>
            </div>
          </transition-group>
        </div>
      </div>
    </div>

    <!-- 右侧数据面板 -->
    <div class="panel-right">
      <!-- 告警趋势 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-s-marketing"></i>
          <span>告警趋势</span>
          <span class="sub-title">Alert Trend</span>
        </div>
        <div ref="alertChart" class="chart-container"></div>
      </div>

      <!-- 告警统计 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-warning"></i>
          <span>告警统计</span>
          <span class="sub-title">Alert Stats</span>
        </div>
        <div class="alert-stats">
          <div class="alert-item high">
            <div class="alert-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="alert-info">
              <span class="label">高风险</span>
              <span class="value">12</span>
            </div>
          </div>
          <div class="alert-item medium">
            <div class="alert-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="alert-info">
              <span class="label">中风险</span>
              <span class="value">28</span>
            </div>
          </div>
          <div class="alert-item low">
            <div class="alert-icon">
              <i class="el-icon-info"></i>
            </div>
            <div class="alert-info">
              <span class="label">低风险</span>
              <span class="value">45</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 需关注事件 -->
      <div class="panel-box">
        <div class="panel-title">
          <i class="el-icon-s-order"></i>
          <span>需关注事件</span>
          <span class="sub-title">Focus Events</span>
        </div>
        <div class="event-list">
          <div v-for="(event, index) in focusEvents" :key="index" class="event-item" :class="event.level" @click="showDetail(event)">
            <div class="event-header">
              <span class="event-type">{{ event.type }}</span>
              <span class="event-level" :class="event.level">{{ event.levelText }}</span>
            </div>
            <div class="event-location">{{ event.location }}</div>
            <div class="event-time">{{ event.time }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 事件详情弹窗 -->
    <el-dialog :visible.sync="detailVisible" title="事件详情" width="600px" custom-class="event-dialog" :modal="false">
      <div class="detail-content" v-if="currentEvent">
        <div class="detail-row">
          <span class="label">事件时间：</span>
          <span class="value">{{ currentEvent.time }}</span>
        </div>
        <div class="detail-row">
          <span class="label">事件类型：</span>
          <span class="value">{{ currentEvent.type }}</span>
        </div>
        <div class="detail-row">
          <span class="label">门牌号：</span>
          <span class="value">{{ currentEvent.location || '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">事件消息：</span>
          <span class="value">{{ currentEvent.message }}</span>
        </div>
        <div class="detail-row">
          <span class="label">事件状态：</span>
          <span class="value" :class="currentEvent.status">{{ currentEvent.statusText }}</span>
        </div>
        
        <!-- AI 应急方案 -->
        <div class="ai-emergency-section">
          <div class="ai-title">
            <i class="el-icon-connection"></i>
            <span>AI 应急方案</span>
            <div class="ai-loading" v-if="aiLoading">
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
            </div>
          </div>
          <div class="ai-content" v-if="emergencyPlan">
            <div class="ai-text">{{ emergencyPlan }}</div>
          </div>
          <div class="ai-placeholder" v-if="!emergencyPlan && !aiLoading">
            点击获取 AI 应急处理方案
          </div>
        </div>
      </div>
      
      <!-- 底部按钮 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false" v-if="!emergencyPlan && !aiLoading">取 消</el-button>
        <el-button type="primary" @click="loadAIPlan" :loading="aiLoading" v-if="!emergencyPlan && !aiLoading">
          <i class="el-icon-connection"></i>
          获取 AI 应急方案
        </el-button>
        <el-button @click="detailVisible = false" v-if="emergencyPlan || aiLoading">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as THREE from 'three'
import * as echarts from 'echarts'
import buildingTextureImg from './tt.png'

export default {
  name: 'Sandbox',
  data() {
    return {
      // 页面状态
      currentTime: '',
      detailVisible: false,
      currentEvent: null,
      emergencyPlan: '',
      aiLoading: false,

      // Three.js 场景核心对象
      scene: null,
      camera: null,
      renderer: null,
      buildings: [],
      buildingTexture: null,

      // 相机控制参数（球坐标系）
      cameraTheta: Math.PI / 4,
      cameraPhi: Math.PI / 3,
      cameraRadius: 600,
      cameraTargetX: 0,
      cameraTargetZ: 0,

      // 鼠标交互状态
      isDragging: false,
      lastMouseX: 0,
      lastMouseY: 0,
      dragButton: null,

      // 日志数据池
      logPool: [
        { type: '老人跌倒检测', level: 'high', levelText: '高', message: '客厅红外传感器检测到老人跌倒，已持续3分钟未移动', location: '5栋2单元301室', status: 'urgent', statusText: '紧急处理' },
        { type: '心率异常报警', level: 'high', levelText: '高', message: '智能手环检测到心率持续高于120次/分，持续15分钟', location: '3栋1单元502室', status: 'processing', statusText: '处理中' },
        { type: '燃气泄漏预警', level: 'high', levelText: '高', message: '厨房燃气传感器检测到浓度超标0.5%，已自动关闭阀门', location: '8栋3单元102室', status: 'pending', statusText: '待处理' },
        { type: '夜间离床超时', level: 'medium', levelText: '中', message: '智能床垫检测到老人离床超过45分钟，未在卫生间活动', location: '2栋1单元401室', status: 'processing', statusText: '处理中' },
        { type: '用药提醒未确认', level: 'medium', levelText: '中', message: '智能药盒8:30用药时间已到，30分钟内未检测到取药动作', location: '6栋2单元201室', status: 'pending', statusText: '待处理' },
        { type: '血压数据异常', level: 'medium', levelText: '中', message: '晨间血压测量值180/110mmHg，连续3天偏高', location: '4栋3单元601室', status: 'resolved', statusText: '已处理' },
        { type: '卫生间滞留', level: 'medium', levelText: '中', message: '卫生间人体传感器检测到滞留超过40分钟', location: '1栋2单元301室', status: 'processing', statusText: '处理中' },
        { type: 'SOS紧急呼叫', level: 'high', levelText: '高', message: '床头紧急按钮被触发，老人自述胸闷气短', location: '7栋1单元102室', status: 'resolved', statusText: '已处理' },
        { type: '血糖偏低预警', level: 'medium', levelText: '中', message: '空腹血糖值3.2mmol/L，低于正常范围', location: '9栋2单元403室', status: 'processing', statusText: '处理中' },
        { type: '长时间未进食', level: 'medium', levelText: '中', message: '厨房传感器检测到超过14小时无烹饪活动', location: '3栋3单元205室', status: 'pending', statusText: '待处理' },
        { type: '体温异常升高', level: 'high', levelText: '高', message: '智能体温计检测到体温38.6℃，持续2小时', location: '2栋2单元506室', status: 'processing', statusText: '处理中' },
        { type: '门窗未关闭', level: 'low', levelText: '低', message: '入户门磁传感器检测到夜间未关闭，已持续3小时', location: '8栋1单元302室', status: 'pending', statusText: '待处理' },
        { type: '血氧饱和度低', level: 'high', levelText: '高', message: '智能指夹检测到血氧饱和度低于90%，持续20分钟', location: '5栋3单元104室', status: 'urgent', statusText: '紧急处理' },
        { type: '活动量骤减', level: 'medium', levelText: '中', message: '过去7天平均步数较上周下降65%', location: '4栋2单元507室', status: 'resolved', statusText: '已处理' },
        { type: '睡眠呼吸暂停', level: 'high', levelText: '高', message: '智能床垫检测到夜间呼吸暂停次数达15次/小时', location: '1栋3单元401室', status: 'processing', statusText: '处理中' },
        { type: '紧急定位求助', level: 'high', levelText: '高', message: '老人佩戴的定位胸牌发出求助信号，位置在花园长椅附近', location: '小区花园东侧', status: 'resolved', statusText: '已处理' },
        { type: '忘关燃气灶', level: 'high', levelText: '高', message: '灶台传感器检测到明火持续燃烧超过2小时', location: '6栋1单元303室', status: 'urgent', statusText: '紧急处理' },
        { type: '摔倒后无响应', level: 'high', levelText: '高', message: '浴室跌倒检测传感器触发，10分钟内无起身动作', location: '7栋2单元602室', status: 'resolved', statusText: '已处理' },
        { type: '心率过缓报警', level: 'medium', levelText: '中', message: '睡眠期间心率低于45次/分，持续超过30分钟', location: '9栋1单元501室', status: 'processing', statusText: '处理中' },
        { type: '异常出门提醒', level: 'low', levelText: '低', message: '凌晨4点检测到老人独自外出，未佩戴定位设备', location: '2栋3单元102室', status: 'resolved', statusText: '已处理' },
        { type: '烟雾报警触发', level: 'high', levelText: '高', message: '客厅烟雾传感器检测到烟雾浓度异常', location: '3栋2单元405室', status: 'urgent', statusText: '紧急处理' },
        { type: '饮水量不足', level: 'low', levelText: '低', message: '智能水杯检测到今日饮水量仅200ml，低于建议值', location: '8栋2单元301室', status: 'pending', statusText: '待处理' },
        { type: '癫痫发作疑似', level: 'high', levelText: '高', message: '床体振动传感器检测到异常抽搐模式', location: '4栋1单元203室', status: 'processing', statusText: '处理中' },
        { type: '空调温度异常', level: 'low', levelText: '低', message: '室内温度16℃，低于设定舒适温度范围', location: '5栋1单元505室', status: 'resolved', statusText: '已处理' },
        { type: '紧急药物缺失', level: 'medium', levelText: '中', message: '智能药盒检测到硝酸甘油存量不足3天用量', location: '1栋1单元306室', status: 'pending', statusText: '待处理' },
        { type: '电视长时间开启', level: 'low', levelText: '低', message: '客厅电视从昨日20点持续播放至今', location: '6栋3单元402室', status: 'resolved', statusText: '已处理' }
      ],

      // 当前显示的日志列表
      displayLogs: [],
      logTimer: null,

      // 需关注事件列表
      focusEvents: [
        { time: '2025-05-06 09:47:23', type: '老人跌倒检测', level: 'high', levelText: '高', location: '5栋2单元301室', message: '客厅红外传感器检测到老人跌倒，已持续3分钟未移动', status: 'urgent', statusText: '紧急处理' },
        { time: '2025-05-06 09:32:18', type: '心率异常报警', level: 'high', levelText: '高', location: '3栋1单元502室', message: '智能手环检测到心率持续高于120次/分，持续15分钟', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 09:15:45', type: '燃气泄漏预警', level: 'high', levelText: '高', location: '8栋3单元102室', message: '厨房燃气传感器检测到浓度超标0.5%，已自动关闭阀门', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 08:58:12', type: '夜间离床超时', level: 'medium', levelText: '中', location: '2栋1单元401室', message: '智能床垫检测到老人离床超过45分钟，未在卫生间活动', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 08:42:33', type: '用药提醒未确认', level: 'medium', levelText: '中', location: '6栋2单元201室', message: '智能药盒8:30用药时间已到，30分钟内未检测到取药动作', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 08:28:55', type: '血压数据异常', level: 'medium', levelText: '中', location: '4栋3单元601室', message: '晨间血压测量值180/110mmHg，连续3天偏高', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 08:15:47', type: '卫生间滞留', level: 'medium', levelText: '中', location: '1栋2单元301室', message: '卫生间人体传感器检测到滞留超过40分钟', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 07:52:20', type: 'SOS紧急呼叫', level: 'high', levelText: '高', location: '7栋1单元102室', message: '床头紧急按钮被触发，老人自述胸闷气短', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 07:35:12', type: '血糖偏低预警', level: 'medium', levelText: '中', location: '9栋2单元403室', message: '空腹血糖值3.2mmol/L，低于正常范围', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 07:18:45', type: '长时间未进食', level: 'medium', levelText: '中', location: '3栋3单元205室', message: '厨房传感器检测到超过14小时无烹饪活动', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 06:55:33', type: '体温异常升高', level: 'high', levelText: '高', location: '2栋2单元506室', message: '智能体温计检测到体温38.6℃，持续2小时', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 06:42:18', type: '门窗未关闭', level: 'low', levelText: '低', location: '8栋1单元302室', message: '入户门磁传感器检测到夜间未关闭，已持续3小时', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 06:28:50', type: '血氧饱和度低', level: 'high', levelText: '高', location: '5栋3单元104室', message: '智能指夹检测到血氧饱和度低于90%，持续20分钟', status: 'urgent', statusText: '紧急处理' },
        { time: '2025-05-06 06:15:27', type: '活动量骤减', level: 'medium', levelText: '中', location: '4栋2单元507室', message: '过去7天平均步数较上周下降65%', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 05:58:42', type: '睡眠呼吸暂停', level: 'high', levelText: '高', location: '1栋3单元401室', message: '智能床垫检测到夜间呼吸暂停次数达15次/小时', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 05:42:15', type: '紧急定位求助', level: 'high', levelText: '高', location: '小区花园东侧', message: '老人佩戴的定位胸牌发出求助信号，位置在花园长椅附近', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 05:28:33', type: '忘关燃气灶', level: 'high', levelText: '高', location: '6栋1单元303室', message: '灶台传感器检测到明火持续燃烧超过2小时', status: 'urgent', statusText: '紧急处理' },
        { time: '2025-05-06 05:15:58', type: '摔倒后无响应', level: 'high', levelText: '高', location: '7栋2单元602室', message: '浴室跌倒检测传感器触发，10分钟内无起身动作', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 04:52:47', type: '心率过缓报警', level: 'medium', levelText: '中', location: '9栋1单元501室', message: '睡眠期间心率低于45次/分，持续超过30分钟', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 04:38:22', type: '异常出门提醒', level: 'low', levelText: '低', location: '2栋3单元102室', message: '凌晨4点检测到老人独自外出，未佩戴定位设备', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 04:25:15', type: '烟雾报警触发', level: 'high', levelText: '高', location: '3栋2单元405室', message: '客厅烟雾传感器检测到烟雾浓度异常', status: 'urgent', statusText: '紧急处理' },
        { time: '2025-05-06 04:12:08', type: '饮水量不足', level: 'low', levelText: '低', location: '8栋2单元301室', message: '智能水杯检测到今日饮水量仅200ml，低于建议值', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 03:58:45', type: '癫痫发作疑似', level: 'high', levelText: '高', location: '4栋1单元203室', message: '床体振动传感器检测到异常抽搐模式', status: 'processing', statusText: '处理中' },
        { time: '2025-05-06 03:42:30', type: '空调温度异常', level: 'low', levelText: '低', location: '5栋1单元505室', message: '室内温度16℃，低于设定舒适温度范围', status: 'resolved', statusText: '已处理' },
        { time: '2025-05-06 03:28:55', type: '紧急药物缺失', level: 'medium', levelText: '中', location: '1栋1单元306室', message: '智能药盒检测到硝酸甘油存量不足3天用量', status: 'pending', statusText: '待处理' },
        { time: '2025-05-06 03:15:40', type: '电视长时间开启', level: 'low', levelText: '低', location: '6栋3单元402室', message: '客厅电视从昨日20点持续播放至今', status: 'resolved', statusText: '已处理' }
      ]
    }
  },
  // 组件挂载后初始化
  mounted() {
    // 第一步：初始化时间显示
    this.updateTime()
    setInterval(this.updateTime, 1000)
    // 第二步：初始化3D场景和建筑
    this.initScene()
    this.initBuildings()
    // 第三步：绑定交互控制并启动渲染
    this.initControls()
    this.animate()
    // 第四步：启动日志流和图表
    this.initLogStream()
    window.addEventListener('resize', this.handleResize)
    setTimeout(() => {
      this.initCharts()
    }, 100)
  },
  // 组件销毁前清理资源
  beforeDestroy() {
    // 第一步：移除事件监听
    window.removeEventListener('resize', this.handleResize)
    if (this.logTimer) {
      clearInterval(this.logTimer)
    }
    // 第二步：移除鼠标事件
    const canvas = this.$refs.canvas
    if (canvas) {
      canvas.removeEventListener('mousedown', this.onMouseDown)
      canvas.removeEventListener('mousemove', this.onMouseMove)
      canvas.removeEventListener('mouseup', this.onMouseUp)
      canvas.removeEventListener('wheel', this.onWheel)
      canvas.removeEventListener('contextmenu', this.onContextMenu)
    }
    // 第三步：释放WebGL资源
    if (this.renderer) {
      this.renderer.dispose()
    }
  },
  methods: {
      // 更新当前时间
      updateTime() {
        const now = new Date()  // 第一步：获取当前时间
        this.currentTime = now.toLocaleString('zh-CN', {  // 第二步：格式化为中文时间
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        }).replace(/\//g, '-')  // 第三步：替换斜杠为横杠
      },
      // 显示事件详情弹窗
      showDetail(event) {
        this.currentEvent = event  // 第一步：保存当前事件数据
        this.detailVisible = true  // 第二步：显示弹窗
        this.emergencyPlan = ''    // 第三步：重置AI方案
        this.aiLoading = false     // 第四步：重置加载状态
      },
      
      // 加载AI应急方案
      async loadAIPlan() {
        if (!this.currentEvent) {  // 第一步：检查是否有选中事件
          this.$message.warning('请先选择一个事件，再获取AI应急方案')
          return
        }
        this.aiLoading = true     // 第二步：设置加载状态
        this.emergencyPlan = ''   // 第三步：清空旧方案
        try {
          const message = `请为以下养老社区预警提供应急处理方案：  // 第四步：组装请求消息
预警类型：${this.currentEvent.type}
发生位置：${this.currentEvent.location || '未知'}
事件描述：${this.currentEvent.message}

请提供详细的应急处理步骤、注意事项和建议措施。`
          const plan = await this.callDifyAI(message)  // 第五步：调用AI接口
          this.emergencyPlan = this.formatAIResponse(plan)  // 第六步：格式化并显示方案
        } catch (error) {
          this.emergencyPlan = `获取 AI 方案失败：${error.message}\n\n请根据现场情况采取相应措施，确保人员安全。`  // 第七步：错误处理
        } finally {
          this.aiLoading = false  // 第八步：结束加载状态
        }
      },
      
      // 调用Dify AI接口
      async callDifyAI(message) {
        const config = {  // 第一步：配置API参数
          apiUrl: '/dify-api/v1/chat-messages',
          apiKey: 'app-GgkaxIhg0WQAP8b1lzd9ct9L',
          userId: 'sandbox-web-user'
        }
        const requestBody = {  // 第二步：组装请求体
          inputs: {},
          query: message,
          user: config.userId,
          response_mode: 'blocking'
        }
        const controller = new AbortController()  // 第三步：创建超时控制器
        const timeoutId = setTimeout(() => {
          controller.abort()
        }, 60000)
        try {
          const response = await fetch(config.apiUrl, {  // 第四步：发送请求
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${config.apiKey}`
            },
            body: JSON.stringify(requestBody),
            signal: controller.signal
          })
          clearTimeout(timeoutId)  // 第五步：清除超时
          if (!response.ok) {  // 第六步：检查响应状态
            let errorText = await response.text()
            let errorMessage = `AI 请求失败：${response.status} - ${response.statusText}`
            try {
              const errorData = JSON.parse(errorText)
              if (errorData.detail || errorData.message || errorData.error) {
                errorMessage += `\n详细信息：${errorData.detail || errorData.message || errorData.error}`
              }
            } catch (e) {
              if (errorText && errorText.length > 0) {
                errorMessage += `\n响应内容：${errorText.substring(0, 500)}`
              }
            }
            throw new Error(errorMessage)
          }
          const data = await response.json()  // 第七步：解析响应数据
          if (data.answer) {  // 第八步：返回AI回答
            return data.answer
          }
          throw new Error(data.message || data.error || 'AI 请求失败，未返回有效数据')
        } catch (error) {
          clearTimeout(timeoutId)  // 第九步：异常时清除超时
          if (error.name === 'AbortError') {  // 第十步：处理超时错误
            throw new Error('请求超时（60秒），请稍后重试')
          }
          throw error
        }
      },
      
      // 格式化 AI 回复
      formatAIResponse(response) {
        if (!response) return ''  // 第一步：空值检查
        let formatted = response.replace(/<think>[\s\S]*?<\/think>/gi, '')  // 第二步：去除think标签
        formatted = formatted.replace(/^(<br\s*\/?>\s*)+|^\n+/, '')  // 第三步：去除开头br标签
        formatted = formatted.replace(/\n{3,}/g, '\n\n')  // 第四步：合并多余空行
        formatted = formatted.trim()  // 第五步：去除首尾空白
        return formatted
      },
      // 动态生成建筑纹理
      generateBuildingTexture(width, height, depth, baseColor) {
        const canvas = document.createElement('canvas')  // 第一步：创建canvas
        const size = 512
        canvas.width = size
        canvas.height = size
        const ctx = canvas.getContext('2d')
        const r = (baseColor >> 16) & 255  // 第二步：解析颜色R分量
        const g = (baseColor >> 8) & 255   // 第三步：解析颜色G分量
        const b = baseColor & 255          // 第四步：解析颜色B分量
        ctx.fillStyle = `rgb(${r}, ${g}, ${b})`  // 第五步：绘制墙面背景
        ctx.fillRect(0, 0, size, size)
        for (let i = 0; i < 5000; i++) {  // 第六步：添加噪点纹理
          const x = Math.random() * size
          const y = Math.random() * size
          const brightness = Math.random() * 40 - 20
          ctx.fillStyle = `rgba(${r + brightness}, ${g + brightness}, ${b + brightness}, 0.3)`
          ctx.fillRect(x, y, 2, 2)
        }
        const windowRows = Math.floor(height / 15)  // 第七步：计算窗户行列数
        const windowCols = Math.floor(width / 10)
        const windowW = size / (windowCols + 1) * 0.6
        const windowH = size / (windowRows + 1) * 0.5
        const spacingX = size / (windowCols + 1)
        const spacingY = size / (windowRows + 1)
        for (let row = 0; row < windowRows; row++) {  // 第八步：绘制窗户
          for (let col = 0; col < windowCols; col++) {
            const x = spacingX * (col + 0.5) - windowW / 2
            const y = spacingY * (row + 0.5) - windowH / 2
            const isLit = Math.random() > 0.4
            if (isLit) {
              const lightIntensity = 0.5 + Math.random() * 0.5
              ctx.fillStyle = `rgba(255, 220, 100, ${lightIntensity})`
              ctx.fillRect(x, y, windowW, windowH)
              const gradient = ctx.createRadialGradient(
                x + windowW / 2, y + windowH / 2, 0,
                x + windowW / 2, y + windowH / 2, windowW
              )
              gradient.addColorStop(0, `rgba(255, 220, 100, ${lightIntensity * 0.5})`)
              gradient.addColorStop(1, 'rgba(255, 220, 100, 0)')
              ctx.fillStyle = gradient
              ctx.fillRect(x - 5, y - 5, windowW + 10, windowH + 10)
            } else {
              ctx.fillStyle = '#1a2332'
              ctx.fillRect(x, y, windowW, windowH)
            }
            ctx.strokeStyle = '#2a3a4a'
            ctx.lineWidth = 2
            ctx.strokeRect(x, y, windowW, windowH)
          }
        }
        const texture = new THREE.CanvasTexture(canvas)  // 第九步：创建纹理
        texture.wrapS = THREE.RepeatWrapping
        texture.wrapT = THREE.RepeatWrapping
        return texture
      },
      // 初始化实时日志流
      initLogStream() {
        for (let i = 0; i < 5; i++) {  // 第一步：预填充5条日志
          this.addNewLog()
        }
        this.logTimer = setInterval(() => {  // 第二步：启动定时轮换
          this.rotateLog()
        }, 3000)
      },
      // 添加一条新日志
      addNewLog() {
        const poolItem = this.logPool[Math.floor(Math.random() * this.logPool.length)]  // 第一步：随机选取日志模板
        const now = new Date()  // 第二步：获取当前时间
        const timeStr = now.toTimeString().slice(0, 8)
        const newLog = {  // 第三步：组装新日志对象
          id: Date.now() + Math.random(),
          time: timeStr,
          isNew: true,
          ...poolItem
        }
        this.displayLogs.unshift(newLog)  // 第四步：插入到列表头部
        if (this.displayLogs.length > 6) {  // 第五步：限制最多6条
          this.displayLogs.pop()
        }
        setTimeout(() => {  // 第六步：1秒后移除高亮
          newLog.isNew = false
        }, 1000)
        return newLog
      },
      // 轮换日志
      rotateLog() {
        this.displayLogs.pop()  // 第一步：移除最旧日志
        this.addNewLog()        // 第二步：添加新日志
      },
      // 初始化Three.js 3D场景
      initScene() {
        const canvas = this.$refs.canvas  // 第一步：获取canvas元素
        const width = canvas.clientWidth
        const height = canvas.clientHeight
        this.scene = new THREE.Scene()  // 第二步：创建场景
        this.scene.background = new THREE.Color(0x0a1628)
        this.scene.fog = new THREE.Fog(0x0a1628, 400, 1500)
        this.camera = new THREE.PerspectiveCamera(45, width / height, 1, 2000)  // 第三步：创建相机
        this.updateCameraPosition()
        this.renderer = new THREE.WebGLRenderer({ canvas, antialias: true })  // 第四步：创建渲染器
        this.renderer.setSize(width, height)
        this.renderer.setPixelRatio(window.devicePixelRatio)
        this.renderer.shadowMap.enabled = true
        this.renderer.shadowMap.type = THREE.PCFSoftShadowMap
        const ambientLight = new THREE.AmbientLight(0xffffff, 0.4)  // 第五步：添加环境光
        this.scene.add(ambientLight)
        const dirLight = new THREE.DirectionalLight(0xffffff, 0.8)  // 第六步：添加方向光
        dirLight.position.set(200, 400, 200)
        dirLight.castShadow = true
        dirLight.shadow.mapSize.width = 2048
        dirLight.shadow.mapSize.height = 2048
        this.scene.add(dirLight)
        const groundGeometry = new THREE.PlaneGeometry(1200, 1200)  // 第七步：创建地面
        const groundMaterial = new THREE.MeshLambertMaterial({ color: 0x0a1628 })
        const ground = new THREE.Mesh(groundGeometry, groundMaterial)
        ground.rotation.x = -Math.PI / 2
        ground.receiveShadow = true
        this.scene.add(ground)
        const textureLoader = new THREE.TextureLoader()  // 第八步：加载建筑贴图
        const self = this
        textureLoader.load(
          buildingTextureImg,
          function(texture) {
            self.buildingTexture = texture
            self.buildingTexture.wrapS = THREE.RepeatWrapping
            self.buildingTexture.wrapT = THREE.RepeatWrapping
            self.rebuildBuildings()
          },
          undefined,
          function(error) {
            console.error('建筑贴图加载失败:', error)
          }
        )
      },
      // 重新生成建筑
      rebuildBuildings() {
        this.buildings.forEach(building => {  // 第一步：清除现有建筑
          this.scene.remove(building)
          building.geometry.dispose()
          building.material.dispose()
        })
        this.buildings = []  // 第二步：清空建筑数组
        this.initBuildings()  // 第三步：重新生成建筑
      },
      // 初始化建筑群
      initBuildings() {
        const grayColors = [0x4a5568, 0x5a6578, 0x6a7588, 0x7a8598, 0x8a95a8]  // 第一步：定义颜色配置
        const specialColors = [0xe53e3e, 0xdd6b20, 0x3182ce]
        const blockSize = 80
        const streetWidth = 24
        const sidewalkWidth = 6
        const gridSize = 6
        const cellSize = blockSize + streetWidth
        const startPos = -((gridSize - 1) * cellSize) / 2
        const citySize = gridSize * cellSize
        const roadMaterial = new THREE.MeshLambertMaterial({ color: 0x1a1a2e })  // 第二步：创建道路材质
        const sidewalkMaterial = new THREE.MeshLambertMaterial({ color: 0x2a2a3e })
        const lineMaterial = new THREE.MeshBasicMaterial({ color: 0x444466 })
        for (let i = 0; i < gridSize - 1; i++) {  // 第三步：创建横向道路
          const z = startPos + i * cellSize + blockSize / 2 + streetWidth / 2
          const roadGeo = new THREE.PlaneGeometry(citySize, streetWidth - sidewalkWidth * 2)
          const road = new THREE.Mesh(roadGeo, roadMaterial)
          road.rotation.x = -Math.PI / 2
          road.position.set(0, 0.1, z)
          this.scene.add(road)
          const sidewalkGeo1 = new THREE.PlaneGeometry(citySize, sidewalkWidth)
          const sidewalk1 = new THREE.Mesh(sidewalkGeo1, sidewalkMaterial)
          sidewalk1.rotation.x = -Math.PI / 2
          sidewalk1.position.set(0, 0.15, z - streetWidth / 2 + sidewalkWidth / 2)
          this.scene.add(sidewalk1)
          const sidewalk2 = sidewalk1.clone()
          sidewalk2.position.set(0, 0.15, z + streetWidth / 2 - sidewalkWidth / 2)
          this.scene.add(sidewalk2)
          const lineGeo = new THREE.PlaneGeometry(citySize, 0.5)
          const line = new THREE.Mesh(lineGeo, lineMaterial)
          line.rotation.x = -Math.PI / 2
          line.position.set(0, 0.12, z)
          this.scene.add(line)
        }
        for (let i = 0; i < gridSize - 1; i++) {  // 第四步：创建纵向道路
          const x = startPos + i * cellSize + blockSize / 2 + streetWidth / 2
          const roadGeo = new THREE.PlaneGeometry(streetWidth - sidewalkWidth * 2, citySize)
          const road = new THREE.Mesh(roadGeo, roadMaterial)
          road.rotation.x = -Math.PI / 2
          road.position.set(x, 0.1, 0)
          this.scene.add(road)
          const sidewalkGeo1 = new THREE.PlaneGeometry(sidewalkWidth, citySize)
          const sidewalk1 = new THREE.Mesh(sidewalkGeo1, sidewalkMaterial)
          sidewalk1.rotation.x = -Math.PI / 2
          sidewalk1.position.set(x - streetWidth / 2 + sidewalkWidth / 2, 0.15, 0)
          this.scene.add(sidewalk1)
          const sidewalk2 = sidewalk1.clone()
          sidewalk2.position.set(x + streetWidth / 2 - sidewalkWidth / 2, 0.15, 0)
          this.scene.add(sidewalk2)
          const lineGeo = new THREE.PlaneGeometry(0.5, citySize)
          const line = new THREE.Mesh(lineGeo, lineMaterial)
          line.rotation.x = -Math.PI / 2
          line.position.set(x, 0.12, 0)
          this.scene.add(line)
        }
        const buildingCount = 150  // 第五步：生成建筑
        const placedBuildings = []
        const minDistance = 25
        let attempts = 0
        const maxAttempts = 3000
        while (placedBuildings.length < buildingCount && attempts < maxAttempts) {
          attempts++
          const bi = Math.floor(Math.random() * gridSize)
          const bj = Math.floor(Math.random() * gridSize)
          const blockX = startPos + bi * cellSize
          const blockZ = startPos + bj * cellSize
          const width = 10 + Math.random() * 14
          const depth = 10 + Math.random() * 14
          const halfBlock = blockSize / 2
          const margin = 4
          const minX = blockX - halfBlock + width / 2 + margin
          const maxX = blockX + halfBlock - width / 2 - margin
          const minZ = blockZ - halfBlock + depth / 2 + margin
          const maxZ = blockZ + halfBlock - depth / 2 - margin
          if (minX >= maxX || minZ >= maxZ) continue
          const x = minX + Math.random() * (maxX - minX)
          const z = minZ + Math.random() * (maxZ - minZ)
          let overlap = false
          for (const existing of placedBuildings) {
            const dx = x - existing.x
            const dz = z - existing.z
            const dist = Math.sqrt(dx * dx + dz * dz)
            if (dist < minDistance) {
              overlap = true
              break
            }
          }
          if (overlap) continue
          const distFromCenter = Math.sqrt(x * x + z * z)
          const maxDist = Math.sqrt(2) * Math.abs(startPos)
          const heightFactor = Math.max(0.3, 1 - distFromCenter / maxDist * 0.6)
          const baseHeight = Math.random() * 100 + 40
          const height = baseHeight * heightFactor + Math.random() * 30
          const isSpecial = Math.random() < 0.1
          const buildingColor = isSpecial
            ? specialColors[Math.floor(Math.random() * specialColors.length)]
            : grayColors[Math.floor(Math.random() * grayColors.length)]
          const geometry = new THREE.BoxGeometry(width, height, depth)
          let material
          if (this.buildingTexture) {
            const texture = this.buildingTexture.clone()
            texture.wrapS = THREE.RepeatWrapping
            texture.wrapT = THREE.RepeatWrapping
            const repeatX = Math.max(1, width / 10)
            const repeatY = Math.max(1, height / 10)
            texture.repeat.set(repeatX, repeatY)
            texture.needsUpdate = true
            material = new THREE.MeshStandardMaterial({
              map: texture,
              color: 0xffffff,
              roughness: 0.8,
              metalness: 0.1
            })
          } else {
            material = new THREE.MeshLambertMaterial({ color: buildingColor })
          }
          const building = new THREE.Mesh(geometry, material)
          building.position.set(x, height / 2 + 0.5, z)
          building.castShadow = true
          building.receiveShadow = true
          this.scene.add(building)
          this.buildings.push(building)
          placedBuildings.push({ x, z, width, depth })
        }
      },
      // 初始化鼠标交互控制
      initControls() {
        const canvas = this.$refs.canvas  // 第一步：获取canvas元素
        canvas.addEventListener('mousedown', this.onMouseDown)  // 第二步：绑定鼠标按下
        canvas.addEventListener('mousemove', this.onMouseMove)  // 第三步：绑定鼠标移动
        canvas.addEventListener('mouseup', this.onMouseUp)      // 第四步：绑定鼠标释放
        canvas.addEventListener('wheel', this.onWheel)          // 第五步：绑定滚轮缩放
        canvas.addEventListener('contextmenu', this.onContextMenu)  // 第六步：禁用右键菜单
      },
      // 禁用右键菜单
      onContextMenu(e) {
        e.preventDefault()  // 第一步：阻止默认右键菜单
      },
      // 鼠标按下事件
      onMouseDown(e) {
        this.isDragging = true  // 第一步：设置拖拽状态
        this.lastMouseX = e.clientX  // 第二步：记录鼠标X位置
        this.lastMouseY = e.clientY  // 第三步：记录鼠标Y位置
        this.dragButton = e.button   // 第四步：记录按键类型
      },
      // 鼠标移动事件
      onMouseMove(e) {
        if (!this.isDragging) return  // 第一步：检查是否正在拖拽
        const deltaX = e.clientX - this.lastMouseX  // 第二步：计算X位移
        const deltaY = e.clientY - this.lastMouseY  // 第三步：计算Y位移
        if (this.dragButton === 0) {  // 第四步：左键旋转视角
          this.cameraTheta += deltaX * 0.01
          this.cameraPhi -= deltaY * 0.01
          this.cameraPhi = Math.max(0.1, Math.min(Math.PI / 2 - 0.1, this.cameraPhi))
        } else if (this.dragButton === 2) {  // 第五步：右键平移视角
          const speed = this.cameraRadius * 0.001
          this.cameraTargetX = (this.cameraTargetX || 0) - deltaX * speed * Math.cos(this.cameraTheta) + deltaY * speed * Math.sin(this.cameraTheta)
          this.cameraTargetZ = (this.cameraTargetZ || 0) - deltaX * speed * Math.sin(this.cameraTheta) - deltaY * speed * Math.cos(this.cameraTheta)
        }
        this.lastMouseX = e.clientX  // 第六步：更新鼠标位置
        this.lastMouseY = e.clientY
        this.updateCameraPosition()  // 第七步：更新相机位置
      },
      // 鼠标释放事件
      onMouseUp() {
        this.isDragging = false  // 第一步：结束拖拽状态
        this.dragButton = null   // 第二步：清空按键记录
      },
      // 滚轮缩放事件
      onWheel(e) {
        e.preventDefault()  // 第一步：阻止默认滚动
        this.cameraRadius += e.deltaY * 0.5  // 第二步：调整相机距离
        this.cameraRadius = Math.max(100, Math.min(1200, this.cameraRadius))  // 第三步：限制缩放范围
        this.updateCameraPosition()  // 第四步：更新相机位置
      },
      // 更新相机位置
      updateCameraPosition() {
        const targetX = this.cameraTargetX || 0  // 第一步：获取目标X
        const targetZ = this.cameraTargetZ || 0  // 第二步：获取目标Z
        this.camera.position.x = targetX + this.cameraRadius * Math.sin(this.cameraPhi) * Math.cos(this.cameraTheta)  // 第三步：计算相机X
        this.camera.position.y = this.cameraRadius * Math.cos(this.cameraPhi)  // 第四步：计算相机Y
        this.camera.position.z = targetZ + this.cameraRadius * Math.sin(this.cameraPhi) * Math.sin(this.cameraTheta)  // 第五步：计算相机Z
        this.camera.lookAt(targetX, 0, targetZ)  // 第六步：相机看向目标点
      },
      // 渲染动画循环
      animate() {
        requestAnimationFrame(this.animate)  // 第一步：递归调用实现动画循环
        this.cameraTheta += 0.001  // 第二步：自动缓慢旋转
        this.updateCameraPosition()  // 第三步：更新相机位置
        this.renderer.render(this.scene, this.camera)  // 第四步：渲染场景
      },
      // 窗口大小变化处理
      handleResize() {
        const canvas = this.$refs.canvas  // 第一步：获取canvas
        const width = canvas.clientWidth
        const height = canvas.clientHeight
        this.camera.aspect = width / height  // 第二步：更新相机宽高比
        this.camera.updateProjectionMatrix()  // 第三步：更新投影矩阵
        this.renderer.setSize(width, height)  // 第四步：更新渲染器尺寸
        if (this.householdChart) this.householdChart.resize()  // 第五步：调整图表尺寸
        if (this.sensorChart) this.sensorChart.resize()
        if (this.alertChart) this.alertChart.resize()
      },
      // 初始化所有图表
      initCharts() {
        this.$nextTick(() => {  // 第一步：等待DOM更新
          this.initHouseholdChart()  // 第二步：初始化住户饼图
          this.initSensorChart()     // 第三步：初始化传感器图表
          this.initAlertChart()      // 第四步：初始化告警趋势图
        })
      },
      // 初始化住户饼图
      initHouseholdChart() {
        const chartDom = this.$refs.householdChart  // 第一步：获取图表DOM
        if (!chartDom) return  // 第二步：DOM不存在则退出
        this.householdChart = echarts.init(chartDom)  // 第三步：初始化ECharts实例
        const option = {  // 第四步：配置图表选项
          tooltip: { trigger: 'item' },  // 第五步：设置提示框
          legend: { bottom: 0, textStyle: { color: '#cbd5e1', fontSize: 10 }, itemWidth: 10, itemHeight: 10 },  // 第六步：设置图例
          series: [{  // 第七步：配置数据系列
            type: 'pie',  // 第八步：设置图表类型为饼图
            radius: ['40%', '70%'],  // 第九步：设置环形半径
            center: ['50%', '45%'],  // 第十步：设置中心位置
            avoidLabelOverlap: false,  // 第十一步：允许标签重叠
            itemStyle: { borderRadius: 5, borderColor: '#0f2847', borderWidth: 2 },  // 第十二步：设置扇区样式
            label: { show: true, fontSize: 12, fontWeight: 'bold', color: '#e2e8f0', formatter: '{b}\n{c}户' },  // 第十三步：设置标签
            emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold', color: '#fff' } },  // 第十四步：设置高亮样式
            data: [  // 第十五步：设置数据
              { value: 856, name: '在线户数', itemStyle: { color: '#22c55e' } },  // 第十六步：在线户数数据
              { value: 144, name: '离线户数', itemStyle: { color: '#64748b' } }  // 第十七步：离线户数数据
            ]  // 第十八步：数据结束
          }]  // 第十九步：系列结束
        }  // 第二十步：配置结束
        this.householdChart.setOption(option)  // 第二十一步：应用配置
      },
      // 初始化传感器状态折线图
      initSensorChart() {
        const chartDom = this.$refs.sensorChart  // 第一步：获取图表DOM
        if (!chartDom) return
        this.sensorChart = echarts.init(chartDom)  // 第二步：初始化ECharts
        const option = {  // 第三步：配置图表选项
          tooltip: { trigger: 'axis' },
          grid: { top: 10, right: 10, bottom: 25, left: 40 },
          xAxis: { type: 'category', data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'], axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } }, axisLabel: { color: '#cbd5e1', fontSize: 9 } },
          yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } }, axisLabel: { color: '#cbd5e1', fontSize: 9 } },
          series: [
            { name: '在线', type: 'line', smooth: true, data: [120, 132, 101, 134, 90, 230], lineStyle: { color: '#22c55e' }, itemStyle: { color: '#22c55e' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(34, 197, 94, 0.3)' }, { offset: 1, color: 'rgba(34, 197, 94, 0)' }]) } },
            { name: '离线', type: 'line', smooth: true, data: [220, 182, 191, 234, 290, 330], lineStyle: { color: '#64748b' }, itemStyle: { color: '#64748b' } },
            { name: '异常', type: 'line', smooth: true, data: [15, 12, 18, 25, 20, 30], lineStyle: { color: '#ef4444' }, itemStyle: { color: '#ef4444' } }
          ]
        }
        this.sensorChart.setOption(option)  // 第四步：应用配置
      },
      // 初始化告警趋势折线图
      initAlertChart() {
        const chartDom = this.$refs.alertChart  // 第一步：获取图表DOM
        if (!chartDom) return
        this.alertChart = echarts.init(chartDom)  // 第二步：初始化ECharts
        const option = {  // 第三步：配置图表选项
          tooltip: { trigger: 'axis' },
          legend: { top: 0, textStyle: { color: '#cbd5e1', fontSize: 10 }, itemWidth: 10, itemHeight: 10 },
          grid: { top: 30, right: 10, bottom: 25, left: 40 },
          xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'], axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } }, axisLabel: { color: '#cbd5e1', fontSize: 9 } },
          yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } }, axisLabel: { color: '#cbd5e1', fontSize: 9 } },
          series: [
            { name: '正在进行', type: 'line', smooth: true, data: [12, 15, 8, 20, 18, 25, 22], lineStyle: { color: '#3b82f6' }, itemStyle: { color: '#3b82f6' } },
            { name: '已解决', type: 'line', smooth: true, data: [25, 28, 32, 30, 35, 40, 38], lineStyle: { color: '#22c55e' }, itemStyle: { color: '#22c55e' } },
            { name: '需关注', type: 'line', smooth: true, data: [8, 10, 12, 15, 10, 18, 14], lineStyle: { color: '#f97316' }, itemStyle: { color: '#f97316' } }
          ]
        }
        this.alertChart.setOption(option)  // 第四步：应用配置
      }
  }
}
</script>

<style scoped>
/* ==================== 全局样式 ==================== */
.data-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: #0f2847;
  overflow: hidden;
}

/* Three.js画布容器 - 铺满整个屏幕 */
.canvas-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

/* ==================== 顶部标题栏 ==================== */
.screen-header {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  background: linear-gradient(180deg, rgba(15, 40, 71, 0.95) 0%, rgba(15, 40, 71, 0.7) 70%, transparent 100%);
  z-index: 100;
}

.header-left .time {
  color: #00d4ff;
  font-size: 14px;
  font-family: 'Courier New', monospace;
}

.header-center .title {
  color: #fff;
  font-size: 26px;
  font-weight: bold;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
  margin: 0;
  position: relative;
}

.header-center .title::before,
.header-center .title::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 100px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00d4ff, transparent);
}

.header-center .title::before {
  right: calc(100% + 20px);
}

.header-center .title::after {
  left: calc(100% + 20px);
}

.header-right .weather {
  color: #cbd5e1;
  font-size: 14px;
}

/* ==================== 左侧数据面板 ==================== */
.panel-left {
  position: absolute;
  top: 90px;
  left: 15px;
  width: 18vw;
  min-width: 220px;
  max-width: 340px;
  height: calc(100vh - 180px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 100;
  overflow: hidden;
}

/* ==================== 右侧数据面板 ==================== */
.panel-right {
  position: absolute;
  top: 90px;
  right: 15px;
  width: 18vw;
  min-width: 220px;
  max-width: 340px;
  height: calc(100vh - 180px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 100;
  overflow: hidden;
}

/* ==================== 通用面板样式 ==================== */
/* 面板盒子 - 带毛玻璃效果的卡片容器 */
.panel-box {
  background: rgba(20, 45, 75, 0.9);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 8px;
  padding: 12px;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.panel-box:nth-child(3) {
  flex: 2;
  min-height: 0;
}

.panel-right .panel-box:nth-child(3) {
  flex: 2;
  min-height: 0;
}

.panel-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(0, 212, 255, 0.2);
}

.panel-title i {
  color: #00d4ff;
  font-size: 16px;
}

.panel-title span {
  color: #fff;
  font-size: 15px;
  font-weight: bold;
}

.panel-title .sub-title {
  color: #64748b;
  font-size: 11px;
  margin-left: auto;
  font-weight: normal;
}

/* 图表容器 */
.chart-container {
  height: 140px;
  width: 100%;
}

/* 基本信息数字统计 */
.household-stats {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 8px 4px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
}

.stat-item .stat-value {
  display: block;
  font-size: 20px;
  font-weight: bold;
}

.stat-item .stat-label {
  display: block;
  font-size: 11px;
  color: #94a3b8;
  margin-top: 2px;
}

.stat-item.online .stat-value {
  color: #22c55e;
}

.stat-item.offline .stat-value {
  color: #64748b;
}

.stat-item.total .stat-value {
  color: #00d4ff;
}

/* 告警统计 */
.alert-stats {
  display: flex;
  gap: 12px;
}

.alert-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 10px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  position: relative;
  overflow: hidden;
}

.alert-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
}

.alert-item.high::before {
  background: linear-gradient(90deg, transparent, #ef4444, transparent);
}

.alert-item.medium::before {
  background: linear-gradient(90deg, transparent, #f97316, transparent);
}

.alert-item.low::before {
  background: linear-gradient(90deg, transparent, #3b82f6, transparent);
}

.alert-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-bottom: 8px;
}

.alert-item.high .alert-icon {
  background: rgba(239, 68, 68, 0.15);
  color: #ef4444;
  box-shadow: 0 0 15px rgba(239, 68, 68, 0.3);
}

.alert-item.medium .alert-icon {
  background: rgba(249, 115, 22, 0.15);
  color: #f97316;
  box-shadow: 0 0 15px rgba(249, 115, 22, 0.3);
}

.alert-item.low .alert-icon {
  background: rgba(59, 130, 246, 0.15);
  color: #3b82f6;
  box-shadow: 0 0 15px rgba(59, 130, 246, 0.3);
}

.alert-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.alert-info .label {
  color: #cbd5e1;
  font-size: 12px;
  margin-bottom: 4px;
}

.alert-info .value {
  color: #fff;
  font-size: 24px;
  font-weight: bold;
}

/* 日志列表 */
.log-list {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.log-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  margin-bottom: 6px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 12px;
}

.log-item:hover {
  background: rgba(0, 212, 255, 0.1);
}

.log-item.high {
  border-left: 2px solid #ef4444;
}

.log-item.medium {
  border-left: 2px solid #f97316;
}

.log-item.low {
  border-left: 2px solid #3b82f6;
}

/* 新日志高亮动画 */
.log-item.is-new {
  animation: newLogFlash 1s ease-out;
}

@keyframes newLogFlash {
  0% {
    background: rgba(0, 212, 255, 0.4);
    transform: scale(1.02);
    box-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
  }
  100% {
    background: rgba(255, 255, 255, 0.03);
    transform: scale(1);
    box-shadow: none;
  }
}

/* 日志滑动动画 */
.log-slide-enter-active,
.log-slide-leave-active {
  transition: all 0.5s ease;
}

.log-slide-enter {
  opacity: 0;
  transform: translateY(-30px);
}

.log-slide-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

.log-time {
  color: #64748b;
  font-size: 11px;
  min-width: 55px;
}

.log-type {
  flex: 1;
  color: #e2e8f0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.log-level {
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 10px;
  font-weight: bold;
}

.log-level.high {
  background: rgba(239, 68, 68, 0.2);
  color: #ef4444;
}

.log-level.medium {
  background: rgba(249, 115, 22, 0.2);
  color: #f97316;
}

.log-level.low {
  background: rgba(59, 130, 246, 0.2);
  color: #3b82f6;
}

/* 事件列表 */
.event-list {
  flex: 1;
  overflow-y: auto;
}

.event-list::-webkit-scrollbar {
  width: 4px;
}

.event-list::-webkit-scrollbar-thumb {
  background: rgba(0, 212, 255, 0.6);
  border-radius: 2px;
}

.event-item {
  padding: 10px;
  margin-bottom: 8px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.event-item:hover {
  background: rgba(0, 212, 255, 0.1);
}

.event-item.high {
  border-left: 3px solid #ef4444;
}

.event-item.medium {
  border-left: 3px solid #f97316;
}

.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.event-type {
  color: #fff;
  font-size: 13px;
  font-weight: bold;
}

.event-level {
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 10px;
  font-weight: bold;
}

.event-level.high {
  background: rgba(239, 68, 68, 0.2);
  color: #ef4444;
}

.event-level.medium {
  background: rgba(249, 115, 22, 0.2);
  color: #f97316;
}

.event-location {
  color: #cbd5e1;
  font-size: 12px;
  margin-bottom: 4px;
}

.event-time {
  color: #64748b;
  font-size: 11px;
}

/* 弹窗样式 */
.event-dialog >>> .el-dialog {
  background: #fff;
  border: 2px solid #333;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.event-dialog >>> .el-dialog__header {
  background: #f5f5f5;
  border-bottom: 2px solid #333;
  padding: 15px 20px;
}

.event-dialog >>> .el-dialog__title {
  color: #333;
  font-size: 20px;
  font-weight: bold;
}

.event-dialog >>> .el-dialog__headerbtn .el-dialog__close {
  color: #666;
  font-size: 20px;
}

.event-dialog >>> .el-dialog__headerbtn .el-dialog__close:hover {
  color: #000;
}

.event-dialog >>> .el-dialog__body {
  padding: 20px;
  background: #fff;
}

.detail-content {
  padding: 10px 0;
}

.detail-row {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #ddd;
  align-items: flex-start;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row .label {
  color: #333;
  min-width: 100px;
  font-size: 15px;
  font-weight: bold;
}

.detail-row .value {
  color: #000;
  flex: 1;
  font-size: 15px;
  line-height: 1.6;
  font-weight: 500;
}

.detail-row .value.pending {
  color: #e65100;
  font-weight: bold;
}

.detail-row .value.processing {
  color: #1565c0;
  font-weight: bold;
}

.detail-row .value.urgent {
  color: #c62828;
  font-weight: bold;
}

.detail-row .value.resolved {
  color: #2e7d32;
  font-weight: bold;
}

/* AI 应急方案样式 */
.ai-emergency-section {
  margin-top: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
  border-radius: 8px;
  border: 1px solid #d0d7e2;
}

.ai-title {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.ai-title i {
  margin-right: 8px;
  color: #667eea;
  font-size: 18px;
}

.ai-loading {
  display: flex;
  gap: 6px;
  margin-left: 12px;
}

.loading-dot {
  width: 8px;
  height: 8px;
  background: #667eea;
  border-radius: 50%;
  animation: aiLoading 1.4s infinite ease-in-out both;
}

.loading-dot:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes aiLoading {
  0%, 80%, 100% {
    transform: scale(0);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.ai-content {
  background: #fff;
  border-radius: 6px;
  padding: 15px;
  max-height: 400px;
  overflow-y: auto;
}

.ai-content::-webkit-scrollbar {
  width: 6px;
}

.ai-content::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.3);
  border-radius: 3px;
}

.ai-content::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.5);
}

.ai-text {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.ai-placeholder {
  text-align: center;
  padding: 30px;
  color: #999;
  font-size: 14px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.dialog-footer .el-button {
  min-width: 100px;
}

/* ==================== 响应式适配 ==================== */
@media screen and (max-width: 1366px) {
  .panel-left,
  .panel-right {
    width: 22vw;
    min-width: 200px;
  }

  .header-center .title {
    font-size: 22px;
  }

  .header-center .title::before,
  .header-center .title::after {
    width: 60px;
  }

  .panel-title span {
    font-size: 13px;
  }

  .chart-container {
    height: 110px;
  }

  .stat-item .stat-value {
    font-size: 16px;
  }

  .alert-info .value {
    font-size: 20px;
  }
}

@media screen and (max-width: 1024px) {
  .panel-left,
  .panel-right {
    width: 26vw;
    min-width: 180px;
  }

  .header-center .title {
    font-size: 18px;
  }

  .header-center .title::before,
  .header-center .title::after {
    display: none;
  }

  .panel-box {
    padding: 8px;
  }

  .chart-container {
    height: 90px;
  }
}

@media screen and (max-width: 768px) {
  .panel-left,
  .panel-right {
    position: relative;
    top: auto;
    left: auto;
    right: auto;
    width: 100%;
    max-width: none;
    height: auto;
    min-height: 200px;
  }

  .data-screen {
    position: relative;
    width: 100%;
    height: auto;
    min-height: 100vh;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
  }

  .canvas-container {
    position: relative;
    height: 50vh;
    min-height: 300px;
  }

  .screen-header {
    position: relative;
    height: auto;
    padding: 10px 15px;
    flex-direction: column;
    gap: 5px;
  }

  .header-center .title {
    font-size: 16px;
  }
}
</style>
