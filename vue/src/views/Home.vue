<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>欢迎你！{{ user.nickname }}</b>
    </div>
    <el-card>
      Made By —— 珞珈壹佰叁
      <el-divider />
      <div style="padding: 15px 0; font-size: 18px">
        <b>项目目标：</b><br></br>
        <p>
          考虑到目前现有儿童防拐卖平台所具备的优点与缺憾以及本项目在全国打击拐卖犯罪行为，找回失踪儿童领域的地位和作用，
          设立总的建设目标为：利用移动互联网、大数据、云计算、物联网、时空数据挖掘、GIS工程开发、故事设计等先进技术，
          以故事讲述的形式宣传儿童防拐的预防措施并共情儿童被拐的家长心情，感染公众，让每个公众作为志愿者参与进服务平台；
          同时实时更新失踪被拐儿童信息特点与故事线，针对故事线的每个场景进行线索分析，利用历史大数据分析设立地理围栏预警范围，
          预测儿童失踪路线，规划警力部署与搜救工作的开展，建立儿童从救援前中后的全过程全周期的预防打击体系，
          构建全方位的儿童安全监测预警与救援云服务平台。平台核心立足于公益组织、公安打拐办部门，
          同时统筹调度公安警务人员（民警、交警、巡警、治安警等）等社会资源，实现对拐卖犯罪大数据分析、儿童实时监护与救援指挥多维一体化的服务体系。
        </p>

      </div>
    </el-card>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>
    <el-card>
    <el-row :gutter="30">
      <el-col :span="8">
        <div style="padding: 20px 0; font-size: 20px"><b>GIS工程设计与开发前后端分离项目框架</b></div>
        <div class="m-10">
          <el-button type="primary" style="margin-left: 50px"><i class="el-icon-link"></i> <a style="color: #fff" href="https://github.com/BinwenWu/GeoCTAP">Github源码</a></el-button>
          <el-button type="danger" style="margin-left: 120px"><i class="el-icon-link"></i> <a style="color: #fff;" href="https://gitee.com/wu-binwen123/GeoCTAP">Gitee源码</a></el-button>
        </div>
        <img src="@/assets/github.png" alt="" style="margin: 10px 0;width: 220px">
        <img src="@/assets/gitee.png" alt="" style="margin: 10px 0;width: 220px">
      </el-col>
      <el-col :span="15">
        <div style="padding: 20px 0; font-size: 20px"><b>技术栈</b></div>
        <el-row>
          <el-col :span="8" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>SpringBoot2</div>
            <div>Hutool</div>
            <div>Apache POI</div>
            <div>Lombok</div>
            <div>Mybatis/Mybatis-plus</div>
            <div>JWT</div>
          </el-col>
          <el-col :span="8" style="line-height: 30px">
            <b>前端</b>
            <div>Vue2</div>
            <div>Vue-Router</div>
            <div>VueX</div>
            <div>ElementUI</div>
            <div>Axios</div>
            <div>Echarts</div>
            <div>Openlayers</div>
          </el-col>
          <el-col :span="8" style="line-height: 30px">
            <b>其他</b>
            <div>Redis</div>
            <div>GeoServer</div>
            <div>Docker</div>
            <div>Containerd + K8S</div>
          </el-col>

        </el-row>

      </el-col>
    </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  }
}
</script>
