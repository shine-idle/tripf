package com.shineidle.tripf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TripfApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripfApplication.class, args);
    }

}
