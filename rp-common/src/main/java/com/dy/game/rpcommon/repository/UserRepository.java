package com.dy.game.rpcommon.repository;


import com.dy.game.rpcommon.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUserCode(String userCode)throws Exception;
}
