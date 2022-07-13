package com.goudong.user.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/7/13 21:37
 */
@Slf4j
@Component
public class XxlJobDemo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @XxlJob("demo")
    public String demo () {
        log.info("demo");
        return "demo1" + LocalDateTime.now().toLocalDate();
    }
}
