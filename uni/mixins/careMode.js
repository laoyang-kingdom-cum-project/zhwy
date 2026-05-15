// 关怀模式 mixin - 用于所有页面
export default {
  // 页面显示时触发
  onShow() {
    // 应用关怀模式
    this.applyCareMode()
    // 自动朗读页面标题
    this.autoSpeakPageTitle()
  },
  // 页面加载时触发
  onLoad() {
    // 应用关怀模式
    this.applyCareMode()
  },
  // 页面卸载时触发
  onUnload() {
    // 停止TTS朗读
    this.stopCareModeTTS()
  },
  // 方法定义
  methods: {
    // 应用关怀模式样式
    applyCareMode() {
      // 读取关怀模式开关状态
      const careMode = uni.getStorageSync('careMode')

      // #ifdef APP-PLUS
      // 获取当前页面栈
      const pages = getCurrentPages()
      // 判断页面栈是否非空
      if (pages.length > 0) {
        // 获取当前页面实例
        const currentPage = pages[pages.length - 1]
        // 获取当前webview实例
        const currentWebview = currentPage.$getAppWebview()
        // 判断webview是否存在
        if (currentWebview) {
          // 设置webview文字缩放比例
          currentWebview.setStyle({
            // 根据关怀模式设置缩放值
            textZoom: careMode ? 120 : 100
          })

          // 注入CSS类名兼容处理
          currentWebview.evalJS(`
            // 判断document是否存在
            if (typeof document !== 'undefined') {
              // 获取body元素
              var body = document.body || document.querySelector('uni-page-body');
              // 判断body是否存在
              if (body) {
                // 根据关怀模式添加或移除类名
                if (${careMode}) {
                  body.classList.add('care-mode-active');
                } else {
                  body.classList.remove('care-mode-active');
                }
              }
            }
          `);
        }
      }
      // #endif

      // #ifdef H5
      // H5环境DOM操作
      this.$nextTick && this.$nextTick(() => {
        // 获取当前页面栈
        const pages = getCurrentPages()
        // 获取当前页面实例
        const currentPage = pages[pages.length - 1]
        // 判断页面是否存在
        if (currentPage) {
          // 定义页面元素变量
          let pageElement = null
          // 尝试从$el获取元素
          if (currentPage.$el) {
            pageElement = currentPage.$el
          // 尝试从$page获取元素
          } else if (currentPage.$page) {
            pageElement = currentPage.$page
          }
          
          // 如果未获取到且document存在
          if (!pageElement && typeof document !== 'undefined') {
            // 通过querySelector获取页面元素
            pageElement = document.querySelector('page') || document.body
          }
          
          // 判断元素和classList是否存在
          if (pageElement && pageElement.classList) {
            // 根据关怀模式添加或移除类名
            if (careMode) {
              pageElement.classList.add('care-mode-active')
            } else {
              pageElement.classList.remove('care-mode-active')
            }
          }
        }
      })
      // #endif
    },
    
    // 自动朗读页面标题
    autoSpeakPageTitle() {
      // 读取关怀模式开关状态
      const careMode = uni.getStorageSync('careMode')
      // 如果未开启则返回
      if (!careMode) return
      
      // 延迟600ms执行
      setTimeout(() => {
        // 定义标题变量
        let titleText = ''
        // #ifdef APP-PLUS
        // 获取当前页面栈
        const pages = getCurrentPages()
        // 判断页面栈是否非空
        if (pages.length > 0) {
          // 获取当前页面实例
          const currentPage = pages[pages.length - 1]
          // 获取当前webview实例
          const currentWebview = currentPage.$getAppWebview()
          // 判断webview是否存在
          if (currentWebview) {
            // 获取webview样式
            const style = currentWebview.getStyle()
            // 判断标题是否存在
            if (style && style.titleNView && style.titleNView.titleText) {
              // 获取标题文本
              titleText = style.titleNView.titleText
            }
          }
        }
        // #endif
        
        // #ifdef H5
        // 判断document是否存在
        if (typeof document !== 'undefined') {
          // 从document获取标题
          titleText = document.title
        }
        // #endif
        
        // 判断标题是否有效
        if (titleText && titleText !== 'uni-app') {
           // 语音播报页面标题
           this.speakText('当前进入：' + titleText)
        }
      }, 600)
    },
    
    // 全局可用的朗读方法
    speakText(text) {
      // 校验参数是否为空
      if (!text) return
      // 读取关怀模式开关状态
      const careMode = uni.getStorageSync('careMode')
      // 如果未开启则返回
      if (!careMode) return
      
      // #ifdef APP-PLUS
      // 判断是否为Android平台
      if (plus.os.name === 'Android') {
        // 获取全局数据
        let appData = getApp().globalData
        // 如果全局数据不存在
        if (!appData) {
          // 初始化全局数据
          getApp().globalData = {}
          // 重新获取全局数据
          appData = getApp().globalData
        }
        
        // 判断TTS是否已初始化
        if (appData.androidTtsReady && appData.androidTts) {
          // 直接执行朗读
          this.doAndroidTTS(appData.androidTts, text)
          // 返回
          return
        }
        
        // 判断是否正在初始化
        if (appData._androidTtsInitializing) {
          // 暂存待朗读文本
          appData._pendingTtsText = text
          // 返回
          return
        }
        
        // 设置初始化标志
        appData._androidTtsInitializing = true
        // 暂存待朗读文本
        appData._pendingTtsText = text
        // 开始try块
        try {
          // 获取主Activity
          const main = plus.android.runtimeMainActivity()
          // 导入TextToSpeech类
          const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
          // 创建初始化监听器
          const listener = plus.android.implements('android.speech.tts.TextToSpeech$OnInitListener', {
            // 初始化回调
            onInit: (status) => {
              // 重置初始化标志
              appData._androidTtsInitializing = false
              // 判断初始化是否成功
              if (status === TextToSpeech.SUCCESS) {
                // 设置TTS就绪标志
                appData.androidTtsReady = true
                // 判断是否有待朗读文本
                if (appData._pendingTtsText) {
                  // 执行朗读
                  this.doAndroidTTS(appData.androidTts, appData._pendingTtsText)
                  // 清空待朗读文本
                  appData._pendingTtsText = ''
                }
              } else {
                // 输出初始化失败错误
                console.error('TTS初始化失败')
              }
            }
          })
          // 创建TTS实例
          appData.androidTts = new TextToSpeech(main, listener)
        // 捕获异常
        } catch(e) {
          // 重置初始化标志
          appData._androidTtsInitializing = false
        }
        // 返回
        return
      }
      // #endif
      
      // #ifdef H5
      // 判断window和speechSynthesis是否存在
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        // 取消当前朗读
        window.speechSynthesis.cancel()
        // 创建语音合成实例
        const utterance = new SpeechSynthesisUtterance(text)
        // 设置语言为中文
        utterance.lang = 'zh-CN'
        // 设置语速
        utterance.rate = 1
        // 执行朗读
        window.speechSynthesis.speak(utterance)
      }
      // #endif
    },
    
    // Android TTS执行方法
    doAndroidTTS(tts, text) {
      // #ifdef APP-PLUS
      // 开始try块
      try {
        // 导入TextToSpeech类
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        // 导入Locale类
        const Locale = plus.android.importClass('java.util.Locale')
        
        // 设置TTS语言为简体中文
        let langResult = plus.android.invoke(tts, 'setLanguage', Locale.SIMPLIFIED_CHINESE)
        // 判断语言设置结果
        if (langResult === TextToSpeech.LANG_MISSING_DATA || langResult === TextToSpeech.LANG_NOT_SUPPORTED) {
           // 尝试设置为中文
           plus.android.invoke(tts, 'setLanguage', Locale.CHINESE)
        }
        
        // 设置音调为1.0
        plus.android.invoke(tts, 'setPitch', 1.0)
        // 设置语速为1.0
        plus.android.invoke(tts, 'setSpeechRate', 1.0)
        
        // 开始内层try块
        try {
          // 执行朗读操作
          plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null, 'care_mode_tts')
        // 捕获异常
        } catch(e) {
          // 使用简化参数执行朗读
          plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null)
        }
      // 捕获异常
      } catch(e) {
        // 输出TTS调用失败错误
        console.error('Android TTS 调用失败', e)
      }
      // #endif
    },
    
    // 停止关怀模式TTS
    stopCareModeTTS() {
      // #ifdef APP-PLUS
      // 获取全局数据
      let appData = getApp().globalData
      // 判断TTS是否就绪
      if (appData && appData.androidTtsReady && appData.androidTts) {
        // 开始try块
        try {
          // 停止TTS朗读
          plus.android.invoke(appData.androidTts, 'stop')
        // 捕获异常
        } catch(e){}
      }
      // #endif
      
      // #ifdef H5
      // 判断window和speechSynthesis是否存在
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        // 取消语音合成
        window.speechSynthesis.cancel()
      }
      // #endif
    },

    // 打开Android TTS设置
    openAndroidTTSSettings() {
      // #ifdef APP-PLUS
      // 判断是否为Android平台
      if (plus.os.name !== 'Android') return
      // 开始try块
      try {
        // 获取主Activity
        const main = plus.android.runtimeMainActivity()
        // 导入Intent类
        const Intent = plus.android.importClass('android.content.Intent')
        // 导入TextToSpeech类
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        // 创建Intent实例
        const intent = new Intent(TextToSpeech.Engine.ACTION_TTS_SETTINGS)
        // 添加新任务标志
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // 启动设置页面
        main.startActivity(intent)
      // 捕获异常
      } catch (err) {
        // 显示打开失败提示
        uni.showToast({ title: '无法打开TTS设置', icon: 'none' })
      }
      // #endif
    }
  }
}
