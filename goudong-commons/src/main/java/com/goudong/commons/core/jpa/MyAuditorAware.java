package com.goudong.commons.core.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 类描述：
 *
 * 获取...
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
            id = 0L;
        }
        return Optional.of(id);
    }
}
