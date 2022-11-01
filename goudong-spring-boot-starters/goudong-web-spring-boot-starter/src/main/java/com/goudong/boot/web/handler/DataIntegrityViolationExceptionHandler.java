package com.goudong.boot.web.handler;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.DatabaseKeyEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.Result;
import com.goudong.core.util.MessageFormatUtil;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 类描述：
 * 数据库约束相关的异常处理
 * @author cfl
 * @version 1.0
 * @date 2022/11/1 16:43
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DataIntegrityViolationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DataIntegrityViolationExceptionHandler.class);

    /**
     * 数据库相关报错
     * ps 最好自己捕获抛出详细信息给前端,但是都用400
     * @param exception
     * @return
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<BasicException> dataIntegrityViolationExceptionDispose(DataIntegrityViolationException exception) {
        BasicException basicException = BasicException.generateByServer(exception);
        String message = exception.getRootCause().getMessage();

        if (exception.getCause() instanceof DataException) {
            DataException dataException = (DataException)exception.getCause();
            /*
                var1示例:
                1. Data truncation: Data too long for column 'name' at row 1
             */
            String serverMessage = dataException.getCause().getMessage();
            if (serverMessage.contains("Data truncation: Data too long for column")) {
                String column = serverMessage.substring(serverMessage.indexOf("'") + 1, serverMessage.lastIndexOf("'"));
                String clientMessage = MessageFormatUtil.format("参数{}太长", column);
                // String clientMessage = MessageFormatUtil.format("参数{}太长", column);
                basicException = ClientException.client(ClientExceptionEnum.BAD_REQUEST, clientMessage, serverMessage);
            }
        } else if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException) { // 外键约束 || 唯一索引约束 || 非空约束
            org.hibernate.exception.ConstraintViolationException constraintViolationException = (org.hibernate.exception.ConstraintViolationException)exception.getCause();
            // 约束索引名，非空约束时值为null
            String constraintName = constraintViolationException.getConstraintName();
            String clientMessage = Optional.ofNullable(
                    DatabaseKeyEnum.getClientMessage(constraintName)
            ).orElse(message);
            String serverMessage = constraintViolationException.getCause().getMessage();
            basicException = ClientException.client(ClientExceptionEnum.BAD_REQUEST, clientMessage, serverMessage);
            System.out.println(123);
        } else {
            if (message.startsWith("Cannot add or update a child row: a foreign key constraint fails") && message.endsWith("ON DELETE RESTRICT ON UPDATE RESTRICT)")) {
                // 数据库更新或删除表的数据，外键值不正确
                // e.getRootCause().getMessage() => Cannot add or update a child row: a foreign key constraint fails (`icc`.`base_role_menu`, CONSTRAINT `fk_base_role_menu__base_menu_id` FOREIGN KEY (`base_menu_id`) REFERENCES `base_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT)
                String clientMessage = MessageFormatUtil.format("参数错误");
                String serverMessage = message;
                basicException = ClientException.client(ClientExceptionEnum.BAD_REQUEST, clientMessage, serverMessage);
            }
        }


        // 修改数据库表非空报错
        if (message.startsWith("Column") && message.endsWith("cannot be null")) {
            String serverMessage = MessageFormatUtil.format("{}. \n {}", message, exception.getMessage());
            basicException = ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
        }

        log.error(BasicExceptionHandler.LOG_ERROR_INFO, basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap());
        // 堆栈跟踪
        BasicExceptionHandler.printErrorMessage("dataIntegrityViolationExceptionDispose", exception);

        return Result.ofFail(basicException);
    }
}
