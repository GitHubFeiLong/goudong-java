package com.goudong.commons.quartz;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/10/17 14:11
 */
@Component("JobTask")
public class JobTask extends AbstractTask {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("key = " + this.getKey());
        System.out.println("dataBaseId = " + this.getDataBaseId());
    }

}
