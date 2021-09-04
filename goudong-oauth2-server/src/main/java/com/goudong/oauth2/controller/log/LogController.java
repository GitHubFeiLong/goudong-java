package com.goudong.oauth2.controller.log;

import com.goudong.commons.pojo.Result;
import com.goudong.oauth2.mapper.SelfAuthorityUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/4 19:46
 */
@Api(tags = "日志测试", hidden = true)
@Slf4j
@Validated
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private SelfAuthorityUserMapper userMapper;

    private static Boolean boo = true;

    @PostMapping("/demo")
    @ApiOperation("测试")
    public Result demo (String name) {
        System.out.println("name = " + name);
        return Result.ofSuccess(request.getHeaderNames().toString());
    }

    @PostMapping(value = "/debug")
    @ApiOperation(value = "开始debug")
    public Result startDebug () {
        boo = true;
        new Thread(){
            @Override
            public void run() {
                while(boo) {
                    log.debug("测试debug日志，测试删除策略");
                    log.info("测试info日志，测试删除策略");
                }
            }
        }.start();
        return Result.ofSuccess();
    }

    @DeleteMapping(value = "/debug")
    @ApiOperation(value = "关闭debug")
    public Result stopDebug () {
        boo = false;
        return Result.ofSuccess();
    }
}
