package com.goudong.modules.spring.boot;

import com.goudong.modules.spring.boot.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 类描述：
 * 测试activiti7
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 17:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Actviti7DemoApplicationTests {
    //~fields
    //==================================================================================================================
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    //~methods
    //==================================================================================================================


    @Test
    public void testActBoot(){
        System.out.println(taskRuntime);
    }

    /**
     * 测试service部署流程
     */
    @Test
    public void testDeployment() {
        repositoryService.createDeployment()
                .addClasspathResource("processes/evection.bpmn20.xml")
                .name("部署出差流程")
                .deploy();
    }

    /**
     * 查看流程定义
     */
    @Test
    public void contextLoads() {
        securityUtil.logInAs("system");
        Page<ProcessDefinition> processDefinitionPage =
                processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("可用的流程定义数量：" + processDefinitionPage.getTotalItems());
        for (org.activiti.api.process.model.ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("流程定义：" + pd);
        }
    }


    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        securityUtil.logInAs("system");
        ProcessInstance pi = processRuntime.start(ProcessPayloadBuilder.
                start().
                withProcessDefinitionKey("evection-spring-boot").
                build());
        System.out.println("流程实例ID：" + pi.getId());
    }


    /**
     **查询任务，并完成自己的任务
     **/
    @Test
    public void testTask() {
        securityUtil.logInAs("jack");
        Page<Task> taskPage=taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage.getTotalItems()>0){
            for (Task task:taskPage.getContent()){
                taskRuntime.claim(TaskPayloadBuilder.
                        claim().
                        withTaskId(task.getId()).build());
                System.out.println("任务："+task);
                taskRuntime.complete(TaskPayloadBuilder.
                        complete().
                        withTaskId(task.getId()).build());
            }
        }
        Page<Task> taskPage2=taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage2.getTotalItems()>0){
            System.out.println("任务："+taskPage2.getContent());
        }
    }
}
