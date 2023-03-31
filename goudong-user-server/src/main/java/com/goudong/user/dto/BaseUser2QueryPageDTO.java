package com.goudong.user.dto;

import com.goudong.boot.web.core.BasePage;
import com.goudong.commons.constant.core.DateConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 类描述：
 * 用户分页查询的条件
 * @author cfl
 * @date 2022/8/20 8:22
 * @version 1.0
 */
@Data
public class BaseUser2QueryPageDTO extends BasePage implements Serializable {

    private static final long serialVersionUID = -2290257073855491469L;

    @ApiModelProperty(value = "勾选的用户id")
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
