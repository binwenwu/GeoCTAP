<template>
  <div style="line-height: 60px; display: flex; justify-content: space-between">
    <div>
      <span
          :class="collapseBtnClass"
          style="cursor: pointer; font-size: 18px"
          @click="collapse"
      ></span>

      <el-breadcrumb
          separator="/"
          style="display: inline-block; margin-left: 10px"
      >
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <span style="font-size: 24px; font-weight: bold; color: #072959;text-shadow: 2px 2px 8px rgba(0,0,0,0.5); letter-spacing: 2px;
    background: linear-gradient(45deg, #072959 30%, #085BA7 90%);-webkit-background-clip: text;
    -webkit-text-fill-color: transparent;">儿童拐卖分析宣传一体化平台后台管理系统</span>  <!-- 添加标题 -->
    <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
      <div style="display: inline-block">
        <img
            :src="user.avatarUrl"
            alt=""
            style="
            width: 30px;
            border-radius: 50%;
            position: relative;
            top: 10px;
            right: 5px;
          "
        />
        <span>{{ user.nickname }}</span
        ><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu
          slot="dropdown"
          style="width: 100px; text-align: center"
      >
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/password">修改密码</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/person">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item class="logout" style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object,
  },
  computed: {
    currentPathName() {
      return this.$store.state.currentPathName; //需要监听的数据
    },
  },
  data() {
    return {};
  },
  methods: {
    collapse() {
      // this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
      this.$emit("asideCollapse");
    },
    logout() {
      this.$store.commit("logout");
      this.$message.success("退出成功");
    },
  },
};
</script>

<style scoped>
.logout:hover {
  color:black
}
</style>
