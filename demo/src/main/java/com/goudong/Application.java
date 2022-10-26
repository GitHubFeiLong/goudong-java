package com.goudong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 21:40
 */
@SpringBootApplication
public class Application {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        System.out.println(1);
    }

    // @Resource
    // private Mon
}
