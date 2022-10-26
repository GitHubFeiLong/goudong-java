package com.goudong.boot.exception.config;

import com.goudong.boot.exception.util.ORMTypeEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * ORM类型自动配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 14:55
 */
@Configuration
public class ORMTypeAutoConfiguration {

    @Bean(autowireCandidate=false)
    @ConditionalOnClass(name = {"org.springframework.data.domain.Page"})
    public void JPA() {
        ORMTypeEnum.CLIENT_TYPES.add(ORMTypeEnum.JPA);
        System.out.println("jpa ----");
    }

    @Bean
    @ConditionalOnClass(name = {"com.baomidou.mybatisplus.extension.plugins.pagination.Page"})
    public void MybatisPlus() {
        ORMTypeEnum.CLIENT_TYPES.add(ORMTypeEnum.MYBATIS_PLUS);
        System.out.println("mybati plus");
    }

}
