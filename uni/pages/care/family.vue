<template>
  <view class="page">
    <view class="header-card">
      <view class="header-bg"></view>
      <view class="header-content">
        <text class="header-title">家人管理</text>
        <text class="header-desc">管理您关注的家人健康信息</text>
        <view class="header-stat">
          <text class="stat-num">{{ familyList.length }}</text>
          <text class="stat-unit">位家人</text>
        </view>
      </view>
    </view>

    <view class="family-list" v-if="familyList.length > 0">
      <view class="family-card" v-for="(item, index) in familyList" :key="index">
        <view class="card-main">
          <view class="avatar-wrap">
            <image class="avatar" src="/static/logo.png" mode="aspectFill"></image>
            <view class="status-dot" :class="item.healthStatus === '0' ? 'green' : item.healthStatus === '1' ? 'orange' : 'red'"></view>
          </view>
          <view class="info">
            <view class="name-row">
              <text class="name">{{ item.name }}</text>
              <view class="status-tag" :class="item.healthStatus === '0' ? 'tag-green' : item.healthStatus === '1' ? 'tag-orange' : 'tag-red'">
                {{ item.healthStatus === '0' ? '健康' : item.healthStatus === '1' ? '关注' : '异常' }}
              </view>
            </view>
            <text class="meta">{{ item.age ? item.age + '岁' : '' }}{{ item.age && item.room ? ' · ' : '' }}{{ item.room || '' }}</text>
            <view class="contact-row" v-if="item.emergencyContact">
              <text class="contact-icon">☎</text>
              <text class="contact-text">{{ item.emergencyContact }}</text>
            </view>
          </view>
        </view>
        <view class="card-actions">
          <view class="act-btn act-edit" @click="editFamily(item)">
            <text class="act-icon">✎</text>
            <text class="act-label">编辑</text>
          </view>
          <view class="act-divider"></view>
          <view class="act-btn act-del" @click="confirmUnbind(item)">
            <text class="act-icon">✕</text>
            <text class="act-label">解绑</text>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-wrap" v-else>
      <view class="empty-illust">
        <text class="empty-emoji">🏠</text>
      </view>
      <text class="empty-title">还没有绑定家人</text>
      <text class="empty-desc">添加家人后可以实时关注他们的健康状况</text>
    </view>

    <view class="bottom-bar">
      <view class="add-btn" @click="openAddForm">
        <text class="add-icon">＋</text>
        <text class="add-text">添加家人</text>
      </view>
    </view>

    <view class="mask" v-if="showPopup" @click="closePopup"></view>
    <view class="popup" :class="{ show: showPopup }">
      <view class="popup-head">
        <text class="popup-title">{{ isEdit ? '编辑家人信息' : '添加家人' }}</text>
        <view class="popup-close" @click="closePopup">
          <text class="close-x">✕</text>
        </view>
      </view>
      <scroll-view scroll-y class="popup-body">
        <view class="field">
          <text class="field-label">姓名 <text class="required">*</text></text>
          <view class="field-input-wrap">
            <input class="field-input" v-model="formData.name" placeholder="请输入家人姓名" />
          </view>
        </view>
        <view class="field">
          <text class="field-label">年龄</text>
          <view class="field-input-wrap">
            <input class="field-input" v-model="formData.age" type="number" placeholder="请输入年龄" />
          </view>
        </view>
        <view class="field">
          <text class="field-label">住址</text>
          <view class="field-input-wrap">
            <input class="field-input" v-model="formData.room" placeholder="请输入住址" />
          </view>
        </view>
        <view class="field">
          <text class="field-label">紧急联系人</text>
          <view class="field-input-wrap">
            <input class="field-input" v-model="formData.emergencyContact" placeholder="姓名 + 手机号" />
          </view>
        </view>
      </scroll-view>
      <view class="popup-foot">
        <view class="submit-btn" @click="submitForm">
          <text class="submit-text">{{ isEdit ? '保存修改' : '确认添加' }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getCareList, bindFamily, updateFamily, unbindFamily } from '@/api/care.js'
import { addWarning } from '@/api/warning.js'

