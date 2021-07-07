package com.jpt21.socialmedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class MiscConfig {

    @Autowired
    Environment environment;

    @Bean
    public String jwtSecret() {
        return environment.getProperty("spring.jwt.secret");
    }
}
