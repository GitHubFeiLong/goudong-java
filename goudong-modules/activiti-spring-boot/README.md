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

## 注意事项
### Activiti工作流引擎启动提示表不存在解决方案
activiti引擎启动后，会去判断数据库中是否有和activiti相关的表（act_开头），如果没有则创建表。问题就是在查询有无相关表时，并不是查询当前选中的数据库，而是查询 mysql 的 INFORMATION_SCHEMA.TABLES表，这个表中存了所有数据库的表信息。言简意赅的说，原因就是当前mysql的其他库里有activiti相关表，所以activiti不会再创建，然而用的时候，它又在库中找不到对应表，所以报错了。
解决办法：
jdbc连接地址加上参数：nullCatalogMeansCurrent=true
```yaml
spring:
  datasource:
    url: jdbc:mysql:///activiti?nullCatalogMeansCurrent=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&createDatabaseIfNotExist=true
```