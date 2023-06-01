<template>
  <el-card class="box-card-total" shadow="never">
    <el-button class="view" @click="drawer = true">
      点我打开
    </el-button>

    <el-drawer title="地图查询" :visible.sync="drawer" direction="rtl" :before-close="search_btn" :modal="false">
      <div>
        <span>年龄</span>
        <span><el-slider v-model="age" range show-stops :max="100"></el-slider></span>
        <span>身高</span>
        <span><el-slider v-model="height" range :max="200"></el-slider></span>
        <span><el-checkbox v-model="allow_null_height">允许空值</el-checkbox></span>
        <span>走丢省份</span>
        <span><el-select v-model="selected_province" placeholder="请选择省份" :popper-append-to-body="false">
            <el-option v-for="province in provinces" :label="province" :value="province" :key="province"
              @click.native="selected_city = '请选择城市'"></el-option>
          </el-select></span>
        <span>走丢城市</span>
        <span><el-select v-model="selected_city" placeholder="请选择城市" :popper-append-to-body="false">
            <el-option v-for="city in citys[selected_province]" :label="city" :value="city" :key="city"></el-option>
          </el-select></span>
        <span>性别</span>
        <span><el-select v-model="selected_sex" placeholder="请选择性别" :popper-append-to-body="false">
            <el-option v-for="sex in ['请选择性别', '男', '女']" :label="sex" :value="sex" :key="sex"></el-option>
          </el-select></span>
        <span>走丢日期</span>
        <span><el-date-picker v-model="lost_date" type="daterange" unlink-panels range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker></span>
      </div>
    </el-drawer>


    <!-- <div>
      <table id="select_table">
        <tr>
          <td>年龄</td>
          <td><el-slider v-model="age" range show-stops :max="100" style="float: left;width: 60%;"></el-slider></td>
        </tr>
        <tr>
          <td>身高</td>
          <td>
            <el-slider v-model="height" range :max="200" style="float: left;width: 60%;"></el-slider>
            <el-checkbox v-model="allow_null_height">允许空值</el-checkbox>
          </td>
        </tr>
        <tr>
          <td>走丢省份</td>
          <td><el-select v-model="selected_province" placeholder="请选择省份" >
              <el-option v-for="province in provinces" :label="province" :value="province" :key="province"
                @click.native="selected_city = '请选择城市'"></el-option>
            </el-select></td>
        </tr>
        <tr>
          <td>走丢城市</td>
          <td><el-select v-model="selected_city" placeholder="请选择城市">
              <el-option v-for="city in citys[selected_province]" :label="city" :value="city" :key="city"></el-option>
            </el-select></td>
        </tr>
        <tr>
          <td>性别</td>
          <td><el-select v-model="selected_sex" placeholder="请选择性别">
              <el-option v-for="sex in ['请选择性别', '男', '女']" :label="sex" :value="sex" :key="sex"></el-option>
            </el-select></td>
        </tr>
        <tr>
          <td>走丢日期</td>
          <td><el-date-picker v-model="lost_date" type="daterange" unlink-panels range-separator="至"
              start-placeholder="开始日期" end-placeholder="结束日期">
            </el-date-picker></td>
        </tr>
        <tr>
          <td><el-button @click="search_btn">查询</el-button></td>
          <td></td>
        </tr>

      </table>

    </div> -->
    <div id="query_map"></div>

    <div id="child_info"> </div>
    <div id="popup" class="ol-popup">
      <a href="#" id="popup-closer" class="ol-popup-closer"></a>
      <div id="popup-content">
        <el-card class="box-card">
          <img :src="child_img" />
          <div v-for="o in child_info_list" class="text item">
            {{ o }}
          </div>
        </el-card>
      </div>
    </div>
  </el-card>
</template>
<script>
import { Map, View, Overlay } from 'ol'
import { toStringHDMS } from 'ol/coordinate'
import { XYZ, TileWMS } from 'ol/source'
import { toLonLat } from 'ol/proj'
import { Tile } from 'ol/layer'
import { Select } from 'ol/interaction'
import { equalTo, and, during } from 'ol/format/filter'

