// package com.goudong.oauth2.entity;
//
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
//
// import java.util.Collection;
// import java.util.List;
//
// /**
//  * 类描述：
//  *
//  * @author msi
//  * @version 1.0
//  * @date 2021/8/29 19:34
//  */
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// public class User implements UserDetails {
//     private Long id;
//     private String username;
//     private String password;
//
//     List<GrantedAuthority> authorityList;
//
//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return authorityList;
//     }
//
//     /**
//      * 用户是否未失效
//      * @return
//      */
//     @Override
//     public boolean isAccountNonExpired() {
//         return true;
//     }
//
//     /**
//      * 账号是否未被锁定
//      * @return
//      */
//     @Override
//     public boolean isAccountNonLocked() {
//         return true;
//     }
//
//     /**
//      * 用户凭证是否未失效
//      * @return
//      */
//     @Override
//     public boolean isCredentialsNonExpired() {
//         return true;
//     }
//
//     /**
//      * 用户是否有效
//      * @return
//      */
//     @Override
//     public boolean isEnabled() {
//         return true;
//     }
// }
