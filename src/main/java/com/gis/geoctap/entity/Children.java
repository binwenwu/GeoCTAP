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
 * @since 2023-05-23
 */
@Getter
@Setter
  @ApiModel(value = "Children对象", description = "")
public class Children implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String childName;

    private String sex;

    private LocalDate registerTime;

    private LocalDate birthday;

    private String volunteer;

    private String otherInfo;

    private String place;

    private String disappearancePlace;

    private String disappearanceType;

    private LocalDate disappearanceTime;

    private Integer height;

    private String feature;

    private String photo;

    private String disappearanceProvince;

    private String disappearanceCity;

    private String disappearanceDistrict;

    private Double disappearanceLongitude;

    private Double disappearanceLatitude;

    private String nowProvince;

    private String nowCity;

    private String nowDistrict;

    private Double nowLongitude;

    private Double nowLatitude;


}
