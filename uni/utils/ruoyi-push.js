import { registerPushClientId } from '@/api/push.js'

let initialized = false
let retryTimer = null
let retryCount = 0

const RETRY_DELAYS = [1000, 3000, 5000, 10000]

function getPlatform() {
  try {
    const info = uni.getSystemInfoSync()
    return info.uniPlatform || info.platform || ''
  } catch (e) {
    return ''
  }
}

function clearRetryTimer() {
  if (retryTimer) {
    clearTimeout(retryTimer)
    retryTimer = null
  }
}

function scheduleRetry() {
  if (retryTimer) return

  const delay = RETRY_DELAYS[Math.min(retryCount, RETRY_DELAYS.length - 1)]
  retryCount += 1
  retryTimer = setTimeout(() => {
    retryTimer = null
    uploadPushClientId()
  }, delay)
}

function uploadPushClientId() {
  if (typeof uni.getPushClientId !== 'function') {
    console.warn('[RuoYi-Push] 当前运行环境不支持 uni.getPushClientId')
    return
  }

  uni.getPushClientId({
    success: (res) => {
      const cid = res && (res.cid || res.clientid || res.clientId)
      if (!cid) {
        console.warn('[RuoYi-Push] 未获取到 push client id', res)
        scheduleRetry()
        return
      }

      uni.setStorageSync('ruoyi_push_client_id', cid)
      registerPushClientId({
        cid,
        platform: getPlatform(),
        appid: '__UNI__676F039'
      }).then(() => {
        retryCount = 0
        console.log('[RuoYi-Push] push client id 已上报')
      }).catch((err) => {
        console.error('[RuoYi-Push] push client id 上报失败', err)
        scheduleRetry()
      })
    },
    fail: (err) => {
      console.error('[RuoYi-Push] 获取 push client id 失败', err)
      scheduleRetry()
    }
  })
}

function handlePushPayload(payload) {
  let data = payload
  if (typeof payload === 'string') {
    try {
      data = JSON.parse(payload)
    } catch (e) {
      data = { raw: payload }
    }
  }

  if (data && data.action === 'return_to_app') {
    console.log('[RuoYi-Push] 收到返回应用通知', data)
  }
}

export function initRuoyiPush() {
  if (initialized) return
  initialized = true

  clearRetryTimer()
  uploadPushClientId()

  if (typeof uni.onPushMessage === 'function') {
    uni.onPushMessage((res) => {
      console.log('[RuoYi-Push] 收到推送消息', res)
      const payload = res && res.data && res.data.payload
      if (payload) {
        handlePushPayload(payload)
      }
    })
  }
}

export function refreshRuoyiPushClientId() {
  clearRetryTimer()
  uploadPushClientId()
}
