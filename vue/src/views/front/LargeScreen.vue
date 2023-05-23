<template>
  <!-- 使用components中UserHeader.vue和views/front中的其他六个vue文件实现 -->
  <div>这是前端大屏
    <router-view/>
  </div>
</template>

<script>
import { dataTool } from 'echarts';
import UserHeader from "@/components/UserHeader";
import Watch from "./Watch.vue";
import Network from "./Network.vue";
import Query from "./Query.vue";
import HistoryMap from "./HistoryMap.vue";
import TopicMap from "./TopicMap.vue";
import Analysis from "./Analysis.vue";
import router from '../../router';

export default {
  name: "LargeScreen",
  data() {
    return {
      
    }
  },
  components: {
    UserHeader,
    Watch,
    Network,
    Query,
    HistoryMap,
    TopicMap,
    Analysis,
    router
}
}
</script>

<style scoped>

</style>
