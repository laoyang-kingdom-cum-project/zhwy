// UniApp 前端统一配置（修改此文件后需重新编译 App 生效）
const env = {
  // 后端 API
  baseUrl: 'http://192.168.0.114:8080',
  apiPrefix: '/por-api',
  timeout: 10000,
  debug: true,

  // Home Assistant
  haHost: '192.168.0.71',
  haPort: 8123,

  // HA 长期访问令牌（外部认证用）
  haAccessToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIxZWVjM2Q4YzY3ZmU0OGIzYWNmZGQ5M2UyZGIxNDk1MyIsImlhdCI6MTc3Nzk2NjM1MywiZXhwIjoyMDkzMzI2MzUzfQ.Xi0xhMufcx_rlB0mFK7XNueIyR1eAPLaEmqPg2yrGtI',

  // 跌倒检测
  haFallSensorEntity: 'sensor.lumi_cn_918860765_mcn02_status_p_2_1',
  haFallAlertState: '自检'
}

export default env
