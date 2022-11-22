package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.impl;

import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.*;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.repository.TroopRepository;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.TroopMaker;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.TroopService;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.validator.TroopFormValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.utils.ArmyServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TroopServiceImpl implements TroopService {

    private TroopRepository repository;
    private SoldierServiceClient soldierServiceClient;
    private TroopFormValidator formValidator;
    private ArmyServiceHelper armyServiceHelper;
    private TroopMaker troopMaker;

    @Autowired
    public void setTroopMaker(TroopMaker troopMaker) {
        this.troopMaker = troopMaker;
    }

    @Autowired
    public void setArmyServiceHelper(ArmyServiceHelper armyServiceHelper) {
        this.armyServiceHelper = armyServiceHelper;
    }

    @Autowired
    public void setSoldierServiceClient(SoldierServiceClient soldierServiceClient) {
        this.soldierServiceClient = soldierServiceClient;
    }

    @Autowired
    public void setFormValidator(TroopFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @Autowired
    public void setRepository(TroopRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public List<TroopVo> createNewTroop(TroopForm form) throws TroopException {
        log.info("Trying to generate troops having strength");

        if(form == null) {
            log.debug("TroopForm provided is null");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of TroopForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("TroopForm has {} errors", err.getErrorCount());
            ArmyErrorCode ec = ArmyErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("TroopForm error detail: {}", ec);
            throw new TroopException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of TroopForm are valid");

        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
        if(activeUnits.isEmpty()) {
            log.error("No soldier units available");
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "creation", "unable to create troop with strength:" + form.getStrength() });
        }

        List<Integer> divisions = troopMaker.generateDivisions(form.getStrength(), activeUnits.size());
        if(divisions.size() != activeUnits.size()) {
            log.error("Available soldier units {} and troop divisions {} do not match", activeUnits.size(), divisions.size());
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "creation", "unable to create troop with strength:" + form.getStrength() });
        }

        List<TroopEntity> troopEntityList = troopMaker.formTroop(divisions, activeUnits);
        troopEntityList = repository.saveAll(troopEntityList);
        troopEntityList.forEach(e -> {
            log.debug("For division: {} and unit: {} @ transaction: {}", e.getQuantity(), e.getUnitId(), e.getTransactionId());
        });
        List<TroopVo> troops = armyServiceHelper.troopEntity2DetailedVo(troopEntityList);
        log.info("Generated {} troops with a strength of: {}", troops.size(), form.getStrength());
        return troops;
    }
}