package com.goudong.commons.config;

import com.goudong.commons.framework.jpa.DataBaseAuditListener;
import com.goudong.commons.framework.jpa.MyAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 类描述：
 * commons配置Jpa
 * @author msi
 * @version 1.0
 * @date 2021/12/12 12:05
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="myAuditorAware")
public class JpaConfig {

    @Bean
    public MyAuditorAware myAuditorAware() {
        return new MyAuditorAware();
    }

    /**
     * jpa内置的审计功能，需要在属性上加上注解
     * @return
     */
    @Bean
    public AuditingEntityListener auditingEntityListener() {
        return new AuditingEntityListener();
    }

    /**
     * 自定义的jpa 审计功能，不需要加注解
     * @return
     */
    @Bean
    public DataBaseAuditListener dataBaseAuditListener() {
        return new DataBaseAuditListener();
    }

}
