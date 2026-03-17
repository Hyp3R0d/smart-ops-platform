package com.smartops.purchase;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.smartops.purchase.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.smartops.purchase.feign")
@SpringBootApplication(scanBasePackages = "com.smartops")
public class PurchaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PurchaseServiceApplication.class, args);
    }
}
