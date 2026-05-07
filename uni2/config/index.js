import env from '../../uni/config/env.js'

const config = {
  baseUrl: env.baseUrl,
  apiPrefix: env.apiPrefix,
  timeout: env.timeout,
  debug: env.debug,
  ai: env.ai,
  // Home Assistant 配置
  haHost: env.haHost,
  haPort: env.haPort,
  haAccessToken: env.haAccessToken
}

export default config
