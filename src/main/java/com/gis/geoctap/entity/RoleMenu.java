package com.gis.geoctap.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限管理
 * @author 吴斌文
 * @since 2023-05-06
 */
@TableName("sys_role_menu")
@Data
public class RoleMenu {

    private Integer roleId;
    private Integer menuId;

}
