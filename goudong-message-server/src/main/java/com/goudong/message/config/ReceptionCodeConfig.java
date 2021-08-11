package com.goudong.message.config;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.openfeign.oauth2.InvalidEmailClient;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.message.util.CodeUtil;
import com.goudong.message.util.SendSms;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 消费 验证码交换机的队列消息
 * @Author msi
 * @Date 2021-05-07 10:45
 * @Version 1.0
 */
@Slf4j
@Validated
@Component
public class ReceptionCodeConfig {
    public static final String emailCodeTemplate = "<p style=\"text-align: left;\"><span style=\"font-size: 10pt;\"><img src=\"http://47.108.31.41/img/logo-20200504-b.44985753.png\" alt=\"\" width=\"227\" height=\"80\" /></span></p>\n <hr><p style=\"text-align: right;\"><span style=\"font-size: 10pt;\">我的狗东&nbsp; &nbsp; 狗东会员&nbsp; &nbsp; &nbsp;退订投诉</span></p>\n<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\">尊敬的用户您好∶</span></p>\n<p><span style=\"font-size: 10pt;\">您的注册验证码为&nbsp; ${code} (请您妥善保管您的验证码，切勿告知他人)，请在页面中输入完成验证。</span></p>\n<p><span style=\"font-size: 10pt;\">安全提示:<br /></span><span style=\"font-size: 10pt;\">为保障您的帐户安全，请在5分钟内完成验证，否则验证码将自动失效。</span></p>\n<p><span style=\"font-size: 8pt;\">您之所以收到这封邮件，是因为您曾经注册成为狗东的用户。本邮件由狗东系统自动发出，请勿直接回复!如果您不愿意继续接收到此类邮件，请点击退订本类邮件，在购物中遇到任何问题，请点击帮助中心。如果您有任何疑问或建议，请点击联系我们。<br /></span><span style=\"font-size: 8pt;\">狗东goudong.shop是专业的综合性网上购物商城。百万种商品，家用电器、手机数码、服装、电脑、母婴、化妆、图书等十几大类。狗东承诺:所售商品100%正品行货，全国联保，机打发票，价格更实惠;支持货到付款，送货上门及POS刷卡服务!</span></p>\n<p><span style=\"font-size: 10pt;\">重要提醒:任何交易和退款只能在狗东官方网站或者APP中完成，谨防假借订单异常，或被设置为经销商为由的电话诈骗!</span></p>\n<p style=\"text-align: center;\"><span style=\"font-size: 10pt;\"><img src=\"http://47.108.31.41/img/logo-20200504-b.44985753.png\" alt=\"\" width=\"94\" height=\"33\" /><br /></span></p>";

    @Resource
    private InvalidEmailClient invalidEmailClient;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Resource
    private SendSms sendSms;

    /**
     * 监听邮箱发送验证码队列
     * exclusive=true, 标明只能有一个消费者获取该队列的数据
     */
    @RabbitListener(queues = CodeDirectRabbitConfig.EMAIL_CODE_DIRECT_QUEUE, exclusive = true)
    @RabbitHandler
    public void emailCode(@NotBlank(message = "邮箱不能为空") @Email(message = "请输入正确邮箱格式") @Payload String email, Channel channel, Message message) throws MessagingException, IOException {
        log.info("{} 队列收到消息。内容是：{}",CodeDirectRabbitConfig.EMAIL_CODE_DIRECT_QUEUE, email);
        try {
            // 告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            log.error("{} 队列消费消息失败。内容是：{}",CodeDirectRabbitConfig.EMAIL_CODE_DIRECT_QUEUE, email);
            e.printStackTrace();
            //丢弃这条消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }
        // 验证码
        String code = CodeUtil.generate();
        log.debug("email:{}, code:{}", email, code);
        redisOperationsUtil.setStringValue(RedisKeyEnum.MESSAGE_AUTH_CODE, code, email);
        // 发送邮件
        sendEmailCode(email, code);
    }

    /**
     * 监听短信发送验证码队列
     * exclusive=true, 标明只能有一个消费者获取该队列的数据
     */
    @RabbitListener(queues = CodeDirectRabbitConfig.PHONE_CODE_DIRECT_QUEUE)
    @RabbitHandler
    public void phoneCode(@Payload String phone, Channel channel, Message message) throws Exception {
        log.debug("{} 队列收到消息。内容是：{}", CodeDirectRabbitConfig.PHONE_CODE_DIRECT_QUEUE, phone);
        System.out.println(CodeDirectRabbitConfig.PHONE_CODE_DIRECT_QUEUE + " 队列收到消息。内容是：" + phone);
        try {
            // 告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            log.error("{} 队列消费消息失败。内容是：{}",CodeDirectRabbitConfig.PHONE_CODE_DIRECT_QUEUE, phone);
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }
        AssertUtil.isPhone(phone, "消费消息，手机号格式错误");
        // 验证码
        String code = CodeUtil.generate();
        log.debug("phone:{}, code:{}", phone, code);
        redisOperationsUtil.setStringValue(RedisKeyEnum.MESSAGE_AUTH_CODE, code, phone);
        // 发送短信
        sendSms.sendCode(phone, code);
    }

    /**
     * 发送富文本格式的邮箱验证码
     * @param email 注册邮箱
     * @param code 验证码
     * @throws MessagingException
     */
    private void sendEmailCode(@NotBlank(message = "邮箱不能为空") @Email(message = "请输入正确邮箱格式") String email, String code) throws MessagingException {
        // 发送验证码
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setFrom(javaMailSender.getUsername());
        helper.setTo(email);
        helper.setSubject("狗东注册验证码");

        String context = emailCodeTemplate.replace("${code}", code);
        helper.setText(context,true);

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailAuthenticationException e) {
            // 邮箱登录失败
            log.error("邮箱登录账号失败");
            e.printStackTrace();
            // 删除redis中的key
            redisOperationsUtil.deleteKey(RedisKeyEnum.MESSAGE_AUTH_CODE, email);
        } catch (MailSendException e) {
            log.error("发送邮件失败: {}", email);
            // 删除redis中的key
            redisOperationsUtil.deleteKey(RedisKeyEnum.MESSAGE_AUTH_CODE, email);
            e.printStackTrace();
        }
    }

}
