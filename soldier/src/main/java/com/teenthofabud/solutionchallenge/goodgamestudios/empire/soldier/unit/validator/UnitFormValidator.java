package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error.SoldierErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@Slf4j
public class UnitFormValidator implements Validator {

    private List<String> fieldsToEscape;

    @Value("#{'${emp.soldier.unit.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }
    
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UnitForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UnitForm form = (UnitForm) target;
        if(!fieldsToEscape.contains("name") && StringUtils.isEmpty(StringUtils.trimWhitespace(form.getName()))) {
            log.debug("UnitForm.name is empty");
            errors.rejectValue("name", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
            return;
        }
    }

}
