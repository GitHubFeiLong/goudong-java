package com.goudong.commons.framework.runner;

import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.dto.oauth2.HideMenu2CreateDTO;
import com.goudong.commons.framework.core.ResourceAntMatcher;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.properties.HideMenuProperties;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 启动应用时，隐藏菜单相关处理
 * @author cfl
 * @version 1.0
 * @date 2022/9/19 21:34
 */
@Slf4j
@RequiredArgsConstructor
public class HideMenuRunner implements ApplicationRunner {
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final GoudongOauth2ServerService goudongOauth2ServerService;
    /**
     * 配置
     */
    private final HideMenuProperties hideMenuProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.info(log, "准备处理隐藏菜单");
        // 记录白名单集合
        List<HideMenu2CreateDTO> hideMenu2CreateDTOS = new ArrayList<>();

        // 扫描源码上的注解进行添加白名单
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanHideMenu(contextPath);

        resourceAntMatchers.stream().forEach(p->{
            hideMenu2CreateDTOS.add(new HideMenu2CreateDTO(p.getPattern(), p.getMethod(), p.getRemark(), p.getSys(), p.getApi()));
        });

        // 使用feign，保存到指定库中
        if (CollectionUtils.isNotEmpty(hideMenu2CreateDTOS)) {
            try {
                /*
                    这里需要进行延时调用，因为存在 goudong-oauth2-server 服务还未注册到注册中心去(goudong-oauth2-server自己调用自己的方式)
                    新开一个子线程，延时指定时间后调用
                 */
                new Thread(()->{
                    // 延迟时长
                    int sleep = HideMenuRunner.this.hideMenuProperties.getSleep() * 1000;
                    // 失败重试次数
                    int failureRetryNum = HideMenuRunner.this.hideMenuProperties.getFailureRetryNum();
                    LogUtil.debug(log, "延迟调用服务保存隐藏菜单，sleep（延迟时长）={}s, failureRetryNum（失败重试次数）={}次", sleep, failureRetryNum);
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LogUtil.debug(log, "==开始调用服务保存隐藏菜单==");
                    // 循环处理
                    for (int i = 0; i < failureRetryNum; i++) {
                        try {
                            Result<List<BaseMenuDTO>> result = goudongOauth2ServerService.addHideMenu(hideMenu2CreateDTOS);
                            // 调用成功了，退出循环
                            LogUtil.info(log, "调用服务保存隐藏菜单成功：服务返回信息：\n{}", result);
                            break;
                        } catch (RuntimeException e) {
                            // 调用失败
                            if (i < failureRetryNum - 1) {
                                LogUtil.warn(log, "调用服务保存隐藏菜单失败，原因：{}", e.getMessage());
                            } else {
                                LogUtil.error(log, "调用服务保存隐藏菜单失败：原因：{}", e.getMessage());
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
            LogUtil.info(log, "没有隐藏菜单需要保存");
        }

    }

}
