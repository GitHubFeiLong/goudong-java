package com.goudong.user.controller;

import com.alibaba.fastjson.JSON;
import com.goudong.module.pojo.Result;
import com.goudong.module.validated.Create;
import com.goudong.user.entity.AuthorityUserDO;
import com.goudong.user.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 *
 * @ClassName UserController
 * @Author msi
 * @Date 2021/2/9 18:11
 * @Version 1.0
 */
@Api(value = "用户", tags = "用户")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {



    @Resource
    private DiscoveryClient client;

    @GetMapping("/hello")
    public String hello () {
        log.info("haha");
        JSON.toJSONString("hello");
        return "hello world";
    }

    @PostMapping("/user")
    @ApiOperation(value = "创建用户", notes = "注册用户")
    public Result createUser(@RequestBody @Validated({Create.class}) AuthorityUserDO authorityUser){

        return Result.ofSuccess(authorityUser);
    }

    @GetMapping("/token")
    @ApiOperation(value = "获取新的token")
    public Result createToken(HttpServletRequest request) {
        // 请求头中的token字符串（包含 Bearer）
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 去掉前面的 "Bearer " 字符串
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        // 解析token为对象
        AuthorityUserDO authorityUserDO = JwtTokenUtil.resolveToken(token);

        // 短期有效
        String shortToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_SHORT_TERM_HOUR);
        // 长期有效
        String longToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_LONG_TERM_HOUR);

        // 返回对象
        Map<String, String> map = new HashMap();
        map.put(JwtTokenUtil.TOKEN, shortToken);
        map.put(JwtTokenUtil.REFRESH_TOKEN, longToken);

        return Result.ofSuccess(map);
    }

//    @Resource
//    private RestOperations restTemplate;
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancerClient loadBalancedClient;   // 注入LoadBalancerClient
    @GetMapping("/demo1")
    public String demo1 () {
        // 获取服务中一个实例
//        ServiceInstance instance = loadBalancedClient.choose("GOUDONG-OAUTH2-SERVER");
//        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/oauth/we-chat/demo1";
        return restTemplate.getForObject("http://GOUDONG-OAUTH2-SERVER"+"/oauth/we-chat/demo1", String.class);
    }

    @GetMapping("/client")
    public Object dis () {
        List<String> list = client.getServices();
        log.info("list >> {}", list);
        List<ServiceInstance> srvList = client.getInstances("GOUDONG-USER-SERVER");
        srvList.forEach(s -> {
            log.info("serviceId:{}, houst:{}, port:{}, uri:{}",s.getServiceId(), s.getHost(),s.getPort(), s.getUri());
        });

        return this.client;
    }
}
