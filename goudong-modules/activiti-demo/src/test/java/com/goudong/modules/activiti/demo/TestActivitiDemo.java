package com.goudong.modules.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 类描述：
 * activiti的基本api使用
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 2:25
 */
public class TestActivitiDemo {
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
                .name("出差申请流程")
                // 后缀是bpmn或者是bpmn20.xml才会正确使用
                .addClasspathResource("bpmn/evection.bpmn20.xml")
                // 图片资源，只能是xxx.png 格式，不能是xxx.bpmn20.png格式，源码做了校验
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4. 输出部署的信息
        System.out.println("流程部署id = " + deploy.getId());
        System.out.println("流程部署name = " + deploy.getName());
    }

    /**
     * 启动流程实例
     * act_hi_actinst 流程实例执行历史
     * act_hi_identitylink 流程参与者的历史信息
     * act_hi_procinst 流程实例的历史信息
     * act_hi_taskinst 任务的历史信息
     * act_ru_execution 流程执行的信息
     * act_ru_identitylink 流程参与者信息
     * act_ru_task 任务信息
     */
    @Test
    public void testStartProcess() {
        // 1. 创建 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3. 根据流程id启动流程
        ProcessInstance myEvection = runtimeService.startProcessInstanceByKey("myEvection");
        // 4. 输出流程
        System.out.println("流程定义ID = " + myEvection.getProcessDefinitionId());
        System.out.println("流程实例ID = " + myEvection.getId());
        System.out.println("当前活动的ID = " + myEvection.getActivityId());
    }

    /**
     * 查询个人待执行的任务
     */
    @Test
    public void testFindPersonTaskList() {
        // 1. 创建 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 3. 根据流程key 和 任务负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                // 流程key
                .processDefinitionKey("myEvection")
                // 要查询的负责人
                .taskAssignee("zhangsan")
                .list();
        // 4. 输出流程
        for (Task task : taskList) {
            System.out.println("流程实例id = " + task.getProcessInstanceId());
            System.out.println("任务id = " + task.getId());
            System.out.println("任务负责人 = " + task.getAssignee());
            System.out.println("任务名称 = " + task.getName());
        }

    }

    /**
     * 完成张三个人任务
     */
    @Test
    public void completeTask() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        TaskService taskService = processEngine.getTaskService();
        // 根据任务id完成任务
        taskService.complete("2505");
    }

    /**
     * 完成剩下所有人的任务
     */
    @Test
    public void completeTask1() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取service
        TaskService taskService = processEngine.getTaskService();
        // 获取jerry的myEvection对应的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                //.taskAssignee("jerry")
                //.taskAssignee("jack")
                .taskAssignee("rose")
                .singleResult();

        System.out.println("流程实例id = " + task.getProcessInstanceId());
        System.out.println("任务id = " + task.getId());
        System.out.println("任务负责人 = " + task.getAssignee());
        System.out.println("任务名称 = " + task.getName());
        // 根据任务id完成任务
        taskService.complete(task.getId());
    }

    /**
     * 使用zip包进行批量部署
     * 好处：一次部署多个流程
     * 坏处：数据表中的资源名字字段只有文件名没有路径。
     */
    @Test
    public void deployProcessByZip() {
        // 1. 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 流程部署
        // 读取资源包文件构造 inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.bpmn20.zip");
        // 用inputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println("流程部署的id = " + deploy.getId());
        System.out.println("流程部署的名称 = " + deploy.getName());
        System.out.println("流程部署的key = " + deploy.getKey());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取ProcessDifinitionQuery对象
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        // 查询当前所有的流程定义,返回流程定义信息的集合
        List<ProcessDefinition> definitionList = definitionQuery
                // 查询流程定义的key
                .processDefinitionKey("myEvection")
                // 进行版本排序
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        for (ProcessDefinition p : definitionList) {
            System.out.println("流程定义id = " + p.getId());
            System.out.println("流程定义name = " + p.getName());
            System.out.println("流程定义key = " + p.getKey());
            System.out.println("流程定义版本 = " + p.getVersion());
            System.out.println("流程部署id = " + p.getDeploymentId());
        }

    }

    /**
     * 删除流程
     * 操作了表：act_ge_bytearray,act_re_deployment,act_re_procdef
     * 当前的流程如果并没有完成，想要删除的话需要使用特殊方式，原理就是 级联删除
     */
    @Test
    public void deleteDeployment() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎 获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 通过部署id来删除部署信息
        String deploymentId = "15001";

        // 该种方式，如果有未完成的流程，删除时会出错（外键）。
        //repositoryService.deleteDeployment(deploymentId);
        // 该种方式，会执行级联删除
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 下载资源文件
     * 方案1：使用activiti提供的api来下载资源文件，保存到文件目录
     * 方案2：自己写代码从数据库中下载文件，使用jdbc对blob或clob类型数据读取出来，保存到文件目录
     * 解决io操作：commons-io.jar
     *
     * 这里我们使用方案1，RepositoryService
     */
    @Test
    public void getDeployment() throws IOException {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 获取查询对象 ProcessDefinitionQuery，查询流程定义信息
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = definitionQuery.processDefinitionKey("myEvection")
                .latestVersion()
                .singleResult();
        // 通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
        // 通过 RepositoryService ，传递部署id参数，读取资源信息（png，和 bpmn）
        // 从流程定义表中，获取bpmn的目录和名字
        // bpmn
        InputStream resourceNameStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
        // 图片
        InputStream diagramResourceNameStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
        // 构造OutputStream
        File file1 = new File("d:/evection.bpmn20.xml");
        File file2 = new File("d:/evection.png");
        FileOutputStream fos1 = new FileOutputStream(file1);
        FileOutputStream fos2 = new FileOutputStream(file2);
        // 输入流 输出流转换
        IOUtils.copy(resourceNameStream, fos1);
        IOUtils.copy(diagramResourceNameStream, fos2);
        // 关闭流
        fos2.close();
        fos1.close();
        diagramResourceNameStream.close();
        resourceNameStream.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取 HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取 act_hi_actinst 表 的查询对象
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 获取查询act_hi_actinst 表
        //historicActivityInstanceQuery.processInstanceId("10001");
        historicActivityInstanceQuery.processDefinitionId("myEvection:1:4");
        // 增加排序操作, orderByHistoricActivityInstanceStartTime 根据开始时间排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> activityInstances = historicActivityInstanceQuery.list();

        // 输出
        for (HistoricActivityInstance hi : activityInstances) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getActivityType());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("===================");
        }
    }
}
