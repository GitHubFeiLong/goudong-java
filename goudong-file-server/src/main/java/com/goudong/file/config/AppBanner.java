package com.goudong.file.config;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * 类描述：
 * 项目启动的横幅,这样不太好修改字符串，推荐使用类路径下的文件的方式
 * resources/banner.txt
 * @auther msi
 * @date 2021/12/1 14:32
 * @version 1.0
 */
public class AppBanner implements Banner {
    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println(
                "                         .___                                _____.__.__          \n" +
                "   ____   ____  __ __  __| _/____   ____    ____           _/ ____\\__|  |   ____  \n" +
                "  / ___\\ /  _ \\|  |  \\/ __ |/  _ \\ /    \\  / ___\\   ______ \\   __\\|  |  | _/ __ \\ \n" +
                " / /_/  >  <_> )  |  / /_/ (  <_> )   |  \\/ /_/  > /_____/  |  |  |  |  |_\\  ___/ \n" +
                " \\___  / \\____/|____/\\____ |\\____/|___|  /\\___  /           |__|  |__|____/\\___  >\n" +
                "/_____/                   \\/           \\//_____/                               \\/\n "+
                "                                                                       \n" +
                " => Spring Boot          ::  (${spring-boot.version})                                  \n" +
                " => goudong-file-server  ::  (v1.0.0)                                  \n" +
                " => github               ::  https://github.com/GitHubFeiLong/goudong-java\n");
    }
}
