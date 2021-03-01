package com.dy.game.rpcommon.response;


/**
 * 响应结果工具类
 */
public class ResponseUtil {

    /**
     * 成功响应
     *
     * @param data    响应数据
     * @param message 响应消息
     * @return Web响应
     */
    public static WebResponse success(Object data, String message) {
        return new WebResponse(ResponseCode.SUCCESS, message, data);
    }

    /**
     * 成功响应
     *
     * @return
     */
    public static WebResponse success(int code, Object data, String message) {
        return new WebResponse(String.valueOf(code), message, data);
    }


    /**
     * 成功响应
     *
     * @return Web响应
     */
    public static WebResponse success() {
        return new WebResponse(ResponseCode.SUCCESS);
    }

    /**
     * 成功响应
     *
     * @param data
     * @return
     */
    public static WebResponse success(Object data) {
        return new WebResponse(ResponseCode.SUCCESS, "操作成功!", data);
    }

    /**
     * 失败响应
     *
     * @param message 响应消息
     * @return Web响应
     */
    public static WebResponse fail(String message) {
        return new WebResponse(ResponseCode.SERVER_ERROR, message);
    }
    /**
     * 失败响应
     *
     * @param message 响应消息
     * @return Web响应
     */
    public static WebResponse fail(Object data,String message) {
        return new WebResponse(ResponseCode.SERVER_ERROR, message,data);
    }

    /**
     * 失败响应
     *
     * @param code    响应错误码
     * @param message 响应消息
     * @return Web响应
     */
    public static WebResponse fail(String code, String message) {
        return new WebResponse(code, message);
    }
}
