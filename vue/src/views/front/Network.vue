<template>
  <el-card class="box-card" shadow="never">

    <div id="container" style="width: 100%;height: 800px"></div>

  </el-card>
</template>

<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts@5.4.2/dist/echarts.min.js"></script>

<script type="text/javascript"
        src="https://fastly.jsdelivr.net/npm/echarts@5.4.2/dist/extension/dataTool.min.js"></script>


<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts-gl@2/dist/echarts-gl.min.js"></script>


<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts-stat@latest/dist/ecStat.min.js"></script>

<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts@4.9.0/map/js/china.js"></script>
<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts@4.9.0/map/js/world.js"></script>

<script type="text/javascript" src="https://api.map.baidu.com/api?v=3.0&ak=204FkdkayE0yU16k2OhHbRKFIDP2EfMO"></script>
<script type="text/javascript" src="https://fastly.jsdelivr.net/npm/echarts@5.4.2/dist/extension/bmap.min.js"></script>



<script>
import $ from 'jquery' // 引入jQuery

import 'echarts-gl' // 引入echarts-gl

// 引入echart3D地球纹理（base64格式）
import {
  world_topo_bathy_200401,
  bathymetry_bw_composite_4k,
  starfield,
  clouds,
  earth,
  night
} from '../../store/echarts3D.js'





export default {
  name: 'Network',
  data() {
    return {
      optionSelect: 2,
      option1: {},
      option2: {},
    }
  },
  mounted() {

    this.initOptions()
    this.initChart(this.optionSelect)

  },
  methods: {
    initOptions() {
       this.option1 = {
        backgroundColor: '#000',
        globe: {
          baseTexture: world_topo_bathy_200401,
          heightTexture: world_topo_bathy_200401,
          displacementScale: 0.04,
          shading: 'realistic',
          environment: starfield,
          realisticMaterial: {
            roughness: 0.9
          },
          postEffect: {
            enable: true
          },
          viewControl: {
            autoRotate: false,
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
          },
          light: {
            main: {
              intensity: 5,
              shadow: true
            },
            ambientCubemap: {
              //texture: 'https://gitee.com/enjoy1234/git-learning/raw/master/echarts3D/pisa.hdr',
              texture: '../../assets/echarts3D/pisa.hdr',
              diffuseIntensity: 0.2
            }
          }
        }
      };

      this.option2 = {
        backgroundColor: '#000',
        globe: {
          baseTexture: earth,
          heightTexture: bathymetry_bw_composite_4k,
          displacementScale: 0.1,
          shading: 'lambert',
          environment: starfield,
          light: {
            ambient: {
              intensity: 0.1
            },
            main: {
              intensity: 1.5
            }
          },
          viewControl: {
            autoRotate: false,
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
          },

          layers: [
            {
              type: 'blend',
              blendTo: 'emission',
              texture: night
            },
            {
              type: 'overlay',
              texture: clouds,
              shading: 'lambert',
              distance: 5
            }
          ]
        },
        series: []
      };
    },

    initChart(optionSelect){
      var dom = document.getElementById('container');
      var myChart = this.$echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
      });

      var option = optionSelect == 1 ? this.option1 : this.option2;
      if (option && typeof option === 'object') {
        myChart.setOption(option);
      }
      window.addEventListener('resize', myChart.resize);
    }
  }


}
</script>


<style scoped>
.box-card {
  background-color: transparent;
  height: 100%;
  width: 100%;
  border: none;
}


</style>
