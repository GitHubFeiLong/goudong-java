// package com.goudong.oauth2.config.security;
//
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.context.HttpRequestResponseHolder;
// import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
// import org.springframework.security.web.context.SecurityContextRepository;
// import org.springframework.web.util.WebUtils;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// /**
//  * 类描述：
//  *
//  * @author msi
//  * @version 1.0
//  * @date 2022/1/22 13:05
//  */
// public class SecurityContextRepositoryImpl implements SecurityContextRepository {
//     //~fields
//     //==================================================================================================================
//
//     //~methods
//     //==================================================================================================================
//     @Override
//     public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
//         return SecurityContextHolder.createEmptyContext();
//     }
//
//     @Override
//     public void saveContext(SecurityContext securityContext, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//         SaveContextOnUpdateOrErrorResponseWrapper responseWrapper = (SaveContextOnUpdateOrErrorResponseWrapper) WebUtils.getNativeResponse(response, SaveContextOnUpdateOrErrorResponseWrapper.class);
//         if (responseWrapper == null) {
//             throw new IllegalStateException("Cannot invoke saveContext on response " + response + ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
//         } else {
//             if (!responseWrapper.isContextSaved()) {
//                 responseWrapper.saveContext(context);
//             }
//
//         }
//     }
//
//     @Override
//     public boolean containsContext(HttpServletRequest httpServletRequest) {
//         return false;
//     }
//
//
//
// }