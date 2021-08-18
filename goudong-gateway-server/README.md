# 微服务网关

## 功能描述

作为微服务应用的网关服务，后端所有服务的入口，生产环境只会暴露网关服务，其它服务不对外开放。

主要功能是对用户请求进行统一鉴权：

1. 登录接口直接放行
2. [Knife4j](https://gitee.com/xiaoym/knife4j)文档使用管理员账号密码登录（所有接口都能查看和调试，生产环境关闭swagger）
3. 白名单资源直接放行
4. 其它资源，需要根据token鉴权。



## 拥有路由

1. goudong-oauth2-server
2. goudong-message-server

