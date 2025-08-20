package com.srm.srm_model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启定时任务
@EnableCaching
public class SrmModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(SrmModelApplication.class, args);
    }
}