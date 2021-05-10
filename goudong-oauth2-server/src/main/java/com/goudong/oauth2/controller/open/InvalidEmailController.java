package com.goudong.oauth2.controller.open;

import com.goudong.commons.pojo.Result;
import com.goudong.oauth2.service.InvalidEmailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;

/**
 * 无效邮箱控制层
 * @Author msi
 * @Date 2021-05-10 10:58
 * @Version 1.0
 */
@Api(tags = "无效邮箱")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/oauth2/invalid-email")
public class InvalidEmailController {

    @Resource
    private InvalidEmailService invalidEmailService;

    /**
     * 添加无效邮箱
     * @param email
     * @return
     */
    @PostMapping("/email/{email}")
    public Result add(@PathVariable @Email(message = "邮箱格式错误") String email){
        int count = invalidEmailService.add(email);
        return Result.ofSuccess(count > 0);
    }
}
