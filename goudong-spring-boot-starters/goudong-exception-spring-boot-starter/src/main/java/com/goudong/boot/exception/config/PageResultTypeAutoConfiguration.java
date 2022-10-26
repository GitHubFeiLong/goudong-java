package com.goudong.boot.exception.config;

import com.goudong.boot.exception.enumerate.PageTypeEnum;
import com.goudong.boot.exception.util.PageResultConvert;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * PageResult
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 14:55
 */
@Configuration
public class PageResultTypeAutoConfiguration {

    @Bean(autowireCandidate=false)
    @ConditionalOnClass(name = {"org.springframework.data.domain.Page"})
    public void JPA() {
        PageResultConvert.CLIENT_TYPES.add(PageTypeEnum.JPA);
        System.out.println("jpa ----");
    }

    @Bean
    @ConditionalOnClass(name = {"com.baomidou.mybatisplus.extension.plugins.pagination.Page"})
    public void MybatisPlus() {
        PageResultConvert.CLIENT_TYPES.add(PageTypeEnum.MYBATIS_PLUS);
        System.out.println("mybati plus");
    }

}
