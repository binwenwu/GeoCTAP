package com.gis.geoctap.service;

import com.gis.geoctap.controller.dto.UserDTO;
import com.gis.geoctap.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 吴斌文
 * @since 2023-04-27
 */
public interface IUserService extends IService<User> {
    boolean login(UserDTO userDTO);
}
