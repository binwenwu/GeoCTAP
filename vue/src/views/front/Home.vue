<template>
  <div>
    <el-button class="mt-10" type="primary" icon="el-icon-loading" plain @click="flash">刷新</el-button>
    <el-card style="margin-top: 15px">
      <div>
        <el-row :gutter="20">
          <el-col :span="3.0" v-for="item in children" :key="item.id" style="margin-bottom: 10px">
            <div style="border: 1px solid #ccc; padding-bottom: 0px">
              <div class="photo" @mouseover="zoomIn($event)" @mouseout="zoomOut($event)" @click="show(item)">
                <el-popover
                    placement="top-start"
                    width="200"
                    trigger="hover"
                    :content="getBaseInfo(item)">
                  <img slot="reference" :src="getImagePath(item.photo)" alt="" style="width: 100%;height:235px">
                </el-popover>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>


    <el-dialog title="人物信息" :visible.sync="dialogVisible" width="30%">
      <div class="photo"><img :src="getImagePath(this.item.photo)" alt="" style="width: auto;height:235px;margin-bottom: 20px"></div>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="item.childName"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="item.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-input v-model="item.birthday"></el-input>
        </el-form-item>
        <el-form-item label="登记日期">
          <el-input v-model="item.regTime"></el-input>
        </el-form-item>
        <el-form-item label="跟进志愿者">
          <el-input v-model="item.volunteer"></el-input>
        </el-form-item>
        <el-form-item label="失踪地址">
          <el-input type="textarea" v-model="item.place"></el-input>
        </el-form-item>
        <el-form-item label="特征描述">
          <el-input type="textarea" v-model="item.feature"></el-input>
        </el-form-item>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      files: [],
      children: [],
      id: 1,
      showOverlay: false, // 控制悬浮框显示和隐藏的变量
      hoveredImage: null, // 添加hoveredImage属性
      dialogVisible: false,
      item: []
    }
  },
  created() {
    this.randomID();
    this.load()
  },
  methods: {
    getImagePath(photo) {
      if (photo === null) {
        return "https://img1.baidu.com/it/u=323166819,4115343185&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=522";
      } else {
        return photo;
      }
    },
    getBaseInfo(item) {
      return item.childName + " " + item.sex
    },
    load() {
      this.request.get("/echarts/file/front/random", {
        params: {
          id: this.id
        }
      }).then(res => {
        // 这里需不需要过滤一下
        this.children = res.data
      })
    },
    flash() {

      this.randomID();
      this.load()
    },
    randomID() {
      //产生一个随机数，范围是1-39788
      this.id = Math.floor(Math.random() * 39788 + 1)
    },
    zoomIn(event) {
      this.showOverlay = true
      const img = event.target;
      const overlay = img.nextElementSibling;
      img.style.transform = "scale(1.2)"; // 放大图片
      overlay.style.opacity = "1"; // 显示悬浮框
    },
    zoomOut(event) {
      this.showOverlay = false
      const img = event.target;
      const overlay = img.nextElementSibling;
      img.style.transform = ""; // 恢复原始大小
      overlay.style.opacity = "0"; // 隐藏悬浮框
    },
    show(item){
      this.item = item;
      this.dialogVisible = true;
    }
  }
}
</script>

<style>
.photo {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.photo img {
  transition: transform 0.3s ease;
}

.photo .overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 18px;
  opacity: 0;
  transition: opacity 0.3s ease;
}


</style>
