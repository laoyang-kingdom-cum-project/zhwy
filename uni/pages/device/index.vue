<template>
  <view class="device-container">
    <web-view 
      id="ha-webview"
      src="http://192.168.0.71:8123" 
      :update-title="false"
      @message="handleMessage"
    ></web-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      timer: null,
      wvId: null
    }
  },
  onShow() {
    uni.showTabBar()
    setTimeout(() => {
      this.findWebview()
    }, 3000)
  },
  onHide() {
    this.stopPolling()
  },
  onUnload() {
    this.stopPolling()
  },
  methods: {
    findWebview() {
      // #ifdef APP-PLUS
      try {
        const allWvs = plus.webview.all()
        console.log('[WebView] 所有webview数量:', allWvs.length)
        
        let wv = null
        for (const w of allWvs) {
          const url = w.getURL() || ''
          console.log('[WebView] webview id:', w.id, 'url:', url)
          if (url.includes('192.168.0.71')) {
            wv = w
            console.log('[WebView] 通过URL匹配到webview')
            break
          }
        }
        
        if (wv) {
          this.wvId = wv.id
          console.log('[WebView] 开始轮询，wvId:', this.wvId)
          this.startPolling()
        } else {
          console.log('[WebView] 未找到webview，3秒后重试')
          setTimeout(() => this.findWebview(), 3000)
        }
      } catch (e) {
        console.error('[WebView] 查找失败:', e)
      }
      // #endif
    },
    
    startPolling() {
      setTimeout(() => {
        this.getValueContent()
        
        this.timer = setInterval(() => {
          this.getValueContent()
        }, 5000)
      }, 5000)
    },
    
    stopPolling() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    
    getValueContent() {
      // #ifdef APP-PLUS
      if (!this.wvId) {
        console.log('[WebView] webview id为空')
        return
      }
      
      try {
        const wv = plus.webview.getWebviewById(this.wvId)
        if (!wv) {
          console.log('[WebView] 无法获取webview实例')
          return
        }
        
        console.log('[WebView] 执行evalJS获取内容...')
        
        // 方法1: 尝试使用回调
        const jsStr = "(function(){try{var els=document.querySelectorAll('*'),res=[];for(var i=0;i<els.length;i++){var c=els[i].className||'';if(typeof c==='string'&&c.indexOf('value')!==-1){res.push({tag:els[i].tagName,cls:c,text:els[i].innerText||els[i].textContent||''})}}return JSON.stringify(res)}catch(e){return JSON.stringify({err:e.message})}})()"
        
        wv.evalJS(jsStr, (result) => {
          console.log('[WebView] evalJS回调结果:', result)
          if (result && result !== 'undefined') {
            this.processResult(result)
          }
        })
        
        // 方法2: 1秒后尝试读取window变量（作为备选）
        setTimeout(() => {
          try {
            // 先注入数据到window
            wv.evalJS("window.__uniapp_value_data=(function(){try{var els=document.querySelectorAll('*'),res=[];for(var i=0;i<els.length;i++){var c=els[i].className||'';if(typeof c==='string'&&c.indexOf('value')!==-1){res.push({tag:els[i].tagName,cls:c,text:els[i].innerText||els[i].textContent||''})}}return JSON.stringify(res)}catch(e){return JSON.stringify({err:e.message})}})()")
            
            // 再读取
            setTimeout(() => {
              const result2 = wv.evalJS('window.__uniapp_value_data||""')
              console.log('[WebView] window变量结果:', result2)
              if (result2 && result2 !== 'undefined' && result2 !== '') {
                this.processResult(result2)
              }
            }, 500)
          } catch (e) {
            console.error('[WebView] window变量方式失败:', e)
          }
        }, 1000)
        
      } catch (e) {
        console.error('[WebView] 执行失败:', e)
      }
      // #endif
    },
    
    processResult(result) {
      if (!result || result === 'undefined') {
        console.log('[WebView] 结果为空')
        return
      }
      
      try {
        const data = JSON.parse(result)
        if (Array.isArray(data)) {
          console.log('[WebView] 找到', data.length, '个元素:')
          data.forEach((item, idx) => {
            console.log('[WebView] [' + idx + '] tag:', item.tag, 'class:', item.cls, 'text:', item.text)
          })
        } else if (data.err) {
          console.log('[WebView] 执行错误:', data.err)
        } else {
          console.log('[WebView] 返回数据:', data)
        }
      } catch (e) {
        console.log('[WebView] 解析失败，原始结果:', result)
      }
    },
    
    handleMessage(event) {
      console.log('[WebView] 收到postMessage:', event.detail)
    }
  }
}
</script>

<style>
.device-container {
  width: 100%;
  height: 100vh;
}
</style>