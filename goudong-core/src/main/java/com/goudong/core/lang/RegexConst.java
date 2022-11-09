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

    /**
     * 邮箱正则表达式
     */
    String EMAIL =  "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 手机号（严谨）
     */
    String PHONE_STRICTNESS = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1|8|9]))\\d{8}$";
    /**
     * 手机号（宽松）
     */
    String PHONE_LOOSE = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$";
    /**
     * 手机号（最宽松）
     */
    String PHONE_MOST_LOOSE = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$";
}
