<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>欢迎你！{{ user.nickname }}</b>
    </div>
    <el-card>
      Made By —— 珞珈壹佰叁
      <el-divider />
      <div style="padding: 20px 0; font-size: 20px">项目简介：。。。。。。</div>
    </el-card>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>
    <el-row :gutter="30">
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px"><b>GIS工程设计与开发前后端分离项目框架</b></div>
        <div class="m-10">
         <el-button type="danger"><i class="el-icon-coin"></i> 免费开源</el-button>
          <el-button type="primary"><i class="el-icon-link"></i> <a style="color: #fff" href="https://github.com/BinwenWu/GeoCTAP">Github源码</a></el-button>
        </div>
        <img src="@/assets/github.png" alt="" style="margin: 10px 0">
      </el-col>
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px"><b>技术栈</b></div>
        <el-row>
          <el-col :span="12" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>SpringBoot2</div>
            <div>Hutool</div>
            <div>Apache POI</div>
            <div>Lombok</div>
            <div>Mybatis/Mybatis-plus</div>
          </el-col>
          <el-col :span="12" style="line-height: 30px">
            <b>前端</b>
            <div>Vue2</div>
            <div>Vue-Router</div>
            <div>VueX</div>
            <div>ElementUI</div>
            <div>Axios</div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>
