// HA 告警监听模块
import { connect, subscribe } from './ha-websocket.js'
import env from '@/config/env.js'

const ALERT_RULES = env.alertRules || []
const HA_WS_URL = `ws://${env.haHost}:${env.haPort}/api/websocket`

// 初始化告警监听
export async function initFallAlert(token) {
  // 第一步：校验token
  if (!token) {
    console.warn('[HA告警] 未配置 access token，跳过')
    return
  }

  try {
    // 第二步：连接HA WebSocket
    await connect(token, HA_WS_URL)
    console.log('[HA告警] WebSocket 已连接，加载 ' + ALERT_RULES.length + ' 条告警规则')

    // 第三步：遍历规则订阅实体
    ALERT_RULES.forEach(rule => {
      subscribe(rule.entityId, (data) => {
        console.log(`[HA告警] ${data.entityId} → ${data.state}`)

        // 第四步：判断是否需要告警
        const shouldAlert = rule.alertState
          ? data.state === rule.alertState
          : data.state && data.state !== '0' && data.state !== 'off' && data.state !== 'closed'

        if (shouldAlert) {
          sendNotification(rule, data)
        }
      })
    })
  } catch (e) {
    // 第五步：连接失败则30秒后重试
    console.error('[HA告警] 连接失败:', e.message)
    setTimeout(() => initFallAlert(token), 30000)
  }
}

// 发送告警通知
function sendNotification(rule, data) {
  // #ifdef APP-PLUS
  // 第一步：APP端使用本地推送
  plus.push.createMessage(rule.content, {
    entityId: data.entityId,
    state: data.state,
    time: data.lastChanged
  }, {
    title: rule.title,
    sound: 'system',
    cover: false
  })
  console.log(`[HA告警] 已推送: ${rule.content}`)
  // #endif

  // #ifdef H5
  // 第二步：H5端使用模态弹窗
  uni.showModal({
    title: rule.title,
    content: rule.content,
    showCancel: false
  })
  // #endif
}
