package com.dy.game.rpApi.aspect;


import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;



/**
 * @作者 liulin
 * @描述 全局日志打印
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.dy.game.rpApi.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容i
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //请求url
        String url = request.getRequestURL().toString();
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof HttpServletRequest)
            return;

        // 记录下请求内容
        log.info("-------------------------------------收到请求 --> " + url + ":" + (joinPoint.getArgs().length == 0 ? null : JSONObject.toJSONString(joinPoint.getArgs()[0])));

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {

        // 处理完请求，返回内容
        log.info("-------------------------------------返回参数--> : " + JSONObject.toJSONString(ret));
    }


}
