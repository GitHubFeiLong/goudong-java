package com.goudong.modules.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

/**
 * 类描述：
 * 创建activiti 的表
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 1:19
 */
public class TestCreate {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 使用activiti提供的默认方式来创建mysql的表
     */
    @Test
    public void testCreateDbTable() {
        // 需要使用activitti提供的工具类 ProcessEngines
        // getDefaultProcessEngine 会默认从resources下读取名字为 activiti.cfg.xml 的文件
        // 创建 ProcessEngine 时，就会创建MySQL的表
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        System.out.println("processEngine = " + processEngine);
    }

    /**
     * 创建
     */
    @Test
    public void testCreateDbTable1() {
        // 使用自定义方式，配置文件的名字可以自定义, bean的名字也可以自定义
        //ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");

        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();

        System.out.println("processEngine = " + processEngine);
    }
}
