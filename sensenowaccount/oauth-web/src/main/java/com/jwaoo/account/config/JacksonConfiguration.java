package com.jwaoo.account.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jwaoo.common.core.jackson.CustomSerializeProvider;
import com.jwaoo.common.core.jackson.NullSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfiguration {

//    @Bean
//    public JodaModule jacksonJodaModule() {
//        JodaModule module = new JodaModule();
//        DateTimeFormatterFactory formatterFactory = new DateTimeFormatterFactory();
//        formatterFactory.setIso(DateTimeFormat.ISO.DATE);
//        module.addSerializer(DateTime.class,new DateTimeSerializer(new JacksonJodaFormat(formatterFactory.createDateTimeFormatter().withZoneUTC()))).addSerializer(LocalDate.class, new LocalDateSerializer());
//        return module;
//    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        CustomSerializeProvider csp = new CustomSerializeProvider();
        csp.setNullValueSerializer(new NullSerializer());
        objectMapper.setSerializerProvider(csp);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        return objectMapper;
    }


}
