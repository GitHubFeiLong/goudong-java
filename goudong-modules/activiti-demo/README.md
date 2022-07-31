# activiti-demo
该模块是使用最基础的Activiti的依赖，未整合spring或spring boot。

## 插件
因为新版本的idea不在支持插件`actiBPM`,所以使用插件`Activiti BPMN visualizer`进行代替

## 目录
1. resources/bpmn 目录下存放的是bpmn2.0的流程图等资源
2. test 包下是具体的代码教程

## 配置文件
acitviti的配置文件，默认是放在resources目录下，并且名字叫activiti.cfg.xml。
> 注意：默认方式目录和文件名不能修改，因为activiti的源码中已经设置，到固定的目录读取固定文件名的文件
> 

## 测试类观看顺序
1. TestCreate 创建数据表
2. TestActivitiDemo activiti的基本api使用
3. TestActivitiBusinessDemo activiti流程实例保存业务标识（业务系统的主键），挂起和激活流程
4. TestAssigneeUel activiti的任务负责人assignee，使用uel-value表达式
5. TestVariables，TestVariables1 activiti使用uel变量
6. TestVariables2Complete activiti的task完成时设置流程变量
7. TestListener、MyTaskListener Activiti监听器
8. TestCandidate 组任务，候选人（多个候选人之间用逗号分开。 ）
9. ActivitiGatewayExclusive 排他网关（都满足，执行id小的，都不满足报错）
10. ActivitiGatewayParallel 并行网关（并行网关不会解析条件。** **即使顺序流中定义了条件，也会被忽略。**）
11. ActivitiGatewayInclusive 包含网关（流程变量不存在，报错;）
12. 事件网关（需要查看资料）
> 流程变量，在任务执行时，如果有流程变量未定义，activit会报错