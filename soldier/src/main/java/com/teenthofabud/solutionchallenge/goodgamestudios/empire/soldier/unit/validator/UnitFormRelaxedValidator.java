package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator;

import com.teenthofabud.core.common.validator.RelaxedValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error.SoldierErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

@Component
@Slf4j
public class UnitFormRelaxedValidator implements RelaxedValidator<UnitForm>  {

    private List<String> fieldsToEscape;

    @Value("#{'${emp.soldier.unit.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public Boolean validateLoosely(UnitForm form, Errors errors) {
        if(!fieldsToEscape.contains("name") && form.getName() != null && StringUtils.isEmpty(StringUtils.trimWhitespace(form.getName()))) {
            errors.rejectValue("name", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
            log.debug("UnitForm.name is empty");
            return false;
        }
        log.debug("UnitForm.name is valid");
        if(!fieldsToEscape.contains("description") && form.getDescription() != null && StringUtils.isEmpty(StringUtils.trimWhitespace(form.getDescription()))) {
            errors.rejectValue("description", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
            log.debug("UnitForm.description is empty");
            return false;
        }
        log.debug("UnitForm.description is valid");
        return true;
    }
}
