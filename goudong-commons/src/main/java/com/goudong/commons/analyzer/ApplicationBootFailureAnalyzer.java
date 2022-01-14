package com.goudong.commons.analyzer;

import com.goudong.commons.exception.core.ApplicationBootFailedException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 类描述：
 * 应用启动失败分析
 * @author msi
 * @version 1.0
 * @date 2022/1/14 19:51
 */
public class ApplicationBootFailureAnalyzer extends AbstractFailureAnalyzer<ApplicationBootFailedException> {
    //~methods
    //==================================================================================================================
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, ApplicationBootFailedException cause) {
        return new FailureAnalysis(cause.getDescription(), cause.getAction(), cause);
    }
}