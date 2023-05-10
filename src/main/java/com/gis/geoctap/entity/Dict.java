package com.gis.geoctap.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * icon 图标选择
 */
@TableName("sys_dict")
@Data
public class Dict {

    private String name;
    private String value;
    private String type;

}
