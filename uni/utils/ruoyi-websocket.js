import config from '@/config/index.js'

let socketTask = null
let _connected = false

export function connectRuoyiWs() {
  if (_connected) return

  // 获取配置中的基础URL，比如 http://localhost:8080，转成 ws://localhost:8080
  let wsUrl = config.baseUrl.replace('http://', 'ws://').replace('https://', 'wss://')
  // 加上我们刚才在后端配置的 endpoint
  wsUrl = wsUrl + '/websocket/device'

  socketTask = uni.connectSocket({
    url: wsUrl,
    success: () => {
      console.log('[RuoYi-WS] 连接成功: ' + wsUrl)
    },
    fail: (err) => {
      console.error('[RuoYi-WS] 连接失败:', err)
    }
  })

  uni.onSocketOpen(() => {
    _connected = true
    console.log('[RuoYi-WS] WebSocket已打开')
  })

  uni.onSocketMessage((res) => {
    console.log('[RuoYi-WS] 收到消息:', res.data)
    try {
      const data = JSON.parse(res.data)
      if (data.type === 'ailife_finished') {
        // 创建本地通知弹窗
        uni.createPushMessage({
          title: '智慧社区',
          content: data.message || '配置已完成，点击返回应用',
          payload: { action: 'return_to_app' },
          success: (res) => {
            console.log('[RuoYi-WS] 本地通知发送成功', res)
          }
        })
      }
    } catch (e) {
      console.error('[RuoYi-WS] 解析消息失败', e)
    }
  })

  uni.onSocketError((err) => {
    _connected = false
    console.error('[RuoYi-WS] 发生错误', err)
  })

  uni.onSocketClose(() => {
    _connected = false
    console.log('[RuoYi-WS] 已关闭')
    // 可以在这里加重连逻辑
  })
}

export function closeRuoyiWs() {
  if (socketTask) {
    socketTask.close()
    socketTask = null
  }
  _connected = false
}
