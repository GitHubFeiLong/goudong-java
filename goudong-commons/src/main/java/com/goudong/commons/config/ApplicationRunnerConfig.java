package com.goudong.commons.config;

import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.pojo.ResourceAntMatcher;
import com.goudong.commons.quartz.JobQuartzManager;
import com.goudong.commons.service.CommonsAuthorityMenuService;
import com.goudong.commons.service.CommonsIgnoreResourceService;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.ResourceUtil;
import com.goudong.commons.vo.AuthorityMenu2InsertVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
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
// @Component
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

    @Resource
    private CommonsIgnoreResourceService commonsIgnoreResourceService;

    @Resource
    private CommonsAuthorityMenuService commonsAuthorityMenuService;

    @Resource
    private JobQuartzManager quartzManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动了服务");

        // 延迟启动（项目启动后，要过一段时间才会注册到nacos中，而这些任务会调用其它服务。）
        new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                addMenus();
                addIgnoreResources();
            }
        }.start();
        quartzManager.start();
    }

    /**
     * 添加菜单
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addMenus() throws IOException, ClassNotFoundException, InterruptedException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanMenu(contextPath);
        if (resourceAntMatchers.size() > 0) {
            List<AuthorityMenu2InsertVO> menu2InsertVOS = new ArrayList<>(resourceAntMatchers.size());
            resourceAntMatchers.forEach(p->{
                menu2InsertVOS.add(new AuthorityMenu2InsertVO(p.getPattern(), p.getMethod(), p.getRemark(), applicationName));
            });

            List<AuthorityMenuDTO> insertDTOS = BeanUtil.copyList(menu2InsertVOS, AuthorityMenuDTO.class);
            commonsAuthorityMenuService.addList(insertDTOS);
        }
    }

    /**
     * 添加白名单
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addIgnoreResources() throws IOException, ClassNotFoundException, InterruptedException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanIgnore(contextPath);
        // 有数据，插入数据库
        if (resourceAntMatchers.size() > 0) {
            List<BaseIgnoreResourceDTO> baseIgnoreResourceDTOS = BeanUtil.copyList(resourceAntMatchers, BaseIgnoreResourceDTO.class);
            // 调用接口
            commonsIgnoreResourceService.addList(baseIgnoreResourceDTOS);
        }
    }

}
