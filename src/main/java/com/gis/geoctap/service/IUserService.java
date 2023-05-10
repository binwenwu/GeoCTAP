package com.gis.geoctap.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.controller.dto.UserDTO;
import com.gis.geoctap.controller.dto.UserPasswordDTO;
import com.gis.geoctap.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> objectPage, String username, String email, String address);
}
