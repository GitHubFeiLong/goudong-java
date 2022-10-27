package com.goudong.modules.jpa.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 类描述：
 * JPA
 * @author cfl
 * @version 1.0
 * @date 2022/10/27 13:42
 */
@SpringBootApplication
@EntityScan("com.goudong.modules.jpa.spring.boot.po")
@EnableJpaRepositories(basePackages = {"com.goudong.modules.jpa.spring.boot.po"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
