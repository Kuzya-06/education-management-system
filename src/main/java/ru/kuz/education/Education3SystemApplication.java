package ru.kuz.education;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Education3SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(Education3SystemApplication.class, args);
    }

}
