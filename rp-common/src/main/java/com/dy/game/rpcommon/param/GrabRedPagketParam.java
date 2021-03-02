package com.dy.game.rpcommon.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@ApiModel(value = "抢红包的参数")
@Data
public class GrabRedPagketParam {

    @ApiModelProperty(name = "用户编码")
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    @ApiModelProperty(name = "群编码")
    @NotBlank(message = "群编码不能为空")
    private String groupCode;

    @ApiModelProperty(name = "红包编码")
    @NotBlank(message = "红包编码不能为空")
    private String redPagketCode;

}
