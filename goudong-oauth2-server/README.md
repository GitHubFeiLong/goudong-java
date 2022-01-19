1. http://localhost:10003/api/oauth2/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
2. 
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