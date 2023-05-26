<template>
  <el-card class="box-card" shadow="never">
    <el-button class="view" @click="dialogVisible = true">查看经典案例故事</el-button>
    <el-dialog
      title="选择一个故事"
      :visible.sync="dialogVisible"
      width="15%"
      top="35vh"
      center>
      <el-select v-model="value" placeholder="请选择" @change="getOneData">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="visualStory">确 定</el-button>
      </span>
    </el-dialog>
    <div id="earth" style="width: 100%;height: 78.5vh;opacity: 1;"></div>
  </el-card>
</template>
<script>
import axios from 'axios'
import 'echarts-gl' // 引入echarts-gl

// 引入echart3D地球纹理（base64格式）
import {
  bathymetry_bw_composite_4k,
  starfield,
  clouds,
  earth,
  night
} from '../../store/echarts3D.js'
import { graphic } from 'echarts'

export default {
  name: 'StoryMap',
  data() {
    return {
      protagonist: '',
      myChart: {}, //整个图
      dialogVisible: false, //对话框
      relationshipsData: [], //关系数据
      reNameData: [], //单个关系名数据
      reLinkData: [], //单个关系链接数据
      examplesData: [], //所有数据
      exampleData: [], //单个案例数据
      seriesData: [], //单个案例点数据 仅地点名字和系列坐标数组
      lineData: [], //单个案例线数据
      dateData: [], //单个案例所有时间点
      eventData: [],//单个案例所有事件
      option: {}, // 云层效果
      options: [{ //选择器
        value: '0',
        label: '符建涛'
      }, {
        value: '1',
        label: '沈云贵'
      }, {
        value: '2',
        label: '杨妞花'
      }, {
        value: '3',
        label: '苗元胜'
      }, {
        value: '4',
        label: '小伟'
      }, {
        value: '5',
        label: '彭文乐'
      }, {
        value: '6',
        label: '孙卓'
      }],
      value: '' //选择键值
    }
  },
  mounted() {
    this.getJson()//获取数据
    this.initOptions()// 初始化option
    this.initChart()// 初始化chart
  },
  methods: {
    // 获取所有数据
    getJson() {
      // 获取public下案例json文件数据
      axios.get('../classicExamples.json').then(res => {
        this.examplesData = res.data;
        // console.log(this.examplesData.filter(item => item.example_id == 0))
      })
      // 获取public下关系json文件数据
      axios.get('../relationship.json').then(res => {
        this.relationshipsData = res.data;
        // console.log(this.examplesData.filter(item => item.example_id == 0))
      })
    },
    // 得到单个案例数据和点线数据
    getOneData() {
      // 单个案例数据
      this.exampleData = this.examplesData.filter(item => item.example_id == this.value);
      // 主人公名字
      this.protagonist = this.exampleData[0].child_name + '的故事';
      // 事件点数据
      this.seriesData = this.exampleData.map(({ location, longitude, latitude }) => ({ name: location, value: [parseFloat(longitude), parseFloat(latitude)] }));
      // 事件线数据
      this.lineData = this.exampleData.map(({ longitude, latitude }) => ([parseFloat(longitude), parseFloat(latitude)])).map((item, index, arr) => {
        const nextItem = arr[index + 1];
        return [item, nextItem, index];
      })
      // 清除尾部异常
      this.lineData.pop();
      // 格式转换
      this.lineData = this.lineData.map(item => {
        const coords = [
          item[0], item[1]
        ];
        const value = item[2]
        return {
          coords,
          value
        };
      });
      // 时间点数据
      this.dateData = this.exampleData.map(item => item.eventdate);
      // 事件数据
      this.eventData = this.exampleData.map(item => item.event)
      // 关系数据
      this.reNameData = this.relationshipsData[0][parseInt(this.value)];
      this.reLinkData = this.relationshipsData[1][parseInt(this.value)];
    },
    initOptions() {
      this.option = {
        // `baseOption` 的属性 timeline基本配置都写在baseOption 中
        timeline: {
          axisType: 'category',
          autoPlay: false,
          loop: false,
          playInterval: 2000,
          bottom: '5%',
          symbol: 'emptyCircle',
          lineStyle: {
            // color: '#2387c1'
          },
          label: {
            formatter: '{value}',
            color: 'white',
            fontWeight: 'bold'
          },
          data: []
        },
        graphic: {
          type: 'text',
          left: '5%',
          top: '40%',
          draggable: true,
          style: {

          },
        },
        globe: {
          baseTexture: earth,
          heightTexture: bathymetry_bw_composite_4k,
          displacementScale: 0.1,
          shading: 'realistic',
          environment: starfield,
          realisticMaterial: {
            roughness: 0.9// 粗糙度
          },
          postEffect: {
            enable: false,// 后期特效
          },
          light: {
            ambient: {
              intensity: 0.2
            },
            main: {
              intensity: 1,// 光照强度
              shadow: true, // 是否显示阴影
            }
          },
          viewControl: {
            //autoRotate: false,
            // targetCoord: [104.0, 37.5], // 设置相机视角位于中国的经纬度坐标
            // zoomSensitivity: 3,
            autoRotate: false,
            beta: 180,
            alpha: 20,
            distance: 100,
          },
          layers: [
            {
              type: 'blend',
              blendTo: 'emission',
              texture: night,
              intensity: 0.5
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
            type: "scatter3D",
            name: "故事发生点",
            coordinateSystem: "globe",
            itemStyle: {
              color: 'red'
            },
            symbol:
              'path://M953.6 179.2c0-6.4-6.4-19.2-12.8-19.2-6.4-6.4-19.2-6.4-25.6-6.4-44.8 6.4-83.2 6.4-128 0-44.8-12.8-96-32-140.8-57.6C595.2 64 556.8 32 531.2 6.4c-6.4-6.4-32-6.4-38.4 0-25.6 25.6-64 57.6-115.2 89.6-44.8 25.6-96 44.8-140.8 57.6-44.8 12.8-89.6 12.8-128 0-6.4 0-19.2 0-25.6 6.4-6.4 6.4-12.8 12.8-12.8 19.2-6.4 64-19.2 179.2 12.8 313.6 6.4 44.8 25.6 89.6 44.8 140.8 6.4 12.8 19.2 19.2 32 19.2h12.8c6.4-6.4 19.2-25.6 12.8-38.4-19.2-44.8-32-89.6-38.4-128-25.6-108.8-25.6-204.8-19.2-262.4 38.4 6.4 83.2 0 121.6-6.4 51.2-12.8 108.8-32 160-64 44.8-32 76.8-57.6 102.4-76.8 25.6 19.2 57.6 51.2 102.4 76.8 51.2 32 108.8 51.2 160 64 38.4 6.4 83.2 12.8 121.6 6.4 0 32 6.4 76.8 0 128 0 19.2 12.8 32 32 32s32-12.8 32-32c6.4-70.4 0-134.4-6.4-172.8zM921.6 422.4c-19.2 0-32 6.4-38.4 25.6v12.8c0 6.4 0 12.8-6.4 25.6-44.8 224-166.4 384-371.2 473.6-128-57.6-224-147.2-288-262.4-6.4-12.8-25.6-19.2-44.8-12.8-12.8 6.4-19.2 25.6-12.8 44.8C236.8 864 345.6 960 492.8 1024h25.6c230.4-102.4 371.2-281.6 416-524.8 0-6.4 0-19.2 6.4-25.6v-12.8c12.8-19.2 0-38.4-19.2-38.4z M256 659.2h12.8l544-192c19.2-12.8 25.6-32 19.2-44.8-6.4-19.2-25.6-25.6-38.4-19.2l-544 192c-19.2 6.4-25.6 25.6-19.2 38.4 0 12.8 12.8 25.6 25.6 25.6z',
            symbolSize: 16,
            label: {
              show: true,
              position: 'right',
              formatter: '{b}',
              textStyle: {
                color: 'white',
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            // blendMode: 'lighter',
            // data: []
          },
          {
            name: "故事轨迹",
            type: "lines3D",
            coordinateSystem: "globe",
            effect: {
              show: true,
              period: 2,
              trailColor: 'red',
              trailWidth: 8
            },
            blendMode: "lighter",
            lineStyle: {
              width: 4,
              color: '#0096FF',
            },
            data: [],
            silent: false,
          },
          {
            type: 'graph',
            layout: 'force',
            force: {
              repulsion: 1000,
              edgeLength: 100,
              layoutAnimation: true,
            },
            top: "10%",
            left: "70%",
            symbolSize: 30,
            nodeScaleRatio: 0, //图标大小是否随鼠标滚动而变
            roam: false, //缩放
            draggable: false, //节点是否可以拖拽
            edgeSymbol: ['none', 'arrow'], //线2头标记
            label: {
              normal: {
                show: true,
                position: 'top',
                color: 'white',
              },
            },
            edgeLabel: {
              normal: {
                show: true,
                textStyle: {
                    fontSize: 12,
                    color: '#FFF',
                },
                formatter: '{c}',
              },
            },
            symbolKeepAspect: false,
            focusNodeAdjacency: false, // 指定的节点以及其所有邻接节点高亮
            itemStyle: {
              normal: {
                borderColor: '#29ACFC',
                borderWidth: 2,
                shadowColor: '#29ACFC',
                color: '#29ACFC',
                curveness: 0.08,
              },
            },
            lineStyle: {
                normal: {
                  opacity: 0.9,
                  width: 2,
                  curveness: 0.15,
                  color: {
                      type: 'linear',
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [
                          {
                              offset: 0,
                              color: '#FFF', // 0% 处的颜色
                          },
                          {
                              offset: 1,
                              color: '#FFF', // 100% 处的颜色
                          },
                      ],
                      globalCoord: false,
                  },
                },
            },
            // data: [
            // ],
            // links: [
            // ],
          },
        ],
        //变量则写在options中 `switchableOption`s:
        options: [

        ]
      };
    },
    initChart() {
      var dom = document.getElementById('earth');
      this.myChart = this.$echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
      });
      this.myChart.setOption(this.option);
      window.addEventListener('resize', this.myChart.resize);
    },
    visualStory() {
      // 设置相机视角位于中国的经纬度坐标
      // this.option.globe.viewControl.targetCoord = [104.0, 37.5];
      // 改变数据值配置项
      // 清空！
      this.option.options = [];
      this.myChart.clear();
      this.dialogVisible = false;
      this.option.timeline.data = this.dateData;
      // 添加option变量
      for (var i = 0; i < this.dateData.length; i++){
        this.option.options.push(
          {
            title: {
              text: '',
              top: '5%',
              left: 'center',
              textStyle: {
                color: 'white',
                fontWeight: 'bold',
                fontSize: '24'
              }
            },
            graphic: {
              style: {
                text: '',
                fontSize: 16,
                fontWeight: 'bold',
                fill: 'white'
              }
            },
            series: [
            {
              data: []
            },
            {
              data: []
            },
            {
              data: [],
              links: []
            }]
          }
        )
      }
      var length = this.option.options.length;
      // console.log(this.option.options.length,this.lineData.length)
      // 按时间设置点 线数据
      for (var i = 0; i < length; i++){
        this.option.options[i].title.text = this.protagonist;
        this.option.options[i].series[0].data = this.seriesData.slice(0, i + 1);
        this.option.options[i].series[2].data = this.reNameData;
        this.option.options[i].series[2].links = this.reLinkData;
        this.option.options[i].graphic.style.text = this.dateData[i] + '\n\n' + this.eventData[i].replace(/；/g,"\n\n\n").replace(/：/g,"：\n\n").replace(/，/g,'，\n\n');
        if (i == 0) {
          // 初始线置为零
          this.option.options[i].series[1].data = [];
          // console.log(this.option.options[i].series[1].data)
        } else {
          // console.log(this.lineData.slice(0,i-1))
          this.option.options[i].series[1].data = this.lineData.slice(0,i);
        }
        // console.log(this.option.options[i].series[0].data)
        // console.log(this.option.options[i].series[1].data)
      }
      // 最终连线
      // this.option.options[length - 1].series[1].data = this.lineData;
      this.myChart.setOption(this.option);
    }
  },
  // watch: {
  //   // 监控数据值改变
  //   option: {
  //     handler(newVal, oldVal) {
  //       if (this.myChart) {
  //         if (newVal) {
  //           this.myChart.setOption(newVal);
  //         } else {
  //           this.myChart.setOption(oldVal);
  //         }
  //       } else {
  //         this.initChart();
  //       }
  //     },
  //     deep:true
  //   },
  // }
}
</script>


<style scoped>
.box-card {
  background-color: transparent;
  height: 100%;
  width: 100%;
  border: none;
}
.view {
  background-color: transparent;
  color: white;
  position: absolute;
  top: 18vh;
  left: 3vw;
  z-index: 999;
}
#earth {
  border: 1px solid white;
  box-shadow:-6px 0px 6px rgba(35,135,193,0.6),   /*左边阴影*/ 
  0px -6px 6px rgba(35,135,193,0.6),  /*上边阴影*/ 
  6px 0px 6px rgba(35,135,193,0.6),  /*右边阴影*/ 
  0px 6px 6px rgba(35,135,193,0.6); /*下边阴影*/
}
</style>
