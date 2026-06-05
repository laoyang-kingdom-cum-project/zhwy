import config from '@/config/index.js'

let socketTask = null
let _connected = false
let _connecting = false
let _manualClose = false
let reconnectTimer = null
let reconnectCount = 0
let wsCandidates = []
let wsCandidateIndex = 0

const RETRY_DELAYS = [1000, 3000, 5000, 10000]

function normalizePath(path) {
  if (!path) return ''
  return path.charAt(0) === '/' ? path : '/' + path
}

function trimTrailingSlash(url) {
  return (url || '').replace(/\/+$/, '')
}

function parseHttpBaseUrl(baseUrl) {
  const match = (baseUrl || '').match(/^(https?):\/\/([^/]+)(\/.*)?$/)
  if (!match) return null

  return {
    protocol: match[1],
    hostPort: match[2]
  }
}

function stripPort(hostPort) {
  if (!hostPort) return ''
  if (hostPort.charAt(0) === '[') {
    const end = hostPort.indexOf(']')
    return end > -1 ? hostPort.substring(0, end + 1) : hostPort
  }
  return hostPort.split(':')[0]
}

function uniquePush(list, url) {
  if (url && list.indexOf(url) === -1) {
    list.push(url)
  }
}

function buildWsCandidates() {
  const list = []
  const wsPath = normalizePath(config.ruoyiWsPath || '/websocket/device')
  const apiPrefix = normalizePath(config.apiPrefix || '')

  uniquePush(list, trimTrailingSlash(config.ruoyiWsUrl || ''))

  const base = parseHttpBaseUrl(config.baseUrl)
  if (base) {
    const wsProtocol = base.protocol === 'https' ? 'wss' : 'ws'
    uniquePush(list, `${wsProtocol}://${base.hostPort}${apiPrefix}${wsPath}`)
    uniquePush(list, `${wsProtocol}://${base.hostPort}${wsPath}`)

    const fallbackPort = config.ruoyiWsFallbackPort || 8080
    if (fallbackPort) {
      uniquePush(list, `${wsProtocol}://${stripPort(base.hostPort)}:${fallbackPort}${wsPath}`)
    }
  }

  return list
}

function clearReconnectTimer() {
  if (reconnectTimer) {
    clearTimeout(reconnectTimer)
    reconnectTimer = null
  }
}

function scheduleReconnect(reason) {
  if (_manualClose || reconnectTimer) return

  const delay = RETRY_DELAYS[Math.min(reconnectCount, RETRY_DELAYS.length - 1)]
  reconnectCount += 1
  console.log(`[RuoYi-WS] ${reason || '连接已断开'}，${delay}ms 后重连`)

  reconnectTimer = setTimeout(() => {
    reconnectTimer = null
    wsCandidates = buildWsCandidates()
    wsCandidateIndex = 0
    connectNextCandidate()
  }, delay)
}

function connectNextCandidate() {
  if (_manualClose || _connected || _connecting) return

  if (!wsCandidates.length) {
    wsCandidates = buildWsCandidates()
  }

  const wsUrl = wsCandidates[wsCandidateIndex++]
  if (!wsUrl) {
    _connecting = false
    scheduleReconnect('所有 WebSocket 地址均连接失败')
    return
  }

  _connecting = true
  let opened = false
  let handledClose = false
  const token = uni.getStorageSync('token')

  const task = uni.connectSocket({
    url: wsUrl,
    header: token ? { Authorization: 'Bearer ' + token } : {},
    success: () => {
      console.log('[RuoYi-WS] 尝试连接: ' + wsUrl)
    },
    fail: (err) => {
      console.error('[RuoYi-WS] 连接失败:', err)
      _connecting = false
      connectNextCandidate()
    }
  })

  socketTask = task

  task.onOpen(() => {
    if (socketTask !== task) return
    opened = true
    _connected = true
    _connecting = false
    reconnectCount = 0
    console.log('[RuoYi-WS] WebSocket已打开: ' + wsUrl)

    try {
      task.send({ data: 'ping' })
    } catch (e) {
      console.warn('[RuoYi-WS] ping发送失败', e)
    }
  })

  task.onMessage((res) => {
    if (socketTask !== task) return
    console.log('[RuoYi-WS] 收到消息:', res.data)
    try {
      const data = JSON.parse(res.data)
      if (data.type === 'ailife_finished') {
        showAiLifeFinishedNotice(data)
      }
    } catch (e) {
      console.error('[RuoYi-WS] 解析消息失败', e)
    }
  })

  task.onError((err) => {
    if (socketTask !== task || handledClose) return
    _connected = false
    _connecting = false
    console.error('[RuoYi-WS] 发生错误', err)

    if (!opened) {
      handledClose = true
      connectNextCandidate()
    } else {
      handledClose = true
      try {
        task.close()
      } catch (e) {
        console.warn('[RuoYi-WS] 关闭异常连接失败', e)
      }
      scheduleReconnect('WebSocket 发生错误')
    }
  })

  task.onClose((res) => {
    if (socketTask !== task || handledClose) return
    handledClose = true
    _connected = false
    _connecting = false
    console.log('[RuoYi-WS] 已关闭', res)

    if (_manualClose) return

    if (!opened) {
      connectNextCandidate()
    } else {
      scheduleReconnect('WebSocket 已关闭')
    }
  })
}

function showAiLifeFinishedNotice(data) {
  const title = data.title || '智慧社区'
  const content = data.message || '配置已完成，请返回应用'
  const payload = JSON.stringify({ action: 'return_to_app', type: data.type })

  if (typeof uni.createPushMessage === 'function') {
    uni.createPushMessage({
      title,
      content,
      payload,
      success: (res) => {
        console.log('[RuoYi-WS] 本地通知发送成功', res)
      },
      fail: (err) => {
        console.error('[RuoYi-WS] 本地通知发送失败，改用弹窗提示', err)
        showFallbackNotice(title, content)
      }
    })
    return
  }

  showFallbackNotice(title, content)
}

function showFallbackNotice(title, content) {
  uni.showModal({
    title,
    content,
    showCancel: false,
    confirmText: '知道了',
    fail: () => {
      uni.showToast({
        title: content,
        icon: 'none'
      })
    }
  })
}

export function connectRuoyiWs() {
  if (_connected || _connecting) return

  _manualClose = false
  clearReconnectTimer()
  wsCandidates = buildWsCandidates()
  wsCandidateIndex = 0
  connectNextCandidate()
}

export function closeRuoyiWs() {
  _manualClose = true
  clearReconnectTimer()
  if (socketTask) {
    socketTask.close()
    socketTask = null
  }
  _connected = false
  _connecting = false
}

export function getRuoyiWsStatus() {
  return {
    connected: _connected,
    connecting: _connecting,
    candidates: wsCandidates
  }
}
