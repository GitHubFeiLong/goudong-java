package com.goudong.commons.utils.core;

import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类描述：
 * swagger 的工具类
 * @author cfl
 * @version 1.0
 * @date 2022/8/27 21:31
 */
public class SwaggerUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 接口添加统一参数
     * @return
     */
    public static List<RequestParameter> getRequestParameters() {
        RequestParameterBuilder requestParameterBuilder = new RequestParameterBuilder();
        List<RequestParameter> requestParameters = new ArrayList<>();
        // token
        requestParameters.add(new RequestParameterBuilder()
                .name("Authorization")
                .description("用户令牌")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // 客户端类型
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_CLIENT_SIDE)
                .description("标明终端类型，" + Arrays.toString(ClientSideEnum.values()))
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // RSA公钥将AES密钥加密后的秘串
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_AES_KEY)
                .description("RSA公钥将AES密钥加密后的秘串")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // RSA公钥将AES密钥加密后的秘串
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_INNER)
                .description("标明本次请求是否属于内部接口之间调用")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // 接口日志打印返回值得长度限制
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_INNER)
                .description("标明本次请求是否属于内部接口之间调用")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        return requestParameters;
    }
}
