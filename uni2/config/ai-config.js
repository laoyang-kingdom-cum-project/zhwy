// AI 配置 - uni2 项目专用
// 从 uni 项目复制，修改此文件后需重新编译 App 生效

// Dify API 基础配置
const difyBaseConfig = {
  apiUrl: 'https://api.dify.ai/v1/chat-messages',
  apiKey: 'app-GgkaxIhg0WQAP8b1lzd9ct9L',
  userId: 'uni2-app-user'
}

// uni2 项目 - 物业 AI 助手配置
export const propertyAiConfig = {
  ...difyBaseConfig,
  welcomeMessage: '您好！我是银铃助手，可以帮您查询应急处理方案、获取周边应急资源、解答物业管理问题、提供政策法规咨询。请问有什么可以帮您？',
  quickQuestions: [
    '火灾应急方案',
    '燃气泄漏处理',
    '电梯困人救援',
    '设备报修流程'
  ]
}

// uni2 项目 - 应急方案配置
export const emergencyAiConfig = {
  ...difyBaseConfig
}

// 默认导出基础配置
export default difyBaseConfig
