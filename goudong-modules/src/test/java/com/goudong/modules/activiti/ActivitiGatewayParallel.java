package com.goudong.modules.activiti;

import com.goudong.modules.activiti.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * activiti 并行网关（parallel）
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 9:40
 */
public class ActivitiGatewayParallel {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 测试部署
     */
    @Test
    public void testDeployment() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 部署流程
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-并行网关")
                .addClasspathResource("bpmn/evection-parallel.bpmn20.xml")
                .deploy();
        System.out.println("流程部署id：" + deploy.getId());
        System.out.println("流程部署名称：" + deploy.getName());
    }

    /**
     * 启动流程实例，设置流程变量
     */
    @Test
    public void startProcess() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义key
        String key = "parallel";
        // 创建变量
        Map<String, Object> map = new HashMap<>();
        Evection evection = new Evection();
        evection.setNum(4d);
        map.put("evection", evection);

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("流程实例名字：" + processInstance.getName());
    }

    @Test
    public void completeTask() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 任务负责人id
        //String assignee = "tom";
        //String assignee = "jerry";
        String assignee = "jack";
        // 流程实例id
        String processInstanceId = "97501";
        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(assignee)
                .singleResult();

        if (task != null) {
            // 完成任务
            taskService.complete(task.getId());
        }
    }
}
