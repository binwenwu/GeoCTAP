**中文** | [English](./README.md)

## 目录

- `sql`：MySQL数据库SQL文件
- `vue`：前端
- `spb-admin-server`：项目监视服务
- `src`：后端
- `files`：存放用户上传文件
- `Redis-x64-5.0.14.1.zip`： Redis安装包


## 简介
> 考虑到目前现有儿童防拐卖平台所具备的优点与缺憾以及本项目在全国打击拐卖犯罪行为，找回失踪儿童领域的地位和作用，设立总的建设目标为：利用移动互联网、大数据、云计算、物联网、时空数据挖掘、GIS工程开发、故事设计等先进技术，以故事讲述的形式宣传儿童防拐的预防措施并共情儿童被拐的家长心情，感染公众，让每个公众作为志愿者参与进服务平台；同时实时更新失踪被拐儿童信息特点与故事线，针对故事线的每个场景进行线索分析。

## 技术栈

**后端：**

- [Springboot](https://spring.io/projects/spring-boot) - *Spring Boot*是一个基于Spring提供了开箱即用的一组套件
- [Hutool](https://hutool.cn/) - Hutool 一个功能丰富且易用的Java工具库
- [Apache POI](https://poi.apache.org/) - 提供纯 Java 库用于读写 Microsoft Office 格式的文件
- [Lombok](https://projectlombok.org/) - Project Lombok是一个java库，它可以自动插入编辑器和构建工具，为您的java锦上添花。
- [Mybatis/Mybatis-plus](https://mybatis.org/mybatis-3/) - MyBatis 是一个 Java 持久性框架

**前端：**

- [Vue2](https://v2.vuejs.org/) - 一个开源模型-视图-视图模型前端 JavaScript 库
- [VueX](https://vuex.vuejs.org/) - *Vuex*是*Vue.js*应用程序的状态管理工具
- [Vue-Router](https://router.vuejs.org/zh/) - 为 *Vue.js* 提供富有表现力、可配置的、方便的路由
- [ElementUI](https://element.eleme.io/#/zh-CN) - 一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的桌面端组件库
- [Axios](https://github.com/axios/axios) - 用于浏览器和node.js的基于Promise的HTTP客户端

**测试：**

- [Postman](https://www.postman.com/) - Postman是一个用于构建和使用API的API平台。
- [Swagger](https://swagger.io/) - 所有人的API开发

## 功能清单

- 登录注册
- 基于RBAC的权限模型（可分配角色和菜单权限）
- 单表增删改查（包括分页模糊查询）
- 登录验证码
- 文件上传
- 导入导出
- 服务器部署
- 文本编辑器
- 树形表格（数据可作为树形菜单使用）
- JWT权限
- 后端1对多查询
- 批量删除
- 地图服务
- 在线留言
- 个人头像
- 修改密码
- 整合Echarts图表

## 使用 Gitpod

在Gitpod（GitHub的免费在线开发环境）中打开项目并立即开始编码。

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/anncwb/vue-vben-admin)


## 安装和使用

- 获取项目代码

```bash
git clone https://github.com/binwenwu/GeoCTAP.git
```

- 安装依赖

```bash
cd GeoCTAP/vue

npm install
```

- 本地运行（前端）

```bash
npm run serve
```

- 启动后端Springboot项目

```TEXT
src/main/java/com/gis/geoctap/SpringbootApplication.java
```

## 展示

- 数据监测

![2](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/2.png)

- 轨迹分析

![3](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/3.png)

- 数据查询

![4](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/4.png)

- 故事地图

![5](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/5.png)

- 专题地图

![6](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/6.png)

- 时空分析

![7](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/7.png)

- 后台管理

![1](https://cdn.jsdelivr.net/gh/binwenwu/picgo_demo/img/1.png)

## 浏览器支持

建议使用`Chrome 80+`浏览器进行本地开发

支持现代浏览器，不包括IE

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|                         not support                          |                       last 2 versions                        |                       last 2 versions                        |                       last 2 versions                        |                       last 2 versions                        |

## 维护人员

[@Binwen Wu](https://github.com/binwenwu)
