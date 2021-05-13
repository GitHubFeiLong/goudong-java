package com.goudong.commons.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

/**
 * authority_user
 * @author
 */
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Data
public class AuthorityUserPO implements Serializable {
    private static final long serialVersionUID = -8673423406924099991L;
    /**
     * uuid
     */
    private String uuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效截止时间
     */
    private Date validTime;

    /**
     * 是否被删除
     */
    private Boolean isDelete;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;
}
