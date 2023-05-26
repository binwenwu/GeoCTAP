<template>
  <div class="LsHeader">
    <div class="LsHeaderTitle">
      宝贝回家——GeoCTAP
    </div>
    <el-row type="flex" justify="space-between">
      <el-col :span="2"><el-tooltip effect="light" content="点击返回初始页" placement="top-start">
        <router-link :to="{name:'Front'}" class="navi-a" style="border: 1px solid #2387c1;border-radius: 10px;padding: 3px 5px;"><i class="el-icon-back"></i></router-link>
      </el-tooltip></el-col>
      <el-col :span="2" :pull="2"><router-link class="navi-a" active-class="active" :to="{name:'Watch'}">
        <i class="el-icon-data-line"></i>数据监测</router-link></el-col>
      <el-col :span="2" :pull="3"><router-link class="navi-a" active-class="active" :to="{name:'Network'}">
        <i class="el-icon-share"></i>轨迹分析</router-link></el-col>
      <el-col :span="2" :pull="4"><router-link class="navi-a" active-class="active" :to="{name:'Query'}">
        <i class="el-icon-search"></i>数据查询</router-link></el-col>
      <el-col :span="2" :push="5"><router-link class="navi-a" active-class="active" :to="{name:'StoryMap'}">
        <i class="el-icon-map-location"></i>故事地图</router-link></el-col>
      <el-col :span="2" :push="4"><router-link class="navi-a" active-class="active" :to="{name:'TopicMap'}">
        <i class="el-icon-copy-document"></i>专题地图</router-link></el-col>
      <el-col :span="2" :push="3"><router-link class="navi-a" active-class="active" :to="{name:'Analysis'}">
        <i class="el-icon-s-operation"></i>时空分析</router-link></el-col>
      <!-- <el-col :span="2"><el-button type="primary" icon="el-icon-full-screen" style="position: relative;top:5.5vh"></el-button></el-col> -->
      <el-col :span="2"></el-col>
    </el-row>
  </div>
</template>
<script>
import Watch from "../views/front/Watch.vue";
import Network from "../views/front/Network.vue";
import Query from "../views/front/Query.vue";
import StoryMap from "../views/front/StoryMap.vue";
import TopicMap from "../views/front/TopicMap.vue";
import Analysis from "../views/front/Analysis.vue";
import router from '../router';

export default {
  name: 'UserHeader',
  data() {
    return {
    }
  },
  components: {
    Watch,
    Network,
    Query,
    StoryMap,
    TopicMap,
    Analysis,
    router
  },
  created(){
  },
  computed:{
  },
  methods:{
  },
}
</script>
<style scoped>
.LsHeader {
  color: white;
  background: url("../assets/largeTitle.png") no-repeat;
  background-size: 100% 180%;
  height: 12vh;
  letter-spacing: calc(100vw * 5 / 1920);
}
.LsHeaderTitle {
  /* flex 布局 */
  /* display: flex; */
  /*实现垂直居中*/
  /* align-items: center; */
  /*实现水平居中*/
  /* justify-content: center; */
  text-align: center;
  font-size: calc(100vw * 36 / 1920);
  font-weight: bold;
  line-height: calc(100vw * 36 / 1920);
  position: relative;
  top: 4vh;
}
.navi-a {
  color: white;
  position: relative;
  top: 5.5vh;
  padding: 5px;
}
.navi-a:hover {
  background-color: #1B6996;
  -webkit-box-shadow: 10px 10px 99px 6px rgba(27,105,150,1);
  -moz-box-shadow: 10px 10px 99px 6px rgba(27,105,150,1);
  box-shadow: 10px 10px 99px 6px rgba(27,105,150,1);
  border: white 1px solid;
  border-radius: 10px;
}
.navi-a:active {
  border: none;
}
.active {
  color: #2387c1;
}
</style>
