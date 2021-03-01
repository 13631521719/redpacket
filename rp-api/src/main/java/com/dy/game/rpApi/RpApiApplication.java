package com.dy.game.rpApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.dy.game"})
public class RpApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpApiApplication.class, args);
    }

}
