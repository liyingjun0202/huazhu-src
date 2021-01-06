package com.hz.show.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liyj
 * @Description
 * @createTime 2021/1/6 15:04
 **/
@Data
public class User {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("userName")
    private String userName;

    @ApiModelProperty("password")
    private String password;

    @ApiModelProperty("phone")
    private String phone;

    @ApiModelProperty("email")
    private String email;

    private long expiredTime;

    private String token;


}
