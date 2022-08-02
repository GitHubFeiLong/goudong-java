package com.goudong.commons.framework.apiResource;

import com.goudong.commons.dto.oauth2.BaseApiResource2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.framework.core.ResourceAntMatcher;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.properties.ApiResourceProperties;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * zpi资源自动配置初始化
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 0:32
 */
@Slf4j
public class ApiResourceInitialize implements ApplicationRunner {
    //~fields
    //==================================================================================================================
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 服务名
     */
    @Value("${spring.application.name}")
    private String applicationName;

    private final GoudongOauth2ServerService goudongOauth2ServerService;
    /**
     * 配置文件配置的白名单
     */
    private final ApiResourceProperties apiResourceProperties;

    //~methods
    //==================================================================================================================
    public ApiResourceInitialize(GoudongOauth2ServerService goudongOauth2ServerService, ApiResourceProperties apiResourceProperties) {
        this.goudongOauth2ServerService = goudongOauth2ServerService;
        this.apiResourceProperties = apiResourceProperties;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.info(log, "准备处理接口资源");
        // 记录白名单集合
        List<BaseApiResource2CreateDTO> baseApiResource2CreateDTOS = new ArrayList<>();

        // 扫描api资源
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanApiResource(contextPath);

        resourceAntMatchers.stream().forEach(p->{
            baseApiResource2CreateDTOS.add(new BaseApiResource2CreateDTO(p.getPattern(), p.getMethod(), applicationName, ""));
        });

        // 使用feign，保存到指定库中
        if (CollectionUtils.isNotEmpty(baseApiResource2CreateDTOS)) {
            try {
                /*
                    这里需要进行延时调用，因为存在 goudong-oauth2-server 服务还未注册到注册中心去(goudong-oauth2-server自己调用自己的方式)
                    新开一个子线程，延时指定时间后调用
                 */
                new Thread(()->{
                    // 延迟时长
                    int sleep = ApiResourceInitialize.this.apiResourceProperties.getSleep() * 1000;
                    // 失败重试次数
                    int failureRetryNum = ApiResourceInitialize.this.apiResourceProperties.getFailureRetryNum();
                    LogUtil.debug(log, "延迟调用服务保存接口资源，sleep（延迟时长）={}s, failureRetryNum（失败重试次数）={}次", sleep, failureRetryNum);
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LogUtil.debug(log, "==开始调用服务保存接口资源==");
                    // 循环处理
                    for (int i = 0; i < failureRetryNum; i++) {
                        try {
                            Result<List<BaseWhitelistDTO>> result = goudongOauth2ServerService.addApiResources(baseApiResource2CreateDTOS);
                            // 调用成功了，退出循环
                            LogUtil.info(log, "调用服务保存接口资源成功：服务返回信息：\n{}", result);
                            break;
                        } catch (RuntimeException e) {
                            // 调用失败
                            if (i < failureRetryNum - 1) {
                                LogUtil.warn(log, "调用服务保存接口资源失败，原因：{}", e.getMessage());
                            } else {
                                LogUtil.error(log, "调用服务保存接口资源失败：原因：{}", e.getMessage());
                            }
                            // 睡眠会
                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }).start();

                return;
            } catch (RuntimeException e) {
                LogUtil.error(log, "认证服务接口调用失败：{}", e.getMessage());
            }
        } else {
            LogUtil.info(log, "没有接口资源需要保存");
        }
    }
}
