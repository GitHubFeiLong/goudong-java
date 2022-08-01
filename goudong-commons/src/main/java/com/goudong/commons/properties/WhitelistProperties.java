package com.goudong.commons.properties;

import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.commons.framework.whitelist.BaseWhitelistDTO;
import com.goudong.commons.utils.core.ResourceUtil;
import com.goudong.commons.utils.core.StringUtil;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 类描述：
 * 白名单属性配置
 * @author msi
 * @version 1.0
 * @date 2022/1/9 10:51
 */
@Data
@ConfigurationProperties(prefix = "commons.whitelist")
public class WhitelistProperties implements InitializingBean {

    //~fields
    //==================================================================================================================
    /**
     * 是否开启白名单
     */
    private Boolean enable = false;

    /**
     * 延迟指定时长后调用服务保存白名单（值须大于等于0，小于等于3600，单位是秒）
     */
    private Integer sleep = 5;

    /**
     * 调用服务保存白名单失败后的重试次数（值须大于等于0，小于10）
     */
    private Integer failureRetryNum = 2;

    /**
     * 自定义白名单配置
     */
    @NestedConfigurationProperty
    private List<BaseWhitelistDTO> whitelists = new ArrayList<>();
    //~methods
    //==================================================================================================================

    /**
     * Bean创建成功后执行操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /*
            参数校验
         */
        List<BaseWhitelistDTO> whitelists = this.getWhitelists();
        if (this.getEnable()) {
            // 延迟时长校验，不能随便配置一些不合理的时长
            if (this.sleep < 0 || this.sleep > 3600) {
                String description = String.format("commons.whitelist.sleep=%s", this.sleep);
                throw new ApplicationBootFailedException("自定义白名单模式配置错误",
                        "请配置正确的白名单,白名单的sleep配置有误:\n" + description,
                        String.format("commons.whitelist.sleep=%s配置正确的sleep", this.sleep));
            }

            // 延迟时长校验，不能随便配置一些不合理的时长
            if (this.failureRetryNum < 0 || this.failureRetryNum > 10) {
                String description = String.format("commons.whitelist.failureRetryNum=%s", this.failureRetryNum);
                throw new ApplicationBootFailedException("自定义白名单模式配置错误",
                        "请配置正确的白名单,白名单的failureRetryNum配置有误:\n" + description,
                        String.format("commons.whitelist.failureRetryNum=%s配置正确的failureRetryNum", this.failureRetryNum));
            }

            // 白名单资源参数校验
            if (CollectionUtils.isNotEmpty(whitelists)) {
                Iterator<BaseWhitelistDTO> iterator = whitelists.iterator();
                int idx = 0;
                while (iterator.hasNext()) {
                    BaseWhitelistDTO whitelistDTO = iterator.next();
                    // 校验模式
                    this.checkPattern(idx, whitelistDTO);
                    // 校验方法
                    checkMethods(idx, whitelistDTO);
                    // 下标自增
                    idx++;
                }
            }

        }
    }

    /**
     * 校验模式配置是否正确，如果发现配置的是路径参数，那么就将其转换成Ant
     * @param idx 下标
     * @param whitelistDTO 白名单具体内容
     * @return
     */
    private void checkPattern(int idx, BaseWhitelistDTO whitelistDTO) {
        String pattern = whitelistDTO.getPattern();
        if (StringUtils.isBlank(pattern)) {
            String description = String.format("commons.whitelist.whitelists[%s].pattern=%s", idx, pattern);
            throw new ApplicationBootFailedException("自定义白名单模式配置错误",
                    "请配置正确的白名单,白名单的pattern配置有误:\n" + description,
                    String.format("commons.whitelist.whitelists[%s].pattern配置正确的uri", idx));
        }
        // 将uri上的`{}`转换为`*`
        whitelistDTO.setPattern(StringUtil.replacePathVariable2Asterisk(pattern));
    }

    /**
     * 校验方法配置是否正确
     * @param idx 下标
     * @param whitelistDTO 白名单具体内容
     * @return
     */
    private void checkMethods(int idx, BaseWhitelistDTO whitelistDTO) {
        String methods = whitelistDTO.getMethods();
        // 校验方法
        boolean valid = ResourceUtil.validMethods(methods);
        if (!valid) {
            throw new ApplicationBootFailedException("自定义白名单请求方法配置错误",
                    String.format("配置方法属性错误:\ncommons.whitelist.whitelists[%s].methods=%s", idx, methods),
                    String.format("请配置commons.whitelist.whitelists[%s].methods属性正确的值%s\n" +
                                    "如果您想配置多个请求方式，请使用英文逗号进行分割\n" +
                                    "正确示例：commons.whitelist.whitelists[%s].methods=GET,POST",
                            idx,
                            HttpMethodConst.ALL_HTTP_METHOD,
                            idx));
        }
    }
}