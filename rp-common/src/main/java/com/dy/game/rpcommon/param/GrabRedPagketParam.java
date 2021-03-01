package com.dy.game.rpcommon.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "抢红包的参数")
@Data
public class GrabRedPagketParam {

    @ApiModelProperty(name = "用户编码")
    private String userCode;

    @ApiModelProperty(name = "群编码")
    private String groupCode;

    @ApiModelProperty(name = "红包编码")
    private String redPagketCode;

}
