package com.smartops.dashboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.smartops.dashboard.mapper")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.smartops")
public class DashboardServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DashboardServiceApplication.class, args);
    }
}
