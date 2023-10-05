package com.goudong.authentication.server.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 类描述：
 * 审计功能，填充 @CreatedBy @LastModifiedBy
 * 填充用户id
 * @author msi
 * @date 2021/12/11 20:41
 * @version 1.0
 */
@Component
public class MyAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Long id = null;
        if (id == null) {
            id = 1L;
        }
        return Optional.of(id);
    }
}
