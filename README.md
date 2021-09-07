# goudong
该项目是使用maven进行打包构建，使用Spring Boot、Spring Cloud、Spring Cloud Alibaba搭建的微服务电商后端项目，该项目提供的接口根据Restful接口风格进行设计



## 模块

1. 公用包：goudong-commons
2. 商品服务：goudong-commodity-server
3. 网关服务：goudong-gateway-server
4. 消息服务：goudong-message-server
5. 认证服务：goudong-oauth2-server
6. 用户服务：goudong-user-server



## 软件及配置项
### 依赖的软件/组件及其版本
| 软件/组件       | 版本  |
| ----- | -----  |
| MySQL | 8.0.16|
| Redis | 3.0.504(后期会升高)|
| MongoDB | 4.4|
| Erlang | 11|
| RabbitMQ | 3.8.9|
| Nacos | 1.4.0|
| sentinel-dashboard | 1.8.2|

> 记录版本，防止使用其它版本有冲突。

### 操作系统环境变量配置
需要修改自己对应的属性值。
#### Windows
```bash
@echo off
echo windows平台设置goudong环境变量
:: MySQL
SET MYSQL_USERNAME=MySQL用户名
SET MYSQL_PASSWORD=MySQL密码

:: Redis
SET REDIS_PASSWORD=Redis密码

:: RabbitMQ
SET RABBITMQ_USERNAME=RabbitMQ用户名
SET RABBITMQ_PASSWORD=RabbitMQ密码

:: 邮箱
SET EMAIL_USERNAME=邮箱账户
SET EMAIL_PASSWORD=邮箱密码

:: 阿里云短信
SET ALI_MESSAGE_ACCESS_KEY_ID=短信key
SET ALI_MESSAGE_ACCESS_KEY_SECRET=短信密码
SET ALI_MESSAGE_SIGN_NAME=短信签名
SET ALI_MESSAGE_TEMPLATE_CODE=短信模板

echo 正在设置中,请稍等...
setx "MYSQL_USERNAME" "%MYSQL_USERNAME%"
setx "MYSQL_PASSWORD" "%MYSQL_PASSWORD%"
setx "REDIS_PASSWORD" "%REDIS_PASSWORD%"
setx "RABBITMQ_USERNAME" "%RABBITMQ_USERNAME%"
setx "RABBITMQ_PASSWORD" "%RABBITMQ_PASSWORD%"
setx "EMAIL_USERNAME" "%EMAIL_USERNAME%"
setx "EMAIL_PASSWORD" "%EMAIL_PASSWORD%"
setx "ALIBABA_MESSAGE_ACCESS_KEY_ID" "%ALI_MESSAGE_ACCESS_KEY_ID%"
setx "ALIBABA_MESSAGE_ACCESS_KEY_SECRET" "%ALI_MESSAGE_ACCESS_KEY_SECRET%"
setx "ALIBABA_MESSAGE_SIGN_NAME" "%ALI_MESSAGE_SIGN_NAME%"
setx "ALIBABA_MESSAGE_TEMPLATE_CODE" "%ALI_MESSAGE_TEMPLATE_CODE%"

pause
```
> 注意：
> 如果有信息是以下字符的，需要转义（使用^符号，如&应该写成^&）
> + @命令行回显屏蔽符 
> + %批处理变量引导符
> + \>重定向符
> + \>> 重定向符
> + \<、>、<& 重定向符
> + | 命令管道符
> + ^ 转义字符
> + & 组合命令
> + && 组合命令
> + || 组合命令
> + ""字符串界定符
> + , 逗号
> + ; 分号
> + () 括号
> + ! 感叹号


## 系统处理请求流程

流程图位置：（goudong-java\drawio\处理用户请求流程.drawio）

整个微服务的处理用户请求的流程如下图，共分为7个大步骤，其中在第2步网关中进行了大量的校验。

![处理用户请求流程-1](README.assets/%E5%A4%84%E7%90%86%E7%94%A8%E6%88%B7%E8%AF%B7%E6%B1%82%E6%B5%81%E7%A8%8B-1.png)

鉴权步骤：

1. 用户发起请求到网关
2. 网关根据请求uri判断是否需要放行（登录等不需要token都能访问的资源）。如果需要鉴权（除了前面的资源），那么就会进行token校验及鉴权：
   1. 校验
      1. 请求头中是否包含正确的请求头（Authorization）？
      2. 请求头Authorization的值是否是正确的格式？
      3. token颁发库中查询是否包含这个token？
      4. token没问题，账户有无失效（到期）？
   2. 鉴权
      1. 查询该资源需要拥有的角色，然后判断用户是否拥有这个角色？
   3. 异常
      1. 当校验token出现错误时，返回401状态码，前端重定向登录页。
      2. 当用户没有权限时，返回403状态码，提示用户没有权限。
   4. 校验和鉴权都正常，直接路由到指定的服务，处理请求。
3. 微服务处理请求，响应数据到网关，网关再反回给用户。



> 数据库是否有token签发记录 解释：系统每次创建token时都要先将其保存到mysql中然后再返给用户，当用户出现：修改密码、账户被冻结、账户被删除等操作后，将之前颁发的token从mysql中删除，达到一定程度的安全性。



## 约定大于配置（重要）

### Flyway

在Flyway的约定上，加上本项目自己的一些约定。

#### 约定1：脚本文件命名

使用简短的字符串，大致表示脚本功能，例如：`V1.0.0__DT_user.sql` `V1.0.0__DT_user#roles.sql`

下面罗列出常见的：

