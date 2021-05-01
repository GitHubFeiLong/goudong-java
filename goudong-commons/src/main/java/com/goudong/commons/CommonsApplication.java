package com.goudong.commons;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author msi
 * @Date 2021-04-09 11:06
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.goudong.commons.dao"}) // 可以不在mapper层添加注解
public class CommonsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonsApplication.class, args);
    }
}
