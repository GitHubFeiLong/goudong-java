package com.goudong.commons.properties;

import com.goudong.commons.exception.core.ApplicationBootFailedException;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 * 接口资源配置
 * @author cfl
 * @date 2022/8/3 0:26
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "commons.api-resource")
public class ApiResourceProperties implements InitializingBean {

    //~fields
    //==================================================================================================================
    /**
     * 是否开启接口资源配置
     */
    private Boolean enable = false;

    /**
     * 延迟指定时长后调用服务保存接口资源（值须大于等于0，小于等于3600，单位是秒）
     */
    private Integer sleep = 5;

    /**
     * 调用服务保存接口资源配置失败后的重试次数（值须大于等于0，小于10）
     */
    private Integer failureRetryNum = 2;

    //~methods
    //==================================================================================================================

    /**
     * Bean创建成功后执行操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        /*
            参数校验
         */
        if (this.getEnable()) {
            // 延迟时长校验，不能随便配置一些不合理的时长
            if (this.sleep < 0 || this.sleep > 3600) {
                String description = String.format("commons.apiResource.sleep=%s", this.sleep);
                throw new ApplicationBootFailedException("自定义接口资源配置错误",
                        "请配置正确的属性值,apiResource的sleep配置有误:\n" + description,
                        String.format("commons.apiResource.sleep=%s配置正确的sleep", this.sleep));
            }

            // 延迟时长校验，不能随便配置一些不合理的时长
            if (this.failureRetryNum < 0 || this.failureRetryNum > 10) {
                String description = String.format("commons.apiResource.failureRetryNum=%s", this.failureRetryNum);
                throw new ApplicationBootFailedException("自定义接口资源配置错误",
                        "请配置正确的属性值,apiResource的failureRetryNum配置有误:\n" + description,
                        String.format("commons.apiResource.failureRetryNum=%s配置正确的failureRetryNum", this.failureRetryNum));
            }
        }
    }

}