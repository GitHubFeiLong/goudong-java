package com.goudong.oaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author msi
 * @Date 2021-05-25 14:06
 * @Version 1.0
 */
@SpringBootApplication
@RestController
public class OaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(OaaApplication.class, args);
    }
}
