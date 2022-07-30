package com.goudong.modules.activiti;

import com.goudong.modules.activiti.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 测试流程变量
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 17:44
 */
public class TestVariables {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 测试流程部署
     */
    @Test
    public void testDeployment() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 使用service进行流程的部署，把bpmn和png部署到数据库中。 定义一个流程的名字。
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables")
                .addClasspathResource("bpmn/evection-global.bpmn20.xml")
                .deploy();
        // 4. 输出部署的信息
        System.out.println("流程部署id = " + deploy.getId());
        System.out.println("流程部署name = " + deploy.getName());
    }

    @Test
    public void deleteDeployment() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("22501");
    }

    /**
     * 启动流程的时候设置流程变量
     * 设置流程变量 num
     * 设置任务负责人
     */
    @Test
    public void testStartProcess() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 设置流程变量
        Evection evection = new Evection();
        evection.setNum(2d);
        Map<String, Object> map = new HashMap<>();
        map.put("evection", evection);

        // 设置Assignee
        map.put("assignee0", "李四");
        map.put("assignee1", "王经理");
        map.put("assignee2", "杨总经理");
        map.put("assignee3", "张财务");

        // 启动流程
        String key = "evection-global";
        runtimeService.startProcessInstanceByKey(key, map);
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();

        // 查询任务
        Task task = taskService.createTaskQuery()
                // 流程定义key
                .processDefinitionKey("evection-global")
                // 任务负责人
                .taskAssignee("王经理")
                .singleResult();

        // 判断是否存在任务
        if (task != null) {
            // 根据任务id完成任务
            taskService.complete(task.getId());
        }
    }
}
