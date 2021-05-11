package com.goudong.commons.bo;

import com.goudong.commons.po.AuthorityUserPO;
import lombok.Data;

/**
 * 用户注册，登录，修改密码等的业务对象
 * @Author msi
 * @Date 2021-05-11 13:22
 * @Version 1.0
 */
@Data
public class AuthorityUserBO {
    /**
     * 用户对象
     */
    private AuthorityUserPO authorityUserPO;
}
