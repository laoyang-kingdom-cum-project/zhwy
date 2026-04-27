/**
 * 应用跳转插件类型定义
 */

/**
 * 通过URL Scheme启动应用
 * @param options 参数对象
 * @returns 是否成功启动
 */
export function openLink(options: { urlScheme: string }): boolean

/**
 * 通过包名启动应用
 * @param options 参数对象
 * @returns 是否成功启动
 */
export function launchApp(options: { bundleName: string; abilityName?: string }): boolean
