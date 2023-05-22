<template>
    <div id="app">
      <router-view/>
    </div>
</template>
<script>

import {serverIp} from "../public/config";
import ScaleBox from 'vue2-scale-box'

export default {
  name: 'App',
  components: {
    ScaleBox
  },
  data() {
    return {
      width: window.screen.width * window.devicePixelRatio,
      height: window.screen.height * window.devicePixelRatio,
    }
  },
  /*看板娘初始化
    参数说明
    model 模型的配置
       json 文件资源路径，可以支持网络路径，此处使用的是相对路径
       scale 模型缩放系数
    display 模型布局
       position 位置
       width 宽度
       height 高度
       hOffset 水平偏移量
       vOffset 垂直偏移量
    mobile 移动端配置
       show 显示/隐藏
       scale 缩放比例
    react 模型矩形框样式
       opacity 透明度
       opacityDefault 默认透明度
       opacityOnHover 鼠标悬浮透明度
  */
  created() {
    setTimeout(() => {
      window.L2Dwidget.init({
        pluginRootPath: '../live2dw/',
        pluginJsPath: 'lib/',
        pluginModelPath: 'live2d-widget-model-haru_1/assets/', //中间这个haru_2就是你的老婆,想换个老婆,换这个就可以了
        tagMode: false,
        debug: false,
        model: {jsonPath: '../live2dw/live2d-widget-model-haru_1/assets/haru01.model.json'},
        display: {position: 'right', width: 150, height: 320},  //调整老婆大小,和位置
        mobile: {show: true},   //要不要老婆盯着你的鼠标看
        log: false,
      })
    }, 500)
  }
}

</script>


