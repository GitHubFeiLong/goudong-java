package com.goudong.modules.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 14:37
 */
public class ActivitiBusinessDemo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 添加业务key到Actitivi的表
     */
    @Test
    public void addBusinessKey() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取 RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程的过程中，添加BusinessKey
        // 第一个参数是流程定义key，第二个参数是businessKey，存出差申请单的id，就是1001
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        // 输出
        System.out.println("businessKey = " + processInstance.getBusinessKey());
    }

    /**
     * 全部流程实例的挂起和激活
     */
    @Test
    public void suspendAllProcessInstance() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 获取当前流程定义的实例，是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 获取流程定义的id
        String definitionId = processDefinition.getId();
        if (suspended) {
            // 如果是挂起状态，改为激活状态
            // 参数1:是流程定义id, 参数2：是否激活，参数3：激活时间
            repositoryService.activateProcessDefinitionById(definitionId, true, null);

            System.out.println("流程定义id：" + definitionId + "，已激活");
        } else {
            // 如果是激活状态，改为挂起状态
            // 参数1:是流程定义id, 参数2：是否暂停，参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
            System.out.println("流程定义id：" + definitionId + "，已挂起");
        }

    }

    /**
     * 挂起或激活单个流程实例
     */
    @Test
    public void suspendSingleProcessInstance() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 通过RuntimeService获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                // act_ru_task的PROC_INST_ID_
                .processInstanceId("10001")
                .singleResult();
        // 得到当前流程实例的状态,true 已暂停，false 已激活
        boolean suspended = processInstance.isSuspended();
        // 获取流程实例id
        String id = processInstance.getId();
        if (suspended) {
            // 判断是否已经暂停，如果暂停了，就执行激活操作
            runtimeService.activateProcessInstanceById(id);
            System.out.println("流程实例id：" + id + "，已经激活");
        } else {
            // 如果是激活状态，就执行暂停操作
            System.out.println("流程实例id：" + id + "，已经暂停");
            runtimeService.suspendProcessInstanceById(id);
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 使用TaskService获取任务, 参数是流程实例的id，负责人
        Task task = taskService.createTaskQuery()
                .processInstanceId("10001")
                .taskAssignee("jerry")
                .singleResult();
        System.out.println("流程实例id = " + task.getProcessInstanceId());
        System.out.println("流程任务id = " + task.getId());
        System.out.println("负责人 = " + task.getAssignee());
        System.out.println("任务名称 = " + task.getName());
        // 根据任务id完成任务
        taskService.complete(task.getId());
    }
}
