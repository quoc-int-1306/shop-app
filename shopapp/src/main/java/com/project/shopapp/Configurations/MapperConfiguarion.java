package com.project.shopapp.Configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguarion {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
