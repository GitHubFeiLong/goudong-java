# 微服务公用组件

微服务中一些公共配置放在该模块中。

## 包及作用
1. analyzer 自定义应用由于开发者自身原因导致启动失败时，输出相关的日志
2. annotation 存放自定义注解
3. aop 配置全局的aop
4. autoconfigure 存放自动配置类,只需在resources/META-INF/spring.factories 添加自动配置类，就会自动进行配置相关功能
5. condition 定义一些自定义的Condition,用于条件加载bean
6. config 对某些功能，某些组件的配置，其它服务想要指定配置生效，只需引入相关依赖，在启动类上加上@EnableCommonXXX类似的注解即可
7. constant 微服务中共享的常量类
8. core 核心包
9. dto 微服务需要跟其它服务传递的对象DTO
10. enumerate 微服务中共享的枚举
11. exception 整套系统定义的最基础的异常类
12. filter 微服务共享的过滤器
13. framework 自己封装框架的工具，和自己写的新功能。
14. function 函数式接口
15. http http请求相关的工具
16. interceptor 微服务共享拦截器
17. po 存储公用的po对象，很少用，服务现在都是各存各的
18. pojo 存放一些普通的类
19. properties 存放配置spring boot 的properties的对象，根据配置文件配置对象值，并在其他地方进行使用配置
20. security 安全相关，加密算法等
21. tree 操作树的一个工具包
22. utils 公用一些工具类
23. validator 存放自定义参数校验功能，搭配指定注解，实现与参数校验注解一样的能力。

## 重点
### 自定义注解的使用
#### @EnableCommonsXXX 注解使用
包地址：该类注解存放在`com.goudong.commons.annotation.enable`包下。
功能：实现类似于`@EnableDiscoveryClient`的功能，同意管理系统的相关组件配置。
开发原因：因为我看见spring boot 使用其他开源框架都有使用，比如：
```java
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.goudong.user.repository"})
```
使用这类注解就可以在启动的时候执行一些特定的流程，感觉很棒，最主要的原因，还是因为微服务太多，每个服务都自己写自己的配置不太优雅，也不好维护，
于是我也开发了这类似的功能，让组件的配置都在`goudong-commons`模块中配置，其它服务需要时，只需要：
1. 引入组件相关依赖
2. 加上我写好的@EnableCommonsXXX的注解即可。

#### @XXXValidator 注解的使用
包地址：该类注解存放在`com.goudong.commons.annotation.validator`包下。
功能：扩展javax.validation和Hibernate参数校验包,让程序的参数校验更加优雅（加注解就完事，减少在控制层写参数校验判断）

### redis的封装
因为redis使用的比较多，在使用过程中常常会冒出一些新的点子：把redis的key统一管理，读取时自动转换，存储时校验key模板和参数，所以我把redis按照我的想法进行了封装。
使用方式：
1. 启动类加上@EnableCommonsRedisConfig
2. 使用时注入redisTool对象
3. 定义枚举同一管理redis的key（例如：com.goudong.commons.framework.redis.RedisKeyTemplateProviderEnum）
4. 使用redisTool 操作key
```java
// 类似这样，就设置了key，并设置了失效时间
redisTool.set(RedisKeyTemplateProviderEnum.GET_EXPIRE1, 1);
```

## 待优化
2022/08/03
1. 在redisTool中新增一个方法，该方法用来在redis存储有过期时间的key时，存储时多设置个随机秒。避免雪崩
2. redisTool 中 看情况使用Lur脚本

## 最后
可以看源码，里面还有很多没有详细介绍的功能，比如白名单的扫描，接口幂等性的功能，自定义的异常机制，AES、RSA算法封装等等


