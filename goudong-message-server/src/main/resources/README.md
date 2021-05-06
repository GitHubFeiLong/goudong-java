# 消息队列需要创建 交换机
1. 手动创建虚拟主机
spring.rabbitmq.virtual-host=goudong-host

2. 手动创建交换机
com.goudong.message.config.EmailDirectRabbitConfig.EMAIL_DIRECT_EXCHANGE="emailDirectExchange";

> 需要登录rabbitmq 手动创建虚拟主机`goudong-host`，交换机`emailDirectExchange`

