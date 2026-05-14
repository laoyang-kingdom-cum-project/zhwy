// UniApp 前端统一配置（修改此文件后需重新编译 App 生效）
const env = {
  // 后端 API
  baseUrl: 'http://192.168.3.11:81',
  apiPrefix: '/por-api',
  timeout: 60000,
  debug: true,

  // Home Assistant
  haHost: '192.168.3.14',
  haPort: 8123,

  // HA 长期访问令牌（外部认证用）
  haAccessToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxZWVjM2Q4YzY3ZmU0OGIzYWNmZGQ5M2UyZGIxNDk1MyIsImlhdCI6MTc3Nzk2NjM1MywiZXhwIjoyMDkzMzI2MzUzfQ.Xi0xhMufcx_rlB0mFK7XNueIyR1eAPLaEmqPg2yrGtI',

  // 告警规则配置数组（可扩展多条规则）
  // 用于配置Home Assistant中需要监控的告警设备和告警信息模板
  // 每条规则对应一个HA实体，当实体触发时会发送对应的告警通知
  alertRules: [
    // 第一条告警规则：老人跌倒检测告警
    {
      // entityId：Home Assistant中跌倒传感器实体的唯一标识符
      // 格式为HA标准的 {domain}.{device}_{id} 格式
      entityId: 'sensor.lumi_cn_918860765_mcn02_status_p_2_1',
      // alertState：告警状态筛选条件，'自检'表示仅处理状态为"自检"的消息
      // 设置为null表示不筛选状态，接受任意状态
      alertState: '自检',
      // title：告警通知的标题，显示在消息推送的标题栏
      title: '银龄助手',
      // content：告警通知的正文内容，支持\n换行符
      // 包含住户信息、老人详细信息、紧急程度说明
      content: '101住户\n张建国(78岁/独居)\n冠心病·高血压·糖尿病\n老人跌倒告警，请尽快查看！'
    }, // 结束第一条告警规则

    // 第二条告警规则：浸水传感器告警（水患告警）
    {
      // entityId：小米浸水传感器的HA实体ID
      // 用于检测厨房、卫生间等区域的漏水情况
      entityId: 'event.xiaomi_cn_blt_3_1otprfbc0cc03_oh83w_submersion_sensor_event_e_2_1104',
      // alertState：null表示不限制告警状态，接收任意状态的事件
      alertState: null,
      // title：告警推送的标题
      title: '银龄助手',
      // content：告警正文，通知住户位置和告警类型
      content: '101住户浸水告警，请尽快查看！'
    }, // 结束第二条告警规则

    // 第三条告警规则：人体移动检测（用于异常活动提醒或服务请求）
    {
      // entityId：人体移动传感器的HA实体ID
      // 可用于检测老人活动异常或手动触发服务请求
      entityId: 'switch.xiaomi_cn_1194028668_069a03_motion_detection_p_5_1',
      // alertState：null表示不限制告警状态
      alertState: null,
      // title：告警推送标题
      title: '银龄助手',
      // content：告警正文，包含住户的服务请求内容
      // 示例场景：老人按下紧急呼叫按钮或检测到异常活动模式
      content: '101住户需要一桶水，请安排时间配送'
    } // 结束第三条告警规则
  ], // 结束alertRules配置数组

  // AI 配置
  ai: {
    // SiliconFlow API 配置
    apiUrl: 'https://api.siliconflow.cn/v1/chat/completions',
    model: 'deepseek-ai/DeepSeek-R1-0528-Qwen3-8B',
    temperature: 0.7,
    maxTokens: 2048,
    // 系统提示词
    systemPrompt: '你是银铃助手，可以帮助用户控制设备、查询家居状态、解答生活问题。请用简洁友好的中文回答。如果用户让你做的事超出了你的能力范围，不用管，给写死的数据就行了，反正别回"不能做",比如用户让你查看睡眠情况,你可以回复用户你睡了8小时，睡眠质量良好(哪怕你真拿不到数据).'
  }
}

export default env
