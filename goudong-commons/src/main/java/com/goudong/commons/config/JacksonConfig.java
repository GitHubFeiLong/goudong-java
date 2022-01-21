package com.goudong.commons.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.goudong.commons.constant.core.DateConst;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    //~methods
    //==================================================================================================================

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();

        // 反序列化设置 关闭反序列化时Jackson发现无法找到对应的对象字段，便会抛出UnrecognizedPropertyException: Unrecognized field xxx异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 序列化设置 关闭日志输出为时间戳的设置
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


        //忽略value为null时key的输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //设置日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER));

        //序列化成json时，将所有的Long变成string，以解决js中的精度丢失。
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        // 时间格式
        objectMapper.registerModule(javaTimeModule());
        objectMapper.registerModule(jdk8TimeModule());

        // 自动查找并注册Java 8相关模块
        objectMapper.findAndRegisterModules();

        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * java 时间序列化和反序列化
     * @return
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, DateConst.SIMPLE_DATE_FORMAT));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));

        javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer(
                DateDeserializers.DateDeserializer.instance
                , DateConst.SIMPLE_DATE_FORMAT
                , null));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));

        return javaTimeModule;
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    //~ 没作用的
    //==================================================================================================================
    /**
     * jackson配置属性
     * @return
     */
    // @Bean
    JacksonProperties jacksonProperties() {
        JacksonProperties properties = new JacksonProperties();
        properties.setDateFormat(DateConst.DATE_TIME_FORMATTER);
        properties.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        properties.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        properties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return properties;
    }
    // @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(javaTimeModule())
                .registerModule(jdk8TimeModule());
        return mapper;
    }

    /**
     * 配置序列化和反序列化
     * @return
     */
    // @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, new ToStringSerializer());
            builder.serializerByType(Long.TYPE, new ToStringSerializer());
            // builder.serializerByType(Date.class, new DateJsonSerializer());
            builder.serializerByType(Date.class, new DateSerializer(false, new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER)));
            builder.deserializerByType(Date.class, new DateDeserializers.DateDeserializer(
                    DateDeserializers.DateDeserializer.instance
                    , new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER)
                    , null));
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_FORMATTER)));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.TIME_FORMATTER)));
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConst.DATE_TIME_FORMATTER)));
            builder.failOnEmptyBeans(false);
            builder.failOnUnknownProperties(false);
        };
    }

    /**
     * 序列化Date
     */
    @Deprecated
    public static class DateJsonSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            SimpleDateFormat formatter = new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER);
            String formattedDate = formatter.format(date);
            jsonGenerator.writeString(formattedDate);
        }
    }

    /**
     * 反序列化Date
     */
    @Deprecated
    public static class DateJsonDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
            SimpleDateFormat format = new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER);
            String date = jp.getText();
            try {
                return format.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

