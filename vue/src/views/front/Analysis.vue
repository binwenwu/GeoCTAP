<template>
  <div>
    <el-card class="box-card" shadow="never">
      分析模块
    </el-card>
    <div>
      <el-radio-group v-model="radio_sum" @change="sum_change">
        <el-radio-button label="grid" >空间自相关(格网分析)</el-radio-button>
        <el-radio-button label="vector" >空间自相关(矢量分析)</el-radio-button>
        <el-radio-button label="error" >误差椭圆分析</el-radio-button>
      </el-radio-group>
    </div>
    <div v-show="is_grid_an">
      <el-radio-group v-model="radio_grid"  @change="grid_change">
        <el-radio-button label="i">局部Moran’s I</el-radio-button>
        <el-radio-button label="hl">高低聚类</el-radio-button>
        <el-radio-button label="g">Getic-Ord G 冷热点</el-radio-button>
      </el-radio-group>
    </div>
    <div v-show="is_vector_an">
      <el-radio-group v-model="radio_vector"  @change="vector_change">
        <el-radio-button label="i">局部Moran’s I</el-radio-button>
        <el-radio-button label="hl">高低聚类</el-radio-button>
        <el-radio-button label="g" >Getic-Ord G 冷热点</el-radio-button>
      </el-radio-group>
    </div>
    <div id="analysis_map"></div>
  </div>
</template>
<script>

import { Map, View, Overlay } from 'ol'
import { XYZ, TileWMS } from 'ol/source'
import { Tile } from 'ol/layer'

export default {

  name: 'Analysis',
  data() {
    return {
      radio_sum: undefined,
      radio_grid: undefined,
      radio_vector: undefined,
      is_grid_an: false,
      is_vector_an: false,
      grid_getic: Tile,
      grid_moran_hl: Tile,
      grid_moran_val: Tile,
      dist_getic: Tile,
      dist_moran_hl: Tile,
      dist_moran_val: Tile,
      error_circle: Tile,
      dis_point: Tile,
      map: Map,
      now_add_layers: [],
    }
  },
  created() {
  },
  computed: {
  },
  methods: {

    remove_former_layers() {
      this.now_add_layers.forEach(e => {
        this.map.removeLayer(e)
      });
    },
    grid_an() { this.is_grid_an = true; this.is_vector_an = false },
    vector_an() { this.is_grid_an = false; this.is_vector_an = true },
    error_an() {
      this.remove_former_layers()
      this.is_grid_an = false; this.is_vector_an = false
      this.map.addLayer(this.dis_point); this.map.addLayer(this.error_circle); this.now_add_layers = [this.dis_point, this.error_circle]
    },
    grid_i_an() {
      this.remove_former_layers()
      this.map.addLayer(this.grid_moran_val)
      this.now_add_layers = [this.grid_moran_val]
    },
    grid_hl_an() {
      this.remove_former_layers()
      this.map.addLayer(this.grid_moran_hl)
      this.now_add_layers = [this.grid_moran_hl]
    },
    grid_g_an() {
      this.remove_former_layers()
      this.map.addLayer(this.grid_getic)
      this.now_add_layers = [this.grid_getic]
    },
    vector_i_an() {
      this.remove_former_layers()
      this.map.addLayer(this.dist_moran_val)
      this.now_add_layers = [this.dist_moran_val]
    },
    vector_hl_an() {
      this.remove_former_layers()
      this.map.addLayer(this.dist_moran_hl)
      this.now_add_layers = [this.dist_moran_hl]
    },
    vector_g_an() {
      this.remove_former_layers()
      this.map.addLayer(this.dist_getic)
      this.now_add_layers = [this.dist_getic]
    },
    sum_change(){
      switch (this.radio_sum) {
        case "grid":
          this.grid_an()
          break;
        case "vector":
          this.vector_an()
          break;
        case "error":
          this.error_an()
          break;
        default:
          break;
      }
    },
    grid_change(){
      switch (this.radio_grid) {
        case "i":
          this.grid_i_an()
          break;
        case "hl":
          this.grid_hl_an()
          break;
        case "g":
          this.grid_g_an()
          break;
        default:
          break;
      }

    },
    vector_change(){switch (this.radio_vector) {
        case "i":
          this.vector_i_an()
          break;
        case "hl":
          this.vector_hl_an()
          break;
        case "g":
          this.vector_g_an()
          break;
        default:
          break;
      }},

  },
  mounted() {
    this.map = new Map({
      view: new View({
        // 地图中心点的经纬度
        center: [114, 30],
        zoom: 10, // 初始缩放级别
        projection: 'EPSG:4326'
      }),
      target: 'analysis_map', // 指定地图渲染的目标元素 ID

    });
    this.map.addLayer(new Tile({
      title: "路网",
      source: new XYZ({
        url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}"
      })
    }))
    this.grid_getic = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:grid_getic',
        }
      })
    })
    this.grid_moran_hl = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:grid_moran_hl',
        }
      })
    })
    this.grid_moran_val = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:grid_moran_val',
        }
      })
    })
    this.dist_getic = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:dist_getic',
        }
      })
    })
    this.dist_moran_hl = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:dist_moran_hl',
        }
      })
    })
    this.dist_moran_val = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:dist_moran_val',
        }
      })
    })
    this.error_circle = new Tile({
      source: new TileWMS({
        url: 'http://125.220.153.26:8080/geoserver/GISProj/wms', params: {
          'LAYERS': 'GISProj:error_circle',
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

#analysis_map {
  width: 90vw;
  height: 80vh;
}</style>
