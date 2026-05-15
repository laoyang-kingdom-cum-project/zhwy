<template>
  <!-- 图表容器：动态绑定类名和样式 -->
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
// 导入echarts库
import * as echarts from 'echarts'
// 导入macarons主题
require('echarts/theme/macarons')
// 导入resize混入
import resize from './mixins/resize'

// 雷达图动画时长常量
const animationDuration = 3000

// 导出组件
export default {
  // 混入resize功能
  mixins: [resize],
  // 定义props
  props: {
    // 图表容器CSS类名
    className: {
      // 类型为字符串
      type: String,
      // 默认值为chart
      default: 'chart'
    },
    // 图表容器宽度
    width: {
      // 类型为字符串
      type: String,
      // 默认值为100%
      default: '100%'
    },
    // 图表容器高度
    height: {
      // 类型为字符串
      type: String,
      // 默认值为300px
      default: '300px'
    }
  },
  // 数据定义
  data() {
    // 返回数据对象
    return {
      // 图表实例
      chart: null
    }
  },
  // 组件挂载后
  mounted() {
    // 在下一个tick初始化图表
    this.$nextTick(() => {
      // 调用初始化方法
      this.initChart()
    })
  },
  // 组件销毁前
  beforeDestroy() {
    // 判断图表是否存在
    if (!this.chart) {
      // 不存在则返回
      return
    }
    // 销毁图表实例
    this.chart.dispose()
    // 清空图表引用
    this.chart = null
  },
  // 方法定义
  methods: {
    // 初始化雷达图
    initChart() {
      // 初始化echarts实例
      this.chart = echarts.init(this.$el, 'macarons')

      // 设置图表配置项
      this.chart.setOption({
        // 配置提示框
        tooltip: {
          // 触发类型为坐标轴
          trigger: 'axis',
          // 坐标轴指示器配置
          axisPointer: {
            // 类型为阴影
            type: 'shadow'
          }
        },
        // 配置雷达坐标系
        radar: {
          // 雷达图半径
          radius: '66%',
          // 雷达图中心位置
          center: ['50%', '42%'],
          // 分割段数
          splitNumber: 8,
          // 分割区域样式
          splitArea: {
            // 区域样式
            areaStyle: {
              // 填充颜色
              color: 'rgba(127,95,132,.3)',
              // 不透明度
              opacity: 1,
              // 阴影模糊度
              shadowBlur: 45,
              // 阴影颜色
              shadowColor: 'rgba(0,0,0,.5)',
              // 阴影水平偏移
              shadowOffsetX: 0,
              // 阴影垂直偏移
              shadowOffsetY: 15
            }
          },
          // 配置各维度指示器
          indicator: [
            // 销售维度
            { name: 'Sales', max: 10000 },
            // 行政维度
            { name: 'Administration', max: 20000 },
            // IT维度
            { name: 'Information Techology', max: 20000 },
            // 客服维度
            { name: 'Customer Support', max: 20000 },
            // 开发维度
            { name: 'Development', max: 20000 },
            // 市场维度
            { name: 'Marketing', max: 20000 }
          ]
        },
        // 配置图例
        legend: {
          // 水平居中
          left: 'center',
          // 底部距离
          bottom: '10',
          // 图例数据
          data: ['Allocated Budget', 'Expected Spending', 'Actual Spending']
        },
        // 配置系列数据
        series: [{
          // 图表类型为雷达图
          type: 'radar',
          // 标记点大小为0
          symbolSize: 0,
          // 区域样式配置
          areaStyle: {
            // 正常状态
            normal: {
              // 阴影模糊度
              shadowBlur: 13,
              // 阴影颜色
              shadowColor: 'rgba(0,0,0,.2)',
              // 阴影水平偏移
              shadowOffsetX: 0,
              // 阴影垂直偏移
              shadowOffsetY: 10,
              // 不透明度
              opacity: 1
            }
          },
          // 数据数组
          data: [
            // 预算分配数据
            {
              // 数值数组
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              // 系列名称
              name: 'Allocated Budget'
            },
            // 预期支出数据
            {
              // 数值数组
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              // 系列名称
              name: 'Expected Spending'
            },
            // 实际支出数据
            {
              // 数值数组
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              // 系列名称
              name: 'Actual Spending'
            }
          ],
          // 动画时长
          animationDuration: animationDuration
        }]
      })
    }
  }
}
</script>
