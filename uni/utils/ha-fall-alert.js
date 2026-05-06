// 跌倒/气体告警监听模块
// 监听 HA 传感器状态 → 状态变为"自检"时发送本地通知
//
// 使用方法: 在 App.vue 的 onLaunch 中调用 initFallAlert()

import { connect, subscribe } from './ha-websocket.js'
import env from '@/config/env.js'

const ENTITY_ID = env.haFallSensorEntity
const ALERT_STATE = env.haFallAlertState
const HA_WS_URL = `ws://${env.haHost}:${env.haPort}/api/websocket`

let connected = false

export async function initFallAlert(token) {
  if (!token) {
    console.warn('[跌倒告警] 未配置 HA access token，跳过')
    return
  }

  try {
    await connect(token, HA_WS_URL)
    connected = true
    console.log('[跌倒告警] HA WebSocket 已连接')

    subscribe(ENTITY_ID, (data) => {
      console.log(`[跌倒告警] ${data.entityId} 状态变化: ${data.state}`)

      if (data.state === ALERT_STATE) {
        sendLocalNotification(data)
      }
    })
  } catch (e) {
    console.error('[跌倒告警] 连接失败:', e.message)
    // 30秒后重试
    setTimeout(() => initFallAlert(token), 30000)
  }
}

function sendLocalNotification(data) {
  // #ifdef APP-PLUS
  plus.push.createMessage(
    '⚠️老人跌倒告警，请尽快查看！',
    {
      entityId: data.entityId,
      state: data.state,
      time: data.lastChanged
    },
    {
      title: '银龄助手',
      sound: 'system',
      cover: false
    }
  )

  console.log('[跌倒告警] 本地通知已发送')
  // #endif

  // H5 环境降级：弹窗提示
  // #ifdef H5
  uni.showModal({
    title: '银龄助手',
    content: '⚠️老人跌倒告警，请尽快查看！',
    showCancel: false
  })
  // #endif
}

export function isConnected() {
  return connected
}
