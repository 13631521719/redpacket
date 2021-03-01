package com.dy.game.rpcommon.response;

public enum ResCode {
    SUCCESS("200","成功"),
    COMMON_ERROR("5000000", "系统内部错误，请联系管理员"),
    OPERATION_FAIL("5000001", "操作失败"),
    REQUEST_PARAM_NULL("5002001", "请求参数不能为空"),
    REQUEST_PARAM_EXSITS("5002002", "请求参数不能重复"),
    AUTH_NOT_LONGIN("5003002", "未获取到有效的sessionId"),
    NO_PERMISSION_ERROR("5005004","无权限"),
    NO_LOGIN_IN_ERROR("5005005","登陆已过期，请重新登陆"),
    LOGIN_NAME_NOT_EXIST("5005008","登录名不存在"),
    NOT_FOUND("404","HTTP Status 404 - Not Found"),
    LOGIN_NAME_OFF("5005009","该账号已经停用");

    private final String code;
    private final String message;

    ResCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    }
