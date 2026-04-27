/**
 * 应用跳转插件 - JS桥接层
 * 用于在UniApp中调用原生能力
 */

/**
 * 通过URL Scheme启动应用
 * @param {Object} options
 * @param {string} options.urlScheme - URL Scheme
 * @param {Function} options.fail - 失败回调
 * @param {Function} options.success - 成功回调
 */
export function openLink(options = {}) {
  const { urlScheme, fail, success } = options

  // #ifdef APP-HARMONY
  // 鸿蒙系统 - 调用UTS原生插件
  try {
    const appLauncher = uni.requireUTSPlugin('uni_modules/app-launcher')
    if (appLauncher && appLauncher.openLink) {
      console.log('调用鸿蒙openLink，options:', options)
      // 把整个options对象传进去
      const result = appLauncher.openLink(options)
      if (result) {
        success && success()
      } else {
        fail && fail({ errMsg: 'openLink返回失败' })
      }
    } else {
      console.error('原生插件openLink未找到')
      fail && fail({ errMsg: '原生插件未找到' })
    }
  } catch (e) {
    console.error('调用原生插件失败:', e)
    fail && fail({ errMsg: String(e) })
  }
  // #endif

  // #ifdef APP-PLUS
  // Android - 使用plus.runtime
  plus.runtime.openURL(urlScheme, () => {
    fail && fail({ errMsg: '打开失败' })
  })
  success && success()
  // #endif

  // #ifndef APP-PLUS || APP-HARMONY
  // H5/小程序
  if (typeof window !== 'undefined' && window.location) {
    window.location.href = urlScheme
    success && success()
  } else {
    fail && fail({ errMsg: '当前环境不支持' })
  }
  // #endif
}

/**
 * 通过包名启动应用
 * @param {Object} options
 * @param {string} options.bundleName - 目标应用包名
 * @param {string} options.abilityName - 目标Ability名称
 * @param {Function} options.fail - 失败回调
 * @param {Function} options.success - 成功回调
 */
export function launchApp(options = {}) {
  const { bundleName, abilityName = 'EntryAbility', fail, success } = options

  // #ifdef APP-HARMONY
  // 鸿蒙系统 - 调用UTS原生插件
  try {
    const appLauncher = uni.requireUTSPlugin('uni_modules/app-launcher')
    if (appLauncher && appLauncher.launchApp) {
      console.log('调用鸿蒙launchApp，包名:', bundleName)
      const result = appLauncher.launchApp(bundleName, abilityName)
      if (result) {
        success && success()
      } else {
        fail && fail({ errMsg: 'launchApp返回失败' })
      }
    } else {
      console.error('原生插件launchApp未找到')
      fail && fail({ errMsg: '原生插件未找到' })
    }
  } catch (e) {
    console.error('调用原生插件失败:', e)
    fail && fail({ errMsg: String(e) })
  }
  // #endif

  // #ifdef APP-PLUS
  // Android - 使用plus.runtime
  plus.runtime.launchApplication({
    pname: bundleName
  }, () => {
    fail && fail({ errMsg: '打开失败' })
  })
  success && success()
  // #endif

  // #ifndef APP-PLUS || APP-HARMONY
  fail && fail({ errMsg: '当前环境不支持' })
  // #endif
}

export default {
  openLink,
  launchApp
}
