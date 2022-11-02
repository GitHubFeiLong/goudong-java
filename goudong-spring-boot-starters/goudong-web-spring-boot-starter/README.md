# Goudong Web Spring Boot Starter
模块具有以下功能：
+ 定义了分页参数基类`com.goudong.boot.web.core.BasePage`
+ 针对各种ORM的分页结果，进行转换成`com.goudong.core.lang.PageResult`
+ 全局异常处理

## 快速使用
1. 在您项目的pom.xml中引入本项目maven依赖:
```xml
<dependency>
    <groupId>com.goudong</groupId>
    <artifactId>goudong-web-spring-boot-starter</artifactId>
    <version>${last.version}</version>
</dependency>
```
2. 在启动类上添加注解`@EnableCommonsWebMvcConfig`
```java
@SpringBootApplication
@EnableCommonsWebMvcConfig
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
这样就能开启项目预置的全局异常处理。

## 高阶使用
### 分页
```java


```
