package com.gis.geoctap.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2023-03-22
 */
@Getter
@Setter
@ApiModel(value = "Activity对象", description = "")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动资金")
    private Integer fund;

    @ApiModelProperty("志愿时长")
    private Integer times;

    @ApiModelProperty("是否开始")
    private Boolean state;

    @ApiModelProperty("负责人id")
    private Integer chargeId;

    @TableField(exist = false)
    private String charge;


}
