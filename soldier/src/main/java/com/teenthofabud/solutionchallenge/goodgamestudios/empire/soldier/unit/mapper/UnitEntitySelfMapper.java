package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.mapper;

import com.teenthofabud.core.common.mapper.SingleChannelMapper;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@Slf4j
public class UnitEntitySelfMapper implements SingleChannelMapper<UnitEntity> {

    @Override
    public Optional<UnitEntity> compareAndMap(UnitEntity source, UnitEntity target) {
        boolean changeSW = false;
        if(source.getId() != null && source.getId().compareTo(target.getId()) != 0) {
            target.setId(source.getId());
            changeSW = true;
            log.debug("Source UnitEntity.id is valid");
        }
        if(source.getName() != null && StringUtils.hasText(StringUtils.trimWhitespace(source.getName())) && source.getName().compareTo(target.getName()) != 0) {
            target.setName(source.getName());
            changeSW = true;
            log.debug("Source UnitEntity.name is valid");
        }
        if(source.getDescription() != null && StringUtils.hasText(StringUtils.trimWhitespace(source.getDescription())) && source.getDescription().compareTo(target.getDescription()) != 0) {
            target.setDescription(source.getDescription());
            changeSW = true;
            log.debug("Source UnitEntity.description is valid");
        }
        if(changeSW) {
            log.debug("All provided UnitEntity attributes are valid");
            return Optional.of(target);
        } else {
            log.debug("Not all provided UnitEntity attributes are valid");
            return Optional.empty();
        }
    }
}
