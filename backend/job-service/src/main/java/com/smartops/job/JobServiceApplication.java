package com.smartops.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.smartops.job.mapper")
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.smartops")
public class JobServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobServiceApplication.class, args);
    }
}
