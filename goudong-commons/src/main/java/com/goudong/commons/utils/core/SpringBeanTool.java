package com.goudong.commons.utils.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 类描述：
 * 获取Spring容器中的Bean工具类
 * 子服务需要在启动类上加上注解{@link com.goudong.commons.annotation.enable.EnableCommonsSpringBeanToolConfig}
 * 或者将其注入到Spring容器中才能使用。
 * @auther msi
 * @date 2022/3/14 14:55
 * @version 1.0
 */
public class SpringBeanTool implements ApplicationContextAware {

    /**
     * 应用上下文
     */
    private static ApplicationContext ac = null;

    /**
     * 当该类注入到Bean容器时，会执行方法将其成员变量设置值，后续在静态方法中就能愉快的使用成员变量ac
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        ac = applicationContext;
    }

    /**
     * 根据bean的名字从spring容器中获取bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return ac.getBean(beanName);
    }

    /**
     * 根据类获取bean
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        return ac.getBean(requiredType);
    }
}
