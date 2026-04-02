package org.company.finance.application.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-29
 *
 */
@Data
@ApiModel("注册请求")
public class RegisterRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty("确认密码")
    private String confirmPassword;
}
