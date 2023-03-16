package com.goudong.wx.central.control.util;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.wx.central.control.constant.WxApiUrlConst;
import com.goudong.wx.central.control.dto.req.CreateMenuReq;
import com.goudong.wx.central.control.dto.req.GetStableAccessTokenReq;
import com.goudong.wx.central.control.dto.resp.AccessTokenResp;
import com.goudong.wx.central.control.dto.resp.BaseResp;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.goudong.wx.central.control.constant.WxApiUrlConst.GET_STABLE_ACCESS_TOKEN;
import static com.goudong.wx.central.control.constant.WxApiUrlConst.GET_TOKEN_TEMPLATE;

/**
 * 类描述：
 * 微信请求工具类
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 21:12
 */
@Slf4j
public class WxRequestUtil {
    //~fields
    //==================================================================================================================
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //~methods
    //==================================================================================================================
    static {
        // 设置属性命名策略
        /*
            java 驼峰 > 转 a_b 格式
            json中的a_b 转 aB格式
         */
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
    /**
     * 获取AccessToken
     * @param appId
     * @param appSecret
     * @return 响应对象
     */
    public static AccessTokenResp getAccessToken(String appId, String appSecret) {
        String url = MessageFormatUtil.format(GET_TOKEN_TEMPLATE, appId, appSecret);
        logReq(url, appId);
        String body = HttpUtil.createGet(url).execute().body();
        logResp(body);
        try {
            AccessTokenResp resp = OBJECT_MAPPER.readValue(body, AccessTokenResp.class);

            AssertUtil.isTrue(resp.getErrcode() == 0,
                    () -> BasicException.server("获取AccessToken失败")
                            .dataMap(getDataMapByError(resp))
            );

            resp.disposeExpires();
            return resp;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 获取稳定版接口调用凭据
     * @param req
     * @return
     */
    public static AccessTokenResp getStableAccessToken(GetStableAccessTokenReq req) {
        String url = GET_STABLE_ACCESS_TOKEN;
        try {
            String reqBody = OBJECT_MAPPER.writeValueAsString(req);
            logReq(url, reqBody);
            String respBody =HttpUtil.createPost(url)
                    .body(reqBody)
                    .execute()
                    .body();
            logResp(respBody);
            AccessTokenResp resp = OBJECT_MAPPER.readValue(respBody, AccessTokenResp.class);

            AssertUtil.isTrue(resp.getErrcode() == 0,
                    () -> BasicException.server("获取AccessToken失败")
                            .dataMap(getDataMapByError(resp))
            );
            resp.disposeExpires();
            return resp;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建菜单
     * @param accessToken
     * @param req
     * @return
     */
    public static BaseResp createMenu(String accessToken, CreateMenuReq req) {
        String url = MessageFormatUtil.format(WxApiUrlConst.POST_CREATE_MENU, accessToken);
        String reqBody = null;
        try {
            reqBody = OBJECT_MAPPER.writeValueAsString(req);
            logReq(url, reqBody);
            String respBody = HttpUtil.createPost(url)
                    .body(reqBody)
                    .execute()
                    .body();
            BaseResp baseResp = OBJECT_MAPPER.readValue(respBody, BaseResp.class);
            logResp(respBody);

            AssertUtil.isTrue(baseResp.getErrcode() == 0,
                    () -> BasicException.server("创建菜单失败")
                            .dataMap(getDataMapByError(baseResp))
            );

            return baseResp;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 打印请求前日志
     * @param url
     * @param body
     */
    private static void logReq(String url, String body) {
        if (log.isDebugEnabled()) {
            log.debug("调用微信接口：{}\n参数：{}", url, body);
        }
    }

    /**
     * 打印响应日志
     * @param body
     */
    private static void logResp(String body) {
        if (log.isDebugEnabled()) {
            log.debug("调用微信接口响应：{}", body);
        }
    }

    /**
     * 获取异常时的map集合
     * @param baseResp
     * @return
     */
    private static Map getDataMapByError(BaseResp baseResp) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("errcode", baseResp.getErrcode());
        map.put("errmsg", baseResp.getErrmsg());
        return map;
    }
}
