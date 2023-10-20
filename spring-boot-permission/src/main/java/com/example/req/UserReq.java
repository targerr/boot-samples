package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: wgs
 * @Date 2023/10/18 14:11
 * @Classname UserReq
 * @Description
 */
@Data
@Schema(name = "用户信息实体",description = "用户对象")
public class UserReq {

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    @Schema(description = "用户名",defaultValue = "1")
    private String username;

    @NotBlank(message = "电话不可以为空")
    @Length(min = 1, max = 13, message = "电话长度需要在13个字以内")
    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不允许为空")
    @Length(min = 5, max = 50, message = "邮箱长度需要在50个字符以内")
    private String mail;

    @Schema(description = "部门id")
    @NotNull(message = "必须提供用户所在的部门")
    private Integer deptId;

    @Schema(description = "用户状态")
    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark = "";
}
