package com.goudong.modules.xxljob.task;

import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/4 21:40
 */
public class XxlJobDemo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @XxlJob("demo")
    public String demo() {
        String s = "成功" + (int)(Math.random() * 100);
        System.out.println(s);
        return s;
    }
}
