package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TroopService {

    /**
     * create new troop matching given parameters with randomized divisions of soldier units amongst themselves
     * @param form parameters controlling troop formation
     * @return troops
     * @throws TroopException
     */
    public List<TroopVo> createNewTroop(TroopForm form) throws TroopException;

}
