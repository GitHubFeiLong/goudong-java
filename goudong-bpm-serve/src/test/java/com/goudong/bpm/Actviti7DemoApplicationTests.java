package com.goudong.bpm;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 测试activiti
 * @author cfl
 * @version 1.0
 * @date 2022/8/1 5:15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Actviti7DemoApplicationTests {
    //~fields
    //==================================================================================================================
    public static final String[] usernames = {"zs", "ls", "ww", "jack"};
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;

    //~methods
    //==================================================================================================================

    public void login(String username) {
        BaseUserDTO user = new BaseUserDTO();
        user.setUsername(username);

        SecurityContextHolder.setContext(
                new SecurityContextImpl(new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        Collection<SimpleGrantedAuthority> roles = user.getRoles() == null ? new ArrayList() : user.getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                                .collect(Collectors.toList());

                        // 给用户添加默认流程角色，使其能正常使用：processRuntime，taskRuntime
                        roles.add( new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));

                        return roles;
                    }
                    @Override
                    public Object getCredentials() {
                        return user.getPassword();
                    }
                    @Override
                    public Object getDetails() {
                        return user;
                    }
                    @Override
                    public Object getPrincipal() {
                        return user;
                    }
                    @Override
                    public boolean isAuthenticated() {
                        return true;
                    }
                    @Override
                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
                    @Override
                    public String getName() {
                        return user.getUsername();
                    }
                })
        );

        //SecurityContextHolder.setContext(
        //        new SecurityContextImpl(
        //                new Authentication() {
        //                    @Override
        //                    public Collection<? extends GrantedAuthority> getAuthorities() {
        //                        Collection<SimpleGrantedAuthority> roles = user.getRoles() == null ? new ArrayList() : user.getRoles()
        //                                .stream()
        //                                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
        //                                .collect(Collectors.toList());
        //
        //                        // 给用户添加默认流程角色，使其能正常使用：processRuntime，taskRuntime
        //                        roles.add( new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));
        //
        //                        return roles;
        //                    }
        //                    @Override
        //                    public Object getCredentials() {
        //                        return user.getPassword();
        //                    }
        //                    @Override
        //                    public Object getDetails() {
        //                        return user;
        //                    }
        //                    @Override
        //                    public Object getPrincipal() {
        //                        return user;
        //                    }
        //                    @Override
        //                    public boolean isAuthenticated() {
        //                        return true;
        //                    }
        //                    @Override
        //                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
        //                    @Override
        //                    public String getName() {
        //                        return user.getUsername();
        //                    }
        //                }));
        // 设置当前用户的负责人信息（使用用户名）
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(user.getUsername());
    }

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
                .addClasspathResource("bpmn/evection-test1.bpmn20.xml")
                .name("部署出差流程")
                .deploy();
    }

    /**
     * 查看流程定义
     */
    @Test
    public void contextLoads() {
        login("zs");
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
        login("zs");
        ProcessInstance pi = processRuntime.start(ProcessPayloadBuilder.
                start()
                .withBusinessKey("100001")
                .withProcessDefinitionKey("evection-test1").
                build());
        System.out.println("流程实例ID：" + pi.getId());
    }


    /**
     **查询任务，并完成自己的任务
     **/
    @Test
    public void testTask() {
        String username = "zs";
        login(username);
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage.getTotalItems()>0){

            for (Task task:taskPage.getContent()){
                log.info("用户{}的任务：{}", username, task);
                taskRuntime.complete(TaskPayloadBuilder.
                        complete().
                        withTaskId(task.getId()).build());
                log.info("用户：{} {} 完成", username, task.getName());
            }
        } else {
            log.info("用户{},暂无任务", username);
        }
        Page<Task> taskPage2=taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage2.getTotalItems()>0){
            System.out.println("任务："+taskPage2.getContent());
        }
    }
}

