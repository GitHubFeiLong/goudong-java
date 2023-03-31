package com.goudong.commons.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 类描述：
 * 用户分页查询的条件
 * @author cfl
 * @date 2023/3/31 10:20
 * @version 1.0
 */
@Data
public class BaseUser2QueryPageReq implements Serializable {

    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1, message = "分页参数错误，page必须大于等于1")
    @ApiModelProperty(value = "第几页,从1开始",required = true)
    private Integer page = 1;

    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1, message = "分页参数错误，size必须大于等于1")
    @ApiModelProperty(value = "一页显示内容长度", required = true)
    private Integer size = 20;

    @ApiModelProperty("勾选的用户id")
    private List<Long> ids;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("失效时间的起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startValidTime;

    @ApiModelProperty("失效时间的结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endValidTime;

    @ApiModelProperty("创建日期的开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;

    @ApiModelProperty("创建日期的结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endCreateTime;
}
