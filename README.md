# goudong
该项目是使用maven进行打包构建，使用Spring Boot、Spring Cloud、Spring Cloud Alibaba搭建的微服务电商后端项目，该项目提供的接口根据Restful接口风格进行设计
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
## 约定大于配置（重要）

### Flyway

在Flyway的约定上，加上本项目自己的一些约定。

#### 约定1：脚本文件命名

使用简短的字符串，大致表示脚本功能，例如：`V1.0.0__DT_user.sql` `V1.0.0__DT_user#roles.sql`

下面罗列出常见的：

| 功能       | 关键字                                                       | 示例                                                | 解释                                    |
| ---------- | ------------------------------------------------------------ | --------------------------------------------------- | --------------------------------------- |
| 删除整张表 | DT:<br />DROP TABLE tablename                                | `V1.0__DT_user.sql`,<br />`V1.0__DT_user#role.sql`  | 删除user表，删除user和role 两张表       |
| 修改表     | AT<br />ALTER TABLE 简写                                     | `v1.0__AT_user.sql`                                 | user表修改字段（AT是ATM，ATA，ATD超集） |
| 修改表字段 | ATM:<br />ALTER TABLE tablename MODIFY [COLUMN] column_definition [FIRST] [AFTER col_name] | `V1.0__ATM_user.sql`<br />`V1.0__ATM_user#role.sql` | 修改user表和role表的某些字段            |
| 增加表字段 | ATA<br />ALTER TABLE tablename ADD [COLUMN] column_definition [FIRST \|AFTER col_umn] | `v1.0__ATA_user.sql`                                | user表新增某些字段                      |
| 删除表字段 | ATD<br />ALTER TABLE tablename DROP [COLUMN] col_name        | `v1.0__ATD_user.sql`                                | user表删除某些字段                      |
| 插入记录   | IT<br />INSERT INTO tablename (filed1,filed2,...,filedn) values(value1,value2,...,valuen); | `v1.0__IT_user.sql`                                 | user表插入记录                          |
| 更新记录   | UT<br />UPDATE tablename SET filed1=value1,filed2=value2,...,filedn=valuen [WHERE CONDITION] | `v1.0__UT_user.sql`                                 | user表更新记录                          |
| 删除记录   | dt<br />DELETE FROM tablename [WHERE CONDITION]              | `v1.0__dt_user.sql`                                 | user表删除记录                          |



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



