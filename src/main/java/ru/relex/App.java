package ru.relex;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@RequiredArgsConstructor
public class App {

    private final Logger logger;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void startApp() {
        logger.info("Start of the Spring Boot application");
    }

    @PreDestroy
    public void endApp() {
        logger.info("The end of Spring Boot application");
    }
}