// 关怀模式 mixin - 用于所有页面
export default {
  onShow() {
    this.applyCareMode()
    this.autoSpeakPageTitle()
  },
  onLoad() {
    this.applyCareMode()
  },
  onUnload() {
    this.stopCareModeTTS()
  },
  methods: {
    // 应用关怀模式样式
    applyCareMode() {
      const careMode = uni.getStorageSync('careMode')

      // #ifdef APP-PLUS
      const pages = getCurrentPages()
      if (pages.length > 0) {
        const currentPage = pages[pages.length - 1]
        const currentWebview = currentPage.$getAppWebview()
        if (currentWebview) {
          // 在APP上尝试修改当前Webview的字体缩放比例 (130表示放大30%)
          currentWebview.setStyle({
            textZoom: careMode ? 130 : 100
          })
          
          // 通过 evalJS 注入全局类名，解决部分机型 textZoom 不生效问题
          currentWebview.evalJS(`
            if (typeof document !== 'undefined') {
              var body = document.body || document.querySelector('uni-page-body');
              if (body) {
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
      // 使用 nextTick 确保 DOM 已渲染
      this.$nextTick && this.$nextTick(() => {
        const pages = getCurrentPages()
        const currentPage = pages[pages.length - 1]
        if (currentPage) {
          // 尝试多种方式获取页面元素
          let pageElement = null
          if (currentPage.$el) {
            pageElement = currentPage.$el
          } else if (currentPage.$page) {
            pageElement = currentPage.$page
          }
          
          // H5 环境下通过 querySelector 获取
          if (!pageElement && typeof document !== 'undefined') {
            pageElement = document.querySelector('page') || document.body
          }
          
          if (pageElement && pageElement.classList) {
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
      const careMode = uni.getStorageSync('careMode')
      if (!careMode) return
      
      setTimeout(() => {
        let titleText = ''
        // 尝试获取页面标题
        // #ifdef APP-PLUS
        const pages = getCurrentPages()
        if (pages.length > 0) {
          const currentPage = pages[pages.length - 1]
          const currentWebview = currentPage.$getAppWebview()
          if (currentWebview) {
            const style = currentWebview.getStyle()
            if (style && style.titleNView && style.titleNView.titleText) {
              titleText = style.titleNView.titleText
            }
          }
        }
        // #endif
        
        // #ifdef H5
        if (typeof document !== 'undefined') {
          titleText = document.title
        }
        // #endif
        
        if (titleText && titleText !== 'uni-app') {
           this.speakText('当前进入：' + titleText)
        }
      }, 600)
    },
    
    // 全局可用的朗读方法
    speakText(text) {
      if (!text) return
      const careMode = uni.getStorageSync('careMode')
      if (!careMode) return
      
      // #ifdef APP-PLUS
      if (plus.os.name === 'Android') {
        let appData = getApp().globalData
        if (!appData) {
          getApp().globalData = {}
          appData = getApp().globalData
        }
        
        if (appData.androidTtsReady && appData.androidTts) {
          this.doAndroidTTS(appData.androidTts, text)
          return
        }
        
        if (appData._androidTtsInitializing) {
          // 正在初始化，暂存
          appData._pendingTtsText = text
          return
        }
        
        appData._androidTtsInitializing = true
        appData._pendingTtsText = text
        try {
          const main = plus.android.runtimeMainActivity()
          const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
          const listener = plus.android.implements('android.speech.tts.TextToSpeech$OnInitListener', {
            onInit: (status) => {
              appData._androidTtsInitializing = false
              if (status === TextToSpeech.SUCCESS) {
                appData.androidTtsReady = true
                if (appData._pendingTtsText) {
                  this.doAndroidTTS(appData.androidTts, appData._pendingTtsText)
                  appData._pendingTtsText = ''
                }
              } else {
                console.error('TTS初始化失败')
              }
            }
          })
          appData.androidTts = new TextToSpeech(main, listener)
        } catch(e) {
          appData._androidTtsInitializing = false
        }
        return
      }
      // #endif
      
      // #ifdef H5
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        window.speechSynthesis.cancel()
        const utterance = new SpeechSynthesisUtterance(text)
        utterance.lang = 'zh-CN'
        utterance.rate = 1
        window.speechSynthesis.speak(utterance)
      }
      // #endif
    },
    
    doAndroidTTS(tts, text) {
      // #ifdef APP-PLUS
      try {
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        const Locale = plus.android.importClass('java.util.Locale')
        
        let langResult = plus.android.invoke(tts, 'setLanguage', Locale.SIMPLIFIED_CHINESE)
        if (langResult === TextToSpeech.LANG_MISSING_DATA || langResult === TextToSpeech.LANG_NOT_SUPPORTED) {
           plus.android.invoke(tts, 'setLanguage', Locale.CHINESE)
        }
        
        plus.android.invoke(tts, 'setPitch', 1.0)
        plus.android.invoke(tts, 'setSpeechRate', 1.0)
        
        try {
          plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null, 'care_mode_tts')
        } catch(e) {
          plus.android.invoke(tts, 'speak', text, TextToSpeech.QUEUE_FLUSH, null)
        }
      } catch(e) {
        console.error('Android TTS 调用失败', e)
      }
      // #endif
    },
    
    stopCareModeTTS() {
      // #ifdef APP-PLUS
      let appData = getApp().globalData
      if (appData && appData.androidTtsReady && appData.androidTts) {
        try {
          plus.android.invoke(appData.androidTts, 'stop')
        } catch(e){}
      }
      // #endif
      
      // #ifdef H5
      if (typeof window !== 'undefined' && window.speechSynthesis) {
        window.speechSynthesis.cancel()
      }
      // #endif
    },

    openAndroidTTSSettings() {
      // #ifdef APP-PLUS
      if (plus.os.name !== 'Android') return
      try {
        const main = plus.android.runtimeMainActivity()
        const Intent = plus.android.importClass('android.content.Intent')
        const TextToSpeech = plus.android.importClass('android.speech.tts.TextToSpeech')
        const intent = new Intent(TextToSpeech.Engine.ACTION_TTS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        main.startActivity(intent)
      } catch (err) {
        uni.showToast({ title: '无法打开TTS设置', icon: 'none' })
      }
      // #endif
    }
  }
}