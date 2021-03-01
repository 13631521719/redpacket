package com.dy.game.rpservice.service.impl;

import com.dy.game.rpcommon.entity.UserEntity;
import com.dy.game.rpcommon.repository.UserRepository;
import com.dy.game.rpservice.exception.BaseException;
import com.dy.game.rpservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @作者 liulin
 * @描述 操作用户信息的service
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    /**
     * 根据用户编码查询用户信息
     * @param userCode 用户编码
     * @return
     * @throws Exception
     */
    @Override
    public UserEntity findByUserCode(String userCode) throws Exception {
        if (StringUtils.isBlank(userCode))
            throw new BaseException("用户编码不能为空");
        UserEntity userEntity=userRepository.findByUserCode(userCode);
        return userEntity;
    }

    /**
     * 保存/修改用户信息
     * @param userEntity
     * @return
     * @throws Exception
     */
    @Override
    public boolean save(UserEntity userEntity) throws Exception {
        if (userEntity==null)
            throw new BaseException("用户信息为空");
        userRepository.save(userEntity);
        return true;
    }
}
