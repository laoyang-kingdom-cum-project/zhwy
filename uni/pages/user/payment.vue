<template>
  <view class="payment-container">
    <!-- 缴费统计 -->
    <view class="payment-stats">
      <view class="stat-card">
        <text class="stat-label">待缴费用</text>
        <text class="stat-value unpaid">¥{{ unpaidAmount }}</text>
      </view>
      <view class="stat-card">
        <text class="stat-label">本月已缴</text>
        <text class="stat-value paid">¥{{ paidAmount }}</text>
      </view>
    </view>

    <!-- 缴费列表 -->
    <view class="payment-section">
      <view class="section-title">待缴费</view>
      <view class="payment-list">
        <view 
          class="payment-item" 
          v-for="(item, index) in unpaidList" 
          :key="index"
          @click="goToPay(item)"
        >
          <view class="item-left">
            <view class="item-icon" :class="item.billType">{{ getIcon(item.billType) }}</view>
            <view class="item-info">
              <text class="item-name">{{ item.billName }}</text>
              <text class="item-time">{{ item.billPeriod }}</text>
            </view>
          </view>
          <view class="item-right">
            <text class="item-amount">¥{{ item.amount }}</text>
            <text class="pay-btn">去缴费</text>
          </view>
        </view>
        <view class="empty-tip" v-if="unpaidList.length === 0">暂无待缴费账单</view>
      </view>
    </view>

    <!-- 历史记录 -->
    <view class="payment-section">
      <view class="section-title">缴费记录</view>
      <view class="history-list">
        <view 
          class="history-item" 
          v-for="(item, index) in historyList" 
          :key="index"
        >
          <view class="item-left">
            <view class="item-icon" :class="item.billType">{{ getIcon(item.billType) }}</view>
            <view class="item-info">
              <text class="item-name">{{ item.billName }}</text>
              <text class="item-time">{{ item.billPeriod }}</text>
            </view>
          </view>
          <view class="item-right">
            <text class="item-amount">-¥{{ item.amount }}</text>
            <text class="item-status">已缴费</text>
          </view>
        </view>
        <view class="empty-tip" v-if="historyList.length === 0">暂无缴费记录</view>
      </view>
    </view>
  </view>
</template>

<script>
import { getBillList, payBill } from '@/api/payment.js'
import careModeMixin from '@/mixins/careMode.js'

export default {
  mixins: [careModeMixin],
  data() {
    return {
      unpaidAmount: 0,
      paidAmount: 0,
      unpaidList: [],
      historyList: []
    }
  },
  onLoad() {
    this.loadBillList()
  },
  onShow() {
    this.loadBillList()
  },
  methods: {
    // 获取图标
    getIcon(type) {
      const iconMap = {
        property: '🏠',
        water: '💧',
        electric: '⚡',
        gas: '🔥',
        parking: '🚗'
      }
      return iconMap[type] || '💰'
    },
    // 加载账单列表
    async loadBillList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        
        const res = await getBillList({
          pageNum: 1,
          pageSize: 100,
          userId: userId
        })
        
        if (res.code === 200 && res.rows) {
          // 分离待缴费和已缴费
          this.unpaidList = res.rows.filter(item => item.status === 0)
          this.historyList = res.rows.filter(item => item.status === 1)
          
          // 计算金额
          this.unpaidAmount = this.unpaidList.reduce((sum, item) => sum + parseFloat(item.amount), 0).toFixed(2)
          this.paidAmount = this.historyList.reduce((sum, item) => sum + parseFloat(item.amount), 0).toFixed(2)
        }
      } catch (e) {
        console.error('获取账单列表失败', e)
      }
    },
    async goToPay(item) {
      uni.showModal({
        title: '确认缴费',
        content: `确认支付 ${item.billName} ¥${item.amount}？`,
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '支付中...' })
            try {
              // 构建完整的账单数据，更新状态为已支付
              const payData = {
                ...item,
                status: 1,
                payMethod: 'alipay',
                payTime: new Date().toISOString().split('T')[0] + ' 00:00:00',
                remark: '已支付'
              }
              const payRes = await payBill(payData)
              uni.hideLoading()
              
              if (payRes.code === 200) {
                uni.showToast({ title: '支付成功', icon: 'success' })
                // 刷新数据
                this.loadBillList()
              } else {
                uni.showToast({ title: payRes.msg || '支付失败', icon: 'none' })
              }
            } catch (e) {
              uni.hideLoading()
              console.error('支付失败', e)
              uni.showToast({ title: '支付失败', icon: 'none' })
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.payment-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.payment-stats {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;

  .stat-card {
    flex: 1;
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    text-align: center;

    .stat-label {
      font-size: 26rpx;
      color: #999;
      display: block;
      margin-bottom: 16rpx;
    }

    .stat-value {
      font-size: 44rpx;
      font-weight: bold;

      &.unpaid {
        color: #FF6B6B;
      }

      &.paid {
        color: #2ED573;
      }
    }
  }
}

.payment-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }

  .payment-list, .history-list {
    .payment-item, .history-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24rpx 0;
      border-bottom: 1rpx solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .item-left {
        display: flex;
        align-items: center;

        .item-icon {
          width: 80rpx;
          height: 80rpx;
          border-radius: 16rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 40rpx;
          margin-right: 20rpx;

          &.property {
            background: #E3F2FD;
          }

          &.water {
            background: #E0F7FA;
          }

          &.electric {
            background: #FFF9C4;
          }

          &.gas {
            background: #FFECB3;
          }

          &.parking {
            background: #F3E5F5;
          }
        }

        .item-info {
          .item-name {
            font-size: 30rpx;
            color: #333;
            display: block;
            margin-bottom: 8rpx;
          }

          .item-time {
            font-size: 24rpx;
            color: #999;
          }
        }
      }

      .item-right {
        text-align: right;

        .item-amount {
          font-size: 32rpx;
          font-weight: bold;
          color: #FF6B6B;
          display: block;
          margin-bottom: 8rpx;
        }

        .pay-btn {
          font-size: 24rpx;
          color: #fff;
          background: #007AFF;
          padding: 8rpx 20rpx;
          border-radius: 8rpx;
        }

        .item-status {
          font-size: 24rpx;
          color: #2ED573;
        }
      }
    }

    .empty-tip {
      text-align: center;
      padding: 60rpx 0;
      color: #999;
      font-size: 28rpx;
    }
  }
}
</style>
