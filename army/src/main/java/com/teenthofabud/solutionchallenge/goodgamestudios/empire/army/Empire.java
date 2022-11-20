package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Empire {

    public static void main(String[] args) {
        Empire t = new Empire();
        for (int i = 1 ; i <= 100 ; i++) {
            log.info("{}: {}", i, t.getParts(3, 3));
        }
    }

    private int getRandom(int limit) {
        return (new Random().nextInt(limit) + 1);
    }

    private List<Integer> getParts(int strength, int division) {
        List<Integer> parts = new LinkedList<>();
        if(strength >= division) {
            int sum = 0;
            for(int i = 0 ; i < division - 1 ; i++) {
                int limit = strength - sum - division + 1 + i;
                log.info("limit: {}", limit);
                int n = getRandom(limit);
                sum = sum + n;
                parts.add(n);
            }
            parts.add(strength - sum);
        }
        return parts;
    }

}
