package com.fubao.dearbao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DearbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DearbaoApplication.class, args);
    }

}
