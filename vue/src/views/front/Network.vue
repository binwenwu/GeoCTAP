<template>
  <el-card class="box-card" shadow="never">


    <div id="earth" style="width: 100%;height: 800px;"></div>


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
      optionSelect: 1,
      option1: {}, // 基本版
      option2: {}, // 云层效果
      option3: {}, // 岩石效果
    }
  },
  mounted() {

    this.initOptions()// 初始化option
    this.initChart(this.optionSelect)// 初始化chart

  },
  methods: {
    initOptions() {
      this.option1 = {
        backgroundColor: '#000',
        globe: {
          baseTexture: world_topo_bathy_200401, // 地球纹理
          heightTexture: world_topo_bathy_200401, // 地球纹理
          displacementScale: 0.04,// 地球凹凸程度
          shading: 'realistic',// 着色
          environment: starfield,// 星空纹理
          realisticMaterial: {
            roughness: 0.9// 粗糙度
          },
          postEffect: {
            enable: true// 后期特效
          },
          viewControl: {
            autoRotate: false,// 自动旋转
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
          },
          light: {
            main: {
              intensity: 5,// 光照强度
              shadow: true // 是否显示阴影
            },
            ambientCubemap: {
              //texture: 'https://echarts.apache.org/examples/data-gl/asset/pisa.hdr',
              texture: '../../assets/echarts3D/pisa.hdr',// 环境光纹理
              diffuseIntensity: 0.2 // 漫反射强度
            }
          }
        },
        series: [
          {
            name: "lines3D",
            type: "lines3D",
            coordinateSystem: "globe",
            effect: {
              show: true,
            },
            blendMode: "lighter",
            lineStyle: {
              width: 2,
            },
            data: [],
            silent: false,
          },
        ]
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
        series: [
          {
            name: "lines3D",
            type: "lines3D",
            coordinateSystem: "globe",
            effect: {
              show: true,
            },
            blendMode: "lighter",
            lineStyle: {
              width: 2,
            },
            data: [],
            silent: false,
          },
        ]
      };
      this.option3 = {
        globe: {
          displacementTexture: bathymetry_bw_composite_4k,
          displacementScale: 0.1,
          displacementQuality: 'ultra',
          shading: 'realistic',
          realisticMaterial: {
            roughness: 0.8,
            metalness: 0
          },
          postEffect: {
            enable: true,
            SSAO: {
              enable: true,
              radius: 2,
              intensity: 1.5,
              quality: 'high'
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
              intensity: 1,
              shadow: true
            },
            ambientCubemap: {
              texture: '../../assets/echarts3D/pisa.hdr',// 环境光纹理
              exposure: 1,
              diffuseIntensity: 0.2
            }
          },
          viewControl: {
            autoRotate: false,// 自动旋转
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
          },
          debug: {
            wireframe: {
              show: true
            }
          }
        },
        series: [
          {
            name: "lines3D",
            type: "lines3D",
            coordinateSystem: "globe",
            effect: {
              show: true,
            },
            blendMode: "lighter",
            lineStyle: {
              width: 2,
            },
            data: [],
            silent: false,
          },
        ]
      };
    },

    initChart(optionSelect) {
      var dom = document.getElementById('earth');
      var myChart = this.$echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
      });

      // 判断optionSelect 为1或2或3

      var option

      switch (optionSelect) {
        case 1:
          option = this.option1;
          break;
        case 2:
          option = this.option2;
          break;
        case 3:
          option = this.option3;
          break;
        default:
          option = this.option1;
          break;
      }


      if (option && typeof option === 'object') {
        // 随机数据 i控制线数量
        for (let i = 0; i < 30; i++) {
          option.series[0].data = option.series[0].data.concat(this.randomData());
        }
        myChart.setOption(option);
      }
      window.addEventListener('resize', myChart.resize);
    },

    // 随机生成起始及终点经纬度坐标
    randomData() {
      let name = "随机点" + Math.random().toFixed(5) * 100000;
      // 起点经纬度-北京
      let longitude = 116.2,
          latitude = 39.56;
      // 随机终点经纬度
      let longitude2 = Math.random() * 360 - 180;
      let latitude2 = Math.random() * 180 - 90;
      return {
        coords: [
          [longitude, latitude],
          [longitude2, latitude2],
        ],
        value: (Math.random() * 3000).toFixed(2),
      };
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
