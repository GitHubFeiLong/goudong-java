package com.goudong.boot.web.config;

import com.goudong.boot.web.handler.BasicExceptionHandler;
import com.goudong.boot.web.handler.DataIntegrityViolationExceptionHandler;
import com.goudong.boot.web.handler.JavaxValidationExceptionHandler;
import com.goudong.boot.web.handler.TransactionSystemExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 异常处理器配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 21:47
 */
@Configuration
@ConditionalOnWebApplication
public class ExceptionHandlerConfiguration {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 通用得异常处理
     *
     * @param request
     * @param response
     * @return
     */
    @Bean
    public BasicExceptionHandler basicExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new BasicExceptionHandler(request, response);
    }

    /**
     * 违反数据库约束得相关异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.dao.DataIntegrityViolationException"})
    public DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler() {
        return new DataIntegrityViolationExceptionHandler();
    }

    /**
     * 违反数据库约束得相关异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.transactionTransactionSystemException"})
    public TransactionSystemExceptionHandler transactionSystemExceptionHandler() {
        return new TransactionSystemExceptionHandler();
    }

    /**
     * javax.validation相关的异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"javax.validation.ValidationException"})
    public JavaxValidationExceptionHandler javaxValidationExceptionHandler() {
        return new JavaxValidationExceptionHandler();
    }

}
