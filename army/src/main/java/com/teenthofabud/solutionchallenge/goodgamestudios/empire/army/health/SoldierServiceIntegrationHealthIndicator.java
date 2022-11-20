package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.health;

import com.teenthofabud.core.common.data.vo.HealthVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class SoldierServiceIntegrationHealthIndicator implements HealthIndicator {

    private SoldierServiceClient soldierServiceClient;

    @Autowired
    public void setItemServiceClient(SoldierServiceClient soldierServiceClient) {
        this.soldierServiceClient = soldierServiceClient;
    }

    private String selectStatus() {
        List<Status> collection = Arrays.asList(Status.UP, Status.DOWN);
        Collections.shuffle(collection, new Random(System.currentTimeMillis()));
        Status selected = collection.get(0);
        return selected.getCode();
    }

    @Override
    public Health health() {
        String key = SoldierServiceClient.SERVICE_CLIENT_NAME;
        Status status = Status.DOWN;
        String value = "Unavailable";
        try {
            HealthVo inventoryHealth = this.soldierServiceClient.health(this.selectStatus());
            if(inventoryHealth.getStatus().toUpperCase().equals(Status.UP.getCode())) {
                status = Status.UP;
                value = "Available";
            }
        } catch (FeignException e) {
            log.error("Unable to query health of " + key, e);
        }
        Health health = Health.status(status).withDetail(key, value).build();
        return health;
    }

}
