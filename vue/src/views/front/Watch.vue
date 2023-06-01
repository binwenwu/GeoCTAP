<template>
  <el-card class="box-card" shadow="never">
    <div v-show="isLoading">
      <el-row :gutter="25">
        <el-col :span="7"><div id="circle" ref="circle" style="width: 29vw; height: 40vh"></div></el-col>
        <el-col :span="10"><div id="map" style="width: 42vw; height: 40vh"></div></el-col>
        <el-col :span="7"><div id="pie" ref="pie" style="width: 29vw; height: 40vh"></div></el-col>
      </el-row>
      <el-row class="wrap" :gutter="0">
        <el-col :span="6"><div id="histogram" ref="histogram" style="width: 25vw; height: 38vh"></div></el-col>
        <el-col :span="6"><div id="bubble" ref="bubble" style="width: 25vw; height: 38vh"></div></el-col>
        <el-col :span="6"><div id="radar" ref="radar" style="width: 25vw; height: 38vh"></div></el-col>
        <el-col :span="6"><div id="line" ref="line" style="width: 25vw; height: 38vh"></div></el-col>
      </el-row>
    </div>
    <div v-loading.fullscreen.lock="!isLoading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)">
    </div>
  </el-card>
</template>
<script>
import { Map, View, Overlay } from 'ol'
import { toStringHDMS } from 'ol/coordinate'
import { XYZ, TileWMS } from 'ol/source'
import { toLonLat } from 'ol/proj'
import { Tile } from 'ol/layer'
import { Select } from 'ol/interaction'
import { equalTo, and, during } from 'ol/format/filter'
import {defaults as defaultControls} from 'ol/control'


