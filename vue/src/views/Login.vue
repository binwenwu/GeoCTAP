<template>
  <div class="wrapper">
    <div class="loginHeader" style="font-family: '华文楷体';font-size: 35px">
      儿童拐卖分析宣传一体化平台（GeoCTAP）
    </div>
    <!--走马图-->
    <el-main class="loginShow">
      <template>
        <el-carousel :interval="3000" type="card" :height="bannerHeight + 'px'" indicator-position="none">
          <el-carousel-item v-for="(img,index) in imgList" :key="index">
            <img :src=img.url width="100%" height="100%">
          </el-carousel-item>
        </el-carousel>
      </template>
    </el-main>
    <div class="loginBody">
      <div style="margin: 15px 0; text-align: center; font-size: 1.5rem;"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          用户名：<el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          密码：<el-input size="medium" prefix-icon="el-icon-lock" show-password
                       v-model="user.password" @keyup.native.enter="login"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="info" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
      },
      //走马灯
      imgList:[
        {
          url: require('../assets/picture1.png')
        },
        {
          url: require('../assets/picture2.png')
        },
        {
          url: require('../assets/picture3.png')
        },
        {
          url: require('../assets/picture4.png')
        }
      ],
      // 图片父容器高度
      bannerHeight :1000,
      // 浏览器宽度
      screenWidth :0,
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
              // 动态设置当前用户的路由
              setRoutes()
              this.$message.success("登录成功")

              if (res.data.role === 'ROLE_NORMAL') {
                this.$router.push("/front/home")
                //this.$router.push("/")
              } else {
                this.$router.push("/")
              }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    setSize() {
      // 通过浏览器宽度(图片宽度)计算高度
      this.bannerHeight = 350 / 1920 * this.screenWidth;
    },
  },
  mounted() {
    // 首次加载时,需要调用一次
    this.screenWidth = window.innerWidth;
    this.setSize();
    // 窗口大小发生改变时,调用一次
    window.onresize = () => {
      this.screenWidth = window.innerWidth;
      this.setSize();
    }
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background: url("../assets/Bg.png") no-repeat;
  /* 以父元素的百分比来设置背景图像的宽度和高度。*/
  background-size: 100% 100%;
  overflow: hidden;
}
.loginHeader {
  /*flex 布局*/
  display: flex;
  /*实现垂直居中*/
  align-items: center;
  /*实现水平居中*/
  justify-content: center;
  color: white;
  font-size: 1.75rem;
  font-weight: bold;
  background: url("../assets/largeTitle.png") no-repeat;
  background-size: 100% 180%;
  height: 12%;
}
.loginShow {
  margin-top: 1rem;
}
.loginBody {
  color: white;
  margin: 10px auto;
  background: url("../assets/textBg.png") no-repeat;
  background-size: 600px 320px;
  width: 600px;
  height: 320px;
  padding: 20px 40px;
}
</style>
