package com.gis.geoctap.service;

import com.gis.geoctap.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