export default {
  name: 'Watch',
  data() {
    return {
      isLoading: false,
      loadingText: 'Loading...',
      timeInterval: null,
      app: {
        currentIndex: 0
      },
      selectIndex: 0,
      histogramOption: null,
      end: 5,
      pieOption: null,
      bubbleOption: null,
      circleOption: null,
      circleTimer: null,
      circleAngle: 0,
      circleChart: null,
      radarOption: null,
      lineOption: null,
      map: Map
    }
  },
  created(){

  },
  computed:{

  },
  mounted() {
    this.loadMap()
    this.showAllCharts()
  },

  methods: {
    showAllCharts() {
      const url = '/children'
      this.request.get(url).then(res => {
        if (res.code != '200') {
          return
        }
        this.isLoading = true
        console.log(this.isLoading)
        let features = res.data
        var info = {}
        let provinceDatas = ['河北省', '山西省', '辽宁省', '吉林省', '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省', '青海省', '台湾省', '北京市', '天津市', '上海市', '重庆市', '内蒙古自治区', '广西壮族自治区', '西藏自治区', '宁夏回族自治区', '新疆维吾尔自治区']
        let ageDatas = Array(19).fill(0)
        var out = {}
        var years = ["91-95", "96-00", "01-05", "06-10", "11-15", "16-20",]
        let disAllYears = Array(6).fill(0)
        let disManYears = Array(6).fill(0)
        let disWomanYears = Array(6).fill(0)
        // 计算被拐儿童年龄
        for (let i = 0; i < features.length; i++) {
          let feature = features[i]
          // 计算年龄
          if (!feature.birthday || !feature.disTime) {
            continue
          }
          let birthday = Number(feature.birthday.split('-')[0])
          let disTime = Number(feature.disTime.split('-')[0])
          if (isNaN(birthday) || isNaN(disTime)) {
            continue
          }
          let age = disTime - birthday
          ageDatas[age] += age
          let sex = feature.sex
          // 统计折线图数据
          if (disTime <= 1995 && disTime >= 1991) {
            disAllYears[0] += 1
            if (sex == '男') {
              disManYears[0] += 1
            } else if (sex == '女') {
              disWomanYears[0] += 1
            }
          } else if (disTime <= 2000) {
            disAllYears[1] += 1
            if (sex == '男') {
              disManYears[1] += 1
            } else if (sex == '女') {
              disWomanYears[1] += 1
            }
          } else if (disTime <= 2005) {
            disAllYears[2] += 1
            if (sex == '男') {
              disManYears[2] += 1
            } else if (sex == '女') {
              disWomanYears[2] += 1
            }
          } else if (disTime <= 2010) {
            disAllYears[3] += 1
            if (sex == '男') {
              disManYears[3] += 1
            } else if (sex == '女') {
              disWomanYears[3] += 1
            }
          } else if (disTime <= 2015) {
            disAllYears[4] += 1
            if (sex == '男') {
              disManYears[4] += 1
            } else if (sex == '女') {
              disWomanYears[4] += 1
            }
          } else if (disTime <= 2020) {
            disAllYears[5] += 1
            if (sex == '男') {
              disManYears[5] += 1
            } else if (sex == '女') {
              disWomanYears[5] += 1
            }
          }
        }
        disAllYears[0] /= 4
        disManYears[0] /= 4
        disWomanYears[0] /= 4
        disAllYears[1] /= 10
        disManYears[1] /= 10
        disWomanYears[1] /= 10
        // 计算各省被拐儿童数量
        for (let i = 0; i < features.length; i++) {
          let feature = features[i]
          // 计算失踪省份
          let province = feature.disProvi
          let k = -1
          provinceDatas.forEach((d, index, self) => {
            if (province == d) {
              k = index
            }
          })
          if (typeof info['province_' + k] == "undefined") {
            info['province_' + k] = 1
          } else {
            info['province_' + k] += 1
          }
          // 计算拐出省份
          let outProvince = feature.nowProvi
          k = -1
          provinceDatas.forEach((d, index, self) => {
            if (province == d) {
              k = index
            }
          })
          if (typeof out['province_' + k] == "undefined") {
            out['province_' + k] = 1
          } else {
            out['province_' + k] += 1
          }
        }
        // 数据记录
        let provinceChildren = []
        let outProvince = []
        let outProvinceChildren = []
        let region = []
        let regionNorth = { name: '华北', itemStyle: { color: '#81D0E8' } }
        let regionSourth = { name: '华南', itemStyle: { color: '#81D0E8' } }
        let regionEast = { name: '华东', itemStyle: { color: '#81D0E8' } }
        let regionCenter = { name: '华中', itemStyle: { color: '#81D0E8' } }
        let regionWest = { name: '华西', itemStyle: { color: '#81D0E8' } }
        // 柱状图统计数据
        for (let i = 0; i < provinceDatas.length; i++) {
          let reg = {}
          reg.value = info['province_' + i]
          reg.name = provinceDatas[i]
          provinceChildren.push(reg)
        }
        // 饼图统计数据
        let count = 0
        for (let i = 0; i < provinceDatas.length; i++) {
          // 华北
          count += provinceChildren[i].value
          if (provinceDatas[i] == '北京市' || provinceDatas[i] == '天津市' || provinceDatas[i] == '河北省' || provinceDatas[i] == '山西省' || provinceDatas[i] == '内蒙古自治区') {
            if (typeof regionNorth.value == 'undefined') {
              regionNorth.value = provinceChildren[i].value
            } else {
              regionNorth.value += provinceChildren[i].value
            }
          }
          // 华南
          if (provinceDatas[i] == '广东省' || provinceDatas[i] == '广西壮族自治区' || provinceDatas[i] == '海南省') {
            if (typeof regionSourth.value == 'undefined') {
              regionSourth.value = provinceChildren[i].value
            } else {
              regionSourth.value += provinceChildren[i].value
            }
          }
          // 华东
          if (provinceDatas[i] == '江苏省' || provinceDatas[i] == '浙江省' || provinceDatas[i] == '安徽省' || provinceDatas[i] == '福建省' || provinceDatas[i] == '山东省' || provinceDatas[i] == '上海市') {
            if (typeof regionEast.value == 'undefined') {
              regionEast.value = provinceChildren[i].value
            } else {
              regionEast.value += provinceChildren[i].value
            }
          }
          // 华中
          if (provinceDatas[i] == '河南省' || provinceDatas[i] == '湖北省' || provinceDatas[i] == '湖南省') {
            if (typeof regionCenter.value == 'undefined') {
              regionCenter.value = provinceChildren[i].value
            } else {
              regionCenter.value += provinceChildren[i].value
            }
          }
          // 华西
          if (provinceDatas[i] == '新疆维吾尔自治区' || provinceDatas[i] == '内蒙古自治区' || provinceDatas[i] == '青海省' || provinceDatas[i] == '甘肃省' || provinceDatas[i] == '宁夏回族自治区' || provinceDatas[i] == '陕西省' || provinceDatas[i] == '西藏自治区' || provinceDatas[i] == '四川省' || provinceDatas[i] == '重庆市' || provinceDatas[i] == '云南省' || provinceDatas[i] == '广西壮族自治区') {
            if (typeof regionWest.value == 'undefined') {
              regionWest.value = provinceChildren[i].value
            } else {
              regionWest.value += provinceChildren[i].value
            }
          }
        }
        region.push(regionNorth)
        region.push(regionSourth)
        region.push(regionEast)
        region.push(regionCenter)
        region.push(regionWest)
        // 气泡图统计数据
        let top10Region = provinceChildren
        top10Region.sort((t1, t2) => {
          return t2.value - t1.value
        })
        top10Region = top10Region.slice(0, 10)
        // 环形图统计数据
        let ageRangeDatas = []
        let ageObj = {}
        for (let i = 1; i < 19; i++) {
          if (i <= 6) {
            if (i == 1) {
              ageObj = { name: '0-6岁', value: ageDatas[i] }
            } else {
              ageObj.value += ageDatas[i]
            }
            if (i == 6) {
              ageRangeDatas.push(ageObj)
            }
          } else if (i <= 12) {
            if (i == 7) {
              ageObj = { name: '7-12岁', value: ageDatas[i] }
            } else {
              ageObj.value += ageDatas[i]
            }
            if (i == 12) {
              ageRangeDatas.push(ageObj)
            }
          } else {
            if (i == 13) {
              ageObj = { name: '13-18岁', value: ageDatas[i] }
            } else {
              ageObj.value += ageDatas[i]
            }
            if (i == 18) {
              ageRangeDatas.push(ageObj)
            }
          }
        }
        // 雷达图统计数据
        let regionNorth1 = { name: '华北' }
        let regionSourth1 = { name: '华南' }
        let regionEast1 = { name: '华东' }
        let regionCenter1 = { name: '华中' }
        let regionWest1 = { name: '华西' }
        let region1 = []
        for (let i = 0; i < provinceDatas.length; i++) {
          outProvinceChildren.push(out['province_' + i])
        }
        for (let i = 0; i < provinceDatas.length; i++) {
          // 华北

          if (provinceDatas[i] == '北京市' || provinceDatas[i] == '天津市' || provinceDatas[i] == '河北省' || provinceDatas[i] == '山西省' || provinceDatas[i] == '内蒙古自治区') {
            if (typeof regionNorth1.value == 'undefined') {
              regionNorth1.value = outProvinceChildren[i]
            } else {
              regionNorth1.value += outProvinceChildren[i]
            }
          }
          // 华南
          if (provinceDatas[i] == '广东省' || provinceDatas[i] == '广西壮族自治区' || provinceDatas[i] == '海南省') {
            if (typeof regionSourth1.value == 'undefined') {
              regionSourth1.value = outProvinceChildren[i]
            } else {
              regionSourth1.value += outProvinceChildren[i]
            }
          }
          // 华东
          if (provinceDatas[i] == '江苏省' || provinceDatas[i] == '浙江省' || provinceDatas[i] == '安徽省' || provinceDatas[i] == '福建省' || provinceDatas[i] == '山东省' || provinceDatas[i] == '上海市') {
            if (typeof regionEast1.value == 'undefined') {
              regionEast1.value = outProvinceChildren[i]
            } else {
              regionEast1.value += outProvinceChildren[i]
            }
          }
          // 华中
          if (provinceDatas[i] == '河南省' || provinceDatas[i] == '湖北省' || provinceDatas[i] == '湖南省') {
            if (typeof regionCenter1.value == 'undefined') {
              regionCenter1.value = outProvinceChildren[i]
            } else {
              regionCenter1.value += outProvinceChildren[i]
            }
          }
          // 华西
          if (provinceDatas[i] == '新疆维吾尔自治区' || provinceDatas[i] == '内蒙古自治区' || provinceDatas[i] == '青海省' || provinceDatas[i] == '甘肃省' || provinceDatas[i] == '宁夏回族自治区' || provinceDatas[i] == '陕西省' || provinceDatas[i] == '西藏自治区' || provinceDatas[i] == '四川省' || provinceDatas[i] == '重庆市' || provinceDatas[i] == '云南省' || provinceDatas[i] == '广西壮族自治区') {
            if (typeof regionWest1.value == 'undefined') {
              regionWest1.value = outProvinceChildren[i]
            } else {
              regionWest1.value += outProvinceChildren[i]
            }
          }
        }

        region1.push(regionNorth1)
        region1.push(regionSourth1)
        region1.push(regionEast1)
        region1.push(regionCenter1)
        region1.push(regionWest1)
        outProvinceChildren = []
        for (let i = 0; i < region1.length; i++) {
          outProvince.push({ name: region1[i].name, max: 15000 })
          outProvinceChildren.push(region1[i].value)
        }
        // 绘制各种统计图表
        this.drawProvinceChildrenHistogram(provinceChildren)
        this.drawProvinceChildrenPie(region, count)
        this.drawProvinceChildrenBubble(top10Region)
        this.drawProvinceChildrenCircle(ageRangeDatas)
        this.drawProvinceChildrenRadar(outProvince, outProvinceChildren)
        this.drawProvinceChildrenLine(years, disAllYears, disManYears, disWomanYears)
      })
    },
    drawProvinceChildrenHistogram(datas) {
      const histogram = this.$refs.histogram
      const histogramChart = this.$echarts.init(histogram)
      let dataList = [], dataName = [], dataValue = []
      datas.forEach(data => {
        dataList.push({
          name: data.name,
          value: data.value
        })
      })
      dataList.sort((d1, d2) => {
        return d2.value - d1.value
      })
      dataList.forEach((d) => {
        dataName.push(d.name)
        dataValue.push(d.value)
      })
      this.histogramOption = {
        title: {
          text: "拐卖排行",
          left: 'center',
          top: 0,
          textStyle: {
            color: '#fff',
            fontWeight: 'normal',
            fontSize: 16,
          }
        },

        grid: {
          left: "5%",
          right: "5%",
          bottom: "0%",
          top: "10%",
          containLabel: true
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow"
          },
          padding: 10,
          backgroundColor: "rgba(0, 0, 0, .8)"
        },
        dataZoom: {
          yAxisIndex: 0,
          show: false,
          type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
          startValue: 0, // 从头开始。
          endValue: this.end
        },

        yAxis: {
          type: "category",
          inverse: true,
          axisLabel: {
            // rotate: 45,
            textStyle: {
              color: "#fff",
              fontSize: 12
            },
            formatter: function (value) {
              let length = 8
              let num = Math.ceil(value.length / length)
              if (num > 1) {
                let newValue = ''
                for (let i = 0; i < num; i++) {
                  newValue += value.substring(i * length, (i + 1) * length) + '\n'
                }
                return newValue
              }
              return value;
            }
          },
          axisTick: {
            show: false
          },
          axisLine: {
            show: false
          },
          data: dataName
        },
        xAxis: {
          type: "value",
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitLine: {
            show: false,
            lineStyle: {
              color: "#fff"
            }
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
              fonSize: 12
            },
            show: false
          },
          min: 3,
          max: 4200,
          interval: 3
        },
        series: [
          {
            type: "bar",
            name: "商品类别",
            barWidth: 20,
            itemStyle: {
              normal: {
                // color: "#1890FF"
                // color: "red"
                // color: (data) => {
                //   if (data.value >= 1500) {
                //     return "#5dcafd"
                //   } else if (data.value >= 700) {
                //     return "yellow"
                //   } else {
                //     return "green"
                //   }
                // }
                color: "#5dcafd"
              }
            },
            label: {
              //图形上的文本标签
              normal: {
                show: true,
                position: "right",
                textStyle: {
                  color: "#fff",
                  fontStyle: "normal",
                  fontFamily: "微软雅黑",
                  fontSize: 12
                }
              }
            },
            data: dataList
          }
        ]
      }
      histogramChart.setOption(this.histogramOption)
      this.autoScroll(dataList.length - 1, histogramChart)
      window.addEventListener("resize", () => {
        // 执行echarts自带的resize方法，即可做到让echarts图表自适应
        histogramChart.resize();
      });
    },
    autoScroll(length, scrollChart) {
      setInterval(() => {
        // 每次向后滚动一个，最后一个从头开始。
        if (Number(this.histogramOption.dataZoom.endValue) === length) {
          this.histogramOption.dataZoom.endValue = this.end;
          this.histogramOption.dataZoom.startValue = 0;
        } else {
          this.histogramOption.dataZoom.endValue = this.histogramOption.dataZoom.endValue + 1;
          this.histogramOption.dataZoom.startValue = this.histogramOption.dataZoom.startValue + 1;
        }
        scrollChart.setOption(this.histogramOption);
      }, 3000);
    },
    drawProvinceChildrenPie(region, count) {
      const pie = this.$refs.pie
      if (!pie) return
      const pieChart = this.$echarts.init(pie)
      const colors = ["#5dcafd", "#73e363", "#05eca7", "#fdbd2c", "#f27b72"]
      for (let i = 0; i < region.length; i++) {
        region[i].label = { color: colors[i] }
        region[i].itemStyle = { color: colors[i] }
      }
      this.pieOption = {
        tooltip: {
          trigger: "item",
          formatter: "地区拐卖统计 <br/>{b} : {c} ({d}%)"
        },
        legend: {
          left: 50,
          top: 10,
          itemGap: 10,
          itemWidth: 14,
          orient: "horizontal",
          textStyle: { fontSize: 14, color: 'white' }
        },
        title: {
          text: count,
          top: "42%",
          left: "49%",
          subtext: "总数",
          itemGap: 5,
          textStyle: {
            color: "#45a0e8",
            fontSize: 24
          },
          subtextStyle: {
            color: "#61bbff",
            fontSize: 20
          },
          textAlign: "center"
        },
        series: [
          {
            name: "Nightingale Chart",
            type: "pie",
            radius: ["30%", "60%"],
            center: ["50%", "50%"],
            roseType: "area",
            itemStyle: {
              borderRadius: 0,
            },
            data: region,
            label: {
              formatter: function (params) {
                return `{normal|${params.name}：}\r\r\n{value|${params.data.value} (${params.percent})%}`
              },
              rich: {
                normal: {
                  //   color: "#8A8A8A",
                  fontSize: 14,
                  align: "left"
                },
                value: {
                  fontSize: 12,
                  //   color: "#8A8A8A",
                  fontWeight: 600,
                  padding: [5, 0, 0, 0]
                }
              }
            },
            labelLine: {
              length: 10,
              length2: 10
            }
          }
        ]
      }
      pieChart.setOption(this.pieOption)
      // pieChart.resize()
      window.addEventListener("resize", () => {
        // 执行echarts自带的resize方法，即可做到让echarts图表自适应
        pieChart.resize();
      });
    },
    drawProvinceChildrenBubble(top10Region) {
      const bubble = this.$refs.bubble
      if (bubble) {
        const bubbleChart = this.$echarts.init(bubble)

        const getSeriesData = (datalist = []) => {
          let data = datalist,
            offsetData = [],
            symbolSizeData = [];
          //计算offsetData和symbolSizeData
          offsetData = [
            [8, 19],
            [17, 69],
            [26, 45],
            [35, 72],
            [44, 22],
            [53, 62],
            [63, 81],
            [72, 34],
            [81, 71],
            [91, 11]
          ];
          data.forEach((d) => {
            symbolSizeData.push((d.value - data[data.length - 1].value / 2) / 35);
          });

          let datas = [];
          for (let i = 0; i < data.length; i++) {
            let item = data[i];
            let itemName = "",
              nameArr = [];
            if (item && item.name) {
              itemName = item.name;
              nameArr = [];
            }
            if (itemName.length > 6) {
              nameArr.push(
                `{name|${itemName.slice(0, 6)}}`,
                `{name|${itemName.slice(6, itemName.length)}}`
              );
            } else {
              nameArr.push(`{name|${itemName}}`);
            }
            let formatter = [...nameArr, `\n{value|${item.value}}`].join("\n");
            datas.push({
              value: offsetData[i],
              symbolSize: symbolSizeData[i],
              label: {
                formatter: formatter,
                align: "center",
                rich: {
                  name: {
                    color: "#FFFFFF",
                    fontSize: "12px",
                    fontWeight: "Bold",
                    wordBreak: "break-all",
                    width: "6px",
                    overflow: "hidden",
                    textOverflow: "hidden",
                  },
                  value: {
                    fontSize: "12px",
                    fontWeight: "Bold",
                    color: "#81D0E8",
                  },
                },
              },
              itemStyle: {
                color: new this.$echarts.graphic.RadialGradient(0.5, 0.45, 0.7, [
                  {
                    offset: 0.3,
                    color: "rgba(0,215,233,0.1)",
                  },
                  {
                    offset: 1,
                    color: "rgba(0,215,233,0.8)",
                  },
                ]),
                opacity: 0.8,
                shadowColor: "#045878",
                borderWidth: 1,
                borderColor: "rgba(0, 215, 233, 1)",
                shadowBlur: 10,
                shadowOffsetX: 1,
                shadowOffsetY: 1,
              },
              emphasis: {
                label: {
                  rich: {
                    name: {
                      color: "#FFB229",
                      fontSize: "18px",
                      fontWeight: "Bold",
                    },
                    value: {
                      fontSize: symbolSizeData[i] > 125 ? "24px" : "18px",
                      fontWeight: "Bold",
                      color: "#FFB229",
                    },
                  },
                },
              },
            });
          }
          return datas;
        };

        let datas = []
        top10Region.forEach(p => {
          datas.push({
            name: p.name,
            value: p.value
          })
        })

        this.bubbleOption = {
          grid: {
            show: false,
            top: 10,
            bottom: 10,
          },
          xAxis: [
            {
              gridIndex: 0,
              type: "value",
              show: false,
              min: 0,
              max: 100,
              nameLocation: "middle",
              nameGap: 5,
            },
          ],
          yAxis: [
            {
              gridIndex: 0,
              min: 0,
              show: false,
              max: 100,
              nameLocation: "middle",
              nameGap: 30,
            },
          ],
          series: [
            {
              type: "scatter",
              symbol: "circle",
              symbolSize: 120,
              label: {
                show: true,
                formatter: "{b}",
                color: "#fff",
                fontSize: "20",
              },

              itemStyle: {
                color: "#00acea",
              },
              emphasis: {
                itemStyle: {
                  color: new this.$echarts.graphic.RadialGradient(0.5, 0.45, 0.7, [
                    {
                      offset: 0.3,
                      color: "rgba(191, 153, 30,0.1)",
                    },
                    {
                      offset: 1,
                      color: "rgba(191, 153, 30,0.8)",
                    },
                  ]),
                  opacity: 0.8,
                  shadowColor: "rgba(191, 153, 30,1)",
                  borderWidth: 1,
                  borderColor: "rgba(191, 153, 30, 1)",
                  shadowBlur: 10,
                  shadowOffsetX: 1,
                  shadowOffsetY: 1,
                },
              },
              data: getSeriesData(datas),
            },
          ],
        }
        // 图表动效及点击效果start
        bubbleChart.setOption(this.bubbleOption)
        window.addEventListener("resize", () => {
          // 执行echarts自带的resize方法，即可做到让echarts图表自适应
          bubbleChart.resize();
        });
        const highlight = (option, datalist) => {
          this.app.currentIndex = 0
          this.timeInterval = setInterval(async () => {
            let dataLen = option.series[0].data.length
            //取消之前高亮的图形
            bubbleChart.dispatchAction({
              type: "downplay",
              seriesIndex: 0,
              dataIndex: this.app.currentIndex,
            })
            this.app.currentIndex = (this.app.currentIndex + 1) % dataLen
            //高亮当前图形
            bubbleChart.dispatchAction({
              type: "highlight",
              seriesIndex: 0,
              dataIndex: this.app.currentIndex,
            })
          }, 1000)
        }
        highlight(this.bubbleOption, datas)

        bubbleChart.on("click", (params) => {
          if (params.seriesType == "scatter") {
            clearInterval(this.timeInterval)
            let { dataIndex } = params
            this.selectIndex = dataIndex
            bubbleChart.dispatchAction({
              type: "downplay",
              seriesIndex: 0,
              dataIndex: this.app.currentIndex,
            })
            bubbleChart.dispatchAction({
              type: "highlight",
              seriesIndex: 0,
              dataIndex: dataIndex,
            })
            this.app.currentIndex = dataIndex;
            startTimeout()
          }
        })

        const startTimeout = () => {
          setTimeout(() => {
            if (this.selectIndex == this.app.currentIndex) {
              restartInterval()
            } else {
              startTimeout()
            }
          }, 2000)
        }

        const restartInterval = () => {
          this.timeInterval = setInterval(async () => {
            let dataLen = datas.length
            //取消之前高亮的图形
            bubbleChart.dispatchAction({
              type: "downplay",
              seriesIndex: 0,
              dataIndex: this.app.currentIndex,
            })
            this.app.currentIndex = (this.app.currentIndex + 1) % dataLen
            //高亮当前图形
            bubbleChart.dispatchAction({
              type: "highlight",
              seriesIndex: 0,
              dataIndex: this.app.currentIndex,
            })
          }, 1000)
        }
      }
    },
    drawProvinceChildrenCircle(ageRangeDatas) {
      // let single = []
      // for (let i = 0; i < ageRangeDatas.length; i++) {
      //   single.push(ageRangeDatas[i].value % 100)
      //   ageRangeDatas[i].value /= 100
      // }
      const circle = this.$refs.circle
      if (!circle) return
      this.circleChart = this.$echarts.init(circle)
      let anothorData = ageRangeDatas
      anothorData[0].itemStyle = { color: '#5dcafd' }
      anothorData[1].itemStyle = { color: '#fdbd2c' }
      anothorData[2].itemStyle = { color: '#f27b72' }
      this.circleOption = {
        tooltip: {
          trigger: 'item'
          // formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          top: 20,
          right: '2%',
          textStyle: {
            color: 'white', // 设置文本颜色
            fontSize: 12, // 设置字体大小
            fontStyle: 'italic', // 设置字体样式（可选，例如：'normal', 'italic', 'oblique'）
          }
        },
        series: [
          {
            name: '总失踪',
            type: 'pie',
            radius: ['40%', '63%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              formatter: '({d}%)'
            },
            // color: ['#fac858', '#ee6666', '#73c0de'],
            emphasis: {
              label: {
                show: true,
                fontSize: 13,
                fontWeight: 'bold',
                // formatter: function (params) {
                //   // 在标签的 formatter 函数中将数据乘以10
                //   return params.value * 100 + single[params.dataIndex];
                // }
              }

            },
            data: ageRangeDatas
          },
          {
            name: '总失踪',
            type: 'pie',
            radius: ['34%', '40%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            silent: true,
            label: {
              formatter: '({d}%)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 13,
                fontWeight: 'bold',
                // formatter: function (params) {
                //   // 在标签的 formatter 函数中将数据乘以10
                //   return params.value * 100 + single[params.dataIndex];
                // }
              }
            },
            data: anothorData
          },
          {
            type: 'custom',
            coordinateSystem: 'none',
            silent: true,
            data: [0],
            renderItem: (params, api) => {
              //环形图半径
              const r = Math.min(api.getWidth(), api.getHeight()) / 2
              //圆心
              const center = {
                x: api.getWidth() / 2,
                y: api.getHeight() / 2
              }
              //大圆半径
              const rBig = r * 0.9
              //小圆半径
              const rSmall = r * 0.78
              //大圆上的扇形
              const bigSector = []
              const smallSector = []
              const circleOnCircle = [] //小圆上携带的小圆圈
              const sectorSize = 60 //扇形长度（弧度）
              const sectorInterval = 30 //扇形与扇形之间的间隔
              const BigStartAngle = 310 //大扇形起始角度
              for (let i = 0; i < 4; i++) {
                const startAngle = ((i * (sectorInterval + sectorSize) + BigStartAngle) * Math.PI) / 180
                const endAngle = startAngle + (sectorSize * Math.PI) / 180
                const smallStartAngle = (Math.PI / 180) * (280 + this.circleAngle + i * (sectorSize + sectorInterval))
                const smallEndAngle = smallStartAngle + (sectorSize * Math.PI) / 180
                bigSector.push({
                  type: 'sector',
                  shape: {
                    cx: center.x,
                    cy: center.y,
                    r: rBig,
                    r0: rBig * 0.93,
                    startAngle,
                    endAngle
                  },
                  style: {
                    fill: '#55EAF1',
                    lineWidth: 2
                  }
                })
                smallSector.push({
                  type: 'sector',
                  shape: {
                    cx: center.x,
                    cy: center.y,
                    r: rSmall * 0.93,
                    r0: rSmall * 0.87,
                    startAngle: smallStartAngle,
                    endAngle: smallEndAngle
                  },
                  style: {
                    fill: '#14769f',
                    lineWidth: 2
                  }
                })
                circleOnCircle.push({
                  type: 'circle',
                  shape: {
                    cx: this.getCirclePoint(center.x, center.y, rSmall, 270 + i * 90 - this.circleAngle).x,
                    cy: this.getCirclePoint(center.x, center.y, rSmall, 270 + i * 90 - this.circleAngle).y,
                    r: 6
                  },
                  style: {
                    fill: '#19ECFF',
                    stroke: '#19ECFF',
                    lineWidth: 2
                  }
                })
              }
              return {
                type: 'group',
                children: [
                  {
                    type: 'group',
                    children: [
                      ...bigSector,
                      {
                        // 外圆环
                        type: 'arc',
                        shape: {
                          cx: center.x,
                          cy: center.y,
                          r: rBig
                        },
                        style: {
                          fill: 'transparent',
                          stroke: '#55EAF1',
                          lineWidth: 2
                        }
                      }
                    ]
                  },
                  {
                    //内圆环
                    type: 'group',
                    children: [
                      ...smallSector,
                      ...circleOnCircle,
                      {
                        //内圆
                        type: 'arc',
                        shape: {
                          cx: center.x,
                          cy: center.y,
                          r: rSmall
                        },
                        style: {
                          fill: 'transparent',
                          stroke: '#55EAF1',
                          lineWidth: 2
                        }
                      }
                    ]
                  }
                ]
              }
            }
          }
        ]
      }
      this.circleChart.on('finished', () => {
        this.play()
      })
      // this.play()
    },
    getCirclePoint(x0, y0, r, angle) {
      let x1 = x0 + r * Math.cos((angle * Math.PI) / 180)
      let y1 = y0 + r * Math.sin((angle * Math.PI) / 180)
      return {
        x: x1,
        y: y1
      }
    },
    play() {
      this.circleAngle += 3
      this.circleOption && this.circleChart.setOption(this.circleOption)
      window.addEventListener("resize", () => {
        // 执行echarts自带的resize方法，即可做到让echarts图表自适应
        this.circleChart.resize();
      });
      if (this.circleTimer) return
      this.circleTimer = setTimeout(() => {
        requestAnimationFrame(this.play)
        this.circleTimer = null
      }, 28)
    },
    drawProvinceChildrenRadar(provinceDatas, out_province) {
      const radar = this.$refs.radar
      if (!radar) return
      var radarChart = this.$echarts.init(radar)
      this.radarOption = {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: (data) => {
            var tip = data.seriesName
            let tmpData = []
            if (data.seriesIndex === 0) {
              tmpData = out_province
            }
            data.value.forEach((item, index) => {
              tip += '<div>'
              tip += '<div>' + provinceDatas[index].name + ': ' + tmpData[index] + '</div>'
              tip += '</div>'
            });
            return tip
          },
        },
        radar: {
          name: {
            textStyle: {
              color: '#2273DA',
              fontSize: 12,
            },
            rich: {
              a: {
                fontSize: 12,
                color: "white",
                // padding: [0, 0, 8, 0]
              },
              b: {
                fontSize: 12,
                color: "white"
              }
            },
            formatter: a => {
              let i = this.contains(provinceDatas, a) // 处理对应要显示的样式
              return `{a| ${a}}{b|(${out_province[i]})}`
            }
          },
          center: ['50%', '50%'],
          radius: '70%',
          startAngle: 90,
          splitNumber: 4,
          splitArea: {
            areaStyle: {
              color: ['rgba(50, 72, 130, 0.8)', 'rgba(50, 72, 130, 0.2)', 'rgba(50, 72, 130, 0.5)'],
            },
          },
          axisLabel: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(50, 72, 130, 0.4)',
            },
          },
          indicator: provinceDatas,
        },
        series: [
          {
            name: '数据',
            type: 'radar',
            symbol: 'circle',
            symbolSize: 10,
            itemStyle: {
              normal: {
                color: 'rgba(19, 173, 255, 1)',
                borderColor: 'rgba(19, 173, 255, 0.4)',
                borderWidth: 10,
              },
            },

            label: {
              normal: {
                show: true,
                formatter: (params) => {
                  return params.value
                },
                color: '#fff',
              },
              fontSize: 12
            },
            areaStyle: {
              normal: {
                color: new this.$echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: 'rgba(82, 247, 242, 1)',
                    },
                    {
                      offset: 1,
                      color: 'rgba(65, 105, 213, 1)',
                    },
                  ],
                  false
                ),
              },
            },
            data: [
              {
                value: out_province
              }
            ]
            // data: out_province
          },
        ],
      };
      radarChart.setOption(this.radarOption)
      window.addEventListener("resize", () => {
        // 执行echarts自带的resize方法，即可做到让echarts图表自适应
        radarChart.resize();
      });
    },
    contains(arr, val) {
      var i = arr.length
      while (i--) {
        if (arr[i].name == val) {
          return i
        }
      }
      return false
    },
    drawProvinceChildrenLine(years, disAllYears, disManYears, disWomanYears) {
      const line = this.$refs.line
      if (!line) return
      var lineChart = this.$echarts.init(line)
      this.lineOption = {
        // backgroundColor: "#344b58",
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 10,
          bottom: 30,
          textStyle: {
            color: "#fff",
          },
        },
        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: "rgba(255,255,255,.5)",
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              interval: 0,
              color: "rgba(255,255,255,0.7)",
              fontSize: 12,
            },
            data: years,
          },
        ],
        yAxis: [
          {
            type: "value",
            splitLine: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              interval: 0,
              color: "rgba(255,255,255,0.5)",
              fontSize: 20,
              show: false
            },
            splitArea: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: "女",
            type: "bar",
            stack: "总量",
            barMaxWidth: 35,
            barGap: "10%",
            itemStyle: {
              normal: {
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "rgba(35, 157, 250, 1)", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "rgba(35, 157, 250, 0)", // 100% 处的颜色
                    },
                  ],
                  global: false, // 缺省为 false
                },
              },
            },
            data: disWomanYears,
          },

          {
            name: "男",
            type: "bar",
            stack: "总量",
            itemStyle: {
              normal: {
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "rgba(35, 250, 187, 1)", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "rgba(35, 250, 187, 0)", // 100% 处的颜色
                    },
                  ],
                  global: false, // 缺省为 false
                },
                barBorderRadius: 0,
              },
            },
            data: disManYears,
          },
          {
            name: "总数",
            type: "line",
            symbolSize: 10,
            symbol: "circle",
            itemStyle: {
              normal: {
                color: "rgba(255, 196, 53, 1)",
                barBorderRadius: 0,
              },
            },
            lineStyle: {
              normal: {
                width: 4,
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "rgba(255, 67, 2, 1)", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "rgba(255, 196, 53, 1)", // 100% 处的颜色
                    },
                  ],
                  global: false, // 缺省为 false
                },
              },
            },
            data: disAllYears,
          },
        ],
      };
      lineChart.setOption(this.lineOption)
      window.addEventListener("resize", () => {
        // 执行echarts自带的resize方法，即可做到让echarts图表自适应
        lineChart.resize();
      });
    },
    loadMap() {
      var tian_di_tu_road_layer = new Tile({
        title: "路网",
        source: new XYZ({
          url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}"
        })
      })
      var view = new View({
        // 地图中心点的经纬度
        center: [114, 30],
        zoom: 10, // 初始缩放级别
        projection: 'EPSG:4326'
      })
      var wms_source = new TileWMS({
        url: 'http://125.220.153.26:31520/geoserver/GISProj/wms', // GeoServer 的 WMS 服务地址
        params: {
          'LAYERS': 'GISProj:children', // 指定图层的名称，格式为 "workspace:layer_name"
          // 'VERSION': '1.1.0'
          env: 'color:FF0000;name:circle;size:6',
        }
      })
      var wmslayer = new Tile({
        source: wms_source
      })
      this.map = new Map({
        view: view,
        target: 'map', // 指定地图渲染的目标元素 ID
        //加载控件到地图容器中
        controls: defaultControls({
            zoom: false,
            rotate: false,
            attribution: false
        })
      });
      this.map.addLayer(tian_di_tu_road_layer)
      this.map.addLayer(wmslayer);
    }
  },
}
</script>
<style scoped>
.box-card {
  background-color: transparent;
  height: 100%;
  width: 100%;
  border: none;
}
#map {
  border: 1px solid white;
  box-shadow:-6px 0px 6px rgba(35,135,193,0.8),   /*左边阴影*/
  0px -6px 6px rgba(35,135,193,0.8),  /*上边阴影*/
  6px 0px 6px rgba(35,135,193,0.8),  /*右边阴影*/
  0px 6px 6px rgba(35,135,193,0.8); /*下边阴影*/
}
.wrap {
  margin-top: 2vh;
  border-top: 1px solid rgb(35,135,193);
  box-shadow:-6px 0px 6px rgba(35,135,193,0.8),   /*左边阴影*/
  0px -6px 6px rgba(35,135,193,0.8),  /*上边阴影*/
  6px 0px 6px rgba(35,135,193,0.8),  /*右边阴影*/
  0px 6px 6px rgba(35,135,193,0.8); /*下边阴影*/
}
</style>
