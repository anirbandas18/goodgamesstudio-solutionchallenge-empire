package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.impl;

import com.teenthofabud.core.common.data.vo.HealthVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("soldierServiceClientFallback")
@Slf4j
public class SoldierServiceClientFallbackImpl implements SoldierServiceClient {

    @Override
    public List<UnitVo> getAllUnitDetails() {
        log.debug("Falling back to default implementation of getting all unit details");
        return new ArrayList<UnitVo>();
    }

    @Override
    public UnitVo getUnitDetailsById(String id, String cascadeUntilLevel) {
        log.debug("Falling back to default implementation of getting unit details by id: {}", id);
        return new UnitVo();
    }

    @Override
    public HealthVo health(String status) {
        log.debug("Falling back to default implementation of querying health status: {}", status);
        return new HealthVo();
    }

}
