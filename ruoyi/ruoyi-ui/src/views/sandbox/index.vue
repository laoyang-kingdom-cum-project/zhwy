<template>
  <div class="data-screen">
    <!-- 3D 沙盘 -->
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
    <el-dialog :visible.sync="detailVisible" title="事件详情" width="500px" custom-class="event-dialog" :modal="false">
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
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as THREE from 'three'
import * as echarts from 'echarts'

export default {
  name: 'Sandbox',
  data() {
    return {
      currentTime: '',
      scene: null,
      camera: null,
      renderer: null,
      buildings: [],
      isDragging: false,
      lastMouseX: 0,
      lastMouseY: 0,
      cameraTheta: Math.PI / 4,
      cameraPhi: Math.PI / 3,
      cameraRadius: 600,
      detailVisible: false,
      currentEvent: null,
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
      displayLogs: [],
      logTimer: null,
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
  mounted() {
    this.updateTime()
    setInterval(this.updateTime, 1000)
    this.initScene()
    this.initBuildings()
    this.initControls()
    this.animate()
    this.initLogStream()
    window.addEventListener('resize', this.handleResize)
    setTimeout(() => {
      this.initCharts()
    }, 100)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.logTimer) {
      clearInterval(this.logTimer)
    }
    const canvas = this.$refs.canvas
    if (canvas) {
      canvas.removeEventListener('mousedown', this.onMouseDown)
      canvas.removeEventListener('mousemove', this.onMouseMove)
      canvas.removeEventListener('mouseup', this.onMouseUp)
      canvas.removeEventListener('wheel', this.onWheel)
      canvas.removeEventListener('contextmenu', this.onContextMenu)
    }
    if (this.renderer) {
      this.renderer.dispose()
    }
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
      }).replace(/\//g, '-')
    },
    showDetail(event) {
      this.currentEvent = event
      this.detailVisible = true
    },
    initLogStream() {
      // 初始化显示5条日志
      for (let i = 0; i < 5; i++) {
        this.addNewLog()
      }
      // 每3秒轮换一条
      this.logTimer = setInterval(() => {
        this.rotateLog()
      }, 3000)
    },
    addNewLog() {
      const poolItem = this.logPool[Math.floor(Math.random() * this.logPool.length)]
      const now = new Date()
      const timeStr = now.toTimeString().slice(0, 8)
      const newLog = {
        id: Date.now() + Math.random(),
        time: timeStr,
        isNew: true,
        ...poolItem
      }
      this.displayLogs.unshift(newLog)
      // 保持最多6条
      if (this.displayLogs.length > 6) {
        this.displayLogs.pop()
      }
      // 1秒后移除高亮
      setTimeout(() => {
        newLog.isNew = false
      }, 1000)
    },
    rotateLog() {
      this.displayLogs.pop()
      this.addNewLog()
    },
    initScene() {
      const canvas = this.$refs.canvas
      const width = canvas.clientWidth
      const height = canvas.clientHeight

      this.scene = new THREE.Scene()
      this.scene.background = new THREE.Color(0x0a1628)
      this.scene.fog = new THREE.Fog(0x0a1628, 400, 1500)

      this.camera = new THREE.PerspectiveCamera(45, width / height, 1, 2000)
      this.updateCameraPosition()

      this.renderer = new THREE.WebGLRenderer({ canvas, antialias: true })
      this.renderer.setSize(width, height)
      this.renderer.setPixelRatio(window.devicePixelRatio)
      this.renderer.shadowMap.enabled = true
      this.renderer.shadowMap.type = THREE.PCFSoftShadowMap

      const ambientLight = new THREE.AmbientLight(0xffffff, 0.4)
      this.scene.add(ambientLight)

      const dirLight = new THREE.DirectionalLight(0xffffff, 0.8)
      dirLight.position.set(200, 400, 200)
      dirLight.castShadow = true
      dirLight.shadow.mapSize.width = 2048
      dirLight.shadow.mapSize.height = 2048
      this.scene.add(dirLight)

      const groundGeometry = new THREE.PlaneGeometry(1200, 1200)
      const groundMaterial = new THREE.MeshLambertMaterial({ color: 0x0a1628 })
      const ground = new THREE.Mesh(groundGeometry, groundMaterial)
      ground.rotation.x = -Math.PI / 2
      ground.receiveShadow = true
      this.scene.add(ground)
    },
    initBuildings() {
      const grayColors = [0x4a5568, 0x5a6578, 0x6a7588, 0x7a8598, 0x8a95a8]
      const specialColors = [0xe53e3e, 0xdd6b20, 0x3182ce]
      const blockSize = 80
      const streetWidth = 24
      const sidewalkWidth = 6
      const gridSize = 6
      const cellSize = blockSize + streetWidth
      const startPos = -((gridSize - 1) * cellSize) / 2
      const citySize = gridSize * cellSize

      const roadMaterial = new THREE.MeshLambertMaterial({ color: 0x1a1a2e })
      const sidewalkMaterial = new THREE.MeshLambertMaterial({ color: 0x2a2a3e })
      const lineMaterial = new THREE.MeshBasicMaterial({ color: 0x444466 })

      for (let i = 0; i < gridSize - 1; i++) {
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

      for (let i = 0; i < gridSize - 1; i++) {
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

      const buildingCount = 150
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
        const color = isSpecial
          ? specialColors[Math.floor(Math.random() * specialColors.length)]
          : grayColors[Math.floor(Math.random() * grayColors.length)]

        const geometry = new THREE.BoxGeometry(width, height, depth)
        const material = new THREE.MeshLambertMaterial({ color })
        const building = new THREE.Mesh(geometry, material)

        building.position.set(x, height / 2 + 0.5, z)
        building.castShadow = true
        building.receiveShadow = true

        this.scene.add(building)
        this.buildings.push(building)
        placedBuildings.push({ x, z, width, depth })
      }
    },
    initControls() {
      const canvas = this.$refs.canvas
      canvas.addEventListener('mousedown', this.onMouseDown)
      canvas.addEventListener('mousemove', this.onMouseMove)
      canvas.addEventListener('mouseup', this.onMouseUp)
      canvas.addEventListener('wheel', this.onWheel)
      canvas.addEventListener('contextmenu', this.onContextMenu)
    },
    onContextMenu(e) {
      e.preventDefault()
    },
    onMouseDown(e) {
      this.isDragging = true
      this.lastMouseX = e.clientX
      this.lastMouseY = e.clientY
      this.dragButton = e.button
    },
    onMouseMove(e) {
      if (!this.isDragging) return

      const deltaX = e.clientX - this.lastMouseX
      const deltaY = e.clientY - this.lastMouseY

      if (this.dragButton === 0) {
        this.cameraTheta += deltaX * 0.01
        this.cameraPhi -= deltaY * 0.01
        this.cameraPhi = Math.max(0.1, Math.min(Math.PI / 2 - 0.1, this.cameraPhi))
      } else if (this.dragButton === 2) {
        const speed = this.cameraRadius * 0.001
        this.cameraTargetX = (this.cameraTargetX || 0) - deltaX * speed * Math.cos(this.cameraTheta) + deltaY * speed * Math.sin(this.cameraTheta)
        this.cameraTargetZ = (this.cameraTargetZ || 0) - deltaX * speed * Math.sin(this.cameraTheta) - deltaY * speed * Math.cos(this.cameraTheta)
      }

      this.lastMouseX = e.clientX
      this.lastMouseY = e.clientY
      this.updateCameraPosition()
    },
    onMouseUp() {
      this.isDragging = false
      this.dragButton = null
    },
    onWheel(e) {
      e.preventDefault()
      this.cameraRadius += e.deltaY * 0.5
      this.cameraRadius = Math.max(100, Math.min(1200, this.cameraRadius))
      this.updateCameraPosition()
    },
    updateCameraPosition() {
      const targetX = this.cameraTargetX || 0
      const targetZ = this.cameraTargetZ || 0

      this.camera.position.x = targetX + this.cameraRadius * Math.sin(this.cameraPhi) * Math.cos(this.cameraTheta)
      this.camera.position.y = this.cameraRadius * Math.cos(this.cameraPhi)
      this.camera.position.z = targetZ + this.cameraRadius * Math.sin(this.cameraPhi) * Math.sin(this.cameraTheta)
      this.camera.lookAt(targetX, 0, targetZ)
    },
    animate() {
      requestAnimationFrame(this.animate)
      this.cameraTheta += 0.001
      this.updateCameraPosition()
      this.renderer.render(this.scene, this.camera)
    },
    handleResize() {
      const canvas = this.$refs.canvas
      const width = canvas.clientWidth
      const height = canvas.clientHeight
      this.camera.aspect = width / height
      this.camera.updateProjectionMatrix()
      this.renderer.setSize(width, height)
      if (this.householdChart) this.householdChart.resize()
      if (this.sensorChart) this.sensorChart.resize()
      if (this.alertChart) this.alertChart.resize()
    },
    initCharts() {
      this.$nextTick(() => {
        this.initHouseholdChart()
        this.initSensorChart()
        this.initAlertChart()
      })
    },
    initHouseholdChart() {
      const chartDom = this.$refs.householdChart
      if (!chartDom) return
      this.householdChart = echarts.init(chartDom)
      const option = {
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { color: '#94a3b8', fontSize: 10 }, itemWidth: 10, itemHeight: 10 },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 5, borderColor: '#0a1628', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold', color: '#fff' } },
          data: [
            { value: 856, name: '在线户数', itemStyle: { color: '#22c55e' } },
            { value: 144, name: '离线户数', itemStyle: { color: '#64748b' } }
          ]
        }]
      }
      this.householdChart.setOption(option)
    },
    initSensorChart() {
      const chartDom = this.$refs.sensorChart
      if (!chartDom) return
      this.sensorChart = echarts.init(chartDom)
      const option = {
        tooltip: { trigger: 'axis' },
        grid: { top: 10, right: 10, bottom: 25, left: 40 },
        xAxis: {
          type: 'category',
          data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'],
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.2)' } },
          axisLabel: { color: '#94a3b8', fontSize: 9 }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
          axisLabel: { color: '#94a3b8', fontSize: 9 }
        },
        series: [
          {
            name: '在线',
            type: 'line',
            smooth: true,
            data: [120, 132, 101, 134, 90, 230],
            lineStyle: { color: '#22c55e' },
            itemStyle: { color: '#22c55e' },
            areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(34, 197, 94, 0.3)' }, { offset: 1, color: 'rgba(34, 197, 94, 0)' }]) }
          },
          {
            name: '离线',
            type: 'line',
            smooth: true,
            data: [220, 182, 191, 234, 290, 330],
            lineStyle: { color: '#64748b' },
            itemStyle: { color: '#64748b' }
          },
          {
            name: '异常',
            type: 'line',
            smooth: true,
            data: [15, 12, 18, 25, 20, 30],
            lineStyle: { color: '#ef4444' },
            itemStyle: { color: '#ef4444' }
          }
        ]
      }
      this.sensorChart.setOption(option)
    },
    initAlertChart() {
      const chartDom = this.$refs.alertChart
      if (!chartDom) return
      this.alertChart = echarts.init(chartDom)
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { top: 0, textStyle: { color: '#94a3b8', fontSize: 10 }, itemWidth: 10, itemHeight: 10 },
        grid: { top: 30, right: 10, bottom: 25, left: 40 },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.2)' } },
          axisLabel: { color: '#94a3b8', fontSize: 9 }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
          axisLabel: { color: '#94a3b8', fontSize: 9 }
        },
        series: [
          {
            name: '正在进行',
            type: 'line',
            smooth: true,
            data: [12, 15, 8, 20, 18, 25, 22],
            lineStyle: { color: '#3b82f6' },
            itemStyle: { color: '#3b82f6' }
          },
          {
            name: '已解决',
            type: 'line',
            smooth: true,
            data: [25, 28, 32, 30, 35, 40, 38],
            lineStyle: { color: '#22c55e' },
            itemStyle: { color: '#22c55e' }
          },
          {
            name: '需关注',
            type: 'line',
            smooth: true,
            data: [8, 10, 12, 15, 10, 18, 14],
            lineStyle: { color: '#f97316' },
            itemStyle: { color: '#f97316' }
          }
        ]
      }
      this.alertChart.setOption(option)
    }
  }
}
</script>

<style scoped>
.data-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: #0a1628;
  overflow: hidden;
}

.canvas-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

/* 顶部标题栏 */
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
  background: linear-gradient(180deg, rgba(10, 22, 40, 0.95) 0%, rgba(10, 22, 40, 0.7) 70%, transparent 100%);
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
  color: #a0aec0;
  font-size: 14px;
}

/* 左侧面板 */
.panel-left {
  position: absolute;
  top: 90px;
  left: 15px;
  width: 300px;
  height: calc(100vh - 180px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 100;
  overflow: hidden;
}

/* 右侧面板 */
.panel-right {
  position: absolute;
  top: 90px;
  right: 15px;
  width: 300px;
  height: calc(100vh - 180px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 100;
  overflow: hidden;
}

/* 面板盒子 */
.panel-box {
  background: rgba(16, 30, 50, 0.85);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 8px;
  padding: 12px;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.panel-box:nth-child(3) {
  flex: 1.5;
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
  color: #94a3b8;
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
  height: 180px;
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
  color: #cbd5e1;
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
  height: 180px;
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
  color: #94a3b8;
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
</style>
