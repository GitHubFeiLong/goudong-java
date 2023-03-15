package com.goudong.wx.central.control.util;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.wx.central.control.dto.resp.AccessTokenResp;
import com.goudong.wx.central.control.dto.resp.BaseResp;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
        String body = HttpUtil.createGet(url).execute().body();

        try {
            AccessTokenResp accessTokenResp = OBJECT_MAPPER.readValue(body, AccessTokenResp.class);
            AssertUtil.isTrue(accessTokenResp.getErrcode() == 0,
                    () -> BasicException.server("获取AccessToken失败")
                            .dataMap(getDataMapByError(accessTokenResp))
            );
            // 设置到期时间,时间提前5分钟。（注意单位）
            accessTokenResp.setExpiresIn(accessTokenResp.getExpiresIn() - 300);
            accessTokenResp.setExpiresTime(System.currentTimeMillis() + accessTokenResp.getExpiresIn() * 1000);
            return accessTokenResp;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
