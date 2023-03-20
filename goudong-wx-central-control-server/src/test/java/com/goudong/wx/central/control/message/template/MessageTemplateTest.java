package com.goudong.wx.central.control.message.template;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.wx.central.control.util.WxRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static com.goudong.wx.central.control.constant.WxApiUrlConst.*;

/**
 * 类描述：
 * 微信消息模板测试
 * @author cfl
 * @version 1.0
 * @date 2023/3/20 16:52
 */
@ExtendWith({})
@Slf4j
public class MessageTemplateTest {

    public static String accessToken;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @BeforeAll
    static void setAccessToken() {
        accessToken = WxRequestUtil.getAccessToken("wx1948fa77ad11a423", "410d996604e66d6278d1e5a4d0f49e9c").getAccessToken();
        log.info("access token : {}", accessToken);
    }

    /**
     * 测试设置行业
     */
    @Test
    void testPOST_API_SET_INDUSTRY() throws JsonProcessingException {
        String url = MessageFormatUtil.format(POST_API_SET_INDUSTRY, accessToken);
        Map<String, Object> param = new HashMap<>();
        param.put("industry_id1", 2);
        param.put("industry_id2", 4);
        // {"errcode":0,"errmsg":"ok"}
        String body = HttpUtil.createPost(url)
                .body(OBJECT_MAPPER.writeValueAsString(param))
                .execute().body();
        log.info(body);
    }

    /**
     * 测试获取设置的行业
     */
    @Test
    void testGET_INDUSTRY() {
        String url = MessageFormatUtil.format(GET_INDUSTRY, accessToken);
        /*
            {"primary_industry":{"first_class":"IT科技","second_class":"IT软件与服务"},"secondary_industry":{"first_class":"IT科技","second_class":"电子技术"}}
         */
        String body = HttpUtil.createGet(url)
                .execute().body();
        log.info(body);
    }

    /**
     * 获得模板ID
     */
    @Test
    void testGET_API_ADD_TEMPLATE() throws JsonProcessingException {
        String url = MessageFormatUtil.format(GET_API_ADD_TEMPLATE, accessToken);
        Map<String, Object> param = new HashMap<>();
        param.put("template_id_short", "TM00015");
        /*
            {"primary_industry":{"first_class":"IT科技","second_class":"IT软件与服务"},"secondary_industry":{"first_class":"IT科技","second_class":"电子技术"}}
         */
        String body = HttpUtil.createGet(url)
                .body(OBJECT_MAPPER.writeValueAsString(param))
                .execute().body();
        log.info(body);
    }


    /**
     * 获取模板列表
     */
    @Test
    void testGET_ALL_PRIVATE_TEMPLATE() throws JsonProcessingException {
        String url = MessageFormatUtil.format(GET_ALL_PRIVATE_TEMPLATE, accessToken);
        String body = HttpUtil.createGet(url)
                .execute().body();
        log.info(body);
    }

    /**
     * 发送模板消息
     */
    @Test
    void testPOST_SEND_TEMPLATE_MESSAGE() throws JsonProcessingException {
        String url = MessageFormatUtil.format(POST_SEND_TEMPLATE_MESSAGE, accessToken);
        String param = "{\n" +
                "           \"touser\":\"opEtJ6-tmfyKxzt0t5d6XS5NFDc0\",\n" +
                "           \"template_id\":\"V6ghspiMnyeAmgQTZ2rB_nnCQa8EmU2i1SIxbHAV8YU\",\n" +
                "           \"url\":\"http://weixin.qq.com/download\",  \n" +
                "           \"data\":{\n" +
                "                   \"account\": {\n" +
                "                       \"value\":\"睡醒\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"name\":{\n" +
                "                       \"value\":\"陈飞龙\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"age\": {\n" +
                "                       \"value\":\"18\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"sex\": {\n" +
                "                       \"value\":\"男\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"address\": {\n" +
                "                       \"value\":\"重庆万州\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"欢迎再次购买！\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        String body = HttpUtil.createPost(url)
                .body(param)
                .execute().body();
        log.info(body);
    }
}
