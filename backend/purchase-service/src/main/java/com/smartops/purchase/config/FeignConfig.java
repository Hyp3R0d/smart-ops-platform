package com.smartops.purchase.config;

import com.smartops.common.constant.CommonConstants;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(CommonConstants.LOGIN_USER_HEADER, "system");
            requestTemplate.header(CommonConstants.LOGIN_UID_HEADER, "0");
        };
    }
}
