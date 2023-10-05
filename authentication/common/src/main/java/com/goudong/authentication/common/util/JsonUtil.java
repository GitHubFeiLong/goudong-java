package com.goudong.authentication.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
@Slf4j
public class JsonUtil {
    //~fields
    //==================================================================================================================
    private static ObjectMapper objectMapper;



    //~methods
    //==================================================================================================================

    /**
     * 获取实例
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (JsonUtil.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    // 使用transient修饰的字段忽略序列化和反序列化
                    objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

                    // 序列化设置 关闭日志输出为时间戳的设置
                    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                    // 请求接口对象没有成员时不报错
                    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                    /*
                        反序列化设置
                     */
                    // 关闭反序列化时Jackson发现无法找到对应的对象字段，便会抛出UnrecognizedPropertyException: Unrecognized field xxx异常；@JsonIgnoreProperties(ignoreUnknown = true)）
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


                    //忽略value为null时key的输出
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                    //设置日期格式
                    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    // 设置时区
                    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                    //序列化成json时，将所有的Long变成string，以解决js中的精度丢失。
                    SimpleModule simpleModule = new SimpleModule();
                    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                    objectMapper.registerModule(simpleModule);
                }
            }
        }

        return objectMapper;
    }

    /**
     * 获取json字符串
     * @param obj 对象
     * @return obj的json字符串
     */
    public static String toJsonString(Object obj) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json字符串失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转对象
     * @param json 对象
     * @param clazz 需要转成的目标类型对象
     * @return {@code clazz}类型的对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("json字符串转对象失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
