<template>
  <view class="agreement-container">
    <view class="content">
      <view class="title">{{ title }}</view>
      <view class="section" v-for="(item, index) in contentList" :key="index">
        <view class="section-title">{{ item.title }}</view>
        <view class="section-text">{{ item.text }}</view>
      </view>
    </view>
  </view>
</template>

<script>
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      type: '',
      title: '',
      contentList: []
    }
  },
  onLoad(options) {
    this.type = options.type || 'user'
    this.loadContent()
  },
  methods: {
    loadContent() {
      if (this.type === 'user') {
        this.title = '用户协议'
        this.contentList = [
          {
            title: '一、协议范围',
            text: '本协议是您与智慧社区平台之间关于使用本平台服务所订立的协议。您在使用本平台服务之前，应当仔细阅读本协议，并同意遵守本协议的全部内容。'
          },
          {
            title: '二、账号注册',
            text: '1. 您在使用本平台服务前需要注册一个账号。账号应当使用真实身份信息注册，并保证信息的真实、准确、完整。\n2. 您有责任妥善保管账号及密码，因您保管不善导致的账号被盗用，本平台不承担责任。\n3. 未经本平台书面同意，您不得将账号转让、出借或以其他方式许可他人使用。'
          },
          {
            title: '三、服务使用规范',
            text: '1. 您在使用本平台服务时，应当遵守国家法律法规，不得利用本平台从事违法违规活动。\n2. 您不得干扰本平台的正常运行，不得攻击、侵入本平台的服务器或网络系统。\n3. 您应当遵守社区公约，文明发言，尊重他人合法权益。'
          },
          {
            title: '四、知识产权',
            text: '本平台的所有内容，包括但不限于文字、图片、音频、视频、软件、程序、页面设计等，均受知识产权法律法规保护。未经本平台书面许可，任何人不得擅自使用、复制、修改或传播。'
          },
          {
            title: '五、免责声明',
            text: '1. 本平台尽力提供准确、完整的信息，但不保证信息的绝对准确性和完整性。\n2. 因不可抗力或第三方原因导致的服务中断或数据丢失，本平台不承担责任。\n3. 您在使用本平台服务过程中产生的任何直接或间接损失，本平台不承担责任。'
          },
          {
            title: '六、协议修改',
            text: '本平台有权根据业务发展需要修改本协议。修改后的协议将在本平台上公布，您继续使用本平台服务视为同意修改后的协议。'
          },
          {
            title: '七、其他',
            text: '本协议的订立、执行和解释均适用中华人民共和国法律。如发生争议，双方应友好协商解决；协商不成的，任何一方均可向本平台所在地有管辖权的人民法院提起诉讼。'
          }
        ]
      } else {
        this.title = '隐私政策'
        this.contentList = [
          {
            title: '一、引言',
            text: '智慧社区平台（以下简称"我们"）非常重视您的隐私保护。本隐私政策说明我们如何收集、使用、存储和保护您的个人信息。请您仔细阅读并理解本政策。'
          },
          {
            title: '二、信息收集',
            text: '我们可能收集以下类型的信息：\n1. 您提供的个人信息：包括姓名、手机号码、身份证号、房产信息等。\n2. 设备信息：包括设备型号、操作系统版本、设备标识符等。\n3. 使用信息：包括您使用我们服务的时间、方式、操作记录等。\n4. 位置信息：经您授权后，我们可能收集您的位置信息以提供相关服务。'
          },
          {
            title: '三、信息使用',
            text: '我们使用您的信息用于以下目的：\n1. 提供、维护和改进我们的服务。\n2. 处理您的报修、缴费等业务请求。\n3. 向您发送服务通知、活动信息等。\n4. 进行数据分析和研究，改善用户体验。\n5. 遵守法律法规要求，保护您和他人的合法权益。'
          },
          {
            title: '四、信息共享',
            text: '我们不会向第三方出售您的个人信息。但在以下情况下，我们可能会共享您的信息：\n1. 经您明确同意。\n2. 与我们的关联公司共享，以提供更好的服务。\n3. 与为我们提供服务的第三方合作伙伴共享（如支付服务提供商）。\n4. 根据法律法规要求或政府部门的合法要求。'
          },
          {
            title: '五、信息保护',
            text: '我们采取多种安全措施保护您的个人信息：\n1. 使用加密技术保护数据传输安全。\n2. 建立严格的访问控制机制。\n3. 定期进行安全审计和风险评估。\n4. 对员工进行隐私保护培训。'
          },
          {
            title: '六、您的权利',
            text: '您对您的个人信息享有以下权利：\n1. 访问权：您有权查看我们持有的关于您的个人信息。\n2. 更正权：您有权要求更正不准确的个人信息。\n3. 删除权：在符合法律规定的情况下，您有权要求删除您的个人信息。\n4. 撤回同意权：您可以随时撤回对个人信息处理的同意。'
          },
          {
            title: '七、Cookie和类似技术',
            text: '我们可能使用Cookie和类似技术来改善您的使用体验。您可以通过浏览器设置拒绝Cookie，但这可能会影响某些功能的正常使用。'
          },
          {
            title: '八、未成年人保护',
            text: '我们非常重视对未成年人个人信息的保护。如果您是未成年人，请在监护人的指导下使用我们的服务，并在征得监护人同意后提供个人信息。'
          },
          {
            title: '九、政策更新',
            text: '我们可能会根据业务发展和法律法规变化更新本隐私政策。更新后的政策将在本平台上公布，重大变更我们会通过适当方式通知您。'
          },
          {
            title: '十、联系我们',
            text: '如果您对本隐私政策有任何疑问或建议，请通过以下方式联系我们：\n邮箱：privacy@smartproperty.com\n电话：400-xxx-xxxx\n地址：xx市xx区xx路xx号'
          }
        ]
      }
    }
  }
}
</script>

<style lang="scss">
.agreement-container {
  min-height: 100vh;
  background: #fff;
  padding: 40rpx 30rpx;
}

.content {
  .title {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    text-align: center;
    margin-bottom: 60rpx;
  }

  .section {
    margin-bottom: 40rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }

    .section-text {
      font-size: 28rpx;
      color: #666;
      line-height: 1.8;
      white-space: pre-wrap;
    }
  }
}
</style>
