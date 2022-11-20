package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.utils;

import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.converter.UnitEntity2VoConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SoldierServiceHelper {

    private UnitEntity2VoConverter unitEntity2VoConverter;

    @Autowired
    public void setUnitEntity2VoConverter(UnitEntity2VoConverter unitEntity2VoConverter) {
        this.unitEntity2VoConverter = unitEntity2VoConverter;
    }

    public List<UnitVo> unitEntity2DetailedVo(List<? extends UnitEntity> unitEntityList) {
        List<UnitVo> unitDetailsList = new LinkedList<>();
        if(unitEntityList != null && !unitEntityList.isEmpty()) {
            for(UnitEntity entity : unitEntityList) {
                UnitVo vo = this.unitEntity2DetailedVo(entity);
                unitDetailsList.add(vo);
            }
        }
        return unitDetailsList;
    }
    

    public UnitVo unitEntity2DetailedVo(UnitEntity unitEntity) {
        if(unitEntity != null) {
            UnitVo vo = unitEntity2VoConverter.convert(unitEntity);
            log.debug("Converting {} to {}", unitEntity, vo);
            return vo;
        }
        throw new TOABSystemException(TOABErrorCode.SYSTEM_INTERNAL_ERROR, new Object[] { "unit entity is null" });
    }
}
