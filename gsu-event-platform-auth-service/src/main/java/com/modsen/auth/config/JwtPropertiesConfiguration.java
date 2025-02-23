package com.modsen.auth.config;

import com.modsen.auth.security.user.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexander Dudkin
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtPropertiesConfiguration {

    @Bean
    public JwtProperties jwtProperties(JwtProperties properties) {
        return properties;
    }

}
