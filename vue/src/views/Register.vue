<template>
  <div class="wrapper">
    <div class="loginHeader">
      儿童拐卖分析宣传一体化平台（GeoCTAP）
    </div>
    <div class="loginBody">
      <div style="margin: 2vh 0; text-align: center; font-size: calc(100vw * 25 / 1920);"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <span class="passwordTip">账号：</span>
          <el-input placeholder="请输入账号" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <span class="passwordTip">密码：</span>
          <el-input placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <span class="passwordTip">确认密码：</span>
          <el-input placeholder="请确认密码" size="medium" prefix-icon="el-icon-lock" show-password
                    v-model="user.confirmPassword" @keyup.native.enter="register"></el-input>
        </el-form-item>
        <el-form-item style="margin: 1vh 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="register">注册</el-button>
          <el-button type="info" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200') {
              this.$message.success("注册成功")
              this.$router.push("/login")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
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
  font-size: calc(100vw * 36 / 1920);;
  font-weight: bold;
  background: url("../assets/largeTitle.png") no-repeat;
  background-size: 100% 180%;
  height: 12%;
}

.loginBody {
  color: white;
  margin: 20vh auto;
  background: url("../assets/textBg.png") no-repeat;
  background-size: 50vw 50vh;
  width: 50vw;
  height: 50vh;
  padding: 2vh 10vw;
}

.passwordTip {
  font-size: calc(100vw * 16 / 1920);
}

div >>> .el-input > input {
  height: 4vh;
}

.el-form-item {
  height: 8vh;
}

div >>> .el-button {
  height: 4vh;
}
</style>
