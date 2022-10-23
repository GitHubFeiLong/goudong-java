package com.goudong;

import com.goudong.exception.core.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 21:40
 */
@RequestMapping("/demo")
@RestController
public class Controller {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @RequestMapping("/get")
    public Result get() {
        return Result.ofSuccess("hello world");
    }
}