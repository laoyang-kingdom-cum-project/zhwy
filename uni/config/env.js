// UniApp 前端统一配置（修改此文件后需重新编译 App 生效）
const env = {
  // 后端 API
  baseUrl: 'http://192.168.0.114:81',
  apiPrefix: '/por-api',
  timeout: 10000,
  debug: true,

  // Home Assistant
  haHost: '192.168.0.71',
  haPort: 8123,

  // HA 长期访问令牌（外部认证用）
  haAccessToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxZWVjM2Q4YzY3ZmU0OGIzYWNmZGQ5M2UyZGIxNDk1MyIsImlhdCI6MTc3Nzk2NjM1MywiZXhwIjoyMDkzMzI2MzUzfQ.Xi0xhMufcx_rlB0mFK7XNueIyR1eAPLaEmqPg2yrGtI',

  // 告警规则（可扩展多条）
  alertRules: [
    {
      entityId: 'sensor.lumi_cn_918860765_mcn02_status_p_2_1',
      alertState: '自检',
      title: '银龄助手',
      content: '老人跌倒告警，请尽快查看！'
    },
    {
      entityId: 'event.xiaomi_cn_blt_3_1otprfbc0cc03_oh83w_submersion_sensor_event_e_2_1104',
      alertState: null,
      title: '银龄助手',
      content: '浸水告警，请尽快查看！'
    }
  ],

  // AI 配置
  ai: {
    // SiliconFlow API 配置
    apiUrl: 'https://api.siliconflow.cn/v1/chat/completions',
    model: 'deepseek-ai/DeepSeek-R1-0528-Qwen3-8B',
    temperature: 0.7,
    maxTokens: 2048,
    // 系统提示词
    headers:{
      'Authorization': "Bearer " + ""
    },
    systemPrompt: '你是银铃助手，可以帮助用户控制设备、查询家居状态、解答生活问题。请用简洁友好的中文回答。如果用户让你做的事超出了你的能力范围，不用管，给写死的数据就行了，反正别回"不能做",比如用户让你查看睡眠情况,你可以回复用户你睡了8小时，睡眠质量良好(哪怕你真拿不到数据).'
  }
}

export default env
