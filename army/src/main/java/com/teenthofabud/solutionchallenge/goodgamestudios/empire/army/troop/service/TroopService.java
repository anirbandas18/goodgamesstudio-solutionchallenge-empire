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

    /*public Set<TroopVo> retrieveAllByNaturalOrdering();
    
    public TroopVo retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws TroopException;

    public List<TroopVo> retrieveAllMatchingDetailsByCriteria(Optional<String> optionalName,
                                                                                    Optional<String> optionalDescription) throws TroopException;

    public String createTroop(TroopForm form) throws TroopException;

    public void updateTroop(String id, TroopForm form) throws TroopException;

    public void deleteTroop(String id) throws TroopException;

    public void applyPatchOnTroop(String id, List<PatchOperationForm> patches) throws TroopException;*/

}
