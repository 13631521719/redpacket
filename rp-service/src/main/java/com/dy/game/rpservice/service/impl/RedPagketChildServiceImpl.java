package com.dy.game.rpservice.service.impl;

import com.dy.game.rpcommon.repository.RedPagketChildRepository;

import com.dy.game.rpservice.service.RedPagketChildService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @作者 liulin
 * @描述 操作红包领取详情的service
 */
@Service
public class RedPagketChildServiceImpl implements RedPagketChildService {
    @Resource
    private RedPagketChildRepository redPagketChildRepository;
}
