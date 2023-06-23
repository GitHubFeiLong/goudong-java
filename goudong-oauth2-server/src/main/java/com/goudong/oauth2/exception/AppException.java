package com.goudong.oauth2.exception;


import com.goudong.boot.web.core.BasicException;
import com.goudong.oauth2.enumerate.ExceptionEnum;

/**
 * 类描述：
 * 应用相关异常
 * <pre>
 *     AppException.builder().clientMessage("应用不存在").serverMessage("请勿伪造应用id").build()
 * </pre>
 * @author cfl
 * @date 2023/6/23 10:32
 * @version 1.0
 */
public class AppException extends BasicException {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 该构造方法不直接使用
     * @param e
     */
    public AppException(BasicException e) {
        super(e);
    }

    /**
     * 创建构建者
     * @return
     */
    public static BasicExceptionBuilder builder() {
        return new AppExceptionBuilder();
    }

    public static BasicExceptionBuilder builder(ExceptionEnum exceptionEnum) {
        return new AppExceptionBuilder(exceptionEnum);
    }

    /**
     * 类描述：
     * 创建ClientExceptionBuilder的构建对象
     * @author cfl
     * @date 2023/3/20 15:15
     * @version 1.0
     */
    public static class AppExceptionBuilder extends BasicExceptionBuilder{

        public AppExceptionBuilder() {
            super();
        }

        public AppExceptionBuilder(ExceptionEnum exceptionEnum) {
            super(exceptionEnum);
        }

        /**
         * 根据成员变量，创建BasicException对象
         *
         * @return BasicException
         */
        @Override
        public BasicException build() {
            return new AppException(super.build());
        }
    }
}
