// package com.goudong.oauth2.config;
//
// import lombok.Getter;
// import org.springframework.security.web.authentication.WebAuthenticationDetails;
//
// import javax.servlet.http.HttpServletRequest;
// import java.io.Serializable;
//
// /**
//  * 类描述：
//  * 自定义web身份验证详细信息(用于登录验证中增加额外参数)
//  * @ClassName CustomWebAuthenticationDetails
//  * @Author msi
//  * @Date 2021-05-02 10:41
//  * @Version 1.0
//  */
// @Getter
// public class CustomWebAuthenticationDetails extends WebAuthenticationDetails implements Serializable {
//     /**
//      * 电话
//      */
//     private String phone;
//     /**
//      * 邮箱
//      */
//     private String email;
//
//     CustomWebAuthenticationDetails(HttpServletRequest httpServletRequest) {
//         super(httpServletRequest);
// //        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
// //        while (headerNames.hasMoreElements()) {
// //            String s = headerNames.nextElement();
// //            String header = httpServletRequest.getHeader(s);
// //            System.out.println(s + ": " + header);
// //        }
// //        phone = httpServletRequest.getParameter("phone");
// //        email = httpServletRequest.getParameter("email");
//     }
//
// }
