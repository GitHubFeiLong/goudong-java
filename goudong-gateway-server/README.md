# 微服务网关

作为微服务应用的网关服务，后端所有服务的入口，生产环境只会暴露网关服务，其它服务不对外开放,主要功能是对用户请求进行统一鉴权。

## 功能描述

网关流程如下：

![image-20210820112939087](README.assets/image-20210820112939087.png)

## 拥有路由

1. goudong-oauth2-server
2. goudong-message-server

