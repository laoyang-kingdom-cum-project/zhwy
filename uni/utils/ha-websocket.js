// HA WebSocket 连接管理器
// 支持订阅实体状态变化，触发回调

let ws = null
let msgId = 0
let authResolve = null
let callbacks = {}     // id → callback
let subscriptions = [] // [{ entityId, callback }]

function nextId() {
  return ++msgId
}

function send(data) {
  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify(data))
  }
}

export function connect(accessToken, url = 'ws://192.168.0.71:8123/api/websocket') {
  return new Promise((resolve, reject) => {
    ws = new WebSocket(url)

    ws.onopen = () => {
      console.log('[HA-WS] 已连接，等待认证...')
    }

    ws.onmessage = (event) => {
      const msg = JSON.parse(event.data)

      // 1. 收到 auth_required → 发送认证
      if (msg.type === 'auth_required') {
        send({ type: 'auth', access_token: accessToken })
      }

      // 2. 认证成功
      if (msg.type === 'auth_ok') {
        console.log('[HA-WS] 认证成功')
        resolve()
        // 重新订阅之前注册的实体
        subscriptions.forEach(s => _doSubscribe(s.entityId, s.callback))
      }

      // 3. 认证失败
      if (msg.type === 'auth_invalid') {
        console.error('[HA-WS] 认证失败')
        reject(new Error(msg.message))
      }

      // 4. subscribe_events 的结果
      if (msg.type === 'result' && callbacks[msg.id]) {
        callbacks[msg.id](msg)
        delete callbacks[msg.id]
      }

      // 5. 状态变化事件
      if (msg.type === 'event' && msg.event) {
        const eventData = msg.event
        if (eventData.event_type === 'state_changed') {
          const entity = eventData.data.entity_id
          const newState = eventData.data.new_state
          if (newState) {
            subscriptions.forEach(s => {
              if (s.entityId === entity) {
                s.callback({
                  entityId: entity,
                  state: newState.state,
                  attributes: newState.attributes,
                  lastChanged: newState.last_changed
                })
              }
            })
          }
        }
      }
    }

    ws.onerror = (e) => {
      console.error('[HA-WS] 连接错误')
      reject(e)
    }

    ws.onclose = () => {
      console.log('[HA-WS] 连接关闭')
    }
  })
}

function _doSubscribe(entityId, callback) {
  const id = nextId()
  callbacks[id] = (result) => {
    if (result.success) {
      console.log(`[HA-WS] 已订阅: ${entityId}`)
    }
  }
  send({
    id,
    type: 'subscribe_events',
    event_type: 'state_changed'
  })
}

export function subscribe(entityId, callback) {
  // 避免重复订阅
  if (!subscriptions.find(s => s.entityId === entityId)) {
    subscriptions.push({ entityId, callback })
  }
  // 如果已连接，立即订阅
  if (ws && ws.readyState === WebSocket.OPEN) {
    _doSubscribe(entityId, callback)
  }
}

export function close() {
  if (ws) {
    ws.close()
    ws = null
  }
  subscriptions = []
}
