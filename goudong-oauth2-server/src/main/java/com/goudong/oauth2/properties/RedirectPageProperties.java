package com.goudong.oauth2.properties;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.core.StringUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.core.OtherUserInfoBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 类描述：
 * 重定向页面配置
 * @auther msi
 * @date 2022/1/28 13:33
 * @version 1.0
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "oauth2.redirect-page")
public class RedirectPageProperties {

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

    /**
     * 创建Bean后，需要校验参数是否正确
     */
    @PostConstruct
    public void init () {
        this.checkUrl();
    }

    /**
     * 检查配置路径是否正确
     */
    private void checkUrl() {
        if (!StringUtil.regexUrl(this.getBindPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置qq登陆成功需要绑定账号跳转页面地址错误(" + this.getBindPage() + ")",
                    "修改属性:user.page.bind-page为正确的URL");
        }
        if (!StringUtil.regexUrl(this.getTransitionPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置重定向页面传参过渡页地址错误(" + this.getTransitionPage() + ")",
                    "修改属性user.page.transition-page为正确的URL");
        }
        if (!StringUtil.regexUrl(this.getIndexPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置重定向应用首页地址错误(" + this.getIndexPage() + ")",
                    "修改属性user.page.index-page为正确的URL");
        }

    }

    /**
     * 获取qq跳转到绑定账号页面的地址
     * @return
     */
    public String getBindPageUrl(@Validated OtherUserInfoBean userInfoBean){
        StringBuilder sb = new StringBuilder(bindPage);
        // 当前时间戳
        sb.append("?current=" + new Date().getTime());
        // 页面的qqopenId
        sb.append("&openId=" + userInfoBean.getOpenId());
        // 昵称
        sb.append("&nickname=" + userInfoBean.getNickname());
        // 头像
        sb.append("&headPortrait30=" + userInfoBean.getHeadPortrait30());
        // 账号类型
        sb.append("&userType=" + userInfoBean.getUserType());
        return sb.toString();
    }

    /**
     * 获取qq登录成功跳转到首页的地址
     * @return
     */
    public String getTransitionPageUrl(Transition transition){
        AssertUtil.isNotNull(transition, "过渡参数对象不能为空");
        AssertUtil.isNotNull(transition.getRedirectUrl(), "过渡参数对象的重定向地址属性不能为空");

        // 转map，拼接
        Map<String, Object> map = BeanUtil.beanToMap(transition);
        StringBuilder sb = new StringBuilder(transitionPage);
        sb.append("?");
        map.forEach((key, value) -> {
            if (value != null) {
                sb.append(key).append("=");
                // 如果是集合就是用逗号分隔值
                if (value instanceof Collection) {
                    sb.append(String.join(",", (Collection) value));
                } else if (value.getClass().isArray()) {
                    log.error("属性是数组类型，这里需要进行修改");
                } else if (value instanceof Date) {
                    sb.append(((Date) value).getTime());
                } else {
                    sb.append(String.join(",", value.toString()));
                }
                sb.append("&");
            }
        });
        // 去掉最后一个 “&”
        String result = sb.toString();
        if (result.indexOf("&") != -1) {
            result = result.substring(0, result.lastIndexOf("&"));
        }

        return result;
    }
}
