package com.smartops.common.config;

import com.smartops.common.security.JwtProperties;
import com.smartops.common.security.JwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenUtil jwtTokenUtil(JwtProperties jwtProperties) {
        return new JwtTokenUtil(jwtProperties);
    }
}
