<template>
  <el-card class="box-card" shadow="never">
<!--    <el-select v-model="value" placeholder="地球纹理" :popper-append-to-body="false" @change="changeModel(value)" class="navi-a">-->
<!--      <el-option-->
<!--          v-for="item in options"-->
<!--          :key="item.value"-->
<!--          :label="item.label"-->
<!--          :value="item.value">-->
<!--      </el-option>-->
<!--    </el-select>-->


    <el-select v-model="value" placeholder="地球纹理" :popper-append-to-body="false" @change="changeModel(value)">
      <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
      </el-option>
    </el-select>

    <el-button type="primary" plain @click="randomTracks">刷新轨迹</el-button>
    <el-input
        style="width: 10%"
        placeholder="请输入地点"
        v-model="place"
        clearable>
    </el-input>
    <el-button type="primary" plain @click="queryByPosition(place)">搜索</el-button>






    <div id="earth" style="width: 100%;height: 80vh;opacity: 1;"></div>
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
      optionSelect: "1",
      option1: {}, // 基本版
      option2: {}, // 云层效果
      option3: {}, // 岩石效果
      options: [{
        value: '1',
        label: '基本版'
      }, {
        value: '2',
        label: '云层效果'
      }, {
        value: '3',
        label: '岩石效果'
      },],
      value: '',
      children: [], // 儿童信息
      id: 1, // 儿童id
      tracks: [], // 轨迹
      place: '', // 输入框
    }
  },
  created(){

  },
  mounted() {
    this.randomTracks()
  },
  methods: {
    randomQueryByID() {
      this.randomID()
      this.tracks = []
      this.request.get("children/front/randomSelect2", {
        params: {
          id: this.id
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data
        // 将数据转换成轨迹
        for (var i = 0; i < this.children.length; i++) {
          var track = []  // 轨迹
          var dis = [] // 失踪地点
          var now = [] // 当前地点
          track.push([this.children[i].disLng, this.children[i].disLat],[this.children[i].nowLng, this.children[i].nowLat])
          this.tracks.push(track)
        }
        this.initChart(this.optionSelect)// 初始化chart
      })
    },
    randomID() {
      //产生一个随机数，范围是1-39311
      this.id = Math.floor(Math.random() * 39311 + 1)
    },
    randomTracks(){
      this.randomQueryByID() // 随机查询500个轨迹
      this.initOptions()// 初始化option
    },
    queryByPosition(place) {
      this.tracks = []
      this.request.get("children/front/queryByPosition", {
        params: {
          place: this.place
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data
        console.log(this.children.length)
        // 将数据转换成轨迹
        for (var i = 0; i < this.children.length; i++) {
          var track = []  // 轨迹
          var dis = [] // 失踪地点
          var now = [] // 当前地点
          track.push([this.children[i].disLng, this.children[i].disLat],[this.children[i].nowLng, this.children[i].nowLat])
          this.tracks.push(track)
        }
        this.initChart(this.optionSelect)// 初始化chart
      })
    },
    initOptions() {
      this.option1 = {
        backgroundColor: '#000',
        globe: {
          baseTexture: world_topo_bathy_200401, // 地球纹理
          heightTexture: world_topo_bathy_200401, // 地球纹理
          displacementScale: 0.04,// 地球凹凸程度
          shading: 'lambert',// 着色
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
              period: 2, // 箭头指向速度，值越小速度越快
              trailWidth: 2,
              trailLength: 0.5,
              trailOpacity: 1,
              trailColor: '#0087f4'
            },
            blendMode: "lighter",
            lineStyle: {
              width: 1,
              color: '#0087f4',
              opacity: 0.4
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
          postEffect: {
            enable: true// 后期特效
          },
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
              period: 2, // 箭头指向速度，值越小速度越快
              trailWidth: 2,
              trailLength: 0.5,
              trailOpacity: 1,
              trailColor: '#0087f4'
            },
            blendMode: "lighter",
            lineStyle: {
              width: 1,
              color: '#0087f4',
              opacity: 0.4
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
          shading: 'lambert',
          environment: starfield,
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
              period: 2, // 箭头指向速度，值越小速度越快
              trailWidth: 2,
              trailLength: 0.5,
              trailOpacity: 1,
              trailColor: '#0087f4'
            },
            blendMode: "lighter",
            lineStyle: {
              width: 1,
              color: '#0087f4',
              opacity: 0.4
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
        case "1":
          option = this.option1;
          break;
        case "2":
          option = this.option2;
          break;
        case "3":
          option = this.option3;
          break;
        default:
          option = this.option1;
          break;
      }


      if (option && typeof option === 'object') {
        option.series[0].data = []
        option.series[0].data = option.series[0].data.concat(this.tracks);
        myChart.clear();
        myChart.setOption(option);
      }
      window.addEventListener('resize', myChart.resize);
    },

    // 更改地球模型
    changeModel(value) {

      this.optionSelect = value;
      this.initChart(this.optionSelect);
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
.el-select {
  position: absolute;
  top: 18vh;
  left: 3vw;
  z-index: 999;
}
.el-input,
::v-deep .el-input__inner,::v-deep .el-select-dropdown {
  background-color: transparent;
  padding: 1px;
  color: white;
}
::v-deep .el-select-dropdown__item{
  color: white;
  font-size: calc(100vw * 16 /1920);
}
::v-deep .el-select-dropdown__item.hover {
  background-color: transparent;
  opacity: 0.8;
}




</style>
