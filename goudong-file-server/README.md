# goudong-file-online-preview-server
使用开源项目 [file-online-preview](https://kkfileview.keking.cn) 进行二次开发。



## 作用

在线预览文件

## 准备工作
### open office
#### 下载安装
#### 启动服务
```shell
soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
```