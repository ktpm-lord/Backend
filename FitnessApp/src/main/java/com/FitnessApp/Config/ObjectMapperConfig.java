package com.FitnessApp.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(List.class, new InstructionsDeserialize());
//
//        objectMapper.registerModule(module);

        return objectMapper;
    }
}
