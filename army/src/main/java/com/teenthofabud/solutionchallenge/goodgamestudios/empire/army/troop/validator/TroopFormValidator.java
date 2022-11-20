package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.validator;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TroopFormValidator implements Validator {

    private List<String> fieldsToEscape;

    private SoldierServiceClient soldierServiceClient;

    @Autowired
    public void setSoldierServiceClient(SoldierServiceClient soldierServiceClient) {
        this.soldierServiceClient = soldierServiceClient;
    }

    @Value("#{'${emp.army.troop.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(TroopForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TroopForm form = (TroopForm) target;
        if(!fieldsToEscape.contains("strength") && (form.getStrength() == null || form.getStrength() <= 0)) {
            log.debug("TroopForm.strength is empty");
            errors.rejectValue("strength", ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.name());
            return;
        } else {
            List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
            List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
            if(form.getStrength() < activeUnits.size()) {
                log.debug("TroopForm.strength is invalid");
                errors.rejectValue("strength", ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.name());
                return;
            }
        }
    }

}