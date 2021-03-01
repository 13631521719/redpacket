package com.dy.game.rpcommon.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@ApiModel(value = "发送红包的参数")
@Data
public class SendRedPagketParam {

    @ApiModelProperty(name = "用户编码")
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    @ApiModelProperty(name = "红包金额")
    @NotNull(message = "金额不能为空")
    private Double money;

    @ApiModelProperty(name = "发出的红包数")
    @NotNull(message = "红包数不能为空")
    private Integer num;

    @ApiModelProperty(name = "提示语")
    private String message;

    @ApiModelProperty(name = "封面图片地址")
    private String coverUrl;



}
