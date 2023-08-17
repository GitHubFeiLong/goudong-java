package com.goudong.authentication.server.config;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ErrorAttributesServiceImpl;
import com.goudong.core.lang.Result;
import io.jsonwebtoken.SignatureException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/24 9:27
 */
public class MyErrorAttributes extends ErrorAttributesServiceImpl {
    private final HttpServletRequest request;

    public MyErrorAttributes(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Throwable error = super.getError(webRequest);
        BasicException be;
        Result result;
        Map map;

        if (error instanceof SignatureException) {
            SignatureException exception = (SignatureException)error;
            result = Result.ofFail(BasicException.builder()
                    .code("403")
                    .status(403)
                    .clientMessage("令牌不属于该应用")
                    .build());
            map = BeanUtil.beanToMap(result, new String[0]);
            this.request.setAttribute("javax.servlet.error.status_code", HttpStatus.NOT_FOUND.value());
            return map;
        }

        return super.getErrorAttributes(webRequest, options);
    }
}
