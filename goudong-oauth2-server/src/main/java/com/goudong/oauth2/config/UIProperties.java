package com.goudong.oauth2.config;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.oauth2.entity.OtherUserInfoBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 类描述：
 * 前端ui 页面属性
 * @Author msi
 * @Date 2021-05-03 11:55
 * @Version 1.0
 */
@Slf4j
@Data
@SuppressWarnings("ALL")
@ConfigurationProperties(prefix = "ui", ignoreUnknownFields = true)
public class UIProperties {

    /**
     * qq登陆成功需要绑定账号跳转页面
     */
    private String bindPage;

    /**
     * 重定向页面传参过渡页
     */
    private String transitionPage;

    /**
     * qq已经绑定过用户了，跳到主页面
     */
    private String indexPage;

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
        AssertUtil.notNull(transition, "过渡参数对象不能为空");
        AssertUtil.notNull(transition.getRedirectUrl(), "过渡参数对象的重定向地址属性不能为空");

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
