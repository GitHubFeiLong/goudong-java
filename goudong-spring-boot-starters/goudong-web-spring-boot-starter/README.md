# Goudong Web Spring Boot Starter
模块具有以下功能：
+ 定义了分页参数基类`com.goudong.boot.web.core.BasePage`
+ 针对JPA和Mybatis Plus的分页结果，进行转换成`com.goudong.core.lang.PageResult`
+ 动态定义多个全局异常处理，根据classpath判断进行加载。
+ 封装接口响应格式统一对象`com.goudong.core.lang.Result`。
+ 自定义了`com.goudong.boot.web.core.BasicException`异常

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
这样项目就有：
1. 动态全局异常
2. 传递Page对象，动态转换分页结果
3. 使用内置的Api快速抛出4xx异常和5xx异常
4. 数据库索引异常，可动态配置客户端提示信息

## 高阶使用

### 统一接口响应格式
```java
public class Result<T> implements Serializable {
    public int status = 200; // http状态码
    public String code = "0"; // 状态码(非”0“就是失败)
    public String clientMessage; // 客户端状态码对应信息
    public String serverMessage; // 服务器状态码对应信息
    public T data; // 数据
    public Map dataMap = new HashMap(); // 其它数据(可以扩充其它属性)
    public Date timestamp = new Date(); // 时间戳
}
```
dataMap可以进行扩展额外的信息，例如全局的日志id，某些错误不弹窗等。

### 分页

BasePage
```java
@Setter
@ToString
public class BasePage {

    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1, message = "分页参数错误，page必须大于等于1")
    @ApiModelProperty(value = "第几页,从1开始",required = true)
    private Integer page;

    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1, message = "分页参数错误，size必须大于等于1")
    @ApiModelProperty(value = "一页显示内容长度", required = true)
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    /**
     * 获取spring-data-jpa 框架的页码（以0开始），
     * @return
     */
    @ApiModelProperty(hidden = true)
    public Integer getJPAPage() {
        return page == null ? null : PageTypeEnum.JPA.getPage(page);
    }


    /**
     * 获取mybatis-plus 框架的页码
     * @return
     */
    @ApiModelProperty(hidden = true)
    public Integer getMPPage() {
        return page == null ? null : PageTypeEnum.MYBATIS_PLUS.getPage(page);
    }
}
```

PageResult
```java
public class PageResult<T> {
    private Long total; // 数据总条数
    private Long totalPage; // 数据总页数
    private Long page; // 当前页码
    private Long size; // 每页显示条目数
    private List<T> content; // 分页结果
}
```
1. 接口参数对象需要继承`com.goudong.boot.web.core.BasePage`
2. 使用`getJPAPage()`或`getMPPage()`获取对应的页码（前端页码固定从1开始）
3. 将JPA或Mybatis Plus查询出来的Page对象转换成统一的格式(`com.goudong.core.lang.PageResult`)
```java
// 如果你的项目中引入了JPA或引入了Mybatis Plus，那么可以直接使用
PageResult result = PageResultConvert.convert(page, target);
```

### 全局异常
项目启动至少会加载`com.goudong.boot.web.handler.BasicExceptionHandler`全局异常处理器。
如果引入了下面的依赖，会动态初始化预置的异常处理器：
1. 如果引入了`spring security`,会初始化`AccessDeniedExceptionHandler`.
2. 如果引入了`spring-tx`,会初始化`DataIntegrityViolationExceptionHandler`，`TransactionSystemExceptionHandler`
3. 如果引入了`validation-api`,会初始化`JavaxValidationExceptionHandler`.

`DataIntegrityViolationExceptionHandler`是处理持久层数据库相关的异常，常见的就是数据库唯一索引等异常

当数据库抛出唯一索引异常时，怎么进行自定义提示描述？
只需要定义一个`com.goudong.boot.web.bean.DatabaseKeyInterface`的bean即可
```java
@Component
public class DatabaseKey implements DatabaseKeyInterface {
    
    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return
     */
    @Override
    public String getClientMessage(String key) {
        // 这里就是根据唯一索引名 获取对应的错误提示
        return DatabaseKeyEnum.getClientMessage(key);
    }
}
```

### 抛出异常
请查看：`com.goudong.boot.web.core.BasicException` ，内部定义了很多静态方法，用来生成异常对象。
```java
BasicException.client("参数错误");
BasicException.server("根据id查询数据报错");
BasicException.quick(400, "参数错误");
...
```
