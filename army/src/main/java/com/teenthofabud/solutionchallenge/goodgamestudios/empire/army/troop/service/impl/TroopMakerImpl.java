package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.impl;

import brave.Tracer;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.TroopMaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TroopMakerImpl implements TroopMaker {

    private Tracer tracer;

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public List<Integer> generateDivisions(int strength, int unitTypeCount) throws TroopException {
        List<Integer> divisions = new ArrayList<>(unitTypeCount);
        if(strength >= unitTypeCount) {
            int sum = 0;
            for(int i = 0 ; i < unitTypeCount - 1 ; i++) {
                int limit = strength - sum - unitTypeCount + 1 + i;
                int n = (new Random().nextInt(limit) + 1);
                sum = sum + n;
                divisions.add(n);
            }
            divisions.add(strength - sum);
            Collections.sort(divisions);
            Collections.reverse(divisions);
            log.debug("Generated divisions: {} of strength: {}", divisions, strength);
        }
        return divisions;
    }

    @Override
    public List<TroopEntity> formTroop(List<Integer> divisions, List<UnitVo> unitVoList) {
        Collections.shuffle(unitVoList);
        List<TroopEntity> troops = new ArrayList<>(divisions.size());
        Iterator<Integer> divisionItr = divisions.iterator();
        Iterator<UnitVo> unitVoItr = unitVoList.iterator();
        while(divisionItr.hasNext() && unitVoItr.hasNext()) {
            Integer quantity = divisionItr.next();
            UnitVo unitVo = unitVoItr.next();
            String txId = tracer.currentSpan().context().traceIdString();
            TroopEntity troopEntity = new TroopEntity(txId, unitVo.getId(), quantity);
            log.debug("For division: {} and unit: {} @ transaction: {}, made: {}", quantity, unitVo.getName(), txId, troopEntity);
            troops.add(troopEntity);
        }
        return troops;
    }
}
