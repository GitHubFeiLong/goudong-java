package com.goudong.authentication.common.core;

import com.goudong.core.util.AssertUtil;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@Data
public class AuthorizationContext {
    //~fields
    //==================================================================================================================
    /**
     * Authorization模式
     * <pre>
     *     1. "Bearer"
     *     2. "GOUDONG-SHA256withRSA"
     * </pre>
     */
    private String model;

    /**
     * 应用id
     */
    private Long appid;

    /**
     * 证书序列号
     */
    private String serialNumber;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String signature;

    /**
     * token
     */
    private String token;

    //~methods
    //==================================================================================================================

    /**
     * 根据请求头获取
     * @param authorization 请求头{@code Authorization}内容
     * @return 拆分请求头各个值
     */
    public static AuthorizationContext get(String authorization) {
        Pattern patternModel = Pattern.compile("(Bearer || GOUDONG-SHA256withRSA ).*");
        Matcher matcherModel = patternModel.matcher(authorization);
        AssertUtil.isTrue(matcherModel.matches(), "请求头Authorization格式错误");
        String model = matcherModel.group(1);
        if (model.equals("Bearer ")) { // 直接解析token
            return new AuthorizationContext(model, authorization.substring(model.length()));
        } else {
            // 提取关键信息
            // 使用正则表达式，提取关键信息
            Pattern pattern = Pattern.compile("GOUDONG-SHA256withRSA appid=\"(\\d+)\",serial_number=\"(.*)\",timestamp=\"(\\d+)\",nonce_str=\"(.*)\",signature=\"(.*)\"");
            Matcher matcher = pattern.matcher(authorization);
            AssertUtil.isTrue(matcher.matches(), "请求头格式错误");
            Long appId =  Long.parseLong(matcher.group(1));             // 应用id
            String serialNumber =  matcher.group(2);                    // 证书序列号
            long timestamp =  Long.parseLong(matcher.group(3));         // 时间戳
            String nonceStr =  matcher.group(4);                        // 随机字符串
            String signature =  matcher.group(5);                       // 签名
            return new AuthorizationContext(model, appId, serialNumber, timestamp, nonceStr, signature);
        }
    }

    public AuthorizationContext(String model, String token) {
        this.model = model;
        this.token = token;
    }

    public AuthorizationContext(String model, Long appid, String serialNumber, Long timestamp, String nonceStr, String signature) {
        this.model = model;
        this.appid = appid;
        this.serialNumber = serialNumber;
        this.timestamp = timestamp;
        this.nonceStr = nonceStr;
        this.signature = signature;
    }
}
