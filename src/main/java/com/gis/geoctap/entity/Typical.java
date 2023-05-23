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
  @ApiModel(value = "Typical对象", description = "")
public class Typical implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDate eventdate;

    private String location;

    private String person;

    private String event;

    private String clue;

    private Integer exampleId;

    private String childName;

    private Double longitude;

    private Double latitude;


}
