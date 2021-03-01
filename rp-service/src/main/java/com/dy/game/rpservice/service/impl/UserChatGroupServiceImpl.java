package com.dy.game.rpservice.service.impl;

import com.dy.game.rpcommon.repository.UserChatGroupRepository;
import com.dy.game.rpservice.service.UserChatGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @作者 liulin
 * @描述 操作用户和聊天群对应关系的service
 */
@Service
public class UserChatGroupServiceImpl  implements UserChatGroupService {
    @Resource
    private UserChatGroupRepository userChatGroupRepository;
}
