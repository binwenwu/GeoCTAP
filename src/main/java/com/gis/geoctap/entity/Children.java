package com.gis.geoctap.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author 吴斌文
 * @since 2023-05-22
 */
@Getter
@Setter
  @ApiModel(value = "Children对象", description = "")
public class Children implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名字")
    private String childName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("注册时间")
    private LocalDate registerTime;

    @ApiModelProperty("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("志愿者")
    private String volunteer;

    @ApiModelProperty("其他信息")
    private String otherInfo;

    @ApiModelProperty("失踪人所在地")
    private String place;

    @ApiModelProperty("失踪地点")
    private String disappearancePlace;

    @ApiModelProperty("失踪类型")
    private String disappearanceType;

    @ApiModelProperty("失踪时间")
    private LocalDate disappearanceTime;

    @ApiModelProperty("身高")
    private Integer height;

    @ApiModelProperty("特征描述")
    private String feature;

    @ApiModelProperty("照片")
    private String photo;

    @ApiModelProperty("失踪省份")
    private String disappearanceProvince;

    @ApiModelProperty("失踪城市")
    private String disappearanceCity;

    @ApiModelProperty("失踪区县")
    private String disappearanceDistrict;

    @ApiModelProperty("失踪位置经度")
    private Double disappearanceLongitude;

    @ApiModelProperty("失踪位置纬度")
    private Double disappearanceLatitude;

    @ApiModelProperty("现所在省份")
    private String nowProvince;

    @ApiModelProperty("现所在城市")
    private String nowCity;

    @ApiModelProperty("现所在区县")
    private String nowDistrict;

    @ApiModelProperty("现所在位置经度")
    private Double nowLongitude;

    @ApiModelProperty("现所在位置纬度")
    private Double nowLatitude;


}
