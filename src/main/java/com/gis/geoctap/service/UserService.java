package com.gis.geoctap.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gis.geoctap.entity.User;
import com.gis.geoctap.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService extends ServiceImpl<UserMapper, User>{
    public boolean saveUser(User user) {
//        if(user.getId() == null) {
//            return save(user);
//        } else {
//            return updateById(user);
//        }

        // TODO 新增或者更新(user没有id，则表示是新增，否则是更新）
        return saveOrUpdate(user);
    }

//    @Autowired
//    private UserMapper userMapper;

//    public int save(User user) {
//        if (user.getId() == null) {  // user没有id，则表示是新增
//            return userMapper.insert(user);
//        } else { // 否则为更新
//            return userMapper.update(user);
//        }
//    }

}
