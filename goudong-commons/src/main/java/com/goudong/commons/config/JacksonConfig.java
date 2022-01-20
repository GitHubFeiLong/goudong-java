package com.goudong.commons.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.goudong.commons.constant.core.DateConst;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

    /**
     * jackson配置属性
     * @return
     */
    @Bean
    JacksonProperties jacksonProperties() {
        JacksonProperties properties = new JacksonProperties();
        properties.setDateFormat(DateConst.DATE_TIME_FORMATTER);
        properties.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        properties.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        properties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return properties;
    }

    /**
     * 配置序列化和反序列化
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, new ToStringSerializer());
            builder.serializerByType(Long.TYPE, new ToStringSerializer());
            builder.serializerByType(Date.class, new DateSerializer(false, DateFormat.getDateTimeInstance()));
            // builder.deserializerByType(Date.class, new DateDeserializers()));
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));
        };
    }

}

