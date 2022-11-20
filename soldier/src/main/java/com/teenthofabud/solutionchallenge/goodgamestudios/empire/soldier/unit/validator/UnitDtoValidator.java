package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error.SoldierErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UnitDtoValidator implements Validator {

    private List<String> fieldsToEscape;

    @Value("#{'${emp.soldier.unit.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UnitDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UnitDto dto = (UnitDto) target;
        Optional<String> optName = dto.getName();
        if(!fieldsToEscape.contains("name") && optName.isPresent() && StringUtils.isEmpty(StringUtils.trimWhitespace(optName.get()))) {
            errors.rejectValue("name", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
            log.debug("UnitDto.name is invalid");
            return;
        }
        Optional<String> optDescription = dto.getDescription();
        if(!fieldsToEscape.contains("description") && optDescription.isPresent() && StringUtils.isEmpty(StringUtils.trimWhitespace(optDescription.get()))) {
            errors.rejectValue("description", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
            log.debug("UnitDto.description is invalid");
            return;
        }
        Optional<String> optActive = dto.getActive();
        if(!fieldsToEscape.contains("active") && optActive.isPresent() && StringUtils.hasText(StringUtils.trimWhitespace(optActive.get()))) {
            Boolean trueSW = optActive.get().equalsIgnoreCase(Boolean.TRUE.toString());
            Boolean falseSW = optActive.get().equalsIgnoreCase(Boolean.FALSE.toString());
            if(!trueSW && !falseSW) {
                errors.rejectValue("active", SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.name());
                log.debug("UnitDto.active is invalid");
                return;
            }
        }
    }

}
