package com.bjfu.notation_jh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.bjfu.*")
@MapperScan(value = "com.bjfu.notation_jh.dao")
@EnableScheduling
public class NotationJhApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotationJhApplication.class, args);
    }

}