export default {
  data() {
    return {
      familyList: [],
      showPopup: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        age: '',
        room: '',
        emergencyContact: '',
        healthStatus: '0'
      }
    }
  },
  onLoad() {
    this.loadFamilyList()
  },
  onShow() {
    this.loadFamilyList()
  },
  methods: {
    async loadFamilyList() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1
        const res = await getCareList({
          pageNum: 1,
          pageSize: 100,
          userid: userId
        })
        if (res.code === 200 && res.rows) {
          this.familyList = res.rows
          this.checkFamilyHealthStatus()
        }
      } catch (e) {
        console.error('获取家人列表失败', e)
      }
    },
    async checkFamilyHealthStatus() {
      const now = new Date()

      for (const member of this.familyList) {
        if (!member.lastActive) continue

        const lastActiveDate = new Date(member.lastActive)
        const hoursSince = (now - lastActiveDate) / (1000 * 60 * 60)

        // 超过4小时自动改为待关注状态
        if (hoursSince > 4 && member.healthStatus === '0') {
          try {
            await updateFamily({
              id: member.id,
              name: member.name,
              age: member.age,
              room: member.room,
              emergencyContact: member.emergencyContact,
              healthStatus: '1',
              steps: member.steps || '0',
              sleep: member.sleep || '0',
              heartRate: member.heartRate || '0',
              pressure: member.pressure || '',
              lastActive: member.lastActive,
              userid: member.userid
            })
            member.healthStatus = '1'
          } catch (e) {
            console.error('更新健康状态失败', e)
          }
        }

        // 超过24小时自动发送安全预警（静默发送，不弹窗）
        if (hoursSince > 24) {
          try {
            const sentWarnings = uni.getStorageSync('sentWarnings') || {}
            const lastSentTime = sentWarnings[member.id]
            const nowTime = Date.now()
            
            if (lastSentTime && (nowTime - lastSentTime) < 24 * 60 * 60 * 1000) {
              continue
            }
            
            const userInfo = uni.getStorageSync('userInfo')
            await addWarning({
              title: `家人「${member.name}」超过24小时无活动`,
              location: member.room || '未知住址',
              time: this.formatDateTime(new Date()),
              state: '0',
              level: '1',
              userid: userInfo ? userInfo.userId : 1
            })
            
            sentWarnings[member.id] = nowTime
            uni.setStorageSync('sentWarnings', sentWarnings)
          } catch (e) {
            console.error('发送安全预警失败', e)
          }
        }
      }
    },
    openAddForm() {
      this.isEdit = false
      this.resetForm()
      this.showPopup = true
    },
    editFamily(item) {
      this.isEdit = true
      this.formData = {
        id: item.id,
        name: item.name || '',
        age: item.age ? String(item.age) : '',
        room: item.room || '',
        emergencyContact: item.emergencyContact || '',
        healthStatus: item.healthStatus || '0'
      }
      this.showPopup = true
    },
    confirmUnbind(item) {
      uni.showModal({
        title: '解绑家人',
        content: `确定要解绑「${item.name}」吗？解绑后将无法查看其健康数据`,
        confirmText: '确认解绑',
        confirmColor: '#FF4D4F',
        success: async (res) => {
          if (res.confirm) {
            try {
              uni.showLoading({ title: '解绑中...' })
              const result = await unbindFamily(item.id)
              uni.hideLoading()
              if (result.code === 200) {
                uni.showToast({ title: '已解绑', icon: 'success' })
                this.loadFamilyList()
              } else {
                uni.showToast({ title: result.msg || '解绑失败', icon: 'none' })
              }
            } catch (e) {
              uni.hideLoading()
              uni.showToast({ title: '解绑失败', icon: 'none' })
            }
          }
        }
      })
    },
    closePopup() {
      this.showPopup = false
      this.resetForm()
    },
    resetForm() {
      this.isEdit = false
      this.formData = {
        id: null,
        name: '',
        age: '',
        room: '',
        emergencyContact: '',
        healthStatus: '0'
      }
    },
    async submitForm() {
      if (!this.formData.name.trim()) {
        uni.showToast({ title: '请输入家人姓名', icon: 'none' })
        return
      }

      try {
        uni.showLoading({ title: this.isEdit ? '保存中...' : '添加中...' })
        const userInfo = uni.getStorageSync('userInfo')
        const userId = userInfo ? userInfo.userId : 1

        const submitData = {
          ...this.formData,
          age: this.formData.age ? Number(this.formData.age) : null,
          userid: userId,
          steps: '0',
          sleep: '0',
          heartRate: '0',
          pressure: '',
          lastActive: new Date().toISOString()
        }

        let result
        if (this.isEdit) {
          result = await updateFamily(submitData)
        } else {
          result = await bindFamily(submitData)
        }

        uni.hideLoading()
        if (result.code === 200) {
          uni.showToast({
            title: this.isEdit ? '修改成功' : '添加成功',
            icon: 'success'
          })
          this.closePopup()
          this.loadFamilyList()
        } else {
          uni.showToast({ title: result.msg || '操作失败', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        uni.showToast({ title: '操作失败', icon: 'none' })
      }
    },
    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style lang="scss">
.page {
  min-height: 100vh;
  background: #f0f2f5;
}

.header-card {
  position: relative;
  overflow: hidden;

  .header-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  .header-content {
    position: relative;
    padding: 48rpx 40rpx 56rpx;

    .header-title {
      font-size: 44rpx;
      font-weight: 700;
      color: #fff;
      display: block;
      margin-bottom: 8rpx;
    }

    .header-desc {
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.75);
      display: block;
      margin-bottom: 32rpx;
    }

    .header-stat {
      display: flex;
      align-items: baseline;

      .stat-num {
        font-size: 64rpx;
        font-weight: 700;
        color: #fff;
        margin-right: 8rpx;
      }

      .stat-unit {
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.family-list {
  padding: 0 24rpx;
  margin-top: -24rpx;
  position: relative;
  z-index: 1;
}

.family-card {
  background: #fff;
  border-radius: 20rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);

  .card-main {
    padding: 32rpx;
    display: flex;
    align-items: center;

    .avatar-wrap {
      position: relative;
      margin-right: 28rpx;

      .avatar {
        width: 108rpx;
        height: 108rpx;
        border-radius: 50%;
        border: 4rpx solid #f0f0f0;
      }

      .status-dot {
        position: absolute;
        bottom: 4rpx;
        right: 4rpx;
        width: 24rpx;
        height: 24rpx;
        border-radius: 50%;
        border: 4rpx solid #fff;

        &.green { background: #52c41a; }
        &.orange { background: #faad14; }
        &.red { background: #ff4d4f; }
      }
    }

    .info {
      flex: 1;

      .name-row {
        display: flex;
        align-items: center;
        margin-bottom: 10rpx;

        .name {
          font-size: 34rpx;
          font-weight: 600;
          color: #1a1a1a;
          margin-right: 16rpx;
        }

        .status-tag {
          font-size: 22rpx;
          padding: 4rpx 16rpx;
          border-radius: 20rpx;

          &.tag-green { background: #f6ffed; color: #52c41a; }
          &.tag-orange { background: #fffbe6; color: #faad14; }
          &.tag-red { background: #fff2f0; color: #ff4d4f; }
        }
      }

      .meta {
        font-size: 26rpx;
        color: #8c8c8c;
        display: block;
        margin-bottom: 8rpx;
      }

      .contact-row {
        display: flex;
        align-items: center;

        .contact-icon {
          font-size: 22rpx;
          margin-right: 6rpx;
          color: #bfbfbf;
        }

        .contact-text {
          font-size: 24rpx;
          color: #bfbfbf;
        }
      }
    }
  }

  .card-actions {
    display: flex;
    align-items: center;
    border-top: 1rpx solid #f5f5f5;

    .act-btn {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 24rpx 0;

      &:active {
        background: #fafafa;
      }

      .act-icon {
        font-size: 28rpx;
        margin-right: 8rpx;
      }

      .act-label {
        font-size: 26rpx;
      }

      &.act-edit {
        color: #595959;

        .act-icon {
          color: #667eea;
        }
      }

      &.act-del {
        color: #595959;

        .act-icon {
          color: #ff4d4f;
        }
      }
    }

    .act-divider {
      width: 1rpx;
      height: 32rpx;
      background: #f0f0f0;
    }
  }
}

.empty-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 160rpx 60rpx 0;

  .empty-illust {
    width: 200rpx;
    height: 200rpx;
    border-radius: 50%;
    background: linear-gradient(135deg, #e8eaf6, #ede7f6);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 40rpx;

    .empty-emoji {
      font-size: 80rpx;
    }
  }

  .empty-title {
    font-size: 34rpx;
    font-weight: 600;
    color: #262626;
    margin-bottom: 12rpx;
  }

  .empty-desc {
    font-size: 26rpx;
    color: #bfbfbf;
    text-align: center;
  }
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 32rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.04);

  .add-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 96rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 48rpx;

    &:active {
      opacity: 0.85;
    }

    .add-icon {
      font-size: 40rpx;
      color: #fff;
      margin-right: 8rpx;
      font-weight: 300;
    }

    .add-text {
      font-size: 32rpx;
      color: #fff;
      font-weight: 500;
    }
  }
}

.mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 99;
}

.popup {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background: #fff;
  border-radius: 32rpx 32rpx 0 0;
  z-index: 100;
  transform: translateY(100%);
  transition: transform 0.3s ease;

  &.show {
    transform: translateY(0);
  }

  .popup-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 36rpx 40rpx 0;

    .popup-title {
      font-size: 36rpx;
      font-weight: 700;
      color: #1a1a1a;
    }

    .popup-close {
      width: 56rpx;
      height: 56rpx;
      border-radius: 50%;
      background: #f5f5f5;
      display: flex;
      align-items: center;
      justify-content: center;

      &:active {
        background: #eee;
      }

      .close-x {
        font-size: 28rpx;
        color: #8c8c8c;
      }
    }
  }

  .popup-body {
    padding: 32rpx 40rpx;
    max-height: 60vh;
  }

  .field {
    margin-bottom: 32rpx;

    .field-label {
      font-size: 28rpx;
      color: #595959;
      margin-bottom: 16rpx;
      display: block;
      font-weight: 500;

      .required {
        color: #ff4d4f;
      }
    }

    .field-input-wrap {
      background: #f7f8fa;
      border-radius: 16rpx;
      padding: 0 24rpx;

      .field-input {
        width: 100%;
        height: 88rpx;
        font-size: 30rpx;
        color: #1a1a1a;
      }
    }
  }

  .popup-foot {
    padding: 0 40rpx 40rpx;
    padding-bottom: calc(40rpx + env(safe-area-inset-bottom));

    .submit-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 96rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 48rpx;

      &:active {
        opacity: 0.85;
      }

      .submit-text {
        font-size: 32rpx;
        color: #fff;
        font-weight: 500;
      }
    }
  }
}
</style>
