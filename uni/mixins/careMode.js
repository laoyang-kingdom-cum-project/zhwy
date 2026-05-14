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
      // APP-PLUS：UniApp平台专用于APP环境的条件编译分支
      // 以下代码仅在原生APP（如Android、iOS）中编译和执行
      // 获取UniApp页面栈，用于获取当前页面实例和webview对象
      const pages = getCurrentPages()
      // pages是页面栈数组，如果数组长度大于0说明有页面存在
      if (pages.length > 0) {
        // 数组length-1表示获取最后一个元素，即当前页面实例
        // getCurrentPages()返回页面栈数组，最后一个是当前页面
        const currentPage = pages[pages.length - 1]
        // $getAppWebview()是UniApp实例方法，用于获取当前页面所属的原生webview对象
        // 返回值是5+App的Webview对象，可调用setStyle、evalJS等方法
        const currentWebview = currentPage.$getAppWebview()
        // 判断webview对象是否存在，防止空指针异常
        if (currentWebview) {
          // setStyle用于设置webview的样式属性，可修改页面的缩放、背景色等
          // textZoom属性用于设置页面的文字缩放比例
          // 当careMode为true时文字放大到120%，false时恢复默认的100%
          currentWebview.setStyle({
            // textZoom:120 表示将文字缩放设置为120%，即放大1.2倍
            // 目的是适配老年用户的视力需求，提供更好的可读性
            textZoom: careMode ? 120 : 100
          })

          // evalJS方法用于向webview中注入并执行JavaScript代码
          // 这里注入的代码用于动态操作DOM元素的class类名
          // 原因：部分Android机型上setStyle的textZoom属性设置后不生效
          // 解决方案：通过CSS类名care-mode-active配合全局样式实现字体放大
          currentWebview.evalJS(`
            // typeof document !== 'undefined' 判断document对象是否存在
            // 兼容APP环境下webview可能不存在document的情况
            if (typeof document !== 'undefined') {
              // document.body是页面的body元素
              // document.querySelector('uni-page-body')是UniApp页面的根元素
              // 使用||的原因是优先使用uni-page-body，如果不存在则使用body
              var body = document.body || document.querySelector('uni-page-body');
              // 判断body元素是否存在，防止操作空对象报错
              if (body) {
                // 如果careMode为true，则添加care-mode-active类名
                // 如果careMode为false，则移除care-mode-active类名
                // 类名的添加和移除会触发CSS样式变化，实现字体放大/缩小效果
                if (${careMode}) {
                  // classList.add()方法向元素的class列表中添加指定类名
                  // 添加后CSS中定义的.care-mode-active样式会生效
                  body.classList.add('care-mode-active');
                } else {
                  // classList.remove()方法从元素的class列表中移除指定类名
                  // 移除后字体缩小恢复到默认状态
                  body.classList.remove('care-mode-active');
                } // 结束if-else careMode判断
              } // 结束if body判断
            } // 结束if document判断
          `); // 结束evalJS方法的字符串参数
        } // 结束if currentWebview判断
      } // 结束if pages.length判断
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