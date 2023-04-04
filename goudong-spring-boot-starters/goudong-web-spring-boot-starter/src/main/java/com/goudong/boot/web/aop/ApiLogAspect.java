// package com.goudong.boot.web.aop;
//
// import cn.hutool.json.JSON;
// import com.goudong.boot.web.core.BasicException;
// import com.goudong.core.lang.Result;
// import com.goudong.core.util.ListUtil;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.catalina.connector.RequestFacade;
// import org.apache.catalina.connector.ResponseFacade;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Pointcut;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.lang.reflect.InvocationTargetException;
// import java.util.*;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
//
// @Aspect
// @Component
// @Slf4j
// public class ApiLogAspect {
//
//     @Value("${}")
//     private static final String logName = "";
//
//
//     // @Pointcut("execution(public * com.hp.lottery.controller..*.*(..))")
//     @Pointcut(logName)
//     public void apiLogPointCut(){}
//
//     //@Around：环绕通知
//     @Around("apiLogPointCut()")
//     public Object saveSysLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//         long startTime = System.currentTimeMillis();
//         HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//         String requestURI = request.getRequestURI();
//         String method = request.getMethod();
//         Map<String, String> requestHead = getRequestHead(request);
//         /*
//             简单打印基础信息
//          */
//         log.debug("URI: {}; Method：{}； HeadParams：{}", requestURI, method, requestHead);
//
//         // 过滤掉部分参数
//         List<Object> args = getArgs(proceedingJoinPoint);
//
//
//         StringBuffer sb = new StringBuffer();
//         sb.append("\n-------------------------------------------------------------\n");
//         sb.append("URI       : ").append(requestURI).append("\n");
//         sb.append("Method    : ").append(method).append("\n");
//         sb.append("HeadParams: ").append(requestHead).append("\n");
//         if (Objects.equals("GET", method)) {
//             sb.append("Params    : ").append(request.getParameterMap()).append("\n");
//         } else {
//             sb.append("Params    : ").append(args).append("\n");
//         }
//
//         // sb.append("token    : ").append(request.getHeader("Authorization")).append("\n");
//         Object result = null;
//         try {
//             result = proceedingJoinPoint.proceed();
//             // 忽略本次打印响应对象（默认不忽略）
//             String s = result.toString();
//             if (s.length() <= 700) {
//                 sb.append("Results   : ").append(s).append("\n");
//             }
//
//             sb.append("Time      : ").append((System.currentTimeMillis() - startTime)).append("\n");
//             sb.append("-------------------------------------------------------------\n");
//             log.info(sb.toString());
//         } catch (BasicException e) {
//             log.info(sb.toString());
//             throw e;
//         } catch (InvocationTargetException ite) {
//             Throwable targetException = ite.getTargetException();
//             log.error(targetException.toString());
//             throw targetException;
//         } catch (Exception ex) {
//             log.info(sb.toString());
//             throw ex;
//         }
//
//         return result;
//     }
//
//     private static List<Object> getArgs(ProceedingJoinPoint proceedingJoinPoint) {
//         List<Class> filter = ListUtil.newArrayList(
//                 RequestFacade.class,
//                 ResponseFacade.class
//         );
//         Object[] argsArr = proceedingJoinPoint.getArgs();
//         // Stream.of(null).collect(Collectors.toList()) 会出现NPE
//         if (argsArr != null && argsArr.length > 0) {
//             log.debug("argsArr ＝{}", argsArr);
//             // 过滤掉大对象，避免转json报错
//             return Stream.of(argsArr)
//                     // 过滤掉
//                     .filter(f -> f != null && !filter.contains(f.getClass()))
//                     .collect(Collectors.toList());
//         }
//
//         return new ArrayList<>(0);
//     }
//
//     /**
//      * 获取x-开头的head参数
//      * @param request
//      * @return
//      */
//     private Map<String, String> getRequestHead(HttpServletRequest request){
//         //获取请求参数
//         Enumeration<String> headerNames = request.getHeaderNames();
//         Map<String, String> data = new HashMap<>();
//         while (headerNames.hasMoreElements()) {
//             String name = headerNames.nextElement();
//             if(name.indexOf("x-")!=-1) {
//                 String value = request.getHeader(name);
//                 data.put(name, value);
//             }
//         }
//         return data;
//     }
//
// }
