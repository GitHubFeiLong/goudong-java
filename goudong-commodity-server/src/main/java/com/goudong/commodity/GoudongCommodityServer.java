package com.goudong.commodity;

import com.goudong.commons.constant.BasePackageConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author msi
 * @Date 2021-05-31 14:09
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {BasePackageConst.COMMODITY, BasePackageConst.COMMONS})
@MapperScan(basePackages = {"com.goudong.commodity.dao"})
public class GoudongCommodityServer {
    public static void main(String[] args) {
        SpringApplication.run(GoudongCommodityServer.class, args);
    }
}
