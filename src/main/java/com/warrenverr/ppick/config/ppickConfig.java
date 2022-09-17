package com.warrenverr.ppick.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ppickConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
