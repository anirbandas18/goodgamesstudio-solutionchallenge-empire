package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier;

import com.teenthofabud.core.common.configuration.TOABMongoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = { TOABMongoAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@EnableEurekaClient
public class SoldierServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoldierServiceApplication.class);
    }
}
