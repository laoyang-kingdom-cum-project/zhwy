<template>
  <!-- template模板部分：渲染图表容器div -->
  <!-- :class="className" 绑定父组件传入的CSS类名，默认值为'chart' -->
  <!-- :style="{height:height,width:width}" 设置容器的高度和宽度样式 -->
  <!-- height和width由父组件传入，默认高度300px，宽度100% -->
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
// script脚本部分：定义Vue组件逻辑
// 引入echarts图表库，用于渲染雷达图等可视化图表
import * as echarts from 'echarts'
// 引入echarts的macarons主题样式，使图表呈现马卡龙配色风格
require('echarts/theme/macarons') // echarts theme
// 引入resize混合器，用于监听窗口resize事件实现图表响应式
import resize from './mixins/resize'

// 定义雷达图动画时长的常量，单位毫秒
// 3000ms的动画时长让图表数据展示更加平滑自然
const animationDuration = 3000

// 导出Vue组件定义对象
export default {
  // mixins数组：注册resize混合器，获取窗口resize能力
  mixins: [resize],
  // props对象：定义父组件向此组件传递的属性
  props: {
    // className属性：图表容器的CSS类名
    className: {
      // type: String 指定属性类型为字符串
      type: String,
      // default: 'chart' 设置默认值，如果父组件不传则使用'chart'
      default: 'chart'
    }, // 结束className属性定义
    // width属性：图表容器的宽度
    width: {
      type: String,
      // 默认为100%宽度，会占满父容器宽度
      default: '100%'
    }, // 结束width属性定义
    // height属性：图表容器的高度
    height: {
      type: String,
      // 默认为300px高度
      default: '300px'
    } // 结束height属性定义
  }, // 结束props对象
  // data函数：定义组件的响应式数据
  data() {
    return {
      // chart属性：存储echarts实例对象
      // 初始化为null，在initChart方法中会被赋值为echarts实例
      chart: null
    } // 结束data返回值对象
  }, // 结束data函数
  // mounted生命周期钩子：组件挂载到DOM后执行
  mounted() {
    // this.$nextTick确保DOM已经渲染完成后再执行图表初始化
    // 传入一个回调函数，在nextTick后执行
    this.$nextTick(() => {
      // 调用initChart方法初始化echarts雷达图
      this.initChart()
    }) // 结束$nextTick回调
  }, // 结束mounted钩子
  // beforeDestroy生命周期钩子：组件销毁前执行，用于清理资源
  beforeDestroy() {
    // 如果chart实例不存在（可能从未初始化），直接返回不做清理
    if (!this.chart) {
      return
    } // 结束if判断
    // dispose()方法：销毁echarts实例，释放内存
    // 调用后图表将不再可用，需要重新initChart才能再次渲染
    this.chart.dispose()
    // 将chart引用置为null，帮助垃圾回收
    this.chart = null
  }, // 结束beforeDestroy钩子
  // methods对象：定义组件的所有方法
  methods: {
    // initChart方法：初始化echarts雷达图的核心方法
    initChart() {
      // echarts.init()：初始化echarts实例
      // 第一个参数：DOM元素，使用this.$el获取当前组件的根元素
      // 第二个参数：使用的theme名称，这里使用'macarons'马卡龙主题
      this.chart = echarts.init(this.$el, 'macarons')

      // chart.setOption()：设置图表的配置项和数据
      // 传入一个配置对象，定义图表的各种属性
      this.chart.setOption({
        // tooltip：提示框配置，控制鼠标悬停时的提示信息
        tooltip: {
          // trigger：触发类型，'axis'表示坐标轴触发
          // 当鼠标移动到坐标轴（雷达图的轴线）时显示提示
          trigger: 'axis',
          // axisPointer：坐标轴指示器配置
          // axisPointer用于指定鼠标悬停时显示的指示器类型
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            // type：指示器类型，'shadow'表示阴影指示器（面积区域）
            // 默认为直线，可选为：'line' | 'shadow'
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          } // 结束axisPointer配置
        }, // 结束tooltip配置
        // radar：雷达图坐标系配置
        radar: {
          // radius：雷达图半径，可以是绝对值或百分比
          // '66%'表示雷达图直径占容器宽度的66%
          radius: '66%',
          // center：雷达图圆心坐标，数组格式[水平偏移, 垂直偏移]
          // ['50%', '42%']表示圆心在容器的水平50%、垂直42%位置
          center: ['50%', '42%'],
          // splitNumber：雷达图分隔区域的数量，默认为8个
          // 将雷达图区域分成8个同心环状区域
          splitNumber: 8,
          // splitArea：分隔区域样式配置
          splitArea: {
            // areaStyle：区域填充样式
            areaStyle: {
              // color：区域填充颜色，支持RGBA格式设置透明度
              // rgba(127,95,132,.3)表示紫红色，透明度0.3
              color: 'rgba(127,95,132,.3)',
              // opacity：区域填充的不透明度，1表示完全不透
              opacity: 1,
              // shadowBlur：阴影模糊半径，值越大阴影越模糊
              shadowBlur: 45,
              // shadowColor：阴影颜色
              shadowColor: 'rgba(0,0,0,.5)',
              // shadowOffsetX：阴影X轴偏移量
              shadowOffsetX: 0,
              // shadowOffsetY：阴影Y轴偏移量
              shadowOffsetY: 15
            } // 结束areaStyle配置
          }, // 结束splitArea配置
          // indicator：雷达图指示器配置，定义每个维度的名称和最大值
          // indicator数组中的每个对象代表雷达图的一个轴
          indicator: [
            // name：维度名称，max：该维度的最大值
            // Sales销售维度，最大值10000
            { name: 'Sales', max: 10000 },
            // Administration行政维度，最大值20000
            { name: 'Administration', max: 20000 },
            // Information Techology信息技术维度，最大值20000
            { name: 'Information Techology', max: 20000 },
            // Customer Support客户支持维度，最大值20000
            { name: 'Customer Support', max: 20000 },
            // Development开发维度，最大值20000
            { name: 'Development', max: 20000 },
            // Marketing市场营销维度，最大值20000
            { name: 'Marketing', max: 20000 }
          ] // 结束indicator数组
        }, // 结束radar配置
        // legend：图例配置，用于显示系列名称和切换系列显示状态
        legend: {
          // left：图例水平位置，'center'表示居中显示
          left: 'center',
          // bottom：图例距离容器底部的距离
          bottom: '10',
          // data：图例数据数组，包含要显示的系列名称
          data: ['Allocated Budget', 'Expected Spending', 'Actual Spending']
        }, // 结束legend配置
        // series：系列配置数组，定义图表的数据和类型
        series: [{
          // type：图表类型，'radar'表示雷达图
          type: 'radar',
          // symbolSize：标记点大小，0表示不显示标记点
          // 这里设为0是因为雷达图主要关注面积区域
          symbolSize: 0,
          // areaStyle：区域填充样式配置
          areaStyle: {
            // normal：正常状态下的样式
            normal: {
              // shadowBlur：阴影模糊半径
              shadowBlur: 13,
              // shadowColor：阴影颜色
              shadowColor: 'rgba(0,0,0,.2)',
              // shadowOffsetX：阴影X轴偏移
              shadowOffsetX: 0,
              // shadowOffsetY：阴影Y轴偏移
              shadowOffsetY: 10,
              // opacity：区域填充不透明度
              opacity: 1
            } // 结束normal配置
          }, // 结束areaStyle配置
          // data：系列数据数组，每个对象代表一个雷达图系列
          data: [
            // 第一个系列：Allocated Budget（预算分配）
            {
              // value：数据值数组，顺序对应indicator中的维度
              // [销售额, 行政, IT, 客服, 开发, 市场]
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              // name：系列名称，用于图例显示和tooltip提示
              name: 'Allocated Budget'
            }, // 结束第一个数据对象
            // 第二个系列：Expected Spending（预期支出）
            {
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              name: 'Expected Spending'
            }, // 结束第二个数据对象
            // 第三个系列：Actual Spending（实际支出）
            {
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              name: 'Actual Spending'
            } // 结束第三个数据对象
          ], // 结束data数组
          // animationDuration：动画持续时间，使用之前定义的常量3000ms
          animationDuration: animationDuration
        }] // 结束series数组
      }) // 结束setOption方法调用
    } // 结束initChart方法定义
  } // 结束methods对象
} // 结束export default组件定义
</script>