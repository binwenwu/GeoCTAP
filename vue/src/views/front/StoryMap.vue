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

    option = {
      backgroundColor: '#000',
      globe: {
        baseTexture: ROOT_PATH + '/data-gl/asset/world.topo.bathy.200401.jpg',
        heightTexture: ROOT_PATH + '/data-gl/asset/bathymetry_bw_composite_4k.jpg',
        displacementScale: 0.2,
        shading: 'realistic',
        environment: ROOT_PATH + '/data-gl/asset/starfield.jpg',
        realisticMaterial: {
          roughness: ROOT_PATH + '/asset/get/s/data-1497599804873-H1SHkG-mZ.jpg',
          metalness: ROOT_PATH + '/asset/get/s/data-1497599800643-BJbHyGWQW.jpg',
          textureTiling: [8, 4]
        },
        postEffect: {
          enable: true
        },
        viewControl: {
          autoRotate: false
        },
        light: {
          main: {
            intensity: 2,
            shadow: true
          },
          ambientCubemap: {
            texture: ROOT_PATH + '/data-gl/asset/pisa.hdr',
            exposure: 2,
            diffuseIntensity: 2,
            specularIntensity: 2
          }
        }
      }
    };

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
