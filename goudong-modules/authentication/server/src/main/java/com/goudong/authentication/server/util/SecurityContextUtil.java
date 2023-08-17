package com.goudong.authentication.server.util;

import com.goudong.authentication.server.service.dto.MyAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/8/3 16:15
 */
public class SecurityContextUtil {

    public static MyAuthentication get() {
        return (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
    }
}
