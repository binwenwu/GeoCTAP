<template>
  <div>
    <el-card class="box-card" shadow="never">
      专题地图模块
    </el-card>
    <div>

      <el-switch v-model="show_point" @change='point_change' active-text="热点">
      </el-switch>
      <el-switch v-model="show_hotmap" @change='hotmap_change' active-text="热力图">
      </el-switch>
    </div>
    <div>
      <el-radio-group v-model="radio_topic" @change="top_change">
        <el-radio-button label="age">走丢平均年龄</el-radio-button>
        <el-radio-button label="disage">平均年龄</el-radio-button>
        <el-radio-button label="count">走丢数量</el-radio-button>
      </el-radio-group>
    </div>
    <div>
      <el-radio-group v-model="radio_scale" @change="scale_change">
        <el-radio-button label="provi">省</el-radio-button>
        <el-radio-button label="city">市</el-radio-button>
        <el-radio-button label="dist">县/区</el-radio-button>
      </el-radio-group>
    </div>


    <div id="topic_map"></div>
  </div>
</template>
<script>

import { Map, View, Overlay } from 'ol'
import { XYZ, TileWMS } from 'ol/source'
import { Tile } from 'ol/layer'
import { FALSE } from 'ol/functions'
export default {

  name: 'TopicMap',
  data() {
    return {
      show_point: false,
      show_hotmap: false,
      radio_topic: undefined,
      radio_scale: undefined,
      age_city: Tile,
      age_dist: Tile,
      age_provi: Tile,
      age_dis_city: Tile,
      age_dis_dist: Tile,
      age_dis_provi: Tile,
      count_city: Tile,
      count_dist: Tile,
      count_provi: Tile,
      dis_point: Tile,
      hotmap: Tile,
      now_layer: undefined,
      map: Map,
    }
  },
  created() {
  },
  computed: {
  },
  methods: {
    point_change() {
      if (this.show_point) {
        this.map.addLayer(this.dis_point)
      }
      else {
        this.map.removeLayer(this.dis_point)
      }
    },
    hotmap_change() {
      if (this.show_hotmap) {
        this.map.addLayer(this.hotmap)
      }
      else {
        this.map.removeLayer(this.hotmap)
      }
    },
    top_scale_change() {

      console.log('调用了')
      console.log(this.radio_topic)
      console.log(this.radio_scale)
      if ((this.radio_topic != undefined) && (this.radio_scale != undefined)) {
        if (this.now_layer != undefined) {
          this.map.removeLayer(this.now_layer)
          this.now_layer = undefined
        }

        if ((this.radio_topic == "age") && (this.radio_scale == "provi")) {
          this.map.addLayer(this.age_provi)
          this.now_layer = this.age_provi
        }
        else if ((this.radio_topic == "age") && (this.radio_scale == "city")) {
          this.map.addLayer(this.age_city)
          this.now_layer = this.age_city
        }
        else if ((this.radio_topic == "age") && (this.radio_scale == "dist")) {
          this.map.addLayer(this.age_dist)
          this.now_layer = this.age_dist
        }
        else if ((this.radio_topic == "disage") && (this.radio_scale == "provi")) {
          this.map.addLayer(this.age_dis_provi)
          this.now_layer = this.age_dis_provi
        }
        else if ((this.radio_topic == "disage") && (this.radio_scale == "city")) {
          this.map.addLayer(this.age_dis_city)
          this.now_layer = this.age_dis_city
        }
        else if ((this.radio_topic == "disage") && (this.radio_scale == "dist")) {
          this.map.addLayer(this.age_dis_dist)
          this.now_layer = this.age_dis_dist
        }
        else if ((this.radio_topic == "count") && (this.radio_scale == "provi")) {
          this.map.addLayer(this.count_provi)
          this.now_layer = this.count_provi
        }
        else if ((this.radio_topic == "count") && (this.radio_scale == "city")) {
          this.map.addLayer(this.count_city)
          this.now_layer = this.count_city
        }
        else if ((this.radio_topic == "count") && (this.radio_scale == "dist")) {
          this.map.addLayer(this.count_dist)
          this.now_layer = this.count_dist
        }
        if (this.show_hotmap) {
          this.map.removeLayer(this.hotmap)
          this.show_hotmap = false
        }
        

      }
    },
    top_change() { this.top_scale_change() },
    scale_change() { this.top_scale_change() },
  },
  mounted() {

    this.map = new Map({
      view: new View({
        center: [114, 30],
        zoom: 10,
        projection: 'EPSG:4326'
      }),
      target: 'topic_map',

    });
    this.map.addLayer(new Tile({
      title: "路网",
      source: new XYZ({
        url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}"
      })
    }))
    this.age_city = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_city',
        }
      })
    })
    this.age_dist = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_dist',
        }
      })
    })
    this.age_provi = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_provi',
        }
      })
    })
    this.age_dis_city = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_dis_city',
        }
      })
    })
    this.age_dis_dist = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_dis_dist',
        }
      })
    })
    this.age_dis_provi = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:age_dis_provi',
        }
      })
    })
    this.count_city = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:count_city',
        }
      })
    })
    this.count_dist = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:count_dist',
        }
      })
    })
    this.count_provi = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:count_provi',
        }
      })
    })
    this.dis_point = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:dis_point',
        }
      })
    })
    this.dis_point.setZIndex(99)
    this.hotmap = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:hotmap',
        }
      })
    })
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

#topic_map {
  width: 90vw;
  height: 80vh;
}</style>
