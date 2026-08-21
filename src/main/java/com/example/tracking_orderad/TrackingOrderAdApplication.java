package com.example.tracking_orderad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity
public class TrackingOrderAdApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackingOrderAdApplication.class, args);
    }

}