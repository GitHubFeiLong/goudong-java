package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.goudong.commons.frame.mybatisplus.BasePO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * authority_user
 * @author
 */
@Data
@TableName("authority_user")
public class AuthorityUserPO extends BasePO {

    private static final long serialVersionUID = -4094738684059087967L;
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
    private LocalDateTime validTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;
}
