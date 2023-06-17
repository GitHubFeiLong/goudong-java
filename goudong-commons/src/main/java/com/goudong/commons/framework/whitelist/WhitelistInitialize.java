// package com.goudong.commons.framework.whitelist;
//
// import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
// import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
// import com.goudong.commons.framework.core.ResourceAntMatcher;
// import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
// import com.goudong.commons.properties.WhitelistProperties;
// import com.goudong.commons.utils.core.LogUtil;
// import com.goudong.commons.utils.core.ResourceUtil;
// import com.goudong.core.lang.Result;
// import com.goudong.core.util.CollectionUtil;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
//
// /**
//  * 类描述：
//  * 白名单处理保存到数据库
//  * @author msi
//  * @date 2022/1/9 11:57
//  * @version 1.0
//  */
// @Slf4j
// public class WhitelistInitialize implements ApplicationRunner {
//     /**
//      * 应用上下文路径
//      */
//     @Value("${server.servlet.context-path}")
//     private String contextPath;
//
//     private final GoudongOauth2ServerService goudongOauth2ServerService;
//     /**
//      * 配置文件配置的白名单
//      */
//     private final WhitelistProperties whitelistProperties;
//
//     public WhitelistInitialize(GoudongOauth2ServerService goudongOauth2ServerService, WhitelistProperties whitelistProperties) {
//         this.goudongOauth2ServerService = goudongOauth2ServerService;
//         this.whitelistProperties = whitelistProperties;
//     }
//
//     @Override
//     public void run(ApplicationArguments args) throws Exception {
//         LogUtil.info(log, "准备处理白名单");
//         // 记录白名单集合
//         List<BaseWhitelist2CreateDTO> baseWhitelist2CreateDTOS = new ArrayList<>();
//
//         // 将自定义配置文件的白名单，放进集合
//         List<com.goudong.commons.framework.whitelist.BaseWhitelistDTO> whitelists = Optional.ofNullable(whitelistProperties.getWhitelists()).orElseGet(()->new ArrayList<>());
//         whitelists.stream().forEach(p->{
//             // 参数校验
//             String s = p.getMethods().toUpperCase();
//
//             List<String> methods = Stream.of(p.getMethods().toUpperCase().split(",")).collect(Collectors.toList());
//
//             baseWhitelist2CreateDTOS.add(new BaseWhitelist2CreateDTO(p.getPattern(), methods, p.getRemark(), p.getIsSystem(), p.getIsInner(), p.getIsDisable()));
//         });
//
//         // 扫描源码上的注解进行添加白名单
//         List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanWhitelist(contextPath);
//
//         resourceAntMatchers.stream().forEach(p->{
//             baseWhitelist2CreateDTOS.add(new BaseWhitelist2CreateDTO(p.getPattern(), p.getMethods(), p.getRemark(), false, p.getIsInner(), p.getIsDisable()));
//         });
//
//         // 使用feign，保存到指定库中
//         if (CollectionUtil.isNotEmpty(baseWhitelist2CreateDTOS)) {
//             try {
//                 /*
//                     这里需要进行延时调用，因为存在 goudong-oauth2-server 服务还未注册到注册中心去(goudong-oauth2-server自己调用自己的方式)
//                     新开一个子线程，延时指定时间后调用
//                  */
//                 new Thread(()->{
//                     // 延迟时长
//                     int sleep = WhitelistInitialize.this.whitelistProperties.getSleep() * 1000;
//                     // 失败重试次数
//                     int failureRetryNum = WhitelistInitialize.this.whitelistProperties.getFailureRetryNum();
//                     LogUtil.debug(log, "延迟调用服务保存白名单，sleep（延迟时长）={}s, failureRetryNum（失败重试次数）={}次", sleep, failureRetryNum);
//                     try {
//                         Thread.sleep(sleep);
//                     } catch (InterruptedException e) {
//                         throw new RuntimeException(e);
//                     }
//                     LogUtil.debug(log, "==开始调用服务保存白名单==");
//                     // 循环处理
//                     for (int i = 0; i < failureRetryNum; i++) {
//                         try {
//                             Result<List<BaseWhitelistDTO>> result = goudongOauth2ServerService.addWhitelists(baseWhitelist2CreateDTOS);
//                             // 调用成功了，退出循环
//                             LogUtil.info(log, "调用服务保存白名单成功：服务返回信息：\n{}", result);
//                             break;
//                         } catch (RuntimeException e) {
//                             // 调用失败
//                             if (i < failureRetryNum - 1) {
//                                 LogUtil.warn(log, "调用服务保存白名单失败，原因：{}", e.getMessage());
//                             } else {
//                                 log.error("调用服务保存白名单失败：原因：{}", e.getMessage());
//                             }
//                             // 睡眠会
//                             try {
//                                 Thread.sleep(sleep);
//                             } catch (InterruptedException ex) {
//                                 throw new RuntimeException(ex);
//                             }
//                         }
//                     }
//                 }).start();
//
//                 return;
//             } catch (RuntimeException e) {
//                 log.error("认证服务接口调用失败：{}", e.getMessage());
//             }
//         } else {
//             LogUtil.info(log, "没有白名单需要保存");
//         }
//
//     }
//
// }
