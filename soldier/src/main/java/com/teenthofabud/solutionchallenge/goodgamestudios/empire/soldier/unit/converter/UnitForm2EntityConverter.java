package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.converter;

import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UnitForm2EntityConverter implements Converter<UnitForm, UnitEntity> {

    private List<String> fieldsToEscape;

    @Value("#{'${emp.soldier.unit.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public UnitEntity convert(UnitForm form) {
        UnitEntity entity = new UnitEntity();
        if(!fieldsToEscape.contains("name")) {
            entity.setName(form.getName());
        }
        if(!fieldsToEscape.contains("description")) {
            entity.setDescription(form.getDescription());
        }
        entity.setActive(Boolean.TRUE);
        log.debug("Converting {} to {}", form, entity);
        return entity;
    }

}
