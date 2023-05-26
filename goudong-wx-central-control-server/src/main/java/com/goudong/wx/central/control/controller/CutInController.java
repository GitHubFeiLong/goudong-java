package com.goudong.wx.central.control.controller;

import com.goudong.core.util.MessageFormatUtil;
import com.goudong.wx.central.control.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 类描述：
 * 接入控制器
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 13:43
 */
@RestController
@RequestMapping("/cut-in")
@Slf4j
public class CutInController {

    /**
     * <pre>
     *      signature	微信加密签名，signature结合了开发者填写的 token 参数和请求中的 timestamp 参数、nonce参数(使用sha1加密后转成十六进制的字符串)。
     *      timestamp	时间戳
     *      nonce	    随机数
     *      echostr	    随机字符串
     * </pre>
     * @return
     */
    @GetMapping
    public String cutIn(@RequestParam("signature") String signature,
                        @RequestParam("timestamp") String timestamp,
                        @RequestParam("nonce") String nonce,
                        @RequestParam("echostr") String echostr) throws NoSuchAlgorithmException {
        log.info("signature：{}", signature);
        log.info("timestamp：{}", timestamp);
        log.info("nonce：{}", nonce);
        log.info("echostr：{}", echostr);
        /*
            1）将token、timestamp、nonce三个参数进行字典序排序
            2）将三个参数字符串拼接成一个字符串进行sha1加密
            3）开发者获得加密后的字符串可与 signature 对比，标识该请求来源于微信
         */
        String token = "123";
        String[] strs = new String[]{token, timestamp, nonce};
        Arrays.sort(strs);
        String s = strs[0] + strs[1] + strs[2];

        MessageDigest md = MessageDigest.getInstance("sha1");
        // 加密
        byte[] digest = md.digest(s.getBytes());
        // 16进制字符数组
        char[] chars = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        // 创建
        StringBuilder sb = new StringBuilder();
        // 高4位和低四位转成16进制拼接
        for(byte b : digest) {
            sb.append(chars[(b >> 4) & 15]).append(chars[b & 15]);
        }
        String mySignature = sb.toString();
        log.info("mySignature：{}", mySignature);

        if (Objects.equals(mySignature, signature)) {
            log.info("对比成功");
            return echostr;
        }
        log.info("对比失败");
        return "error";
    }

    /**
     * 处理用户发送的消息
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @PostMapping
    public String handlerMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取发送的信息
        Map<String, String> map = XMLUtil.toMap(request.getInputStream());
        log.info("{}", map);

        // 回复文本
        String responseText = "<xml>\n" +
                "  <ToUserName><![CDATA[{toUser}]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[{fromUser}]]></FromUserName>\n" +
                "  <CreateTime>{12345678}</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[{}]]></Content>\n" +
                "</xml>";
        int createTime = (int)(System.currentTimeMillis() / 1000);

        String text = MessageFormatUtil.format(responseText, map.get("FromUserName"), map.get("ToUserName"), createTime, "hello world");

        return text;
    }

    /**
     * 处理用户发送的消息
     * @return
     * @throws IOException
     */
    @GetMapping("/send")
    public String handlerMessage(@RequestParam String content) throws IOException {
        // 回复文本
        String responseText = "<xml>\n" +
                "  <ToUserName><![CDATA[{toUser}]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[{fromUser}]]></FromUserName>\n" +
                "  <CreateTime>{12345678}</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[{}]]></Content>\n" +
                "</xml>";
        int createTime = (int)(System.currentTimeMillis() / 1000);

        String text = MessageFormatUtil.format(responseText, "opEtJ6-tmfyKxzt0t5d6XS5NFDc0", "gh_aceb4f257187", createTime, content);

        return text;
    }
}
