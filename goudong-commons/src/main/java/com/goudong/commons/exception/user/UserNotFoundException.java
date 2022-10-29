// package com.goudong.commons.exception.user;
//
//
// import com.goudong.boot.web.enumerate.ClientExceptionEnum;
//
// /**
//  * 类描述：
//  * 用户未找到异常
//  * @author msi
//  * @version 1.0
//  * @date 2022/1/22 23:13
//  */
// public class UserNotFoundException extends UserException{
//
//     //~fields
//     //==================================================================================================================
//     public static final String DEFAULT_CLIENT_MESSAGE = "账户已过期";
//     //~methods
//     //==================================================================================================================
//
//     /**
//      * 默认提示信息
//      */
//     public UserNotFoundException() {
//         super(ClientExceptionEnum.UNAUTHORIZED, DEFAULT_CLIENT_MESSAGE);
//
//     }
//
//     /**
//      * 自定义提示信息
//      * @param clientMessage
//      */
//     public UserNotFoundException(String clientMessage) {
//         super(ClientExceptionEnum.UNAUTHORIZED, clientMessage);
//
//     }
// }
