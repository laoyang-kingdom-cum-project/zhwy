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

// sendNotification：发送告警通知函数
// 参数rule：告警规则配置对象，包含entityId、alertState、title、content等属性
// 参数data：HA WebSocket推送的设备数据对象，包含entityId、state、lastChanged等属性
// 根据不同平台使用不同方式发送通知
function sendNotification(rule, data) {
  // #ifdef APP-PLUS
  // APP-PLUS：UniApp条件编译指令，仅在原生APP环境（Android/iOS）中编译执行
  // plus.push是UniApp的推送模块API，用于发送本地推送通知
  // createMessage方法创建并发送一条本地推送消息
  // 第一个参数：消息内容（body）
  // 第二个参数：扩展数据对象，会随消息一起发送
  plus.push.createMessage(rule.content, {
    // entityId：HA实体的唯一标识符，用于标识触发告警的设备
    entityId: data.entityId,
    // state：设备当前状态，如'on'、'off'、'自检'等
    state: data.state,
    // time：设备状态最后变更时间戳
    time: data.lastChanged
  }, { // 第三个参数：推送选项配置对象
    // title：推送通知的标题，显示在系统通知栏
    title: rule.title,
    // sound：通知提示音，'system'表示使用系统默认提示音
    sound: 'system',
    // cover：推送覆盖标识，false表示不覆盖之前的推送，支持多条告警并存
    cover: false
  }) // 结束plus.push.createMessage调用
  // console.log输出调试信息，记录告警推送成功
  console.log(`[HA告警] 已推送: ${rule.content}`)
  // #endif 结束APP-PLUS条件编译分支

  // #ifdef H5
  // H5：UniApp条件编译指令，仅在H5/Web环境中小编译执行
  // uni.showModal是UniApp的模态对话框API，用于在页面上显示警告框
  // 用于在H5环境中替代原生推送通知
  uni.showModal({
    // title：对话框的标题栏文本
    title: rule.title,
    // content：对话框的正文内容，显示告警信息
    content: rule.content,
    // showCancel：是否显示取消按钮，false表示只显示确定按钮
    // 设置为false适合告警场景，减少用户操作步骤
    showCancel: false
  }) // 结束uni.showModal调用
  // #endif 结束H5条件编译分支
} // 结束sendNotification函数
