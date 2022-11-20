package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Test {

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println(t.getParts(167, 3));
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
    }

}
