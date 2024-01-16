package com.goudong.boot.web.core;

import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * 类描述：
 * 打印应用信息日志
 * @author msi
 * @version 1.0
 * @date 2021/12/4 14:12
 */
@Slf4j
public class LogApplicationStartup {

    /**
     * 根据spring的环境打印统一的启动日志
     * @param env
     */
    public static void logApplicationStartup(Environment env, int totalTimeSecond) {

        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
                .ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtil::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        // 文档是否启用
        Boolean knife4jEnabled = Optional.ofNullable(env.getProperty("knife4j.enable", Boolean.class))
                .orElse(false);

        // 提示信息
        StringBuilder allMessage = new StringBuilder();

        allMessage.append(
            String.format("\n----------------------------------------------------------------------------------------------------" +
                            "\n\tApplication '%s' is running,耗时:%ss! Access URLs:" +
                            "\n\tLocal: \t\t%s://localhost:%s%s" +
                            "\n\tExternal: \t%s://%s:%s%s",
                    env.getProperty("spring.application.name"),
                    totalTimeSecond,
                    protocol,
                    serverPort,
                    contextPath,
                    protocol,
                    hostAddress,
                    serverPort,
                    contextPath
            )
        );

        if (knife4jEnabled) {
            allMessage.append(
                    String.format("\n\tswagger:\thttp://%s:%s%s/doc.html",
                        hostAddress,
                        serverPort,
                        contextPath
                )
            );

            // 是否启用认证
            Boolean basicEnabled = Optional.ofNullable(env.getProperty("knife4j.basic.enable",Boolean.class))
                    .orElse(false);
            if (basicEnabled) {
                allMessage.append(
                        String.format("\n\t用户名:\t\t%s" +
                                    "\n\t密码:\t\t%s",
                            env.getProperty("knife4j.basic.username"),
                            env.getProperty("knife4j.basic.password")
                    )
                );
            }
        }

        if (env.getActiveProfiles().length > 0) {
            allMessage.append(
                    String.format(
                            "\n\tProfile(s): %s",
                            env.getActiveProfiles())
            );
        }

        allMessage.append("\n----------------------------------------------------------------------------------------------------");
        log.info(allMessage.toString());
    }

    private LogApplicationStartup(){

    }
}
