package com.dy.game.rpservice.service;

import com.dy.game.rpcommon.entity.UserEntity;

public interface UserService {
    UserEntity findByUserCode(String userCode)throws Exception;
    boolean save(UserEntity userEntity)throws Exception;
}
