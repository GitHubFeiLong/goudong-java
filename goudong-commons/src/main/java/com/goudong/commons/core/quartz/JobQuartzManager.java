package com.goudong.commons.core.quartz;

import com.goudong.commons.core.quartz.job.AbstractTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 * 定时任务管理容器 component （单例模式）
 * @author msi
 * @version 1.0
 * @date 2021/10/17 14:12
 */
@Component
@Scope("singleton")
public class JobQuartzManager implements ApplicationContextAware {

    /**
     * 创建新的scheduler
     */
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private Scheduler scheduler;

    /**
     * 定义组名称，不同的组用于区分任务
     */
    private static final String JOB_GROUP_NAME = "JOB_GROUP_NAME";

    private static final String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_NAME";

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(JobQuartzManager.class);

    private ApplicationContext applicationContext;

    @Autowired
    private JobFactory jobFactory;

    public void start() {
        //启动定时任务（初始化）
        try {
            this.scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory); //设置定时任务工厂模式
            //项目启动时默认给spring容器添加动态的定时任务
            // this.addJob("job" + 100L, 100L, JobTask.class, "0/2 * * * * ?");
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("init Scheduler failed");
        }
    }

    public boolean addJob(String jobName, Long dataBaseId, Class jobClass, String cronExp) {
        boolean result = false;
        if (!CronExpression.isValidExpression(cronExp)) {
            logger.error("Illegal cron expression format({})", cronExp);
            return result;
        }
        try {
            JobDetail jobDetail = JobBuilder.newJob().withIdentity(new JobKey(jobName, JOB_GROUP_NAME))
                    .ofType((Class<AbstractTask>) Class.forName(jobClass.getName()))
                    .build();
            //创建完jobDetail之后，使用语句传参数值，方便定时任务内部识别它是什么标识
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put("key", jobName);
            jobDataMap.put("dataBaseId", dataBaseId);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                    .withIdentity(new TriggerKey(jobName, TRIGGER_GROUP_NAME))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("QuartzManager add job failed");
        }
        return result;
    }

    public boolean updateJob(String jobName, String cronExp) {
        boolean result = false;
        if (!CronExpression.isValidExpression(cronExp)) {
            logger.error("Illegal cron expression format({})", cronExp);
            return result;
        }
        JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
        TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);
        try {
            if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                        .withIdentity(new TriggerKey(jobName, TRIGGER_GROUP_NAME))
                        .build();
                scheduler.rescheduleJob(triggerKey, newTrigger);
                result = true;
            } else {
                logger.error("update job name:{},group name:{} or trigger name:{},group name:{} not exists..",jobKey.getName(), jobKey.getGroup(), triggerKey.getName(), triggerKey.getGroup());
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            logger.error("update job name:{},group name:{} failed!", jobKey.getName(), jobKey.getGroup());
        }
        return result;
    }

    public boolean deleteJob(String jobName) {
        boolean result = false;
        JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
        try {
            if (scheduler.checkExists(jobKey)) {
                result = scheduler.deleteJob(jobKey);
            } else {
                logger.error("delete job name:{},group name:{} not exists.", jobKey.getName(), jobKey.getGroup());
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            logger.error("delete job name:{},group name:{} failed!", jobKey.getName(), jobKey.getGroup());
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}