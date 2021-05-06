package com.goudong.message;

import com.goudong.commons.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-05-04 22:36
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.goudong.message"}, scanBasePackageClasses = {GlobalExceptionHandler.class})
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
