// HA WebSocket 连接管理器（使用 uni.connectSocket）
// 订阅实体状态变化，触发回调

let socketTask = null
let msgId = 0
let callbacks = {}     // id → callback
let subscriptions = [] // [{ entityId, callback }]
let _connected = false
let _authenticated = false

function nextId() { return ++msgId }

function send(data) {
  if (socketTask) {
    socketTask.send({ data: JSON.stringify(data) })
  }
}

export function connect(accessToken, url) {
  return new Promise((resolve, reject) => {
    // 注册全局事件（uni socket API 是全局监听）
    uni.onSocketOpen(() => {
      console.log('[HA-WS] 已连接')
      _connected = true
    })

    uni.onSocketMessage((res) => {
      let msg
      try { msg = JSON.parse(res.data) } catch (e) { return }

      // 1. 要求认证
      if (msg.type === 'auth_required') {
        send({ type: 'auth', access_token: accessToken })
        return
      }

      // 2. 认证成功
      if (msg.type === 'auth_ok') {
        console.log('[HA-WS] 认证成功')
        _authenticated = true
        resolve(true)
        subscriptions.forEach(s => _doSubscribe(s.entityId, s.callback))
        return
      }

      // 3. 认证失败
      if (msg.type === 'auth_invalid') {
        console.error('[HA-WS] 认证失败:', msg.message)
        reject(new Error(msg.message))
        return
      }

      // 4. 订阅结果回调
      if (msg.type === 'result' && msg.id && callbacks[msg.id]) {
        callbacks[msg.id](msg)
        delete callbacks[msg.id]
        return
      }

      // 5. 状态变化事件
      if (msg.type === 'event' && msg.event && msg.event.event_type === 'state_changed') {
        const data = msg.event.data
        if (data && data.new_state) {
          subscriptions.forEach(s => {
            if (s.entityId === data.entity_id) {
              s.callback({
                entityId: data.entity_id,
                state: data.new_state.state,
                attributes: data.new_state.attributes,
                lastChanged: data.new_state.last_changed
              })
            }
          })
        }
      }
    })

    uni.onSocketError((err) => {
      console.error('[HA-WS] 连接错误:', JSON.stringify(err))
      reject(err)
    })

    uni.onSocketClose(() => {
      console.log('[HA-WS] 连接关闭')
      _connected = false
      _authenticated = false
    })

    // 创建连接
    socketTask = uni.connectSocket({
      url,
      success: () => console.log('[HA-WS] socketTask 创建成功'),
      fail: (err) => reject(err)
    })
  })
}

function _doSubscribe(entityId, callback) {
  const id = nextId()
  callbacks[id] = (result) => {
    if (result.success) {
      console.log(`[HA-WS] 已订阅: ${entityId}`)
    }
  }
  send({ id, type: 'subscribe_events', event_type: 'state_changed' })
}

export function subscribe(entityId, callback) {
  if (!subscriptions.find(s => s.entityId === entityId)) {
    subscriptions.push({ entityId, callback })
  }
  if (_authenticated) {
    _doSubscribe(entityId, callback)
  }
}

export function close() {
  if (socketTask) {
    socketTask.close()
    socketTask = null
  }
  _connected = false
  _authenticated = false
  subscriptions = []
}

export function isConnected() {
  return _connected && _authenticated
}
