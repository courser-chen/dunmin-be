package com.dunmin.timezone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 时区配置
 * 实现 Spring MVC JSON 序列化/反序列化时的自动时区转换
 */
@Configuration
public class JacksonTimeZoneConfig {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    @Bean
    public ObjectMapper originObjectMapper() {
        return  JsonMapper.builder().build();
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper mapper = JsonMapper.builder().build();
        SimpleModule module = new SimpleModule();

        // LocalDateTime
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        // LocalDate
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        // LocalTime
        module.addSerializer(LocalTime.class, new LocalTimeSerializer());
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());

        mapper.registeredModules().add(module);

        return mapper;
    }



    public static class LocalDateTimeSerializer extends ValueSerializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            String userTimeZone = UserTimeZoneContext.getTimeZone();
            LocalDateTime userTime = toUserTime(value, userTimeZone);
            gen.writeString(userTime.format(FORMATTER));
        }
    }

    public static class LocalDateTimeDeserializer extends ValueDeserializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            String value = p.getValueAsString();
            if (value == null || value.isEmpty()) {
                return null;
            }
            String userTimeZone = UserTimeZoneContext.getTimeZone();
            LocalDateTime userTime = LocalDateTime.parse(value, FORMATTER);
            return toUtc(userTime, userTimeZone);
        }
    }



    public static class LocalDateSerializer extends ValueSerializer<LocalDate> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            gen.writeString(value.format(FORMATTER));
        }
    }

    public static class LocalDateDeserializer extends ValueDeserializer<LocalDate> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            String value = p.getValueAsString();
            if (value == null || value.isEmpty()) {
                return null;
            }
            return LocalDate.parse(value, FORMATTER);
        }
    }

    // ==================== LocalTime ====================

    public static class LocalTimeSerializer extends ValueSerializer<LocalTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            gen.writeString(value.format(FORMATTER));
        }
    }

    public static class LocalTimeDeserializer extends ValueDeserializer<LocalTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            String value = p.getValueAsString();
            if (value == null || value.isEmpty()) {
                return null;
            }
            return LocalTime.parse(value, FORMATTER);
        }
    }

    // ==================== 工具方法 ====================

    private static LocalDateTime toUserTime(LocalDateTime utc, String timeZone) {
        if (utc == null) return null;
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId userZone = ZoneId.of(timeZone);
        return utc.atZone(utcZone).withZoneSameInstant(userZone).toLocalDateTime();
    }

    private static LocalDateTime toUtc(LocalDateTime userTime, String timeZone) {
        if (userTime == null) return null;
        ZoneId userZone = ZoneId.of(timeZone);
        ZoneId utcZone = ZoneId.of("UTC");
        return userTime.atZone(userZone).withZoneSameInstant(utcZone).toLocalDateTime();
    }
}
