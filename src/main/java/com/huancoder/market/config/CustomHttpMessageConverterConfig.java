package com.huancoder.market.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@Log4j2
public class CustomHttpMessageConverterConfig {
    public CustomHttpMessageConverterConfig() {
        log.info("Custom Convert Configuration");
    }
    //config convert from json to data object? todo.....
    @Autowired
    ObjectMapper objectMapper;
    @Bean
    public HttpMessageConverter<Object> customJsonMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // Cấu hình các tùy chọn cho converter (nếu cần)
        // Ví dụ: converter.setPrettyPrint(true);
        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
