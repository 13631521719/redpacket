package com.dy.game.rpApi.controller;

import com.dy.game.rpcommon.param.GrabRedPagketParam;
import com.dy.game.rpcommon.param.SendRedPagketParam;
import com.dy.game.rpcommon.response.WebResponse;
import com.dy.game.rpservice.service.RedPagketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @作者 liulin
 * @描述 操作红包控制器
 */
@Controller
@Api(tags = "费用控制器")
public class RedPagketController {
    @Resource
    private RedPagketService redPagketService;

    @ApiOperation(value = "发红包")
    @RequestMapping(value = "/redPagket/sendRedPagket",method = RequestMethod.POST)
    public @ResponseBody
    WebResponse sendRedPagket(@RequestBody @Validated SendRedPagketParam sendRedPagketParam)throws Exception{
        return redPagketService.sendRedPagket(sendRedPagketParam);
    }

    @ApiOperation(value = "抢红包")
    @RequestMapping(value = "/redPagket/gradRedPagket",method = RequestMethod.POST)
    public @ResponseBody WebResponse gradRedPagket(@RequestBody @Validated GrabRedPagketParam grabRedPagketParam)throws Exception{
        return redPagketService.gradRedPagket(grabRedPagketParam);
    }
}
