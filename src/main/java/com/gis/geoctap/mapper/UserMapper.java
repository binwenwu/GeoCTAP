package com.gis.geoctap.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gis.geoctap.controller.dto.UserPasswordDTO;
import com.gis.geoctap.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author 吴斌文
 * @since 2023-05-06
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);
}
