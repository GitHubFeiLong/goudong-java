package com.goudong.boot.web.bean;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 请求接口切面处理
 * @author cfl
 * @version 1.0
 * @date 2023/4/4 10:25
 */
@Slf4j
public class ApiLogInterceptor implements MethodInterceptor {

    /**
     * Implement this method to perform extra treatments before and
     * after the invocation. Polite implementations would certainly
     * like to invoke {@link Joinpoint#proceed()}.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the call to {@link Joinpoint#proceed()};
     * might be intercepted by the interceptor
     * @throws Throwable if the interceptors or the target object
     *                   throws an exception
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        Map<String, String> requestHead = getRequestHead(request);
        /*
            简单打印基础信息
         */
        log.debug("URI: {}; Method：{}； HeadParams：{}", requestURI, method, requestHead);

        // 过滤掉部分参数

        List<Object> args = getArgs(invocation);


        StringBuffer sb = new StringBuffer();
        sb.append("\n-------------------------------------------------------------\n");
        sb.append("URI       : ").append(requestURI).append("\n");
        sb.append("Method    : ").append(method).append("\n");
        sb.append("HeadParams: ").append(requestHead).append("\n");
        ObjectMapper objectMapper = new ObjectMapper();
        // transient 忽略属性
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        // 修改时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        if (Objects.equals("GET", method)) {
            String params = objectMapper.writeValueAsString(request.getParameterMap());
            sb.append("Params    : ").append(params).append("\n");
        } else {
            String params = objectMapper.writeValueAsString(args);
            sb.append("Params    : ").append(params).append("\n");
        }

        Object result;
        try {
            result = invocation.proceed();
            // 忽略本次打印响应对象（默认不忽略）
            String s = objectMapper.writeValueAsString(result);
            if (s.length() <= 700) {
                sb.append("Results   : ").append(s).append("\n");
            }

            sb.append("Time      : ").append((System.currentTimeMillis() - startTime)).append("\n");
            sb.append("-------------------------------------------------------------\n");
            log.info(sb.toString());
        } catch (BasicException e) {
            log.info(sb.toString());
            throw e;
        } catch (InvocationTargetException ite) {
            Throwable targetException = ite.getTargetException();
            log.error(targetException.toString());
            throw targetException;
        } catch (Exception ex) {
            log.info(sb.toString());
            throw ex;
        }

        return result;
    }

    /**
     * 获取请求参数
     * @param invocation
     * @return
     */
    private static List<Object> getArgs(MethodInvocation invocation) {
        List<Class> filter = ListUtil.newArrayList(
                RequestFacade.class,
                ResponseFacade.class
        );
        Object[] argsArr = invocation.getArguments();
        // Stream.of(null).collect(Collectors.toList()) 会出现NPE
        if (argsArr != null && argsArr.length > 0) {
            log.debug("argsArr ＝{}", argsArr);
            // 过滤掉大对象，避免转json报错
            return Stream.of(argsArr)
                    // 过滤掉
                    .filter(f -> f != null && !filter.contains(f.getClass()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>(0);
    }

    /**
     * 获取x-开头的head参数和Authorization参数
     * @param request
     * @return
     */
    private Map<String, String> getRequestHead(HttpServletRequest request){
        //获取请求参数
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> data = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if(name.indexOf("x-")!=-1) {
                String value = request.getHeader(name);
                data.put(name, value);
            }
            if (Objects.equals(name.toLowerCase(), "authorization")) {
                data.put(name, request.getHeader(name));
            }
        }
        return data;
    }
}
