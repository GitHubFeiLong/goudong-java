package com.goudong.oauth2;

import com.goudong.commons.frame.core.LogApplicationStartup;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.util.StopWatch;

/**
 * 访问下面的路径获取code
 * http://localhost:10003/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
 * 教程：https://juejin.cn/post/6844903987137740813
 * 教程：https://blog.csdn.net/bluuusea/article/details/80284458?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant
 * 教程：https://cloud.tencent.com/developer/article/1450973
 * 推荐看：https://www.toutiao.com/i7051392545401733644/?tt_from=weixin&utm_campaign=client_share&wxshare_count=1&timestamp=1641892828&app=news_article&utm_source=weixin&utm_medium=toutiao_android&use_new_style=1&req_id=20220111172028010131039217260E735A&share_token=fabd93af-46e0-4819-863e-dfeab31370c2&group_id=7051392545401733644&wid=1641909741474
 * 网关作为资源 https://www.cnblogs.com/haoxianrui/p/13719356.html#%E5%9B%9B-%E8%B5%84%E6%BA%90%E6%9C%8D%E5%8A%A1%E5%99%A8
 * 认证方式：https://blog.csdn.net/u014203449/article/details/120487310
 * @Author msi
 * @Date 2021-05-25 14:06
 * @Version 1.0
 */
@SpringBootApplication
@EnableAuthorizationServer
// @EnableResourceServer
@EnableDiscoveryClient
@EntityScan("com.goudong.oauth2.po")
@EnableJpaRepositories(basePackages = {"com.goudong.oauth2.repository"})
public class GoudongOauth2Server {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GoudongOauth2Server.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(context.getEnvironment(), (int) stopWatch.getTotalTimeSeconds());
    }
}
