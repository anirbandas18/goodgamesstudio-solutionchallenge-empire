package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = { "com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.repository" })
@EnableTransactionManagement
@EnableCaching
public class ArmyJPAConfiguration {

}
