package com.goudong.modules.activiti.demo.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 类描述：
 * 监听器
 * @author cfl
 * @version 1.0
 * @date 2022/7/30 17:05
 */
public class MyTaskListener implements TaskListener {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 指定负责人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        // 判断当前任务的name是创建申请，并且是 `create`事件
        if ("创建申请".equals(delegateTask.getName()) &&
            "create".equals(delegateTask.getEventName())
        ) {
            delegateTask.setAssignee("张三listener");
        }
    }
}
