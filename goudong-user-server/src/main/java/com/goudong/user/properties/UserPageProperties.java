package com.goudong.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 * 页面配置
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:20
 */
@Data
@ConfigurationProperties(prefix = "user.page")
public class UserPageProperties {

    /**
     * qq登陆成功需要绑定账号跳转页面
     */
    private String bindPage = "http://localhost:9998/bindPage.html";

    /**
     * 重定向页面传参过渡页
     */
    private String transitionPage = "http://localhost:9998/transition.html";

    /**
     * qq已经绑定过用户了，跳到主页面
     */
    private String indexPage = "http://localhost:9998/index.html";
}
