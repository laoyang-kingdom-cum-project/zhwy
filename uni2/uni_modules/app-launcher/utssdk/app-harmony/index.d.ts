/**
 * 通过包名启动应用（鸿蒙原生实现）
 * @param bundleName 目标应用包名
 * @param abilityName 目标Ability名称（可选，默认EntryAbility）
 * @returns 是否成功启动
 */
export function launchApp(bundleName: string, abilityName?: string): boolean;

/**
 * 检查应用是否已安装
 * @param bundleName 目标应用包名
 * @returns 是否已安装
 */
export function isAppInstalled(bundleName: string): boolean;
