package com.gis.geoctap.entity;

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
 * @since 2023-05-24
 */
@Getter
@Setter
  @ApiModel(value = "Children对象", description = "")
public class Children implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String childName;

    private String sex;

    private LocalDate regTime;

    private LocalDate birthday;

    private String volunteer;

    private String otherInfo;

    private String place;

    private String disPlace;

    private String disType;

    private LocalDate disTime;

    private Integer height;

    private String feature;

    private String photo;

    private String disProvi;

    private String disCity;

    private String disDist;

    private Double disLng;

    private Double disLat;

    private String nowProvi;

    private String nowCity;

    private String nowDist;

    private Double nowLng;

    private Double nowLat;


}
