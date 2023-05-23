<template>
  <!-- 使用components中UserHeader.vue和views/front中的其他六个vue文件实现 -->
  <el-container style="min-height: 100vh">
    <el-header height="12vh">
      <UserHeader></UserHeader>
    </el-header>
    <el-main>
      <router-view style="color: white;"/>
    </el-main>
  </el-container>
</template>

<script>
import UserHeader from "@/components/UserHeader";

export default {
  name: "LargeScreen",
  data() {
    return {
    }
  },
  components: {
    UserHeader,
  },
  created() {
  },
  methods: {
  }
}
</script>

<style scoped>
.el-container {
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  height: 100vh;
  background: url("../../assets/Bg.png") no-repeat;
  /* 以父元素的百分比来设置背景图像的宽度和高度。*/
  background-size: 100% 100%;
  overflow: hidden;
  user-select: none;
}
.el-main {
  background-image: url(../../assets/card.png);
  /* 以父元素的百分比来设置背景图像的宽度和高度。*/
  background-size: 100% 100%;
}
</style>
