package com.goudong.commons;

import com.goudong.commons.constant.core.BasePackageConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MapperScan 可以不在mapper层添加注解
 * @author msi
 * @Date 2021-04-09 11:06
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan(BasePackageConst.COMMONS_MAPPER)
public class GoudongCommons {
    public static void main(String[] args) {
        SpringApplication.run(GoudongCommons.class, args);
    }
}
