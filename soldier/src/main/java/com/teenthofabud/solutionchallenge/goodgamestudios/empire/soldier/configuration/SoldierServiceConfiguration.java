package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableEurekaClient
public class SoldierServiceConfiguration {

    @Profile("!test")
    @Bean
    public OpenAPI soldierServiceAPI(@Value("${spring.application.name}") String applicationName,
                                     @Value("${emp.soldier.description}") String applicationDescription,
                                     @Value("${emp.soldier.version}") String applicationVersion) {
        return new OpenAPI()
                .info(new Info().title(applicationName)
                        .description(applicationDescription)
                        .version(applicationVersion));
    }

    @Bean
    public ObjectMapper om() {
        return new ObjectMapper();
    }

}
