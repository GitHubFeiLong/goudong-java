# GOUDONG-OAUTH2-SERVER
待办列表：
1. 接入Oauth2.0
2. 菜单（页面菜单/按钮菜单）进行分配权限

认证授权服务
## 认证流程
本服务作为整个微服务的认证入口，支持用户名密码登录,接入qq快捷登录，微信登录(微信未接入需要企业)
![用户认证流程.svg](./README.assets/用户认证流程.svg)
根据用户选择登录方式进行处理登录流程。
### 认证成功处理流程
本项目支持配置认证信息(详情请查看`com.goudong.oauth2.properties.TokenExpiresProperties`源码)，例如：
```yaml
# 配置oauth2
oauth2:
  # 配置令牌失效时长
  token-expires:
    # 是否允许同时登陆
    enableRepeatLogin: false
    # 浏览器
    browser:
      access: 1
      access-time-unit: hours
      refresh: 2
      refresh-time-unit: hours
    # app
    app:
      access: 7
      access-time-unit: days
      refresh: 15
      refresh-time-unit: days
```
1. 配置同一个账号是否允许同时登陆
2. 配置browser/app 访问令牌有效时长，刷新令牌有效时长

大致流程如下：

![用户认证流程-认证成功处理器流程.svg](./README.assets/用户认证流程-认证成功处理器流程.svg)


## 鉴权
本套系统，采用网关鉴权，外网访问服务须进行鉴权，内网内部不需要鉴权，鉴权流程图如下：

![网关鉴权流程图](./README.assets/鉴权流程图.svg)



2. http://localhost:10003/api/oauth2/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
# Oauth2.0
讲解和教程：https://mp.weixin.qq.com/s/IjNtY9gYpfP6-hOuatlMfQ


## HTTP请求头/响应头

### 请求头
接口请求时（除去登录接口），需要带上Authorization请求头
[Authorization]<https://cloud.tencent.com/developer/section/1189908>
### 响应头
当认证过期时，需要带上WWW-Authenticate响应头
WWW-Authenticate: <type> realm=<realm>
[教程]<https://cloud.tencent.com/developer/section/1190024>

## 从长远设计功能
注意：现在整套系统采用网关鉴权，所以内部服务调用不会进行鉴权（未经过网关），所以内部接口调用鉴权暂时做不到。
如果需要内部服务之间调用也要做强校验，那么需要将网关鉴权下发到每个服务中单独鉴权，调用内部服务鉴权只需要判断请求头是否有X-Inner请求头即可，有就是
内部服务调用，没有就是外部服务调用（网关进行请求头清理功能）。然后根据白名单中的是否是内部服务（is_inner）来进行判断放行。