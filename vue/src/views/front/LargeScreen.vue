<template>
  <!-- 使用components中UserHeader.vue和views/front中的其他六个vue文件实现 -->
  <el-container style="min-height: 100vh">
    <el-header height="12vh" ref="header">
      <UserHeader></UserHeader>
    </el-header>
    <a ref="fScreen" class="navi-a" :style="{'top':fSPosition}">
      <i ref="icon" :class="iconStyle" @click="fullScreen"></i>
    </a>
    <el-main>
      <router-view style="color:white;"/>
    </el-main>
  </el-container>
</template>

<script>
import UserHeader from "@/components/UserHeader";

export default {
  name: "LargeScreen",
  data() {
    return {
      fSPosition: "15vh",
      iconStyle: "el-icon-full-screen",
      displayStatus: "block",
    }
  },
  components: {
    UserHeader,
  },
  created() {
  },
  methods: {
    fullScreen() {
      // console.log(this.$refs.header.$el)
      this.displayStatus = this.displayStatus === "block" ? "none" : "block";
      this.$refs.header.$el.style.display = this.displayStatus;
      this.fSPosition = this.fSPosition === "15vh" ? "3vh" : "15vh";
      this.$refs.fScreen.style.top = this.fSPosition;
      this.$refs.icon.classList.remove(this.iconStyle)
      this.iconStyle = this.iconStyle === "el-icon-full-screen" ? "el-icon-close" : "el-icon-full-screen"
    }
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
.navi-a {
  color: white;
  position: absolute;
  right: calc(100vw * 32 / 1920);
  /* top: 15vh; */
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
</style>
