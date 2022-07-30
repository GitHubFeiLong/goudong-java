package com.goudong.modules.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 类描述：
 * 测试组任务(候选人)
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 20:29
 */
public class TestCandidate {
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
                .name("出差申请流程-Candidate")
                .addClasspathResource("bpmn/evection-candidate.bpmn20.xml")
                .deploy();
        // 4. 输出部署的信息
        System.out.println("流程部署id = " + deploy.getId());
        System.out.println("流程部署name = " + deploy.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("testCandidate");
    }

    /**
     * 查询组任务
     */
    @Test
    public void findGroupTaskList() {
        String key = "testCandidate";
        // 任务候选人
        String candidate = "wangwu";
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 查询组任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
                // 根据候选人查询任务
                .taskCandidateUser(candidate)
                .list();
        for (Task t : list) {
            System.out.println("流程实例id = " + t.getProcessInstanceId());
            System.out.println("任务id = " + t.getId());
            System.out.println("任务负责人 = " + t.getAssignee());
            System.out.println("===========");
        }


    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                // 流程定义key
                .processDefinitionKey("testCandidate")
                // 任务负责人
                .taskAssignee("zhangsan")
                .singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println("任务id=" + task.getId() + "完成了任务");
        }
    }
}
