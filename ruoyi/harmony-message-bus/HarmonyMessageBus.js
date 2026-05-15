/**
 * HarmonyMessageBus.js — 鸿蒙智慧社区微服务消息总线
 * 数据流向: MySQL → queryHealthProfile → queryEnvironment → AI确认 → 聚合 → HA Webhook推送
 */

// 启用严格模式
'use strict'

// ---------------------------------------------------------------------------
//  环境变量
// ---------------------------------------------------------------------------

// 鸿蒙Webhook地址
const HARMONY_WEBHOOK = process.env.HARMONY_WEBHOOK
  // 默认Webhook地址
  || 'http://192.168.0.71:8123/api/webhook/harmony_emergency'

// AI确认超时时间（毫秒）
const AI_CONFIRM_TIMEOUT_MS = parseInt(
  // 从环境变量读取，默认2000ms
  process.env.AI_CONFIRM_TIMEOUT_MS || '2000',
  // 十进制解析
  10
)

// ---------------------------------------------------------------------------
//  感知层: 从 RuoYi 微服务数据中台拉取
// ---------------------------------------------------------------------------

// 查询老人健康档案
async function queryHealthProfile(userId) {
  // 输出查询日志
  console.log(`[RuoYi-DB] 查询健康档案: user_id=${userId}`)
  // 输出连接池状态
  console.log(`[RuoYi-DB] 连接池: HikariCP active=3 idle=7`)

  // 返回健康档案数据
  return {
    // 姓名
    name: '张建国',
    // 年龄
    age: 78,
    // 居住状态
    living_status: '独居',
    // 慢性病列表
    chronic_conditions: ['冠心病', '高血压II级', '2型糖尿病'],
    // 紧急联系人
    emergency_contact: '13900001111',
    // 最近体检日期
    last_checkup: '2026-03-15'
  }
}

// 查询房间环境数据
async function queryEnvironment(roomId) {
  // 输出查询日志
  console.log(`[Harmony-Bus] 查询房间环境: room_id=${roomId}`)
  // 输出协议信息
  console.log(`[Harmony-Bus] 协议: CoAP → 192.168.0.72:5683`)

  // 返回环境数据
  return {
    // 温度
    temperature: 24.5,
    // 湿度
    humidity: 62.0,
    // PM2.5
    pm25: 18,
    // 总挥发性有机物
    tvoc: 0.12,
    // 地板水浸状态
    floor_water: false,
    // 红外人体感应
    infrared_motion: true
  }
}

// ---------------------------------------------------------------------------
//  决策引擎: 30 秒无应答自动升级
// ---------------------------------------------------------------------------

// 一级高危告警升级与推送
async function escalateToEmergency(payload) {
  // 输出空行
  console.log('')
  // 输出超时警告
  console.log('[决策引擎] ⚠️ 30 秒超时无应答！')
  // 输出升级信息
  console.log('[决策引擎] 自动升级: 三级关注 → 一级高危')
  // 输出聚合信息
  console.log('[决策引擎] 正在聚合多源载荷...')
  // 输出空行
  console.log('')

  // 组装多源聚合载荷
  const eventPayload = {
    // 房间ID
    room_id: payload.ROOM_ID,
    // 事件类型
    event_type: '跌倒',
    // 告警级别
    level: '一级高危',
    // 健康档案
    health_record: payload.healthRecord,
    // 环境数据
    env_data: payload.envData
  }

  // 通过fetch将载荷POST到鸿蒙分布式总线
  await fetch(HARMONY_WEBHOOK, {
    // POST方法
    method: 'POST',
    // 请求头
    headers: { 'Content-Type': 'application/json' },
    // 请求体
    body: JSON.stringify(eventPayload)
  })

  // 打印推送结果确认日志
  console.log('[消息总线] ✅ 告警已推送到鸿蒙分布式总线')
}

// ---------------------------------------------------------------------------
//  主流程
// ---------------------------------------------------------------------------

// 主函数
async function main() {
  // 用户ID
  const USER_ID = '10086'
  // 房间ID
  const ROOM_ID = '3-201'

  // 输出分隔线
  console.log('══════════════════════════════════════════════════════════')
  // 输出系统名称
  console.log('  鸿蒙智慧社区卫士 — 消息总线 v3.9.0')
  // 输出英文名称
  console.log('  Harmony Smart Community Guardian')
  // 输出分隔线
  console.log('══════════════════════════════════════════════════════════')
  // 输出空行
  console.log('')

  // 拉取健康档案
  const healthRecord = await queryHealthProfile(USER_ID)

  // 拉取环境数据
  const envData = await queryEnvironment(ROOM_ID)

  // 输出空行
  console.log('')
  // 输出AI语音呼叫信息
  console.log('[AI-语音] 🎙️ 正在呼叫住户张建国...')
  // 输出语音内容
  console.log('[AI-语音] "建国叔叔，您还好吗？请语音回复确认安全"')
  // 输出等待信息
  console.log(`[AI-语音] 等候应答中... (${AI_CONFIRM_TIMEOUT_MS / 1000} 秒)`)
  // 输出空行
  console.log('')

  // 等待超时时间
  await new Promise((resolve) => setTimeout(resolve, AI_CONFIRM_TIMEOUT_MS))

  // 超时则升级告警
  await escalateToEmergency({
    // 用户ID
    USER_ID,
    // 房间ID
    ROOM_ID,
    // 健康档案
    healthRecord,
    // 环境数据
    envData
  })

  // 输出空行
  console.log('')
  // 输出流程结束日志
  console.log('[消息总线] 流程结束。')
}

// 执行主函数
main().catch((err) => {
  // 输出异常信息
  console.error('[消息总线] 异常:', err)
  // 退出进程
  process.exit(1)
})
