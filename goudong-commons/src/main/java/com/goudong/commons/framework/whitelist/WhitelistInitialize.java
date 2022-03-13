package com.goudong.commons.framework.whitelist;

import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.framework.core.ResourceAntMatcher;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.properties.WhitelistProperties;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 白名单处理保存到数据库
 * @author msi
 * @date 2022/1/9 11:57
 * @version 1.0
 */
@Slf4j
public class WhitelistInitialize implements ApplicationRunner {
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final GoudongOauth2ServerService goudongOauth2ServerService;
    /**
     * 配置文件配置的白名单
     */
    private final WhitelistProperties whitelistProperties;

    public WhitelistInitialize(GoudongOauth2ServerService goudongOauth2ServerService, WhitelistProperties whitelistProperties) {
        this.goudongOauth2ServerService = goudongOauth2ServerService;
        this.whitelistProperties = whitelistProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.info(log, "开始处理白名单");
        // 记录白名单集合
        List<BaseWhitelist2CreateDTO> baseWhitelist2CreateDTOS = new ArrayList<>();

        // 将自定义配置文件的白名单，放进集合
        List<com.goudong.commons.framework.whitelist.BaseWhitelistDTO> whitelists = Optional.ofNullable(whitelistProperties.getWhitelists()).orElseGet(()->new ArrayList<>());
        whitelists.stream().forEach(p->{
            // 参数校验
            String s = p.getMethods().toUpperCase();

            List<String> methods = Stream.of(p.getMethods().toUpperCase().split(",")).collect(Collectors.toList());

            baseWhitelist2CreateDTOS.add(new BaseWhitelist2CreateDTO(p.getPattern(), methods, p.getRemark(), p.getIsSystem()));
        });

        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanWhitelist(contextPath);

        resourceAntMatchers.stream().forEach(p->{
            baseWhitelist2CreateDTOS.add(new BaseWhitelist2CreateDTO(p.getPattern(), p.getMethods(), p.getRemark(), false));
        });

        if (CollectionUtils.isNotEmpty(baseWhitelist2CreateDTOS)) {
            // 使用feign，保存到指定库中
            try {
                Result<List<BaseWhitelistDTO>> result = goudongOauth2ServerService.addWhitelist(baseWhitelist2CreateDTOS);
                LogUtil.info(log, "结束处理白名单:\n{}", result.getData());
                return;
            } catch (RuntimeException e) {
                LogUtil.error(log, "认证服务接口调用失败：{}", e.getMessage());
            }

        }

        LogUtil.info(log, "没有白名单需要保存");

    }

}
