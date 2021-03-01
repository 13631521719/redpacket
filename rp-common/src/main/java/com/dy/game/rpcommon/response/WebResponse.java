package com.dy.game.rpcommon.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "服务器响应")
public class WebResponse<T> implements Serializable {

    /**
     * 响应结果码
     */
    @ApiModelProperty(value = "响应码", example = "0")
    private String code;
    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", example = "查询成功")
    private String message;
    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    public WebResponse() {
    }

    public WebResponse(String code) {
        this.code = code;
    }

    public WebResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public WebResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public WebResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}