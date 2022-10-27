package com.goudong.boot.exception.config;

import com.goudong.boot.exception.util.PageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 分页类型自动配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 14:55
 */
@Configuration
public class PageTypeAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PageTypeAutoConfiguration.class);

    @Bean
    @ConditionalOnClass(name = {"org.springframework.data.jpa.repository.JpaRepository"})
    public void springData() {
        if (log.isDebugEnabled()) {
           log.debug("CLIENT_TYPES add jpa");
        }
        PageTypeEnum.CLIENT_TYPES.add(PageTypeEnum.JPA);
    }

    @Bean
    @ConditionalOnClass(name = {"com.baomidou.mybatisplus.extension.plugins.pagination.Page"})
    public void mybatisPlus() {
        if (log.isDebugEnabled()) {
            log.debug("CLIENT_TYPES add mybatis plus");
        }
        PageTypeEnum.CLIENT_TYPES.add(PageTypeEnum.MYBATIS_PLUS);
    }

}