import {defaults as defaultControls} from 'ol/control'

export default {
  name: 'Query',
  data() {
    return {
      child_info_list: [],
      child_img:"",
      container: HTMLElement,
      content: HTMLElement,
      closer: HTMLElement,
      drawer: false,
      allow_null_height: true,
      height: [0, 200],
      map: Map,
      select_wmslayer: undefined,
      selected_city: "请选择城市",
      selected_province: "请选择省份",
      lost_date: undefined,
      selected_sex: "请选择性别",
      age: [0, 100],
      provinces: ["请选择省份", "北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "台湾省", "香港特别行政区", "澳门特别行政区",],
      citys: {
        "请选择省份": ["请选择城市"],
        "北京市": ["请选择城市", "东城区", "西城区", "朝阳区", "丰台区", "石景山区", "海淀区", "门头沟区", "房山区", "通州区", "顺义区", "昌平区", "大兴区", "怀柔区", "平谷区", "密云区", "延庆区"], "天津市": ["请选择城市", "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "东丽区", "西青区", "津南区", "北辰区", "武清区", "宝坻区", "滨海新区", "宁河区", "静海区", "蓟州区"], "河北省": ["请选择城市", "石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"], "山西省": ["请选择城市", "太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"], "内蒙古自治区": ["请选择城市", "呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟"], "辽宁省": ["请选择城市", "沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"], "吉林省": ["请选择城市", "长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市", "延边朝鲜族自治州"], "黑龙江省": ["请选择城市", "哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市", "大兴安岭地区"], "上海市": ["请选择城市", "黄浦区", "徐汇区", "长宁区", "静安区", "普陀区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "奉贤区", "崇明区"], "江苏省": ["请选择城市", "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市"], "浙江省": ["请选择城市", "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"], "安徽省": ["请选择城市", "合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市", "宿州市", "六安市", "亳州市", "池州市", "宣城市"], "福建省": ["请选择城市", "福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"], "江西省": ["请选择城市", "南昌市", "景德镇市", "萍乡市", "九江市", "新余市", "鹰潭市", "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"], "山东省": ["请选择城市", "济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"], "河南省": ["请选择城市", "郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市", "济源市"], "湖北省": ["请选择城市", "武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市", "恩施土家族苗族自治州", "仙桃市", "潜江市", "天门市", "神农架林区"], "湖南省": ["请选择城市", "长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市", "娄底市", "湘西土家族苗族自治州"], "广东省": ["请选择城市", "广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"], "广西壮族自治区": ["请选择城市", "南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"], "海南省": ["请选择城市", "海口市", "三亚市", "三沙市", "儋州市", "五指山市", "琼海市", "文昌市", "万宁市", "东方市", "定安县", "屯昌县", "澄迈县", "临高县", "白沙黎族自治县", "昌江黎族自治县", "乐东黎族自治县", "陵水黎族自治县", "保亭黎族苗族自治县", "琼中黎族苗族自治县"], "重庆市": ["请选择城市", "万州区", "涪陵区", "渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "綦江区", "大足区", "渝北区", "巴南区", "黔江区", "长寿区", "江津区", "合川区", "永川区", "南川区", "璧山区", "铜梁区", "潼南区", "荣昌区", "开州区", "梁平区", "武隆区", "城口县", "丰都县", "垫江县", "忠县", "云阳县", "奉节县", "巫山县", "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县"], "四川省": ["请选择城市", "成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市", "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"], "贵州省": ["请选择城市", "贵阳市", "六盘水市", "遵义市", "安顺市", "毕节市", "铜仁市", "黔西南布依族苗族自治州", "黔东南苗族侗族自治州", "黔南布依族苗族自治州"], "云南省": ["请选择城市", "昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市", "楚雄彝族自治州", "红河哈尼族彝族自治州", "文山壮族苗族自治州", "西双版纳傣族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"], "西藏自治区": ["请选择城市", "拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市", "阿里地区"], "陕西省": ["请选择城市", "西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"], "甘肃省": ["请选择城市", "兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市", "临夏回族自治州", "甘南藏族自治州"], "青海省": ["请选择城市", "西宁市", "海东市", "海北藏族自治州", "黄南藏族自治州", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州"], "宁夏回族自治区": ["请选择城市", "银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"], "新疆维吾尔自治区": ["请选择城市", "乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市", "昌吉回族自治州", "博尔塔拉蒙古自治州", "巴音郭楞蒙古自治州", "阿克苏地区", "克孜勒苏柯尔克孜自治州", "喀什地区", "和田地区", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市", "北屯市", "铁门关市", "双河市", "可克达拉市", "昆玉市", "胡杨河市"], "香港特别行政区": ["请选择城市", "中西区", "湾仔区", "东区", "南区", "油尖旺区", "深水埗区", "九龙城区", "黄大仙区", "观塘区", "荃湾区", "屯门区", "元朗区", "北区", "大埔区", "西贡区", "沙田区", "葵青区", "离岛区"], "澳门特别行政区": ["请选择城市", "花地玛堂区", "花王堂区", "望德堂区", "大堂区", "风顺堂区", "嘉模堂区", "路凼填海区", "圣方济各堂区"
        ]
      }
    }
  },
  created() {
  },
  computed: {
  },
  methods: {
    search_btn() {
      this.drawer = false
      console.log(`${this.selected_city}\n
      ${this.selected_province}\n
      ${this.lost_date}\n
      ${this.selected_sex}\n
      ${this.age}`)
      // var cql=""
      var now_year = (new Date()).getFullYear()
      //   filter_conditions.push(during('birthday',(new Date(now_year - this.age[1], 0, 1)).toISOString(), (new Date(now_year - this.age[0], 11, 31)).toISOString()))
      var cql = "birthday after " + (new Date(now_year - this.age[1], 0, 1)).toISOString() + " and birthday before " + (new Date(now_year - this.age[0], 11, 31)).toISOString()
      // var filter_conditions = []


      if (this.allow_null_height) {
        cql += `and ((height>=${this.height[0]} and height <=${this.height[1]}) or height is null)`
      }
      else {
        cql += `and height>=${this.height[0]} and height <=${this.height[1]}`
      }

      if (this.selected_province != "请选择省份") {
        // filter_conditions.push(equalTo('dis_provi', this.selected_province))
        cql += "and dis_provi = '" + this.selected_province + "'"
      }
      if (this.selected_sex != "请选择性别") {
        //   filter_conditions.push(equalTo('sex', this.selected_sex))
        cql += "and sex = '" + this.selected_sex + "'"
      }



      if (this.selected_city != "请选择城市") {
        if (this.selected_province.endsWith('省')) {
          //     filter_conditions.push(equalTo('dis_city', this.selected_city))
          cql += "and dis_city='" + this.selected_city + "'"
        }
        else {
          //     filter_conditions.push(equalTo('dis_dist', this.selected_province))
          cql += "and dis_dist='" + this.selected_city + "'"
        }
      }
      if (this.select_wmslayer != undefined) {
        this.map.removeLayer(this.select_wmslayer)
      }
      // var sum_filter
      // if (filter_conditions.length==1){
      //   sum_filter=filter_conditions[0]
      // }
      // else{
      //   sum_filter=and(...filter_conditions)
      // }
      // console.log(sum_filter.toString())

      var select_wms = new TileWMS({
        url: 'http://125.220.153.26:31520/geoserver/GISProj/wms', // GeoServer 的 WMS 服务地址
        params: {
          'LAYERS': 'GISProj:children', // 指定图层的名称，格式为 "workspace:layer_name"
          // 'VERSION': '1.1.0'
          env: 'color:0000FF;name:triangle;size:12',  //重点处：修改点状符号为：蓝色、三角形、大小为12
          // filter: JSON.stringify(sum_filter)
          CQL_FILTER: cql,
          // CQL_FILTER: "birthday after 2000-05-24T09:20:12.507Z and birthday before 2023-05-24T09:20:12.507Z and dis_provi = '湖北省'"
        }
      })
      this.select_wmslayer = new Tile({
        source: select_wms
      })
      this.map.addLayer(this.select_wmslayer)
    },
    show_info(datatext) {
      var feat = JSON.parse(datatext).features
      if (feat.length > 0) {
        var pro = feat[0].properties
        //         document.getElementById('child_info').innerHTML = `
        //       <img src="${pro.photo}">
        //       <table>
        // <tr><td <td style='width:20%'>姓名</td><td <td style='width:80%'>${pro.child_name}</td></tr>
        // <tr><td>性别</td><td>${pro.sex}</td></tr>
        // <tr><td>走丢地点</td><td>${pro.dis_place}</td></tr>
        // <tr><td>走丢时间</td><td>${pro.dis_time.split('T')[0]}</td></tr>
        // <tr><td>走丢类型</td><td>${pro.dis_type}</td></tr>
        // <tr><td>特征</td><td>${pro.feature}</td></tr>
        // <tr><td>身高</td><td>${pro.height == null ? '未知' : pro.height}CM</td></tr>
        // <tr><td>其他信息</td><td>${pro.other_info}</td></tr>
        // <tr><td>所在地</td><td>${pro.place}</td></tr>
        // <tr><td>注册时间</td><td>${pro.reg_time.split('T')[0]}</td></tr>
        // <tr><td>志愿者</td><td>${pro.volunteer}</td></tr>
        // </table>`
        this.child_img=pro.photo
        this.child_info_list = [
          `姓名：${pro.child_name}`,
          `性别：${pro.sex}`,
          `走丢地点：${pro.dis_place}`,
          `走丢时间：${pro.dis_time.split('T')[0]}`,
          `走丢类型：${pro.dis_type}`,
          `特征：${pro.feature}`,
          `身高：${pro.height == null ? '未知' : pro.height}CM`,
          `其他信息：${pro.other_info}`,
          `所在地：${pro.place}`,
          `注册时间：${pro.reg_time.split('T')[0]}`,
          `志愿者：${pro.volunteer}`,


        ]
      } else {
        this.child_info_list = []
        this.child_img=""
      }
    }

  },
  mounted() {
    this.container = document.getElementById('popup');
    this.content = document.getElementById('popup-content');
    this.closer = document.getElementById('popup-closer');

    //路网
    var tian_di_tu_road_layer = new Tile({
      title: "底图",
      source: new XYZ({
        url: "http://t3.tianditu.com/DataServer?T=img_w&tk=85c9d12d5d691d168ba5cb6ecaa749eb&x={x}&y={y}&l={z}"
      })
    });
    var tian_di_tu_mark = new Tile({
      title: "路网",
      source: new XYZ({
        url: "http://t3.tianditu.com/DataServer?T=cia_w&tk=85c9d12d5d691d168ba5cb6ecaa749eb&x={x}&y={y}&l={z}"
      })
    });
    var overlay = new Overlay({
      element: this.container,
      autoPan: {
        animation: {
          duration: 250,
        },
      },
    });
    this.closer.onclick = function () {
      overlay.setPosition(undefined);
      closer.blur();
      return false;
    };
    var view = new View({
      // 地图中心点的经纬度
      center: [114, 30],
      zoom: 10, // 初始缩放级别
      projection: 'EPSG:4326'
    })
    this.map = new Map({
      view: view,
      overlays: [overlay],
      target: 'query_map', // 指定地图渲染的目标元素 ID
      //加载控件到地图容器中
      controls: defaultControls({
          zoom: false,
          rotate: false,
          attribution: false
      })

    });
    var wms_source = new TileWMS({
      url: 'http://125.220.153.26:31520/geoserver/GISProj/wms', // GeoServer 的 WMS 服务地址
      params: {
        'LAYERS': 'GISProj:children', // 指定图层的名称，格式为 "workspace:layer_name"
        // 'VERSION': '1.1.0'
        env: 'color:FF0000;name:circle;size:6',
      }
    })
    var wmslayer = new Tile({
      source: wms_source
    })
    this.map.addLayer(tian_di_tu_road_layer)
    this.map.addLayer(tian_di_tu_mark)
    this.map.addLayer(wmslayer);
    this.map.on('singleclick', (evt) => {
      const coordinate = evt.coordinate;
      // const hdms = toStringHDMS(toLonLat(coordinate));

      // this.content.innerHTML = '<p>You clicked here:</p><code>' + hdms + '</code>';

      console.log('点了')
      var viewResolution = /** @type {number} */ (view.getResolution());
      var url = wms_source.getFeatureInfoUrl(
        evt.coordinate, viewResolution, 'EPSG:4326',
        { 'INFO_FORMAT': 'application/json' });
      if (url) {
        fetch(url)
          .then((response) => response.text()).then((datatext) => {
            // console.log(this)
            this.show_info(datatext)
            if (this.child_info_list.length>0) {
              console.log(this.child_info_list)
              overlay.setPosition(coordinate);
            }
            else{
              overlay.setPosition(undefined);
            }
          })

      }
    });

  },
}
</script>
<style scoped>
.box-card-total {
  background-color: transparent;
  height: 100%;
  width: 100%;
  border: none;
}
#query_map {
width:100%;
height:78vh;
border: 1px solid white;
box-shadow:-6px 0px 6px rgba(35,135,193,0.8),   /*左边阴影*/
0px -6px 6px rgba(35,135,193,0.8),  /*上边阴影*/
6px 0px 6px rgba(35,135,193,0.8),  /*右边阴影*/
0px 6px 6px rgba(35,135,193,0.8); /*下边阴影*/
}

