package com.goudong.commons.framework.redis;

import com.goudong.commons.exception.ServerException;
import com.goudong.commons.utils.core.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


/**
 * 类描述：
 * 生成redis的key
 * @ClassName GenerateRedisKeyUtil
 * @Description
 * @Author msi
 * @Date 2020/12/25 11:49
 * @Version 1.0
 */
@Slf4j
public class GenerateRedisKeyUtil {

    /**
     * 聪明的生成redis的key，需要替换表达式
     * @param redisKey redisKey对象
     * @param keys
     * @return
     */
    public static String generateByClever (RedisKeyProvider redisKey, Object... keys) {
        return GenerateRedisKeyUtil.generateByClever(redisKey.getKey(), keys);
    }

    /**
     * 聪明的生成redis的key，需要替换表达式
     * @param keyTemplate   redis key 模板
     * @param keys
     * @return
     */
    public static String generateByClever (String keyTemplate, Object... keys) {

        AssertUtil.isTrue(StringUtils.hasText(keyTemplate), "redis key template 字符串不能为空");

        // 模板字符串中包含“$” 就必须传参数，没有就不用
        if (keyTemplate.indexOf("$") != -1) {

            AssertUtil.isTrue(keys != null && keys.length > 0, "redis key template 的转换参数不能为空");

            // 表达式 数量和 需要替换的数量不匹配
            checkKeyAndParamLength(keyTemplate, keys);
        }


        return replaceAndGenerate(keyTemplate, keys);
    }

    /**
     * 检查模板和参数长度
     * @param key   模板
     * @param keys  参数
     */
    private static void checkKeyAndParamLength(String key, Object... keys) {
        int num = key.split("\\$").length - 1;
        int num1 = keys.length;
        // 参数和所需要的参数长度不一致！
        AssertUtil.isTrue(num == num1, "key模板 和 keys参数 长度不一致");
    }

    /**
     * 替换模板为参数
     * @param keyTemplate   redis key 模板
     * @param params    参数
     * @return
     */
    private static String replaceAndGenerate(String keyTemplate, Object... params) {
        StringBuilder result = new StringBuilder(keyTemplate);

        // 从下标1开始，跳过第一个主体，然后使用后续的字符串替换掉主体的表达式
        try {
            for (int i = 0; i < params.length; i++) {
                result.replace(result.indexOf("$"), result.indexOf("}")+1, params[i]+"");
            }
        } catch (StringIndexOutOfBoundsException s) {
            // redis key 中的"${}" 格式错误
            throw ServerException.methodParamError("key模板 格式错误");
        }

        return result.toString();
    }
}
