package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.utils;

import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.converter.TroopEntity2VoConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class ArmyServiceHelper {

    private TroopEntity2VoConverter troopEntity2VoConverter;

    @Autowired
    public void setTroopEntity2VoConverter(TroopEntity2VoConverter troopEntity2VoConverter) {
        this.troopEntity2VoConverter = troopEntity2VoConverter;
    }

    public List<TroopVo> troopEntity2DetailedVo(List<? extends TroopEntity> troopEntityList) {
        List<TroopVo> troopDetailsList = new LinkedList<>();
        if(troopEntityList != null && !troopEntityList.isEmpty()) {
            for(TroopEntity entity : troopEntityList) {
                TroopVo vo = this.troopEntity2DetailedVo(entity);
                troopDetailsList.add(vo);
            }
        }
        return troopDetailsList;
    }

    public TroopVo troopEntity2DetailedVo(TroopEntity troopEntity) {
        if(troopEntity != null) {
            TroopVo vo = troopEntity2VoConverter.convert(troopEntity);
            log.debug("Converting {} to {}", troopEntity, vo);
            return vo;
        }
        throw new TOABSystemException(TOABErrorCode.SYSTEM_INTERNAL_ERROR, new Object[] { "troop entity is null" });
    }
}
