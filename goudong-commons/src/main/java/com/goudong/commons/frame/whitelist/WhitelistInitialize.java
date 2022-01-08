package com.goudong.commons.frame.whitelist;

import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;
import com.goudong.commons.frame.core.ResourceAntMatcher;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.openfeign.GoudongUserServerService;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统启动就先加载热数据到redis中
 * @Author msi
 * @Date 2021-04-09 10:24
 * @Version 1.0
 */
@Slf4j
public class WhitelistInitialize implements ApplicationRunner {
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final GoudongUserServerService goudongUserServerService;

    public WhitelistInitialize(GoudongUserServerService goudongUserServerService) {
        this.goudongUserServerService = goudongUserServerService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.info(log, "开始处理白名单");
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanWhitelist(contextPath);

        // 将其转换成指定类型集合
        List<BaseWhitelist2CreateDTO> baseWhitelist2CreateDTOS = new ArrayList<>(resourceAntMatchers.size());
        resourceAntMatchers.stream().forEach(p->{
            baseWhitelist2CreateDTOS.add(new BaseWhitelist2CreateDTO(p.getPattern(), p.getMethods(), p.getRemark(), false));
        });

        // 使用feign，保存到指定库中
        Result<List<BaseWhitelistDTO>> result = goudongUserServerService.addWhitelist(baseWhitelist2CreateDTOS);
        LogUtil.info(log, "结束处理白名单:\n{}", result.getData());
    }

}
