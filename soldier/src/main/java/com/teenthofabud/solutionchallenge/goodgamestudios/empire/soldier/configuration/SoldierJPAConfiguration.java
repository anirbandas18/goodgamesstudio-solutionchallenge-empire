package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "baseAuditPropertyHandler")
@EnableJpaRepositories(basePackages = {
        "com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.repository" })
@EnableTransactionManagement
public class SoldierJPAConfiguration {

}
