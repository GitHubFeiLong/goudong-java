package com.goudong.commons.framework.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goudong.commons.po.core.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 类描述：
 * 用户分页结果对象
 * @author cfl
 * @version 1.0
 * @date 2023/7/7 9:08
 */
@Data
public class BaseUser2QueryPageResp extends BasePO {
    /**
     * 用户名
     */
    private String username;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;

    /**
     * 用户名、电话或邮箱
     */
    private String loginName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 激活状态（true：激活；false：未激活）
     */
    private Boolean enabled;

    /**
     * 性别（0：未知；1：男；2：女）
     */
    private Integer sex;

    /**
     * 锁定状态（true：已锁定；false：未锁定）
     */
    private Boolean locked;

    @ApiModelProperty("角色集合")
    private List<Role> roles;

    /**
     * 类描述：
     * 角色
     * @author cfl
     * @date 2023/6/20 16:00
     * @version 1.0
     */
    @Data
    public static class Role {

        private Long id;
        /**
         * 角色名称(必须以ROLE_起始命名)
         */
        @Column(name = "role_name")
        private String roleName;

        /**
         * 角色名称中文
         */
        @Column(name = "role_name_cn")
        private String roleNameCn;
    }
}
