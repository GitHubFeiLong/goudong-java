package com.goudong.commons.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * jackson配置
 * @Author e-Feilong.Chen
 * @Date 2022/1/20 8:58
 */
@Configuration
public class JacksonConfig {
    //~fields
    //==================================================================================================================
    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Map<Class<?>, JsonSerializer<?>> map = new HashMap<>();
        map.put(Long.class, ToStringSerializer.instance);
        map.put(Long.TYPE, ToStringSerializer.instance);
        map.put(Date.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
        return builder -> builder.serializersByType(map);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .findModulesViaServiceLoader(true)
                .simpleDateFormat(DATE_TIME_FORMATTER)
                // .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
                //         DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
                // .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
                //         DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
                .build();
    }

}