/* #select_table {
  float: left;
  width: 50%;
} */

.el-select {
  width: 50%;
  background-color: transparent;
}

.el-date-picker {
  width: 50%;
}

span {
  white-space: nowrap;
  color: #000;
  display: block
}

.el-drawer {
  text-align: center;
}
.el-drawer div {
  margin-left: 5%;
  margin-right: 5%;
}

.ol-popup {
  position: absolute;
  background-color: transparent;;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  padding: 15px;
  border-radius: 10px;
  border: 1px solid #cccccc;
  bottom: 12px;
  left: -50px;
  min-width: 20vw;
  width: 30vw;
}
::v-deep .el-drawer {
  background-color: rgba(0, 0, 0, 0.2) !important;
  border-left: 1px solid rgba(35,135,193,0.5);
  box-shadow:-5px 0px 5px rgba(35,135,193,0.5);
}
::v-deep .el-drawer__header{
  color: white;
  font-weight: bold;
  font-size: calc(24*100vw/1920);
}
.el-drawer__body span{
  color: white;
  margin-top: 3vh;
  font-size: calc(16*100vw/1920);
}
::v-deep .el-checkbox__label{
  color: white;
}
.el-card__body{
  float:left;
}
.box-card {
  color: white;
  background-color: rgba(0,150,255,0.2);
}
.ol-popup:after,
.ol-popup:before {
  top: 100%;
  border: solid transparent;
  content: " ";
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
}

.ol-popup:after {
  border-top-color: white;
  border-width: 10px;
  left: 48px;
  margin-left: -10px;
}

.ol-popup:before {
  border-top-color: #cccccc;
  border-width: 11px;
  left: 48px;
  margin-left: -11px;
}

.ol-popup-closer {
  text-decoration: none;
  position: absolute;
  top: 2px;
  right: 8px;
}

.ol-popup-closer:after {
  content: "✖";
}

.view {
  background-color: transparent;
  color: white;
  position: absolute;
  top: 18vh;
  left: 3vw;
  z-index: 999;
}

.el-input,
::v-deep .el-input__inner {
  background-color: transparent !important;
  padding: 1px;
  color: white;
}
::v-deep .el-select-dropdown {
  background-color: rgba(64, 158, 255,0.6)
}
::v-deep .el-select-dropdown__item{
  color: white;
  font-size: calc(100vw * 16 /1920);
}
::v-deep .el-select-dropdown__item.hover {
  background-color: transparent;
  opacity: 0.8;
}
::v-deep .el-range-input{
  background-color: transparent;
  height: 30px;
	color:#ffffff;
}
::v-deep .el-date-editor .el-range__icon {
	line-height: 24.4px;
}
</style>
