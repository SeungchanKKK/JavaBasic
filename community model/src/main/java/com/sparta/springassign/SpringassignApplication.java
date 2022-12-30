package com.sparta.springassign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringassignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringassignApplication.class, args);
    }

}

