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
      content: '⚠️老人跌倒告警，请尽快查看！'
    },
    {
      entityId: 'event.xiaomi_cn_blt_3_1otprfbc0cc03_oh83w_submersion_sensor_event_e_2_1104',
      alertState: null,
      title: '银龄助手',
      content: '🌊浸水告警，请尽快查看！'
    }
  ]
}

export default env
