// package com.goudong.commons.enumerate.jpa;
//
// import org.springframework.data.auditing.DateTimeProvider;
//
// import java.time.LocalDateTime;
// import java.time.temporal.TemporalAccessor;
// import java.util.Optional;
//
// /**
//  * 类描述：
//  *
//  * @author msi
//  * @version 1.0
//  * @date 2021/12/12 17:58
//  */
// public enum MyCurrentDateTimeProvider implements DateTimeProvider {
//     INSTANCE;
//
//     private MyCurrentDateTimeProvider() {
//     }
//
//     public Optional<TemporalAccessor> getNow() {
//         return Optional.of(LocalDateTime.now());
//     }
// }
