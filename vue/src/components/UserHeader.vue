<template>
  <div class="LsHeader">
    <div class="LsHeaderTitle">
      儿童拐卖分析宣传一体化平台
    </div>
    <router-link :to="{name:'Watch'}">儿童拐卖数据监测</router-link> | 
    <router-link :to="{name:'Network'}">儿童拐卖网络分析</router-link> | 
    <router-link :to="{name:'Query'}">儿童拐卖数据查询</router-link> | 
    <router-link :to="{name:'HistoryMap'}">儿童拐卖故事地图</router-link> | 
    <router-link :to="{name:'TopicMap'}">儿童拐卖专题地图</router-link> | 
    <router-link :to="{name:'Analysis'}">儿童拐卖时空分析</router-link>
  </div>
</template>
<script>
import Watch from "../views/front/Watch.vue";
import Network from "../views/front/Network.vue";
import Query from "../views/front/Query.vue";
import HistoryMap from "../views/front/HistoryMap.vue";
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
    HistoryMap,
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
  letter-spacing: calc(100vw * 10 / 1920);
}
.LsHeaderTitle {
  /*flex 布局*/
  display: flex;
  /*实现垂直居中*/
  align-items: center;
  /*实现水平居中*/
  justify-content: center;
  font-size: calc(100vw * 36 / 1920);;
  font-weight: bold;
}
a {
  color: white;
}
</style>