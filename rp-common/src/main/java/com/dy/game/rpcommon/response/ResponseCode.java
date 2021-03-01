package com.dy.game.rpcommon.response;

/**
 * 响应状态码
 */
public class ResponseCode {

    /**
     * 成功
     */
    public static final String SUCCESS = "0";
    /**
     * 404
     */
    public static final int NOT_FOUND = 400404;

    /**
     * 运行中
     */
    public static final String RUNNING = "000001";
    /**
     * 失败
     */
    public static final String FAIL = "000002";
    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "000007";
    /**
     * 没有权限
     */
    public static final String NOT_RIGHT = "000008";
    /**
     * 会话超时
     */
    public static final String SESSION_TIMEOUT = "000009";
    /**
     * 拒绝访问（安全）
     */
    public static final String DENY_ACCESS = "000010";
    /**
     * 跨域访问（安全）
     */
    public static final String OUT_DOMAIN = "000011";
    /**
     * sql异常
     */
    public static final String BAD_SQL_ERROR = "000012";
    /**
     * 对象不存储
     */
    public static final String NOT_EXIST = "000013";
    /**
     * 需要token（开放接口）
     */
    public static final String NEED_TOKEND = "000014";
    /**
     * 没有权限
     */
    public static final String BAD_AUTH = "000015";
    /**
     * 对应已经存在
     */
    public static final String ALREADY_EXIST = "000016";
    /**
     * 文件错误（文件上传）
     */
    public static final String BAD_FILE = "000017";
    /**
     * 上传异常（文件上传）
     */
    public static final String FILE_UPLOAD_ERROR = "000018";
    /**
     * 方法不支持
     */
    public static final String REST_METHOD_NOT_SUPPORT = "000019";
    /**
     * 参数校验失败
     */
    public static final String VALIDATE_ERROR = "000020";
    /**
     * 参数为空
     */
    public static final String PARAMETER_NULL = "000021";
    /**
     * 参数格式不正确
     */
    public static final String PARAMETER_FORMAT_ERROR = "000022";
    /**
     * 服务器错误
     */
    public static final String SERVER_ERROR = "000500";



}
