package com.goudong.user.analyzer;

import com.goudong.commons.exception.user.UserPageException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/14 19:51
 */
public class UserPageSettingFailureAnalyzer extends AbstractFailureAnalyzer<UserPageException> {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, UserPageException cause) {
        return new FailureAnalysis("启动项目失败，配置用户跳转页面错误", "action", cause);
    }

}