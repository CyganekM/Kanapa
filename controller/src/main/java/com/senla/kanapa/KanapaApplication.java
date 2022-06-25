package com.senla.kanapa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KanapaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanapaApplication.class, args);
    }
}
