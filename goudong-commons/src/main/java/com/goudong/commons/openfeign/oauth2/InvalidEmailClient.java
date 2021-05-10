package com.goudong.commons.openfeign.oauth2;

import com.goudong.commons.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * openfeign客户端
 * @Author msi
 * @Date 2021-05-10 11:19
 * @Version 1.0
 */
@RequestMapping("/api/oauth2/invalid-email")
@FeignClient(name="goudong-oauth2-server")
public interface InvalidEmailClient {

    /**
     * 添加无效邮箱
     * @param email
     * @return
     */
    @PostMapping("/email/{email}")
    Result add(@PathVariable String email);
}
