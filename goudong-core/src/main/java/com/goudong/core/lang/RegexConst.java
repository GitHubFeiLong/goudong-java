package com.goudong.core.lang;

/**
 * 接口描述：
 * 正则表达式常量
 * @author cfl
 * @version 1.0
 * @date 2022/11/4 13:47
 */
public interface RegexConst {

    /**
     * 正则表达式{@code {[^\}]*}}<br>
     * 例如：{@code {}
     *
     * {阿斯顿\\{}}
     */
    String PLACEHOLDER_1 = "\\{[^\\}]*\\}";

    /**
     * 正则表达式{@code ${[^\}]*}}<br>
     */
    String PLACEHOLDER_2 = "\\$\\{[^\\}]*\\}";
}
