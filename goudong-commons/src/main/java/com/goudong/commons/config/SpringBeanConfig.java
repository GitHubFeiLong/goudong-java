package com.goudong.commons.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 * 获取Spring容器中的Bean工具类
 * @Author e-Feilong.Chen
 * @Date 2021/8/11 15:25
 */
@Component
public class SpringBeanConfig implements ApplicationContextAware {

    private static ApplicationContext ac = null;
    private static SpringBeanConfig springConfigTool = null;

    public synchronized static SpringBeanConfig init() {
        if (springConfigTool == null) {
            springConfigTool = new SpringBeanConfig();
        }
        return springConfigTool;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        ac = applicationContext;
    }

    /**
     * 根据bean的名字从spring容器中获取bean
     * @param beanName
     * @return
     */
    public synchronized static Object getBean(String beanName) {
        return ac.getBean(beanName);
    }

    /**
     * 根据bean的名字从spring容器中获取bean
     * @param beanName
     * @return
     */
    // public synchronized static Object getBean(String beanName, Class<?> clazz) {
    //     return (clazz)ac.getBean(beanName);
    // }
}
