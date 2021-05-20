package com.goudong.oauth2.config;

import com.goudong.oauth2.entity.OtherUserInfoBean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * 类描述：
 * 前端ui 页面属性
 * @Author msi
 * @Date 2021-05-03 11:55
 * @Version 1.0
 */
@Data
@SuppressWarnings("ALL")
@ConfigurationProperties(prefix = "ui")
public class UIPageValue {

    /**
     * qq登陆成功需要绑定账号跳转页面
     */
    private String bindPage;

    /**
     * qq已经绑定过用户了，跳到主页面
     */
    private String index;

    /**
     * 获取qq跳转的地址
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
}
