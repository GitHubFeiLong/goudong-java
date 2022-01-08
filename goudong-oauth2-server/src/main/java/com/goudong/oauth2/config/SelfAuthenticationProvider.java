// package com.goudong.oauth2.config;
//
// import com.goudong.commons.enumerate.ClientExceptionEnum;
// import com.goudong.commons.exception.ClientException;
// import com.goudong.oauth2.service.SelfUserDetailsService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
//
// import javax.annotation.Resource;
//
// /**
//  * 自定义登录认证
//  * @Author msi
//  * @Date 2021-04-02 13:14
//  * @Version 1.0
//  */
// @Slf4j
// @Component
// public class SelfAuthenticationProvider implements AuthenticationProvider {
//
//     @Resource
//     private SelfUserDetailsService userService;
//
//     @Override
//     public Authentication authenticate(Authentication authentication) {
//
//         log.info("authentication >> {}", authentication.toString());
//         // 登录请求中的其他参数
//         //获取身份验证详细信息
//         CustomWebAuthenticationDetails customWebAuthenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
//         String phone = customWebAuthenticationDetails.getPhone();
//         String email = customWebAuthenticationDetails.getEmail();
//
//         //表单输入的用户名
//         String username = (String) authentication.getPrincipal();
//         //表单输入的密码
//         String password = (String) authentication.getCredentials();
//
//         // 用户名/电话/邮箱不传时直接抛出异常
//         if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
//             throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "请输入用户名和密码");
//         }
//         // 根据用户名查询用户是否存在
//         UserDetails userInfo = userService.loadUserByUsername(username);
//
//         // 使用 BCrypt 加密的方式进行匹配
//         boolean matches = new BCryptPasswordEncoder().matches(password, userInfo.getPassword());
//         // 密码不正确，抛出异常
//         if (!matches) {
//             throw ClientException.clientException(ClientExceptionEnum.UNPROCESSABLE_ENTITY, "用户名或密码错误");
//         }
//         // 验证通过，返回用户信息
//         return new UsernamePasswordAuthenticationToken(username, userInfo.getPassword(), userInfo.getAuthorities());
//     }
//
//     @Override
//     public boolean supports(Class<?> aClass) {
//         return true;
//     }
// }
