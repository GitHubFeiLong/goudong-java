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
public class SpringConfigTool implements ApplicationContextAware {

    private static ApplicationContext ac = null;
    private static SpringConfigTool springConfigTool = null;

    public synchronized static SpringConfigTool init() {
        if (springConfigTool == null) {
            springConfigTool = new SpringConfigTool();
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
}
