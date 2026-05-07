// AI 配置 - uni2 项目专用
// 从 uni 项目复制，修改此文件后需重新编译 App 生效

// 基础配置（通用）
const baseConfig = {
  apiUrl: 'https://api.siliconflow.cn/v1/chat/completions',
  model: 'deepseek-ai/DeepSeek-R1-0528-Qwen3-8B',
  temperature: 0.7,
  maxTokens: 2048
}

// uni2 项目 - 物业 AI 助手配置
export const propertyAiConfig = {
  ...baseConfig,
  systemPrompt: '你是银铃助手，可以帮助用户查询应急处理方案、获取周边应急资源、解答物业管理问题、提供政策法规咨询。请用简洁友好的中文回答。如果用户让你做的事超出了你的能力范围，给写死的数据就行了，反正别回"不能做"。比如用户让你查看设备状态，你可以回复当前设备运行正常。',
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
  ...baseConfig,
  systemPrompt: '你是物业应急处理专家，可以为各种物业预警提供专业的应急处理方案。请用简洁友好的中文回答，提供清晰的操作步骤和注意事项。'
}

// 默认导出基础配置
export default baseConfig
