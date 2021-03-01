package com.dy.game.rpservice.service.impl;

import com.dy.game.rpcommon.repository.ChatGroupRepository;
import com.dy.game.rpservice.service.ChatGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @作者 liulin
 * @描述 操作聊天群组的service
 */
@Service
public class ChatGroupServiceImpl implements ChatGroupService {
    @Resource
    private ChatGroupRepository chatGroupRepository;
}
