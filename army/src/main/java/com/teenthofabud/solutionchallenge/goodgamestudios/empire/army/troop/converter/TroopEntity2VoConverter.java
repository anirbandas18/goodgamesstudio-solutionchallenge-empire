package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.converter;

import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.converter.TOABBaseEntity2VoConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TroopEntity2VoConverter extends TOABBaseEntity2VoConverter<TroopEntity, TroopVo> implements Converter<TroopEntity, TroopVo> {

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
    public TroopVo convert(TroopEntity entity) {
        TroopVo vo = new TroopVo();
        if(!fieldsToEscape.contains("quantity")) {
            vo.setQuantity(entity.getQuantity());
        }
        if(!fieldsToEscape.contains("unit")) {
            UnitVo unitVo = soldierServiceClient.getUnitDetailsById(entity.getUnitId(), TOABCascadeLevel.ONE.getLevelCode());
            vo.setUnit(unitVo.getName());
        }
        super.expandAuditFields(entity, vo);
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }

}
