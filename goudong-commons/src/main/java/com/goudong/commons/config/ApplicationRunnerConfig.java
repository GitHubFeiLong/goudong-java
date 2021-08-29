package com.goudong.commons.config;

import com.goudong.commons.openfeign.UserService;
import com.goudong.commons.pojo.ResourceAntMatcher;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.ResourceUtil;
import com.goudong.commons.vo.AuthorityMenu2InsertVO;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统启动就先加载热数据到redis中
 * @Author msi
 * @Date 2021-04-09 10:24
 * @Version 1.0
 */
@Slf4j
@Component
public class ApplicationRunnerConfig implements ApplicationRunner {
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动了服务");

        // 延迟启动（项目启动后，要过一段时间才会注册到nacos中，而这些任务会调用其它服务。）
        new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(5000);
                addMenus();
                addIgnoreResources();
            }
        }.start();
    }

    /**
     * 添加菜单
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addMenus() throws IOException, ClassNotFoundException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanMenu(contextPath);
        if (resourceAntMatchers.size() > 0) {
            List<AuthorityMenu2InsertVO> menu2InsertVOS = new ArrayList<>(resourceAntMatchers.size());
            resourceAntMatchers.forEach(p->{
                menu2InsertVOS.add(new AuthorityMenu2InsertVO(p.getPattern(), p.getMethod(), p.getRemark(), applicationName));
            });

            // 调用接口
            userService.addMenus(menu2InsertVOS);
        }
    }

    /**
     * 添加白名单
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addIgnoreResources() throws IOException, ClassNotFoundException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanIgnore(contextPath);
        // 有数据，插入数据库
        if (resourceAntMatchers.size() > 0) {
            List<BaseIgnoreResourceVO> baseIgnoreResourceVOS = BeanUtil.copyList(resourceAntMatchers, BaseIgnoreResourceVO.class);

            // 调用接口
            userService.addIgnoreResources(baseIgnoreResourceVOS);
        }

    }
}
