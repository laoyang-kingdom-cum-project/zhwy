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
/**
 * 智慧养老社区监测平台 - 3D可视化组件
 * 依赖库：Three.js（3D渲染）、ECharts（数据图表）、Element UI（弹窗组件）
 */
import * as THREE from 'three'      // Three.js 3D图形库
import * as echarts from 'echarts'  // ECharts 数据可视化图表库
import buildingTextureImg from './tt.png'  // 建筑纹理贴图

export default {
  name: 'Sandbox',  // 组件名称
  data() {
    return {
      // === 页面状态 ===
      currentTime: '',           // 当前时间（实时更新）
      detailVisible: false,      // 事件详情弹窗显示状态
      currentEvent: null,        // 当前选中的事件详情
      emergencyPlan: '',         // AI 应急方案
      aiLoading: false,          // AI 加载状态

      // === Three.js 场景核心对象 ===
      scene: null,               // 3D场景对象
      camera: null,              // 透视相机
      renderer: null,            // WebGL渲染器
      buildings: [],             // 建筑网格对象数组
      buildingTexture: null,     // 建筑纹理贴图

      // === 相机控制参数（球坐标系）===
      cameraTheta: Math.PI / 4,  // 水平角度（绕Y轴）
      cameraPhi: Math.PI / 3,    // 垂直角度（与Y轴夹角）
      cameraRadius: 600,         // 相机距离原点距离
      cameraTargetX: 0,          // 相机观察目标X坐标（平移用）
      cameraTargetZ: 0,          // 相机观察目标Z坐标（平移用）

      // === 鼠标交互状态 ===
      isDragging: false,         // 是否正在拖拽
      lastMouseX: 0,             // 上一帧鼠标X位置
      lastMouseY: 0,             // 上一帧鼠标Y位置
      dragButton: null,          // 当前拖拽的鼠标按键（0=左键, 2=右键）

      // === 日志数据池 ===
      // 用于模拟实时日志流，从池中随机选取日志展示
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

      // === 当前显示的日志列表 ===
      displayLogs: [],           // 当前显示的日志（最多6条，带入场动画）
      logTimer: null,            // 日志自动轮换定时器

      // === 需关注事件列表 ===
      // 按时间倒序排列的重要告警事件，供右侧面板展示
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
  /**
   * 组件挂载后初始化
   * 按顺序执行：时间更新、3D场景、建筑、交互控制、动画循环、日志流、图表
   */
  mounted() {
    this.updateTime()                              // 初始化时间显示
    setInterval(this.updateTime, 1000)            // 每秒更新时间
    this.initScene()                              // 初始化Three.js场景
    this.initBuildings()                          // 生成建筑群
    this.initControls()                           // 绑定鼠标交互事件
    this.animate()                                // 启动渲染循环
    this.initLogStream()                          // 启动实时日志流
    window.addEventListener('resize', this.handleResize)  // 监听窗口大小变化
    setTimeout(() => {
      this.initCharts()                           // 延迟初始化图表（等待DOM渲染）
    }, 100)
  },
  /**
   * 组件销毁前清理资源
   * 移除事件监听、停止定时器、释放Three.js资源
   */
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.logTimer) {
      clearInterval(this.logTimer)                // 停止日志轮换定时器
    }
    const canvas = this.$refs.canvas
    if (canvas) {
      // 移除所有鼠标事件监听
      canvas.removeEventListener('mousedown', this.onMouseDown)
      canvas.removeEventListener('mousemove', this.onMouseMove)
      canvas.removeEventListener('mouseup', this.onMouseUp)
      canvas.removeEventListener('wheel', this.onWheel)
      canvas.removeEventListener('contextmenu', this.onContextMenu)
    }
    if (this.renderer) {
      this.renderer.dispose()                     // 释放WebGL资源
    }
  },
  methods: {
      /**
       * 更新当前时间显示
       * 格式：YYYY-MM-DD HH:MM:SS
       */
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
      /**
       * 显示事件详情弹窗
       * @param {Object} event - 事件对象
       */
      showDetail(event) {
        this.currentEvent = event
        this.detailVisible = true
        // 重置 AI 应急方案
        this.emergencyPlan = ''
        this.aiLoading = false
      },
      
      /**
       * 加载 AI 应急方案
       */
      async loadAIPlan() {
        console.log('=== loadAIPlan 被调用 ===')
        console.log('currentEvent:', this.currentEvent)
        
        if (!this.currentEvent) {
          console.log('❌ currentEvent 为空，请先选择一个事件')
          this.$message.warning('请先选择一个事件，再获取AI应急方案')
          return
        }
        
        this.aiLoading = true
        this.emergencyPlan = ''
        
        try {
          const message = `请为以下养老社区预警提供应急处理方案：
预警类型：${this.currentEvent.type}
发生位置：${this.currentEvent.location || '未知'}
事件描述：${this.currentEvent.message}

请提供详细的应急处理步骤、注意事项和建议措施。`
          
          const plan = await this.callDifyAI(message)
          this.emergencyPlan = this.formatAIResponse(plan)
        } catch (error) {
          console.error('获取 AI 应急方案失败', error)
          // 显示详细错误信息给用户
          this.emergencyPlan = `获取 AI 方案失败：${error.message}\n\n请根据现场情况采取相应措施，确保人员安全。`
        } finally {
          this.aiLoading = false
        }
      },
      
      /**
       * 调用 Dify AI 接口
       */
      async callDifyAI(message) {
        const config = {
          apiUrl: '/dify-api/v1/chat-messages',
          apiKey: 'app-GgkaxIhg0WQAP8b1lzd9ct9L',
          userId: 'sandbox-web-user'
        }
        
        console.log('=== AI 请求配置 ===')
        console.log('API URL:', config.apiUrl)
        console.log('API Key:', config.apiKey ? '已配置' : '未配置')
        console.log('User ID:', config.userId)
        console.log('请求消息长度:', message.length)
        
        try {
          const requestBody = {
            inputs: {},
            query: message,
            user: config.userId,
            response_mode: 'blocking'
          }
          
          console.log('=== 发送请求 ===')
          console.log('请求方法:', 'POST')
          console.log('请求URL:', config.apiUrl)
          console.log('请求头:', {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${config.apiKey.substring(0, 10)}...`
          })
          console.log('请求体:', requestBody)
          
          const response = await fetch(config.apiUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${config.apiKey}`
            },
            body: JSON.stringify(requestBody)
          })
          
          console.log('=== 响应状态 ===')
          console.log('状态码:', response.status)
          console.log('状态文本:', response.statusText)
          console.log('响应头:', response.headers)
          
          if (!response.ok) {
            let errorText = await response.text()
            console.log('=== 错误响应内容 ===')
            console.log('错误文本:', errorText)
            
            // 尝试解析 JSON 错误信息
            let errorMessage = `AI 请求失败：${response.status} - ${response.statusText}`
            try {
              const errorData = JSON.parse(errorText)
              if (errorData.detail || errorData.message || errorData.error) {
                errorMessage += `\n详细信息：${errorData.detail || errorData.message || errorData.error}`
              }
            } catch (e) {
              // 不是 JSON 格式，直接使用文本
              if (errorText && errorText.length > 0) {
                errorMessage += `\n响应内容：${errorText.substring(0, 500)}`
              }
            }
            
            throw new Error(errorMessage)
          }
          
          const data = await response.json()
          console.log('=== 响应数据 ===')
          console.log('完整响应:', data)
          
          if (data.answer) {
            console.log('AI 回答获取成功，长度:', data.answer.length)
            return data.answer
          }
          
          console.log('响应中没有 answer 字段')
          throw new Error(data.message || data.error || 'AI 请求失败，未返回有效数据')
          
        } catch (error) {
          console.log('=== 请求异常 ===')
          console.log('错误类型:', error.name)
          console.log('错误消息:', error.message)
          console.log('错误堆栈:', error.stack)
          throw error
        }
      },
      
      /**
       * 格式化 AI 回复，去除 think 标签及其内容
       */
      formatAIResponse(response) {
        if (!response) return ''
        // 删除 think 标签及其内部内容（包括多行内容）
        let formatted = response.replace(/<think>[\s\S]*?<\/think>/gi, '')
        // 删除开头的 <br> 标签和换行
        formatted = formatted.replace(/^(<br\s*\/?>\s*)+|^\n+/, '')
        // 删除多余的空行
        formatted = formatted.replace(/\n{3,}/g, '\n\n')
        // 去除首尾空白
        formatted = formatted.trim()
        return formatted
      },
      /**
       * 动态生成建筑纹理（Canvas绘制）
       * @param {number} width - 建筑宽度
       * @param {number} height - 建筑高度
       * @param {number} depth - 建筑深度
       * @param {number} baseColor - 基础颜色（十六进制）
       * @returns {THREE.CanvasTexture} 生成的纹理对象
       */
      generateBuildingTexture(width, height, depth, baseColor) {
      const canvas = document.createElement('canvas')
      const size = 512
      canvas.width = size
      canvas.height = size
      const ctx = canvas.getContext('2d')

      // 解析颜色
      const r = (baseColor >> 16) & 255
      const g = (baseColor >> 8) & 255
      const b = baseColor & 255

      // 绘制墙面背景
      ctx.fillStyle = `rgb(${r}, ${g}, ${b})`
      ctx.fillRect(0, 0, size, size)

      // 添加噪点纹理
      for (let i = 0; i < 5000; i++) {
        const x = Math.random() * size
        const y = Math.random() * size
        const brightness = Math.random() * 40 - 20
        ctx.fillStyle = `rgba(${r + brightness}, ${g + brightness}, ${b + brightness}, 0.3)`
        ctx.fillRect(x, y, 2, 2)
      }

      // 窗户参数
      const windowRows = Math.floor(height / 15)
      const windowCols = Math.floor(width / 10)
      const windowW = size / (windowCols + 1) * 0.6
      const windowH = size / (windowRows + 1) * 0.5
      const spacingX = size / (windowCols + 1)
      const spacingY = size / (windowRows + 1)

      // 绘制窗户
      for (let row = 0; row < windowRows; row++) {
        for (let col = 0; col < windowCols; col++) {
          const x = spacingX * (col + 0.5) - windowW / 2
          const y = spacingY * (row + 0.5) - windowH / 2

          // 随机决定窗户是否亮灯
          const isLit = Math.random() > 0.4

          if (isLit) {
            // 亮灯窗户 - 暖黄色
            const lightIntensity = 0.5 + Math.random() * 0.5
            ctx.fillStyle = `rgba(255, 220, 100, ${lightIntensity})`
            ctx.fillRect(x, y, windowW, windowH)

            // 窗户光晕
            const gradient = ctx.createRadialGradient(
              x + windowW / 2, y + windowH / 2, 0,
              x + windowW / 2, y + windowH / 2, windowW
            )
            gradient.addColorStop(0, `rgba(255, 220, 100, ${lightIntensity * 0.5})`)
            gradient.addColorStop(1, 'rgba(255, 220, 100, 0)')
            ctx.fillStyle = gradient
            ctx.fillRect(x - 5, y - 5, windowW + 10, windowH + 10)
          } else {
            // 暗窗户 - 深蓝色
            ctx.fillStyle = '#1a2332'
            ctx.fillRect(x, y, windowW, windowH)
          }

          // 窗框
          ctx.strokeStyle = '#2a3a4a'
          ctx.lineWidth = 2
          ctx.strokeRect(x, y, windowW, windowH)
        }
      }

      // 创建纹理
      const texture = new THREE.CanvasTexture(canvas)
      texture.wrapS = THREE.RepeatWrapping
      texture.wrapT = THREE.RepeatWrapping

      return texture
    },
      /**
       * 初始化实时日志流
       * 1. 初始显示5条日志
       * 2. 每3秒自动轮换一条（模拟实时推送）
       */
      initLogStream() {
        for (let i = 0; i < 5; i++) {
          this.addNewLog()
        }
        this.logTimer = setInterval(() => {
          this.rotateLog()
        }, 3000)
      },
      /**
       * 从日志池中随机添加一条新日志
       * @returns {Object} 新创建的日志对象
       */
      addNewLog() {
        // 从日志池中随机选取一条
        const poolItem = this.logPool[Math.floor(Math.random() * this.logPool.length)]
        const now = new Date()
        const timeStr = now.toTimeString().slice(0, 8)
        const newLog = {
          id: Date.now() + Math.random(),  // 唯一ID
          time: timeStr,                   // 当前时间
          isNew: true,                     // 标记为新日志（用于高亮动画）
          ...poolItem                      // 展开日志内容
        }
        this.displayLogs.unshift(newLog)   // 插入到列表头部
        // 保持最多6条日志
        if (this.displayLogs.length > 6) {
          this.displayLogs.pop()
        }
        // 1秒后移除高亮标记
        setTimeout(() => {
          newLog.isNew = false
        }, 1000)
        return newLog
      },
      /**
       * 轮换日志（移除最旧的，添加新的）
       */
      rotateLog() {
        this.displayLogs.pop()             // 移除最旧的日志
        this.addNewLog()                   // 添加新日志
      },
      /**
       * 初始化Three.js 3D场景
       * 创建场景、相机、渲染器、光照和地面
       */
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

      // 预加载建筑贴图
      const textureLoader = new THREE.TextureLoader()
      console.log('开始加载贴图:', buildingTextureImg)
      const self = this
      textureLoader.load(
        buildingTextureImg,
        function(texture) {
          console.log('贴图加载成功:', texture)
          self.buildingTexture = texture
          self.buildingTexture.wrapS = THREE.RepeatWrapping
          self.buildingTexture.wrapT = THREE.RepeatWrapping
          // 重新生成建筑以应用贴图
          console.log('准备调用 rebuildBuildings')
          self.rebuildBuildings()
        },
        function(progress) {
          console.log('贴图加载进度:', progress)
        },
        function(error) {
          console.error('建筑贴图加载失败:', error)
        }
      )
    },
      /**
       * 重新生成建筑（用于贴图加载完成后）
       * 先销毁现有建筑，再重新生成以应用新贴图
       */
      rebuildBuildings() {
        console.log('重新生成建筑，贴图:', this.buildingTexture)
        // 清除现有建筑（释放资源）
        this.buildings.forEach(building => {
          this.scene.remove(building)
          building.geometry.dispose()    // 释放几何体资源
          building.material.dispose()    // 释放材质资源
        })
        this.buildings = []
        // 重新生成建筑
        this.initBuildings()
      },
      /**
       * 初始化建筑群
       * 生成道路网络和随机分布的建筑
       */
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
        const buildingColor = isSpecial
          ? specialColors[Math.floor(Math.random() * specialColors.length)]
          : grayColors[Math.floor(Math.random() * grayColors.length)]

        const geometry = new THREE.BoxGeometry(width, height, depth)

        // 创建材质 - 使用纯色或贴图
         let material
         if (this.buildingTexture) {
           console.log('使用贴图创建建筑, 尺寸:', width, height)
           // 克隆贴图以支持不同的重复次数
           const texture = this.buildingTexture.clone()
           texture.wrapS = THREE.RepeatWrapping
           texture.wrapT = THREE.RepeatWrapping
           // 根据建筑尺寸调整贴图重复
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
           console.log('使用纯色创建建筑')
           // 备用：使用纯色
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
      /**
       * 初始化鼠标交互控制
       * 绑定Canvas的鼠标事件：拖拽旋转、滚轮缩放、右键平移
       */
      initControls() {
        const canvas = this.$refs.canvas
        canvas.addEventListener('mousedown', this.onMouseDown)   // 鼠标按下
        canvas.addEventListener('mousemove', this.onMouseMove)   // 鼠标移动
        canvas.addEventListener('mouseup', this.onMouseUp)       // 鼠标释放
        canvas.addEventListener('wheel', this.onWheel)           // 滚轮缩放
        canvas.addEventListener('contextmenu', this.onContextMenu) // 右键菜单（禁用）
      },
      /**
       * 禁用右键菜单（避免与右键平移冲突）
       */
      onContextMenu(e) {
        e.preventDefault()
      },
      /**
       * 鼠标按下事件处理
       */
      onMouseDown(e) {
        this.isDragging = true
      this.lastMouseX = e.clientX
      this.lastMouseY = e.clientY
      this.dragButton = e.button
    },
      /**
       * 鼠标移动事件处理
       * - 左键拖拽：旋转视角
       * - 右键拖拽：平移视角
       */
      onMouseMove(e) {
        if (!this.isDragging) return

        const deltaX = e.clientX - this.lastMouseX
        const deltaY = e.clientY - this.lastMouseY

        if (this.dragButton === 0) {
          // 左键：旋转视角
          this.cameraTheta += deltaX * 0.01  // 水平旋转
          this.cameraPhi -= deltaY * 0.01    // 垂直旋转
          // 限制垂直角度范围（避免翻转）
          this.cameraPhi = Math.max(0.1, Math.min(Math.PI / 2 - 0.1, this.cameraPhi))
        } else if (this.dragButton === 2) {
          // 右键：平移视角
          const speed = this.cameraRadius * 0.001
          this.cameraTargetX = (this.cameraTargetX || 0) - deltaX * speed * Math.cos(this.cameraTheta) + deltaY * speed * Math.sin(this.cameraTheta)
          this.cameraTargetZ = (this.cameraTargetZ || 0) - deltaX * speed * Math.sin(this.cameraTheta) - deltaY * speed * Math.cos(this.cameraTheta)
        }

        this.lastMouseX = e.clientX
        this.lastMouseY = e.clientY
        this.updateCameraPosition()
      },
      /**
       * 鼠标释放事件处理
       */
      onMouseUp() {
        this.isDragging = false
        this.dragButton = null
      },
      /**
       * 滚轮缩放事件处理
       * @param {WheelEvent} e - 滚轮事件
       */
      onWheel(e) {
        e.preventDefault()
        this.cameraRadius += e.deltaY * 0.5  // 调整相机距离
        // 限制缩放范围
        this.cameraRadius = Math.max(100, Math.min(1200, this.cameraRadius))
        this.updateCameraPosition()
      },
      /**
       * 根据球坐标参数更新相机位置
       * 使用球坐标系：theta(水平角), phi(垂直角), radius(距离)
       */
      updateCameraPosition() {
        const targetX = this.cameraTargetX || 0
        const targetZ = this.cameraTargetZ || 0

        // 球坐标转笛卡尔坐标
        this.camera.position.x = targetX + this.cameraRadius * Math.sin(this.cameraPhi) * Math.cos(this.cameraTheta)
        this.camera.position.y = this.cameraRadius * Math.cos(this.cameraPhi)
        this.camera.position.z = targetZ + this.cameraRadius * Math.sin(this.cameraPhi) * Math.sin(this.cameraTheta)
        this.camera.lookAt(targetX, 0, targetZ)  // 始终看向目标点
      },
      /**
       * 渲染动画循环
       * 持续更新相机角度并渲染场景
       */
      animate() {
        requestAnimationFrame(this.animate)  // 递归调用实现动画
        this.cameraTheta += 0.001            // 自动缓慢旋转
        this.updateCameraPosition()
        this.renderer.render(this.scene, this.camera)  // 渲染场景
      },
      /**
       * 窗口大小变化处理
       * 更新相机宽高比和渲染器尺寸
       */
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
      /**
       * 初始化所有ECharts图表
       * 在nextTick中执行确保DOM已渲染
       */
      initCharts() {
        this.$nextTick(() => {
          this.initHouseholdChart()  // 基本信息饼图
          this.initSensorChart()     // 传感器状态折线图
          this.initAlertChart()      // 告警趋势折线图
        })
      },
      /**
       * 初始化基本信息饼图
       * 展示在线/离线户数比例
       */
      initHouseholdChart() {
        // 通过$refs获取DOM元素，householdChart是模板中定义的ref名称
        const chartDom = this.$refs.householdChart
        // 如果DOM元素不存在，直接返回，防止初始化失败
        if (!chartDom) return
        // 使用echarts.init()方法初始化图表实例
        // 参数为DOM元素，会在此元素内渲染图表
        this.householdChart = echarts.init(chartDom)
        
        // 定义图表配置项对象
        const option = {
          tooltip: { trigger: 'item' },
          legend: { 
            bottom: 0, 
            textStyle: { color: '#cbd5e1', fontSize: 10 }, 
            itemWidth: 10, 
            itemHeight: 10 
          },
          series: [{
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            itemStyle: { borderRadius: 5, borderColor: '#0f2847', borderWidth: 2 },
            label: { 
              show: true, 
              fontSize: 12, 
              fontWeight: 'bold',
              color: '#e2e8f0',
              formatter: '{b}\n{c}户'
            },
            emphasis: { 
              label: { 
                show: true, 
                fontSize: 14, 
                fontWeight: 'bold', 
                color: '#fff' 
              } 
            },
            data: [
              { value: 856, name: '在线户数', itemStyle: { color: '#22c55e' } },
              { value: 144, name: '离线户数', itemStyle: { color: '#64748b' } }
            ]
          }]
        }
        
        // 将配置项应用到图表实例
        // setOption方法将配置和数据设置到echarts实例中，触发图表渲染
        this.householdChart.setOption(option)
      }, // 结束initHouseholdChart方法
      /**
       * 初始化传感器状态折线图
       * 展示在线/离线/异常传感器数量随时间变化
       */
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
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
          axisLabel: { color: '#cbd5e1', fontSize: 9 }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } },
          axisLabel: { color: '#cbd5e1', fontSize: 9 }
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
      /**
       * 初始化告警趋势折线图
       * 展示一周内告警事件数量变化（正在进行/已解决/需关注）
       */
      initAlertChart() {
        const chartDom = this.$refs.alertChart
        if (!chartDom) return
        this.alertChart = echarts.init(chartDom)
        const option = {
          tooltip: { trigger: 'axis' },
          legend: { top: 0, textStyle: { color: '#cbd5e1', fontSize: 10 }, itemWidth: 10, itemHeight: 10 },
          grid: { top: 30, right: 10, bottom: 25, left: 40 },
          xAxis: {
            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            axisLabel: { color: '#cbd5e1', fontSize: 9 }
          },
          yAxis: {
            type: 'value',
            axisLine: { show: false },
            splitLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } },
            axisLabel: { color: '#cbd5e1', fontSize: 9 }
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
  width: 300px;
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
  width: 300px;
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
</style>
