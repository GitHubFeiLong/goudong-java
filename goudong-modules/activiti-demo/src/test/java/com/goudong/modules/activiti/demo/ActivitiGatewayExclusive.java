package com.goudong.modules.activiti.demo;

import com.goudong.modules.activiti.demo.activiti.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * Activiti的排他网关测试类（exclusive）
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 9:10
 */
public class ActivitiGatewayExclusive {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 部署流程定义
     */
    @Test
    public void testDeployment() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 进行部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection-exclusive.bpmn20.xml")
                .name("出差申请流程-排他网关1")
                .deploy();
        // 输出部署信息
        System.out.println("流程部署id：" + deploy.getId());
        System.out.println("流程部署名称：" + deploy.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义key
        String key = "evection-exclusive";
        // 创建变量集合
        Map<String, Object> map = new HashMap<>();
        Evection evection = new Evection();
        evection.setNum(2d);
        // 将变量放入集合
        map.put("evection", evection);

        // 启动流程
        runtimeService.startProcessInstanceByKey(key, map);
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        // 流程实例id
        String processInstanceId = "87501";
        // 任务负责人id
        //String assignee = "zs";
        String assignee = "王经理";
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 查询负责人在流程的待办任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(assignee)
                .singleResult();

        // 完成任务
        if (task != null) {
            taskService.complete(task.getId());

            System.out.println("任务id：" + task.getId() + ",任务名：" + task.getName() + "。已完成");
        }
    }
}
