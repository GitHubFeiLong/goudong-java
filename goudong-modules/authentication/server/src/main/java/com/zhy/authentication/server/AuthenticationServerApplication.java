package com.zhy.authentication.server;

import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.bean.DatabaseKey;
import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.boot.web.core.ErrorAttributesService;
import com.goudong.boot.web.core.LogApplicationStartup;
import com.zhy.authentication.server.config.MyErrorAttributes;
import com.zhy.authentication.server.enums.DatabaseKeyEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/17 14:07
 */
@SpringBootApplication
@EntityScan("com.zhy.authentication.server.domain")
@EnableJpaRepositories(basePackages = {"com.zhy.authentication.server.repository"})
@EnableCommonsWebMvcConfig
@EnableCommonsRedisConfig
public class AuthenticationServerApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ConfigurableApplicationContext context = SpringApplication.run(AuthenticationServerApplication.class, args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }

    /**
     * 数据库索引异常配置
     * @return
     */
    @Bean
    public DatabaseKey databaseKey() {
        Map<String, String> map = Stream.of(DatabaseKeyEnum.values()).collect(Collectors.toMap(DatabaseKeyInterface::getKey, p -> p.getClientMessage(), (k1, k2) -> k1));
        DatabaseKey databaseKey = new DatabaseKey(map);
        return databaseKey;
    }

    /**
     * <pre>
     *     @EnableCommonsWebMvcConfig 加在了启动类上，会导致goudong-web-spring-boot-starter会优先加载jar里面Bean
     * </pre>
     * @param request
     * @return
     */
    @Bean
    public ErrorAttributesService errorAttributesService(HttpServletRequest request) {
        return new MyErrorAttributes(request);
    }

}
