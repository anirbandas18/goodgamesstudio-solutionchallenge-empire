package com.teenthofabud.solutionchallenge.goodgamestudios.empire.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayFallbackController {

    @GetMapping("/soldierServiceFallBack")
    public String soldierServiceFallBackMethod(){
        return "soldier-service is taking longer than expected, please try again later!!!";
    }

    @GetMapping("/armyServiceFallBack")
    public String armyServiceFallBackMethod(){
        return "army-service is taking longer than expected, please try again later!!!";
    }

}
