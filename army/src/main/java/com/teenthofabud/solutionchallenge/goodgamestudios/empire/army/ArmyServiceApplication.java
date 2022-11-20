package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army;

import com.teenthofabud.core.common.configuration.TOABMongoAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = { TOABMongoAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
public class ArmyServiceApplication /*implements CommandLineRunner*/ {
    public static void main(String[] args) {
        SpringApplication.run(ArmyServiceApplication.class);
    }

    /*@Override
    public void run(String... args) throws Exception {
        int strength = 167;
        int divisions = 3;
        // strength > division
        // strength  = sum of all division
        // division[i] != division [i+1]

        List<Integer> parts = this.getParts(strength, divisions);
        log.info("{} into {} parts = {}", strength, divisions, parts);
    }

    private int getRandom(int limit) {
        return (new Random(System.currentTimeMillis()).nextInt(limit) + 1);
    }

    private List<Integer> getParts(int strength, int division) {
        List<Integer> parts = new LinkedList<>();
        int sum = 0;
        for(int i = 0 ; i < division - 1 ; i++) {
            int limit = strength - sum - division + 1;
            int n = getRandom(limit);
            sum = sum + n;
            parts.add(n);
        }
        parts.add(strength - sum);
        return parts;
    }*/
}
