package com.goudong.modules.activiti.spring;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 类描述：
 * 测试activiti与spring的整合是否成功
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 17:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:activiti-spring.xml")
public class ActivitiTest {
    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void test01(){
        System.out.println("部署对象:"+repositoryService);
    }

    /**
     * 测试部署
     */
    @Test
    public void testDeployment() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection.bpmn20.xml")
                .addClasspathResource("bpmn/evection.png")
                .name("测试spring出差申请流程")
                .deploy();
        System.out.println("流程部署成功：" + deploy);
    }
}
