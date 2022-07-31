# activiti-spring
Activiti7发布正式版之后，它与SpringBoot2.x已经完全支持整合开发。

Activiti7可以自动部署流程，前提是在resources目录下，创建一个新的目录processes，用来放置bpmn文件。

## 插件
因为新版本的idea不在支持插件`actiBPM`,所以使用插件`Activiti BPMN visualizer`进行代替

## 使用
直接注入需要的service使用即可

## security
activiti-spring-boot-starter 中使用了spring security，所以需要配置security相关的配置。
并且观看源码，发现
`org.activiti.api.process.runtime.ProcessRuntime`和
`org.activiti.runtime.api.impl.TaskRuntimeImpl`
执行都需要角色`ROLE_ACTIVITI_USER`才能有权限使用