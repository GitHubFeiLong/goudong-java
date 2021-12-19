package com.goudong.user.controller.other;

import com.goudong.commons.core.quartz.JobQuartzManager;
import com.goudong.commons.core.quartz.job.JobTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/10/17 14:15
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobQuartzManager quartzManager;

    @PostMapping("addJob")
    @ResponseBody
    public String addJob(@RequestParam("dataBaseId") Long dataBaseId, @RequestParam("cronExp") String cronExp){
        boolean success = quartzManager.addJob("job" + dataBaseId, dataBaseId, JobTask.class, cronExp);
        if(success){
            return "添加成功";
        }else{
            return "添加失败！";
        }
    }

    @PostMapping("deleteJob")
    @ResponseBody
    public String deleteJob(@RequestParam("jobName") String jobName){
        boolean success = quartzManager.deleteJob(jobName);
        if(success){
            return "删除成功";
        }else{
            return "删除失败！";
        }
    }

    @PostMapping("updateJob")
    @ResponseBody
    public String updateJob(@RequestParam("jobName") String jobName, @RequestParam("cronExp") String cronExp){
        boolean success = quartzManager.updateJob(jobName, cronExp);
        if(success){
            return "更新成功";
        }else{
            return "更新失败！";
        }
    }

}