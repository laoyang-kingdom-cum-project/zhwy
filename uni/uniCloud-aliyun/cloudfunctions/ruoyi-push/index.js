'use strict'

exports.main = async (event) => {
  const expectedSecret = process.env.RUOYI_PUSH_SECRET || ''
  if (expectedSecret && event.secret !== expectedSecret) {
    return {
      code: 401,
      message: 'invalid secret'
    }
  }

  const clientIds = Array.isArray(event.clientIds) ? event.clientIds.filter(Boolean) : []
  if (!clientIds.length) {
    return {
      code: 400,
      message: 'clientIds is empty'
    }
  }

  const appId = event.appId || '__UNI__676F039'
  const title = event.title || '智慧社区'
  const content = event.content || '配置已完成，请返回应用'
  const payload = event.payload || {}
  const uniPush = uniCloud.getPushManager({ appId })

  try {
    const result = await uniPush.sendMessage({
      push_clientid: clientIds,
      title,
      content,
      payload,
      force_notification: true,
      request_id: `${Date.now()}_${Math.random().toString(36).slice(2)}`
    })

    return {
      code: 200,
      message: 'ok',
      result
    }
  } catch (error) {
    return {
      code: 500,
      message: error.message || 'send push failed',
      error
    }
  }
}