| 功能         | 关键字                                                       | 示例                                                | 解释                                    |
| ------------ | ------------------------------------------------------------ | --------------------------------------------------- | --------------------------------------- |
| 初始化表数据 | INIT:<br/>truncate table [表名] ；<br/>create table [表名]；<br/>insert into [表名] | R1.0__INIT_user.sql                                 | 创建全新的表并插入基本信息              |
| 删除整张表   | DT:<br />DROP TABLE tablename                                | `V1.0__DT_user.sql`,<br />`V1.0__DT_user#role.sql`  | 删除user表，删除user和role 两张表       |
| 修改表       | AT<br />ALTER TABLE 简写                                     | `v1.0__AT_user.sql`                                 | user表修改字段（AT是ATM，ATA，ATD超集） |
| 修改表字段   | ATM:<br />ALTER TABLE tablename MODIFY [COLUMN] column_definition [FIRST] [AFTER col_name] | `V1.0__ATM_user.sql`<br />`V1.0__ATM_user#role.sql` | 修改user表和role表的某些字段            |
| 增加表字段   | ATA<br />ALTER TABLE tablename ADD [COLUMN] column_definition [FIRST \|AFTER col_umn] | `v1.0__ATA_user.sql`                                | user表新增某些字段                      |
| 删除表字段   | ATD<br />ALTER TABLE tablename DROP [COLUMN] col_name        | `v1.0__ATD_user.sql`                                | user表删除某些字段                      |
| 插入记录     | IT<br />INSERT INTO tablename (filed1,filed2,...,filedn) values(value1,value2,...,valuen); | `v1.0__IT_user.sql`                                 | user表插入记录                          |
| 更新记录     | UT<br />UPDATE tablename SET filed1=value1,filed2=value2,...,filedn=valuen [WHERE CONDITION] | `v1.0__UT_user.sql`                                 | user表更新记录                          |
| 删除记录     | dt<br />DELETE FROM tablename [WHERE CONDITION]              | `v1.0__dt_user.sql`                                 | user表删除记录                          |

> 如果一张表和其他表没有关联关系，那么可以使用`R`开头的脚本，例如`R__init_table.sql` ，这样脚本更改后，就会执行

### POJO

#### 简介

[简介](https://blog.csdn.net/uestcyms/article/details/80244407)

本项目的约定如下

#### 约定1：视图层，服务层，持久层 使用对应XO

![image-20210517144446184](README.assets/image-20210517144446184.png)

解释：

1. 前端传递对象到视图层（控制器），视图层使用VO来接收（避免参数太多，前端根据文档，传参迷茫~）

2. 视图层将VO转换成DTO，然后将DTO传递给服务层（提高服务层的可用性）。
3. 服务层，将DTO转换成PO，一个PO对应数据库一个记录，进行数据库持久化操作。
4. 持久层将PO返回给服务层，服务层将其转换成DTO继续业务处理。
5. 业务层可能还会使用BO
6. 业务层将DTO返回给视图层。
7. 控制层将服务层返回的DTO，进行转换成前端需要的数据VO



### HTTP 响应代码

项目基本参照 [规范](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status) 来正确返回响应码



## 日志

使用log4j2：

1. 配置了开发环境和生产环境日志配置文件。
2. 配置日志归档及日志删除策略。

### log4j2.xml中格式转换字符

使用是需要加上`%`，例如`%c`。

| 字符 | 描述                                                         | 备注                                                    |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------- |
| c    | category的名称，可使用｛n}限制输出的精度。例如：logger名为"a.b.c"，%c{2}将输出"b.c"。（获取Logger对象类的完全限定名） | 输出Logger对象的名字，完全类名                          |
| C    | 产生log事件的java完全限定类名。可使用｛n}限制输出的精度。例如：“org.apache.xyz.SomeClass”,%C{2}将输出“SomeClass”。 | 硬编码日志打印的完全类名                                |
| d    | 时间和日期的输出格式，例如：%d{yyyy MM dd HH:mm:ss,SS}，可不带后面的日期格式字符。 | 输出日期                                                |
| F    | 产生log事件的java源文件名，带“.java”后缀及包名称。           | 输出的是文件名，是字符c的输出的简写                     |
| l    | log发生位置的详细描述，包括方法名、文件名及行号。            | 发生位置：打印日志的地方                                |
| L    | log发生在源文件中的位置。                                    | 发生位置的行号                                          |
| m    | log事件的消息内容。                                          | 输出的日志内容                                          |
| M    | log发生时所在的方法名称。                                    | 无                                                      |
| n    | 根据所运行的平台输出相应的行分隔字符。                       | 默认 `-`                                                |
| p    | log事件的级别。                                              | OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL |
| r    | 自程序运行至log事件产生所经过的时间。                        | 单位是ms                                                |
| t    | 产生log的线程名称。                                          | 无                                                      |

### 颜色

`%style{%d}{bright,green}`, 只需要将`%d`和`green` 修改成上面介绍的字符和颜色单词即可。



> 通过在``application.yml`中的`spring.profiles.active: xxx` 配置，然后在application-xxx.yml文件中配置`logging.config.classpath: log4j2-dev.xml` 属性，让其在不同环境不同日志配置。



## 用户角色权限



## 出色功能

### 防止重复提交

需要使用注解@Repeat，可以定义时间段

### 扫描白名单接口

使用注解@IgnoreResource，将接口保存到白名单（数据库中），程序启动后会定时扫描。

### 接口请求日志

使用AOP进行接口请求日志打印，将请求路径，请求参数，响应数据，异常等信息打印日志

### RedisTemplate扩展

RedisOperationsUtil，主要是因为整个系统的redis key 进行统一枚举处理（RedisKeyEnum），需要将key模板解析，redis失效等额外信息进行处理封装。
