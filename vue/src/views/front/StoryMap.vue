<template>
  <el-card class="box-card" shadow="never">
    <div id="main" style="width: 100%;height: 80vh;opacity: 1;"></div>
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

import * as echarts from "echarts";

var ROOT_PATH = 'https://echarts.apache.org/examples';
import $ from 'jquery' // 引入jQuery
import axios from 'axios';
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

import jsonData from '@/assets/echarts3D/population.json';

export default {
  name: 'HistoryMap',
  components: {},
  data() {
    return {}
  },
  created() {
  },
  computed: {},
  methods: {},
  mounted() {
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;


    // 请求成功，获取到JSON数据
    var data = jsonData;
    data = data
        .filter(function (dataItem) {
          return dataItem[2] > 0;
        })
        .map(function (dataItem) {
          return [dataItem[0], dataItem[1], Math.sqrt(dataItem[2])];
        });
    myChart.setOption({
      visualMap: {
        show: false,
        min: 0,
        max: 60,
        inRange: {
          symbolSize: [1.0, 10.0]
        }
      },
      globe: {
        environment: starfield,
        heightTexture: bathymetry_bw_composite_4k,
        displacementScale: 0.05,
        displacementQuality: 'high',
        globeOuterRadius: 100,
        baseColor: '#000',
        shading: 'realistic',
        realisticMaterial: {
          roughness: 0.2,
          metalness: 0
        },
        postEffect: {
          enable: true,
          depthOfField: {
            focalRange: 15,
            enable: true,
            focalDistance: 100
          }
        },
        temporalSuperSampling: {
          enable: true
        },
        light: {
          ambient: {
            intensity: 0
          },
          main: {
            intensity: 0.1,
            shadow: false
          },
          ambientCubemap: {
            texture: '../../assets/echarts3D/lake.hdr',
            exposure: 1,
            diffuseIntensity: 0.5,
            specularIntensity: 2
          }
        },
        viewControl: {
          autoRotate: false,
          beta: 180,
          alpha: 20,
          distance: 100
        }
      },
      series: {
        type: 'scatter3D',
        coordinateSystem: 'globe',
        blendMode: 'lighter',
        symbolSize: 2,
        itemStyle: {
          color: 'rgb(50, 50, 150)',
          opacity: 1
        },
        data: data
      }
    });



    option && myChart.setOption(option);
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
