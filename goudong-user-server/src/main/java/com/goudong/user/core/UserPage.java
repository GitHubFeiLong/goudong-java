package com.goudong.user.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.user.entity.OtherUserInfoBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 类描述：
 * 用户页面对象
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:30
 */
@Data
@Slf4j
@Validated
public class UserPage {
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
                    LogUtil.error(log, "属性是数组类型，这里需要进行修改");
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
