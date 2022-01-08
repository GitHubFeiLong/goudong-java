package com.goudong.commons.frame.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：
 * 任务抽象类，其它任务继承它重写executeInternal方法，或实现Job接口重写execute方法。
 * @author msi
 * @version 1.0
 * @date 2021/10/17 14:11
 */
public abstract class AbstractTask implements Job {

    private Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    protected abstract void executeInternal(JobExecutionContext context) throws Exception;

    /**
     * 定时任务标识
     */
    private String key;

    /**
     * 数据库里配置的主键id
     */
    private Long dataBaseId;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            executeInternal(context);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("job execute failed!");
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDataBaseId() {
        return dataBaseId;
    }

    public void setDataBaseId(Long dataBaseId) {
        this.dataBaseId = dataBaseId;
    }
}
