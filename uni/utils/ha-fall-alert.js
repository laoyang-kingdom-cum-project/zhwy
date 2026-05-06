// HA 告警监听模块
// 支持多条告警规则：监听 HA 传感器/事件 → 发送本地通知

import { connect, subscribe } from './ha-websocket.js'
import env from '@/config/env.js'

const ALERT_RULES = env.alertRules || []
const HA_WS_URL = `ws://${env.haHost}:${env.haPort}/api/websocket`

export async function initFallAlert(token) {
  if (!token) {
    console.warn('[HA告警] 未配置 access token，跳过')
    return
  }

  try {
    await connect(token, HA_WS_URL)
    console.log('[HA告警] WebSocket 已连接，加载 ' + ALERT_RULES.length + ' 条告警规则')

    ALERT_RULES.forEach(rule => {
      subscribe(rule.entityId, (data) => {
        console.log(`[HA告警] ${data.entityId} → ${data.state}`)

        const shouldAlert = rule.alertState
          ? data.state === rule.alertState
          : data.state && data.state !== '0' && data.state !== 'off' && data.state !== 'closed'

        if (shouldAlert) {
          sendNotification(rule, data)
        }
      })
    })
  } catch (e) {
    console.error('[HA告警] 连接失败:', e.message)
    setTimeout(() => initFallAlert(token), 30000)
  }
}

function sendNotification(rule, data) {
  // #ifdef APP-PLUS
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
  uni.showModal({
    title: rule.title,
    content: rule.content,
    showCancel: false
  })
  // #endif
}
