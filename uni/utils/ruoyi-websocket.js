import config from '@/config/index.js'

let socketTask = null
let _connected = false
let _connecting = false

export function connectRuoyiWs() {
  if (_connected || _connecting) return
  _connecting = true

  // 解析出 baseUrl 中的 IP
  const urlObj = new URL(config.baseUrl)
  const host = urlObj.hostname
  
  // 强制指定为后端的 8080 端口
  const wsUrl = `ws://${host}:8080/websocket/device`

  socketTask = uni.connectSocket({
    url: wsUrl,
    success: () => {
      console.log('[RuoYi-WS] 尝试连接: ' + wsUrl)
    },
    fail: (err) => {
      console.error('[RuoYi-WS] 连接失败:', err)
      _connecting = false
    }
  })

  socketTask.onOpen(() => {
    _connected = true
    _connecting = false
    console.log('[RuoYi-WS] WebSocket已打开')
  })

  socketTask.onMessage((res) => {
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
          },
          fail: (err) => {
            console.error('[RuoYi-WS] 本地通知发送失败', err)
          }
        })
      }
    } catch (e) {
      console.error('[RuoYi-WS] 解析消息失败', e)
    }
  })

  socketTask.onError((err) => {
    _connected = false
    _connecting = false
    console.error('[RuoYi-WS] 发生错误', err)
  })

  socketTask.onClose(() => {
    _connected = false
    _connecting = false
    console.log('[RuoYi-WS] 已关闭')
  })
}

export function closeRuoyiWs() {
  if (socketTask) {
    socketTask.close()
    socketTask = null
  }
  _connected = false
  _connecting = false
}
