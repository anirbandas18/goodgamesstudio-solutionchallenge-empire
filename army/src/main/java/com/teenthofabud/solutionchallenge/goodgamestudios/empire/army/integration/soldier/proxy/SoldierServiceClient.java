package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy;

import com.teenthofabud.core.common.data.vo.HealthVo;
import com.teenthofabud.core.common.marker.TOABFeignErrorHandler;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.configuration.SoldierServiceIntegrationConfiguration;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.error.SoldierServiceClientExceptionHandler;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = SoldierServiceClient.SERVICE_CLIENT_NAME, url = "${emp.army.soldier.service.url}", configuration = SoldierServiceIntegrationConfiguration.class)
public interface SoldierServiceClient {

    public static final String SERVICE_CLIENT_NAME = "soldier-service";

    @GetMapping("/soldier/unit")
    @TOABFeignErrorHandler(SoldierServiceClientExceptionHandler.class)
    @CircuitBreaker(name = SERVICE_CLIENT_NAME)
    public List<UnitVo> getAllUnitDetails();

    @GetMapping("/soldier/unit/{id}")
    @TOABFeignErrorHandler(SoldierServiceClientExceptionHandler.class)
    @CircuitBreaker(name = SERVICE_CLIENT_NAME)
    public UnitVo getUnitDetailsById(@PathVariable String id, @RequestParam String cascadeUntilLevel);

    @GetMapping("/actuator/health")
    @TOABFeignErrorHandler(SoldierServiceClientExceptionHandler.class)
    @CircuitBreaker(name = SERVICE_CLIENT_NAME)
    public HealthVo health(@RequestParam(required = false) String status);

}