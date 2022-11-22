package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TroopMaker {

    /**
     * divides strength into unitTypeCount parts such that each part is unbiased and different from other parts, thereby returning all parts arranged in descending order
     * @param strength
     * @param unitTypeCount
     * @return parts making up strength in descending order
     */
    public List<Integer> generateDivisions(int strength, int unitTypeCount) throws TroopException;

    /**
     * randomly arrange units among itself and assign them to natural ordering of divisions in 1:1 parallel operation
     * @param divisions
     * @param unitVoList
     * @return assignment of units to troop divisions in natural order of divisions
     */
    public List<TroopEntity> formTroop(List<Integer> divisions, List<UnitVo> unitVoList);

}
