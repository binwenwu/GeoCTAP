<template>
  <!-- 使用components中UserHeader.vue和views/front中的其他六个vue文件实现 -->
  <el-container style="min-height: 100vh">
    <el-header height="12vh">
      <UserHeader></UserHeader>
    </el-header>
    <el-main>
      <router-view style="color: white;" @refreshUser="getUser"/>
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
    // 从后台获取最新的User数据
    this.getUser()
  },
  methods: {
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }
  }
}
</script>

<style scoped>
.el-container {
  height: 100vh;
  background: url("../../assets/Bg.png") no-repeat;
  /* 以父元素的百分比来设置背景图像的宽度和高度。*/
  background-size: 100% 100%;
  overflow: hidden;
}
</style>
