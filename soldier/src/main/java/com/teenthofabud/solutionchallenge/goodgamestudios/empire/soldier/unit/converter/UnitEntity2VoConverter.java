package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.converter;

import com.teenthofabud.core.common.converter.TOABBaseEntity2VoConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.utils.SoldierServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Slf4j
public class UnitEntity2VoConverter extends TOABBaseEntity2VoConverter<UnitEntity, UnitVo> implements Converter<UnitEntity, UnitVo> {

    private List<String> fieldsToEscape;
    private SoldierServiceHelper menuServiceHelper;

    @Value("#{'${emp.soldier.unit.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }


    @Autowired
    public void setSoldierServiceHelper(SoldierServiceHelper menuServiceHelper) {
        this.menuServiceHelper = menuServiceHelper;
    }

    @Override
    public UnitVo convert(UnitEntity entity) {
        UnitVo vo = new UnitVo();
        if(!fieldsToEscape.contains("id")) {
            vo.setId(entity.getId().toString());
        }
        if(!fieldsToEscape.contains("name")) {
            vo.setName(entity.getName());
        }
        if(!fieldsToEscape.contains("description")) {
            vo.setDescription(entity.getDescription());
        }
        super.expandAuditFields(entity, vo);
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }

}
