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
        placeholder="拐出地"
        v-model="disPlace"
        clearable>
    </el-input>
    <el-button type="primary" plain @click="queryByPosition1(disPlace)">搜索</el-button>

    <el-input
        style="width: 10%"
        placeholder="拐入地"
        v-model="nowPlace"
        clearable>
    </el-input>
    <el-button type="primary" plain @click="queryByPosition2(nowPlace)">搜索</el-button>

    <div id="gender" ref="gender"
         style="width: 500px;height: 400px;opacity: 1;position: absolute; margin-top: 300px;z-index: 999"></div>






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
  night,
  jsonData
} from '../../store/echarts3D.js'


var ROOT_PATH = 'https://echarts.apache.org/examples';

export default {
  name: 'Network',
  data() {
    return {
      optionSelect: "1",
      option1: {}, // 基本版
      option2: {}, // 云层效果
      option3: {}, // 岩石效果
      option4: {}, // 人口密度
      option5: {}, // 金属质感
      options: [{
        value: '1',
        label: '基本版'
      }, {
        value: '2',
        label: '云层效果'
      }, {
        value: '3',
        label: '岩石效果'
      }, {
        value: '4',
        label: '人口密度'
      },{
        value: '5',
        label: '金属质感'
      }],
      value: '',
      children: [], // 儿童信息
      id: 1, // 儿童id
      tracks: [], // 轨迹
      disPlace: '', // 拐出地
      nowPlace: '', // 拐入地
      genderPercent: [], // 性别比例
      genderDOM: null, // 性别比例DOM
      population: [], // 人口密度
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
      this.genderPercent = []
      this.request.get("children/front/randomSelect2", {
        params: {
          id: this.id
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data

        // 性别比例统计
        var male = 0 // 男性
        var female = 0   // 女性

        // 将数据转换成轨迹
        for (var i = 0; i < this.children.length; i++) {
          var track = []  // 轨迹
          var dis = [] // 失踪地点
          var now = [] // 当前地点
          track.push([this.children[i].disLng, this.children[i].disLat],[this.children[i].nowLng, this.children[i].nowLat])
          this.tracks.push(track)

          if (this.children[i].sex === '男') {
            male += 1
          } else {
            female += 1
          }
        }
        this.genderPercent.push((male / this.children.length * 100).toFixed(2), (100 - male / this.children.length * 100).toFixed(2))
        this.initChart(this.optionSelect)// 初始化chart
      })
    },
    randomID() {
      //产生一个随机数，范围是1-39311
      this.id = Math.floor(Math.random() * 39311 + 1)
    },
    randomTracks(){
       // 从浏览器session中取出optionSelect
      try {
        this.optionSelect = localStorage.getItem("optionSelect");

      } catch (e) {
        console.error("localStorage.getItem()报错, ", e.name, ": ", e.message);
      }

      this.randomQueryByID() // 随机查询500个轨迹
      this.initOptions()// 初始化option
    },
    queryByPosition1(disPlace) {
      this.tracks = []
      this.genderPercent = []
      this.request.get("children/front/queryByPosition1", {
        params: {
          disPlace: this.disPlace
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data
        // 性别比例统计
        var male = 0 // 男性
        var female = 0   // 女性
        // 将数据转换成轨迹
        for (var i = 0; i < this.children.length; i++) {
          var track = []  // 轨迹
          var dis = [] // 失踪地点
          var now = [] // 当前地点
          track.push([this.children[i].disLng, this.children[i].disLat], [this.children[i].nowLng, this.children[i].nowLat])
          this.tracks.push(track)

          if (this.children[i].sex == '男') {
            male += 1
          } else {
            female += 1
          }
        }

        this.genderPercent.push((male / this.children.length * 100).toFixed(2), (100 - male / this.children.length * 100).toFixed(2))
        this.initChart(this.optionSelect)// 初始化chart

      })
    },
    queryByPosition2(nowPlace) {
      this.tracks = []
      this.genderPercent = []
      this.request.get("children/front/queryByPosition2", {
        params: {
          nowPlace: this.nowPlace
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data
        // 性别比例统计
        var male = 0 // 男性
        var female = 0   // 女性
        // 将数据转换成轨迹
        for (var i = 0; i < this.children.length; i++) {
          var track = []  // 轨迹
          var dis = [] // 失踪地点
          var now = [] // 当前地点
          track.push([this.children[i].disLng, this.children[i].disLat], [this.children[i].nowLng, this.children[i].nowLat])
          this.tracks.push(track)

          if (this.children[i].sex == '男') {
            male += 1
          } else {
            female += 1
          }
        }
        this.genderPercent = []
        this.genderPercent.push((male / this.children.length * 100).toFixed(2), (100 - male / this.children.length * 100).toFixed(2))
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
            autoRotate: false,
            beta: 180,
            alpha: 20,
            distance: 100,
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
            beta: 180,
            alpha: 20,
            distance: 100,
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
            autoRotate: false,
            beta: 180,
            alpha: 20,
            distance: 100,
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
      var data = jsonData
          .filter(function (dataItem) {
            return dataItem[2] > 0;
          })
          .map(function (dataItem) {
            return [dataItem[0], dataItem[1], Math.sqrt(dataItem[2])];
          });
      this.option4 = {
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
            distance: 100,
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
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
          {
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
        ]
      };
      this.option5 = {
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
            autoRotate: false,
            beta: 180,
            alpha: 20,
            distance: 100,
            targetCoord: [104.0, 37.5] // 设置相机视角位于中国的经纬度坐标
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
        case "4":
          option = this.option4;
          break;
        case "5":
          option = this.option5;
          break;
        default:
          option = this.option1;
          break;
      }


      if (option && typeof option === 'object'&& optionSelect=='1') {
        option.series[0].data = []
        option.series[0].data = option.series[0].data.concat(this.tracks);

        myChart.setOption(option);
      }else{
        option.series[0].data = []
        option.series[0].data = option.series[0].data.concat(this.tracks);
        myChart.clear();
        myChart.setOption(option);
      }

      this.genderTable()  // 性别统计饼图


      window.addEventListener('resize', myChart.resize);
    },

    // 更改地球模型
    changeModel(value) {

      // 将value 存入浏览器session
      localStorage.setItem("optionSelect", value);
      // 刷新页面
      location.reload();
      this.optionSelect = value;
      this.initChart(this.optionSelect);
    },
    // 性別统计饼图
    genderTable() {
      var gender = document.getElementById('gender');


      var genderChart = this.$echarts.init(gender);

      const typeList = ['男', "女"]; // 类型


      var genderPercent = this.genderPercent


      /* 数据处理 */
      let data = [];
      // 空格数据
      const sum = genderPercent.reduce((per, cur) => per + cur, 0);
      const gap = (1 * sum) / 100;
      const gapData = {
        name: '空格',
        value: gap,
        itemStyle: {
          color: 'transparent',
        },
      };
      // 颜色系列
      const colorList = [{
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
          offset: 0,
          color: 'rgba(0, 178, 255, 1)' // 0% 处的颜色
        }, {
          offset: 1,
          color: 'rgba(0, 114, 255, 1)' // 100% 处的颜色
        }],
      }, {
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
          offset: 0,
          color: '#f00072' // 0% 处的颜色
        }, {
          offset: 1,
          color: '#f06292' // 100% 处的颜色
        }],
      }];
      // 循环添加数据
      for (let i = 0; i < typeList.length; i++) {
        data.push({
          name: typeList[i],
          value: genderPercent[i],
          itemStyle: {
            borderRadius: "50%",
            color: colorList[i]
          },
        });
        data.push(gapData)
      }

      var option = {
        backgroundColor: "#010c20",
        legend: {
          type: "scroll",
          orient: 'vertical',
          height: '80%',
          left: '70%',
          top: 'center',
          icon: "roundRect", //设置为圆，删除则为矩形
          itemWidth: 2,
          itemHeight: 16,
          itemGap: 25,
          data: typeList,
          formatter: function (name) {
            for (let i = 0; i < typeList.length; i++) {
              if (name == typeList[i]) {
                return `{name|${name}}{value${i}|${genderPercent[i]}%}`
              }
            }
          },
          textStyle: {
            rich: {
              name: {
                fontSize: 14,
                fontWeight: 400,
                //width: 100,
                height: 16,
                verticalAlign: "top",
                padding: [4, 0, 0, 4],
                color: '#D1E5FF',
                fontFamily: 'Source Han Sans CN-Regular',
              },
              value0: {
                fontSize: 20,
                fontWeight: 500,
                height: 16,
                width: 50,
                align: 'left',
                verticalAlign: "top",
                padding: [5, 0, 0, 16],
                color: "#0090ff",
                fontFamily: 'PangMenZhengDao',
              },
              value1: {
                fontSize: 20,
                fontWeight: 500,
                height: 16,
                width: 50,
                align: 'left',
                verticalAlign: "top",
                padding: [5, 0, 0, 16],
                color: "#f06292",
                fontFamily: 'PangMenZhengDao',
              },
            }
          }
        },
        series: [{
          type: 'pie',
          radius: ['43%', '55%'],
          center: ["35%", "50%"],
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          data: data
        }]
      };
      genderChart.setOption(option);
    },


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
