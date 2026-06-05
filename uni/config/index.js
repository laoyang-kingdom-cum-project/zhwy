import env from './env.js'

const config = {
  baseUrl: env.baseUrl,
  apiPrefix: env.apiPrefix,
  ruoyiWsUrl: env.ruoyiWsUrl,
  ruoyiWsPath: env.ruoyiWsPath,
  ruoyiWsFallbackPort: env.ruoyiWsFallbackPort,
  timeout: env.timeout,
  debug: env.debug,
  ai: env.ai
}

export default config
